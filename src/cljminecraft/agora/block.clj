(ns cljminecraft.agora.block
  (:require
   [cljminecraft.agora.location :as location]
   [cljminecraft.agora.player :as player]

   [cljminecraft.logging :as log])
  (:import
   [org.bukkit Material]
   [org.bukkit.util BlockIterator]))

(def ICE Material/ICE)
(def OBSIDIAN Material/OBSIDIAN)
(def STONE Material/STONE)
(def TNT Material/TNT)
(def WOOL Material/WOOL)

(defn make-block
  [{:keys [bkt-loc block-type]}]
  (let [b (.getBlock bkt-loc)]
    (.setType b block-type)
    b))

(defn make-stone
  [bkt-loc]
  (make-block {:loc bkt-loc
               :block-type STONE}))

(defn block-type
  [b] (.getType b))

(defn transparent?
  [b]
  (= Material/AIR (block-type b)))

(defn location
  [b]
  (location/location b))

(defn in-sight
  [p]
  (let [pointing-up? (player/pointing-up? p)
        p-loc (player/location p)
        search-flipped? (fn [block-loc]
                          (let [{block-y :y} block-loc
                                {player-y :y} p-loc
                                p-height-margin 20]
                            (or
                             (and pointing-up?
                                  (< (+ block-y p-height-margin)
                                     player-y))
                             (and (not pointing-up?)
                                  (> (- block-y p-height-margin)
                                     player-y)))))]
       (loop [bi (BlockIterator. p 0)]
         (if-let [b (and (.hasNext bi)
                         (.next bi))]
           (if (not (search-flipped? (location b)))
             (if (transparent? b)
               (recur bi)
               b))))))
