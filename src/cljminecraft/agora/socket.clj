(ns cljminecraft.agora.socket
  (:require
   [clojure.edn :as edn]
   [http.async.client :as h]
   [cljminecraft.agora.grid :as grid]
   [cljminecraft.logging :as log]))

(def WS_URI "ws://localhost:3000/agora-socket")

(defonce client (atom nil))
(defonce ws (atom nil))

(defonce output-text (atom []))

;; { :name "datomic"
;;   :type :db-txn
;;   :msg
;;    { :x 33
;;      :y 16
;;      :point 277076930201398
;;      :magnitude 84.64807137846947
;;      :grid-name "agora" }}

(defn open-socket
  ([]
     (open-socket {}))
  ([{:keys [text open close]}]
     (reset! client (h/create-client))
     (reset! ws (h/websocket @client
                             WS_URI
                             :text (or text
                                       (fn [con msg]
                                         (log/info "ws msg: %s" msg)))
                             :close (or close
                                        (fn [con status]
                                          (log/info "ws close status: %s" status)))
                             :open (or open
                                       (fn [con]
                                         (log/info "ws open")))))))

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
    (send (make-msg {:msg msg}))))

(defn enable-polling
  []
  (send (make-msg {:msg "start polling" :msg-type :poll})))

(defn disable-polling
  []
  (send (make-msg {:msg "stopt polling" :msg-type :poll})))

(defn close-socket
  []
  (when @ws
    (h/close @ws)
    (reset! ws nil))
  (when @client
    (.close @client)
    (reset! client nil)))

(defn draw-mag
  [grid-origin x y magnitude]
  (let [loc (grid/base-loc {:origin-loc grid-origin :col x :row y})]
    (grid/make-sector loc magnitude)))

(defn handle-socket-msg
  [grid-origin]
  (fn [con socket-msg]
    (let [{:keys [type msg name]} (edn/read-string socket-msg)]
     (if (= type :db-txn)
       (let [{:keys [x y magnitude]} msg]
         (log/info "drawing magnitude: %f" magnitude)
         (draw-mag grid-origin x y magnitude))))))

(defn connect-to-agora
  [grid-origin]
  (grid/make-grid {:origin-loc grid-origin
                   :cols grid/COLS
                   :rows grid/ROWS})
  (open-socket
   {:text (handle-socket-msg grid-origin)})
  (send-chat-msg "socket connected from minecraft")
  (enable-polling)
  (send-chat-msg "polling enabled"))
