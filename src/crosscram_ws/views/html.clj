(ns crosscram-ws.views.html
  (:require [hiccup.core :as hiccup]
            [hiccup.form :as form]
            [hiccup.element :as elem]
            [hiccup.page :as page]))

(defn- template
  "An HTML template to be used by all or most HTML views"
  ([body]
     (template "Crosscram" body))
  ([title body]
     (template title nil nil body))
  ([title js-paths link-maps body]
     (let [js-includes (map #(page/include-js %) js-paths)
           links (map #(vector :link %) link-maps)]
       (hiccup/html
        `[:html
          [:head
           [:title ~title]
           ~@js-includes
           ~@links]
          [:body
           [:div#body-wrapper
            [:div#header
             [:h1 "Crosscram"]]
            [:div#content-wrapper
             ~body]]]]))))

(defn start-to-html
  "Returns an HTML representaion of the 'start' resource."
  []
  (template "Welcome to Crosscram!"
            [:h2 "Welcome to Crosscram!"]
            [:ul
             [:li (elem/link-to "/create-game" "Create a new game")]
             [:li (elem/link-to "/games" "List of previously played games")]]))

(defn games-to-html
  "Returns an HTML representaion of the 'games' resource given a collection of IDs."
  [gameids]
  (template "List of Crosscram Games"
            [:h2 "Played games:"]
            [:ul (map (fn [fname]
                        [:li (elem/link-to (str "game/" fname) fname)])
                      gameids)]))

(defn create-game-to-html
  "Returns an HTML representaion of the 'create-game' resource."
  []
  (template "Create a game of Crosscram"
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
                          (form/submit-button "Create Game"))))

(defn game-to-html
  "Returns an HTML representaion of the 'game' resource."
  [g]
  (let [js-paths ["/js/game.js"]
        link-maps [{:href (str "/game/" (:id g)) :rel "self"}]]
    (template "Crosscram Game" js-paths link-maps
              [:div
               [:h2 "Crosscram Game"]
               [:canvas#game-canvas {:style "border:4px solid #000000; box-shadow: 5px 5px 5px #888;"}]])))
