(ns crosscram-ws.views.html
  (:require [hiccup.core :as hiccup]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [hiccup.page :as page]))

(defn start-to-html
  "Returns an HTML representaion of the 'start' resource."
  []
  (hiccup/html
   [:html
    [:head
     [:title "Welcome to Crosscram!"]]
    [:body
     [:h2 "Welcome to Crosscram!"]
     [:ul
      [:li (elem/link-to "/create-game" "Create a new game")]
      [:li (elem/link-to "/games" "List of previously played games")]]]]))

(defn games-to-html
  "Returns an HTML representaion of the 'games' resource given a collection of IDs."
  [gameids]
  (hiccup/html
   [:html
    [:head
     [:title "List of Crosscram Games"]]
    [:body
     [:h2 "Played games:"]
     [:ul (map (fn [fname]
                 [:li (elem/link-to (str "game/" fname) fname)])
               gameids)]]]))

(defn create-game-to-html
  "Returns an HTML representaion of the 'create-game' resource."
  []
  (hiccup/html
   [:html
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
                   (form/submit-button "Create Game"))]]))

(defn game-to-html
  "Returns an HTML representaion of the 'game' resource."
  [g]
  (hiccup/html
   [:html
    [:head
     [:title "Crosscram Game"]
     (page/include-js "/js/game.js")
     [:link {:href (str "/game/" (:id g)) :rel "self"}]]
    [:body
     [:h2 "Crosscram Game"]
     [:div
      [:canvas#game-canvas {:width 400 :height 400
                            :style "border:4px solid #000000; box-shadow: 5px 5px 5px #888;"}]]]]))
