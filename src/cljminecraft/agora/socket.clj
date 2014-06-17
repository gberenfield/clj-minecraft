(ns cljminecraft.agora.socket
  (:require
   [http.async.client :as h]

   [cljminecraft.logging :as log]))

(def WS_URI "ws://localhost:3000/agora-socket")

(defonce client (atom nil))
(defonce ws (atom nil))

(def output-text (atom []))

;; { :name "datomic"
;;   :type :db-txn
;;   :msg
;;    { :x 33,
;;      :y 16
;;      :point 277076930201398
;;      :magnitude 84.64807137846947
;;      :grid-name "agora"}}

(defn open-socket
  []
  (reset! client (h/create-client))
  (reset! ws (h/websocket @client
                          WS_URI
                          :text (fn [con msg]
                                  (log/info "ws text")
                                  (log/info "conn: %s" con)
                                  (log/info "msg: %s" msg)
                                  (swap! output-text conj msg))
                          :close (fn [con status]
                                   (log/info "ws close")
                                   (log/info "conn: %s" con)
                                   (log/info "status: %s" status)
                                   (swap! output-text conj "closed"))
                          :open (fn [con]
                                  (log/info "ws open")
                                  (log/info "conn: %s" con)))))

(defn make-msg
  [{:keys [msg msg-type name]
    :or {msg-type :chat name "minecraft"}}]
  {:name name
   :msg msg
   :type msg-type})

(defn send
  [msg-data]
  (if @ws
    (h/send @ws :text (pr-str msg-data))))

(defn send-chat-msg
  [msg]
  (if @ws
    (send (make-msg msg))))

(defn enable-polling
  []
  (send (make-msg {:msg "start polling" :msg-type :poll})))

(defn disable-polling
  []
  (send (make-msg {:msg "stopt polling" :msg-type :poll})))

(defn close-socket
  []
  (h/close @ws)
  (reset! ws nil)
  (.close @client)
  (reset! client nil))
