(ns cljminecraft.agora.permission)

(defn set-op
  ([p]
     (set-op p true))
  ([p make-op?]
     (.setOp p make-op?)))

(defn make-op [p] (set-op p))

(defn remove-op [p] (set-op p false))
