(ns crosscram-ws.resources.games
  (:require [crosscram-ws.resources.game :as game]
            [crosscram-ws.views.html :as html])
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

(defn- get-games
  "Returns a coll of game maps."
  []
  (map #(game/get-game %) (get-game-ids)))

(defn get
  "Function for a GET on the 'games' resource."
  [req]
  (let [games (get-games)
        accept ((:headers req) "accept")
        convfn (ct-map accept)]
    (if convfn
      (convfn games)
      {:status 415
       :body "Unsupported Media Type"})))