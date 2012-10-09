(ns crosscram-ws.views.text
  (:require [clojure.pprint :as pp]))

(defn game-to-text
  "Returns a text string representation of the given game state g."
  [g]
  (with-out-str
    (pp/pprint g)))
