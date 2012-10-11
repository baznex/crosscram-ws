(ns crosscram-ws.server
  (:require [compojure.core :as comp]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [ring.middleware.params :as rmp]
            [crosscram-ws.resources.start :as start]
            [crosscram-ws.resources.games :as games]
            [crosscram-ws.resources.game :as game]
            [crosscram-ws.resources.create-game :as create-game]
            [crosscram-ws.middleware :as ccmw]))

(comp/defroutes routes
  (comp/GET "/" [] start/get)
  (comp/GET "/game/:id" [id] game/get)
  (comp/POST "/game" [] game/post)
  (comp/GET "/games" [] games/get)
  (comp/GET "/create-game" [] create-game/get)
  (route/files "/")
  (route/not-found "<h1>Page not found</h1>"))

(def app (-> routes
             (rmp/wrap-params)
             (ccmw/wrap-accept-from-extension)))

(defn -main [port]
  (ring/run-jetty app {:port (Integer/parseInt port)}))
