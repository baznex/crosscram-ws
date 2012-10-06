(ns crosscram-ws.server
  (:require [compojure.core :as comp]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [ring.middleware.params :as rmp]
            [crosscram-ws.resources.games :as games]
            [crosscram-ws.resources.game :as game]
            [crosscram-ws.resources.create-game :as create-game]))

(comp/defroutes routes
  (comp/GET "/game/:id" [id] game/get-game)
  (comp/POST "/game" [] game/post-game)
  (comp/GET "/games" [] games/get-games)
  (comp/GET "/create-game" [] create-game/create-game)
  (route/not-found "<h1>Page not found</h1>"))

(def app (-> routes
             (rmp/wrap-params)))

(defn -main [port]
  (ring/run-jetty app {:port (Integer/parseInt port)}))
