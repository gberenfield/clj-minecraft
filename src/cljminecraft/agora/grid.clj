(ns cljminecraft.agora.grid
  (:require
   [cljminecraft.agora.block :as block]
   [cljminecraft.agora.location :as location]))

(def COLS 80)
(def ROWS 40)
(def SECTOR-W 1)
(def GRID-BASE-H 1)

(defn block-for-height
  [h]
  (condp > h
    75 block/ICE
    50 block/STONE
    25 block/TNT
    :else block/OBSIDIAN))

(defn block-for-base
  [c r]
  (cond
   (and (even? c) (even? r)) block/OBSIDIAN
   (and (odd? c) (even? r)) block/STONE
   (and (even? c) (odd? r)) block/STONE
   :else block/OBSIDIAN))

(defn make-sector
  [mat base-loc magnitude]
  (let [h (int magnitude)]
    (for [dy (range h)
          :let [loc (location/loc-plus base-loc {:y dy})]]
      (block/make-block {:bkt-loc (:bkt-loc loc)
                   :block-type mat}))))

(defn make-grid
  [{:keys [origin-loc cols rows]}]
  (future
    (for [c (range cols)
          r (range rows)
          :let [base-loc (location/loc-plus origin-loc
                                            {:x c :z r})]]
      (do
        (make-sector (block-for-base c r)
                     base-loc
                     GRID-BASE-H)
        (Thread/sleep 5)))
    {:msg :done}))
