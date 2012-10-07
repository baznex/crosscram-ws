(ns crosscram-ws.views.html
  (:require [hiccup.core :as hiccup]
            [hiccup.form :as form]
            [hiccup.element :as elem]))

(defn games-to-html
  "Returns an HTML representaion of the 'games' resource given a collection of IDs."
  [gameids]
  (hiccup/html
   [:head
    [:title "Create a game of Crosscram"]]
   [:body
    [:h2 "Played games:"]
    [:ul (map (fn [fname]
                [:li (elem/link-to (str "game/" fname) fname)])
              gameids)]]))

(defn create-game-to-html
  "Returns an HTML representaion of the 'create-game' resource."
  []
  (hiccup/html
   [:head
    [:title "Create a game of Crosscram"]]
   [:body
    [:h2 "Create a Crosscram Game"]
    (form/form-to [:post "/game"]
                  [:div
                   [:label "Number of rows:"]
                   (form/text-field "rows")]
                  [:div
                   [:label "Number of columns:"]
                   (form/text-field "cols")]
                  [:div
                   [:label "First bot:"]
                   (form/text-field "bot1")]
                  [:div
                   [:label "Second bot:"]
                   (form/text-field "bot2")]
                  (form/submit-button "Create Game"))]))
