(ns crosscram-ws.resources.create-game
  (:require [crosscram-ws.views.html :as html])
  (:refer-clojure :exclude (get)))

(def ^:private ct-map {"text/html" html/create-game-to-html})

(defn get
  "Returns HTML for a form to create a crosscram game."
  [req]
  (let [accept ((:headers req) "accept")
        convfn (ct-map accept)]
    (if convfn
      (convfn)
      {:status 415
       :body "Unsupported Media Type"})))
