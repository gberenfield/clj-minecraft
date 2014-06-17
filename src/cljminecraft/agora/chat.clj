(ns cljminecraft.agora.chat)

(defn say
  [p msg]
  (.chat p msg))
