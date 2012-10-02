(ns crosscram-ws.server
  (:require [compojure.core :as comp]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [ring.middleware.params :as rmp]
            [crosscram.engine :as engine]
            [crosscram.game :as game]
            [crosscram.main]
            [clojure.data.json :as json]))

(defn- game
  "Returns a game played between bot-a and bot-b (bot fns) on a board with
dimensions dim-1 and dim-2."
  [botfns dims]
  (let [g (game/make-game dims 0)
        g (engine/play g (first botfns) (second botfns))]
    g))

(defn- get-game
  [req]
  (let [dims (if-let [dimstr ((:params req) "dims")]
               (read-string dimstr)
               [8 8])
        botnames (if-let [botstr ((:params req) "bots")]
                   (read-string botstr)
                   ['crosscram.samples.random 'crosscram.samples.random])
        botfns (map #(:make-move (#'crosscram.main/load-player %)) botnames)]
    (json/json-str (game botfns dims))))

(comp/defroutes routes
  (comp/GET "/game" [] get-game)
  (route/not-found "<h1>Page not found</h1>"))

(def app (-> routes
             (rmp/wrap-params)))

(defn -main [port]
  (ring/run-jetty app {:port (Integer/parseInt port)}))
