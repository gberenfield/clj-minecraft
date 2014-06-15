(ns cljminecraft.scratch
  (:require [cljminecraft.core :as core]
            [cljminecraft.bukkit :as bk]
            [cljminecraft.blocks :as blocks]
            [cljminecraft.events :as ev]
            [cljminecraft.entity :as ent]
            [cljminecraft.player :as plr]
            [cljminecraft.util :as util]
            [cljminecraft.logging :as log]
            [cljminecraft.config :as cfg]
            [cljminecraft.commands :as cmd]
            [cljminecraft.recipes :as r]
            [cljminecraft.items :as i]
            [cljminecraft.files]
            [cheshire.core :as ch]
            [clojure.tools.nrepl.server :refer (start-server stop-server)])
  (:import [org.bukkit Location Material]))
 
;; M-x nrepl to connect to Minecraft server on port 4005
;; or, use $ lein repl :connect 4005
 
;; C-c C-k to eval this buffer

;; You probably want to /gamemode creative
;; in order to get all the blocks you need.

;;(in-ns 'cljminecraft.core)
;;(in-ns 'cljminecraft.scratch)
;;(ev/find-event "break")
;;(ev/describe-event "block.block-break")
 
(defn mybreakfn [ev]
  {:msg (format "You broke a %s" (.getBlock ev))})
 
(defn unregister-all-events []
  (let [plugin @core/clj-plugin]
    (log/info "Unregistering all events for plugin %s" (.getName plugin))
    (reset! ev/registered-events #{})
    (org.bukkit.event.HandlerList/unregisterAll plugin)))
 
;;(ev/register-event @core/clj-plugin "block.block-break" #'mybreakfn)
;;(unregister-all-events)
 
(defn mybreakfn [ev]
  {:msg (format "You broke a %s" (.getType (.getBlock ev)))})
 
(defn first-player []
  (first (bk/online-players)))
 
;;(def me (first-player))
(defn max-health [player]
  (.setHealth player (.getMaxHealth player)))
 
;;(.sendMessage me "hello")
;;(.getLocation me)
;; (def loc (.getLocation me))
;; (.getY loc)
;; (.getX loc)
;; (.getZ loc)
;; (.getWorld loc)
;; (.setType (.getBlock (.getLocation me)) org.bukkit.Material/STONE)

(defn location-delta [loc {:keys [x y z] :or {x 0 y 0 z 0}}]
  (new Location (.getWorld loc)
       (+ x (.getX loc))
       (+ y (.getY loc))
       (+ z (.getZ loc))))

(defn create-block-at-loc [loc block-type]
  (-> (.getBlock loc)
      (.setType block-type)))

(defn create-stone-at-loc [loc]
  (-> (.getBlock loc)
      (.setType Material/STONE)))

(defn create-block-tower-at-loc
  [loc height block-type]
  (doseq [dy (range 0 height)]
    (create-block-at-loc (location-delta loc {:y dy}) block-type)))

(defn create-stone-tower-at-loc
  ([loc height]
     (doseq [dy (range 0 height)]
       (create-stone-at-loc (location-delta loc {:y dy}))))
  ([loc]
     (create-stone-tower-at-loc loc 10)))
 
(defn target-loc-of-player [player]
  (.getLocation (.getTargetBlock player nil 256)))
 
(defn create-stone-at-target-of-player [player]
  (-> (.getTargetBlock player nil 256)
      (.setType Material/STONE)))
 
(defn create-stone-at-loc-of-player [player]
  (create-stone-at-loc (.getLocation player)))

(comment
  (def BIRTH-DATA-FILE "/Users/koba/work/MinecraftMods/clj-minecraft/data/birth.json")
  (def transformed-birth-data (reduce #(assoc % (Integer. (%2 "year")) (Float. (%2 "rate_per_1000_resident_population"))) {} birth-data))
  ;;(def indexed-birth-data (map-indexed (comp vec flatten vector) transformed-birth-data))
  (def sorted-birth-data (sort-by first (vec transformed-birth-data)))
  (def rates (mapv second sorted-birth-data))
  (defn index-of-year [year data]
    (first (keep-indexed #(when (= (first %2) year) %1) data)))

  (defn create-birth-graph-at-loc
    ([loc year block-type]
       (let [year-idx (index-of-year year sorted-birth-data)]
         (doseq [dx (range 0 (count rates))
                 :let [loc (location-delta loc {:x dx})
                       b-type (if (= dx year-idx)
                                Material/ICE
                                block-type)]]
           (create-block-tower-at-loc loc (nth rates dx) b-type))))
    ([loc year]
       (create-birth-graph-at-loc loc year Material/STONE))
    ([loc]
       (create-birth-graph-at-loc loc 0)))

  (defn create-birth-graph-at-target-of-player [player]
    (let [loc (target-loc-of-player player)]
      (create-birth-graph-at-loc loc)))

  (defn burn-birth-graph-at-loc
    [loc]
    (create-birth-graph-at-loc loc 0 Material/FIRE)))

(defn create-staircase-at-loc [loc height]
  (doseq [h (range height)
          x (range 3)
          :let [step-loc (location-delta loc {:x x :y h :z h})]]
    (create-stone-at-loc step-loc)))

(defn create-monolith-block
  ([loc dx dy dz block-type sleep-time]
     (doseq [dx (range dx)
             dy (range dy)
             dz (range dz)
             :let [loc (location-delta loc {:x dx :y dy :z dz})]]
       (println "monolith block at " dx dy dz)
       (create-block-at-loc loc block-type)
       (when (< 0 sleep-time)
         (Thread/sleep sleep-time))))
  ([loc dx dy dz]
     (create-monolith-block loc dx dy dz Material/OBSIDIAN 250)))

(def MONOLITH-X 6)
(def MONOLITH-HEIGHT 10)
(def MONOLITH-Z 2)

(defn create-monolith-at-loc [loc]
  (create-monolith-block loc MONOLITH-X MONOLITH-HEIGHT MONOLITH-Z))

(defn burn-monolith-at-loc [loc]
  (create-monolith-block loc MONOLITH-X MONOLITH-HEIGHT MONOLITH-Z Material/FIRE 0))

(defn rainbow-at-loc [loc]
  (let [length 40
        half (/ length 2)]
    (doseq [x (range (* -1 half) length)
           :let [y (+ half (int (* -0.1 x x)))
                 loc (location-delta loc {:x x :y y})]]
     (create-block-at-loc loc Material/STONE))))

(defn tnt-lightcycle
  ([player]
     (tnt-lightcycle player 100 50 Material/TNT))
  ([player num-blocks sleep-time mat]
     (future
       (let [start (.getLocation player)]
         (doseq [cycles (range num-blocks)]
           (create-block-at-loc (.getLocation player) mat)
           (Thread/sleep sleep-time))
         (if (= Material/TNT mat)
           (create-block-at-loc start Material/FIRE))))))
