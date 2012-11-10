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
           ~(page/include-css "/css/crosscram.css")
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
            [:div
             [:h2 "Welcome to Crosscram!"]
             [:ul
              [:li (elem/link-to "/create-game" "Create a new game")]
              [:li (elem/link-to "/games" "List of previously played games")]]]))

(defn games-to-html
  "Returns an HTML representaion of the 'games' resource given a coll of game maps."
  [games]
  (template "List of Crosscram Games"
            [:div
             [:h2 "Played games:"]
             [:table {:border 1}
                [:tr [:th "ID"] [:th "Number of Rows"] [:th "Number of Columns"] [:th "Timestamp"] [:th "Bot 1"] [:th "Bot 2"]]
                (map (fn [game]
                       [:tr [:td (elem/link-to (str "/game/" (:id game)) (:id game))] [:td ((:dims game) 0)] [:td ((:dims game) 1)] [:td (:timestamp game)] [:td (:bot1 game)] [:td (:bot2 game)]])
                     games)]]))

(defn create-game-to-html
  "Returns an HTML representaion of the 'create-game' resource."
  []
  (template "Create a game of Crosscram"
            [:div
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

(defn game-to-html
  "Returns an HTML representaion of the 'game' resource."
  [game]
  (let [js-paths ["/js/game.js"]
        link-maps [{:href (str "/game/" (:id game)) :rel "self"}]]
    (template "Crosscram Game" js-paths link-maps
              [:div#game
               [:h2 "Crosscram Game"]
               [:ul
                [:li (str "ID: " (:id game))]
                [:li (str "Rows: " ((:dims game) 0))]
                [:li (str "Columns: " ((:dims game) 1))]
                [:li (str "Timestamp: " (:timestamp game))]
                [:li#bot1 (str "Bot 1: " (:bot1 game))]
                [:li#bot2 (str "Bot 2: " (:bot2 game))]]
               [:canvas#game-canvas {:style "border:4px solid #000000; box-shadow: 5px 5px 5px #888;"}]])))
