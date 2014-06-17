(ns cljminecraft.agora.location
  (:import
   [org.bukkit Location]))

(defn bkt-loc->loc
  [bkt-loc]
  {:x       (.getX bkt-loc)
   :y       (.getY bkt-loc)
   :z       (.getZ bkt-loc)
   :pitch   (.getPitch bkt-loc)
   :yaw     (.getYaw bkt-loc)
   :world   (.getWorld bkt-loc)
   :bkt-loc bkt-loc})

(defn location
  [thing]
  (let [bkt-loc (.getLocation thing)]
    (bkt-loc->loc bkt-loc)))

(defn bk-location-plus
  [bkt-loc {:keys [x y z] :or {x 0 y 0 z 0}}]
  (let [loc (bkt-loc->loc bkt-loc)]
    (new Location (:world loc)
         (+ x (:x loc))
         (+ y (:y loc))
         (+ z (:z loc)))))

(defn loc-plus
  [{:keys [bkt-loc]} delta]
  (bkt-loc->loc (bk-location-plus bkt-loc delta)))
