(ns crosscram-ws.server
  (:require [compojure.core :as comp]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [crosscram.engine :as engine]
            [crosscram.game :as game]
            [crosscram.samples.random]
            [clojure.pprint :as pp]))

(defn- load-player
  "Fetch a player map from a namespace, or nil. The map will contain:
:make-move - The make-move function."
  [ns-name]
  (let [ns-sym (symbol ns-name)]
    (when-let [make-move (ns-resolve ns-sym 'make-move)]
      {:make-move (deref make-move)})))

(defn- game
  "Returns a game played between bot-a and bot-b (bot fns) on a board with
dimensions dim-1 and dim-2."
  [bot-a bot-b dim-1 dim-2]
  (let [g (game/make-game [dim-1 dim-2] 0)
        g (engine/play g bot-a bot-b)]
    g))

(comp/defroutes app
  (comp/GET "/game" [] (let [bot-fn (:make-move (load-player 'crosscram.samples.random))
                             dim 8
                             g (game bot-fn bot-fn dim dim)]
                         (str "<pre>"
                              (with-out-str (pp/pprint g))
                              "</pre>"))))

(defn -main [port]
  (ring/run-jetty app {:port (Integer/parseInt port)}))
