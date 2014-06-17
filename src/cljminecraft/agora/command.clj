(ns cljminecraft.agora.command
  (:import
   [org.bukkit Bukkit]
   [org.bukkit.command.defaults TimeCommand]))

(defn time-set
  [v]
  (let [t (condp = v
            "day" 0
            "night" 12500
            (int v))]
    (doseq [world (Bukkit/getWorlds)]
      (.setTime world t))))

;; (defn time-set
;;   [player v]
;;   (.execute (new TimeCommand) player "alias" (into-array ["set" (str v)])))
