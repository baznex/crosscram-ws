(ns crosscram-ws.resources.games
  (:require [hiccup.core :as hiccup]
            [hiccup.form :as form]
            [hiccup.element :as elem])
  (:refer-clojure :exclude (get)))

(defn get
  "Returns the HTML for the list of games in the database."
  [req]
  (let [gamedir "games"
        gameext ".clj"
        gameids (->> (file-seq (java.io.File. gamedir))
                     (map #(.getName %))
                     (filter #(.endsWith % gameext))
                     (map #(.substring % 0 (.lastIndexOf % "."))))]
    (hiccup/html
     [:head
      [:title "Create a game of Crosscram"]]
     [:body
      [:h2 "Played games:"]
      [:ul (map (fn [fname]
                  [:li (elem/link-to (str "game/" fname) fname)])
                gameids)]])))