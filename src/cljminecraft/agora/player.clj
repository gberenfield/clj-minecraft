(ns cljminecraft.agora.player
  (:require
   [cljminecraft.bukkit :as bkt]
   [cljminecraft.agora.chat :as chat]
   [cljminecraft.agora.permission :as permission])
  (:import
   [org.bukkit GameMode]))

(defn players
  []
  (bkt/online-players))

(defn player-name
  [player]
  (.getDisplayName player))

(defn player-names
  []
  (map player-name (players)))

(defn give-gamemode
  [p m]
  (if-not (= m (.getGameMode p))
    (.setGameMode p m)))

(defn give-creative
  ([p]
     (give-gamemode p GameMode/CREATIVE)))

(defn godify-player
  [p]
  (permission/set-op p)
  (chat/say p "I am an Op now.")
  (give-creative p)
  (chat/say p "I am in creative mode now."))

(defn godify-player
  []
  (doseq [p (players)]
    (godify-player p)))
