(ns cljminecraft.agora.grid
  (:require
   [cljminecraft.agora.block :as block]
   [cljminecraft.agora.location :as location]
   [cljminecraft.logging :as log]))

(def MAX-H 100)
(def COLS 80)
(def ROWS 40)
(def SECTOR-W 1)
(def GRID-BASE-H 1)

(defn material-for-height
  [h]
  (condp < h
    75 block/ICE
    50 block/STONE
    25 block/TNT
    block/OBSIDIAN))

(defn material-for-base
  [c r]
  (cond
   (and (even? c) (even? r)) block/OBSIDIAN
   (and (odd? c) (even? r)) block/STONE
   (and (even? c) (odd? r)) block/STONE
   :else block/OBSIDIAN))

(defn draw-sector
  [mat base-loc magnitude]
  (let [h (int magnitude)]
    (doall
     (for [dy (range h)
           :let [loc (location/loc-plus base-loc {:y dy})]]
       (block/make-block {:bkt-loc (:bkt-loc loc)
                          :block-type mat})))))

(defn clear-sector
  [base-loc]
  (draw-sector block/AIR base-loc MAX-H))

(defn make-sector
  ([base-loc magnitude]
     (make-sector (material-for-height (int magnitude)) base-loc magnitude))
  ([mat base-loc magnitude]
     (if-not (>= magnitude MAX-H)
       (clear-sector base-loc))
     (draw-sector mat base-loc magnitude)))

(defn base-loc
  [{:keys [origin-loc col row]}]
  (location/loc-plus origin-loc
                     {:x col :z row}))

(defn make-grid
  [{:keys [origin-loc cols rows]}]
  (future
    (dorun
     (for [c (range cols)
           r (range rows)
           :let [base-loc (base-loc {:origin-loc origin-loc
                                     :col c :row r})]]
       (do
         (make-sector (material-for-base c r)
                      base-loc
                      GRID-BASE-H)
         (Thread/sleep 5))))
    {:msg :done}))
