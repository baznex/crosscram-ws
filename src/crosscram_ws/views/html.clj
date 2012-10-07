(ns crosscram-ws.views.html
  (:require [hiccup.core :as hiccup]
            [hiccup.form :as form]
            [hiccup.element :as elem]))

(defn games-to-html
  "Return an HTML representaion of the 'games' resource given a collection of IDs."
  [gameids]
  (hiccup/html
   [:head
    [:title "Create a game of Crosscram"]]
   [:body
    [:h2 "Played games:"]
    [:ul (map (fn [fname]
                [:li (elem/link-to (str "game/" fname) fname)])
              gameids)]]))
