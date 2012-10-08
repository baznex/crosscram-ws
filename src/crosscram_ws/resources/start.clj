(ns crosscram-ws.resources.start
  (:require [crosscram-ws.views.html :as html])
  (:refer-clojure :exclude (get)))

(def ^:private ct-map {"text/html" html/start-to-html})

(defn get
  "Function for a GET on the 'start' resource."
  [req]
  (let [accept ((:headers req) "accept")
        convfn (ct-map accept)]
    (if convfn
      (convfn)
      {:status 415
       :body "Unsupported Media Type"})))
