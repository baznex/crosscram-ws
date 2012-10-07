(ns crosscram-ws.resources.game
  (:require [crosscram.engine :as engine]
            [crosscram.game :as game]
            [crosscram.main]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [ring.util.response :as rur])
  (:refer-clojure :exclude (get)))

(def ^:private ct-map {"application/json" json/json-str})

(defn- play-game
  "Returns a game played between bot-a and bot-b (bot fns) on a board with
dimensions dim-1 and dim-2."
  [botfns dims]
  (let [g (game/make-game dims 0)
        g (engine/play g (first botfns) (second botfns))]
    g))

(defn- get-game
  "Returns a map of game state of the game with the given id retrieved from the
  store."
  [id]
  (let [
        fname (str "games/" id ".clj")
        f (java.io.File. fname)]
    (when (.exists f)
      (let [g (with-open [rdr (io/reader f)]
                (read (java.io.PushbackReader. rdr)))]
        g))))

(defn- get-dims
  [req]
  (let [params (:params req)
        {:strs [dims rows cols]} params]
    (if (or (nil? rows) (nil? cols))
      (when (> (count dims) 0)
        (read-string dims))
      (when (and (> (count rows) 0) (> (count cols) 0))
        [(read-string rows) (read-string cols)]))))

(defn- get-bots
  [req]
  (let [params (:params req)
        {:strs [bots bot1 bot2]} params]
    (if (or (nil? bot1) (nil? bot2))
      (when (> (count bots) 0)
        (read-string bots))
      (when (and (> (count bot1) 0) (> (count bot2) 0))
        [(read-string bot1) (read-string bot2)]))))

(defn post
  "Runs a game of crosscram using the given post parameters and returns a 303
  \"See Other\" response including a link to the game resource."
  [req]
  (let [dims (get-dims req)
        bots (get-bots req)]
    (if (or (nil? dims) (nil? bots))
      {:status 400 :body "One or more parameters were missing."}
      (let [botfns (map #(:make-move (#'crosscram.main/load-player %)) bots)
            g (play-game botfns dims)
            gidstr (->> (rand)
                        (* 1e6)
                        int
                        str)
            fname (str "games/" gidstr ".clj")]
        (with-open [wrtr (io/writer fname)]
          (binding [*print-dup* true
                    *out* wrtr]
            (prn g)))
        (rur/redirect-after-post (str "/game/" gidstr))))))

(defn get
  "Returns the game map for the game whose ID is given in the :id parameter of
the request."
  [req]
  (let [gid ((:params req) :id)
        g (get-game gid)
        accept ((:headers req) "accept")
        convfn (ct-map accept)]
    (if g
      (if convfn
          (convfn g)
          {:status 415
           :body "Unsupported Media Type"})
      (rur/not-found "No such game."))))
