(ns cljminecraft.simples
    (:require [cljminecraft.bukkit :as bk]
              [cljminecraft.blocks :as blocks]
              [cljminecraft.events :as ev]
              [cljminecraft.entity :as ent]
              [cljminecraft.player :as plr]
              [cljminecraft.util :as util]
              [cljminecraft.logging :as log]
              [cljminecraft.config :as cfg]
              [cljminecraft.commands :as cmd]
              [cljminecraft.recipes :as r]
              [cljminecraft.items :as i]
              [cljminecraft.files :as f]))

(defonce plugin (atom nil))

(defn random-command
  [sener]
  {:msg (format (cfg/get-string @plugin "diceroll.string")  (inc (int (rand 6))))})

(defn sign-change
  [ev]
  {:msg (format (cfg/get-string @plugin "signplace.string") (first (.getLines ev)))})

;; Plugin lifecycle
(defn events []
  [(ev/event "block.sign-change" #'sign-change)])

(defn start
  [plugin-instance]
  (log/info "%s" "in start simples")
  (reset! plugin plugin-instance)
  (ev/register-eventlist @plugin (events))
  (log/info "%s" @plugin)
  (cmd/register-command @plugin "random" #'random-command) ; be sure to add command to plugin.yml!
  )

(defn stop
  [plugin]
  (log/info "%s" "in stop simples"))

