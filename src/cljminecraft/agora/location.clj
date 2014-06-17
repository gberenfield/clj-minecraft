(ns cljminecraft.agora.location)

(defn location
  [thing]
  (let [bk-loc (.getLocation thing)]
    {:x       (.getX bk-loc)
     :y       (.getY bk-loc)
     :z       (.getZ bk-loc)
     :pitch   (.getPitch bk-loc)
     :yaw     (.getYaw bk-loc)
     :world   (.getWorld bk-loc)
     :bkt-bk-loc bk-loc}))
