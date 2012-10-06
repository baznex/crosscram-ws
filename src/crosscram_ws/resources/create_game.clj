(ns crosscram-ws.resources.create-game
  (:require [hiccup.core :as hiccup]
            [hiccup.form :as form]
            [hiccup.element :as elem])
  (:refer-clojure :exclude (get)))

(defn get
  "Returns HTML for a form to create a crosscram game."
  [req]
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
