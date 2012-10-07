(ns crosscram-ws.resources.games
  (:require [crosscram-ws.views.html :as html])
  (:refer-clojure :exclude (get)))

(def ^:private ct-map {"text/html" html/games-to-html})

(defn- get-game-ids
  "Returns a seq of game IDs."
  []
  (let [gamedir "games"
        gameext ".clj"
        gameids (->> (file-seq (java.io.File. gamedir))
                     (map #(.getName %))
                     (filter #(.endsWith % gameext))
                     (map #(.substring % 0 (.lastIndexOf % "."))))]
    gameids))

(defn get
  "Returns the HTML for the list of games in the database."
  [req]
  (let [gameids (get-game-ids)
        accept ((:headers req) "accept")
        convfn (ct-map accept)]
    (if convfn
      (convfn gameids)
      {:status 415
       :body "Unsupported Media Type"})))