(ns crosscram-ws.server
  (:require [compojure.core :as comp]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [ring.middleware.params :as rmp]
            [ring.util.response :as rur]
            [crosscram.engine :as engine]
            [crosscram.game :as game]
            [crosscram.main]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [hiccup.core :as hiccup]
            [hiccup.form :as form]
            [hiccup.element :as elem]))

(defn- game
  "Returns a game played between bot-a and bot-b (bot fns) on a board with
dimensions dim-1 and dim-2."
  [botfns dims]
  (let [g (game/make-game dims 0)
        g (engine/play g (first botfns) (second botfns))]
    g))

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

(defn- post-game
  "Runs a game of crosscram using the given post parameters and returns a 303
  \"See Other\" response including a link to the game resource."
  [req]
  (let [dims (get-dims req)
        bots (get-bots req)]
    (if (or (nil? dims) (nil? bots))
      {:status 400 :body "One or more parameters were missing."}
      (let [botfns (map #(:make-move (#'crosscram.main/load-player %)) bots)
            g (game botfns dims)
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

(defn- get-game
  "Returns the game map for the game whose ID is given in the :id parameter."
  [req]
  (let [gid ((:params req) :id)
        fname (str "games/" gid ".clj")
        f (java.io.File. fname)]
    (if (.exists f)
      (let [g (with-open [rdr (io/reader f)]
                (read (java.io.PushbackReader. rdr)))]
        (json/json-str g))
      (rur/not-found "No such game."))))

(defn- create-game
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

(defn- get-games
  "Returns the HTML for the list of games in the database."
  [req]
  (let [gamedir "games"
        gameext ".clj"
        gameids (->> (file-seq (java.io.File. gamedir))
                       (map #(.getName %))
                       (filter #(.endsWith % gameext))
                       (map #(.substring % 0 (.lastIndexOf % "."))))]
    (hiccup/html
     [:head
      [:title "Create a game of Crosscram"]]
     [:body
      [:h2 "Played games:"]
      [:ul (map (fn [fname]
                  [:li (elem/link-to (str "game/" fname) fname)])
                gameids)]])))

(comp/defroutes routes
  (comp/GET "/game/:id" [id] get-game)
  (comp/POST "/game" [] post-game)
  (comp/GET "/games" [] get-games)
  (comp/GET "/create-game" [] create-game)
  (route/not-found "<h1>Page not found</h1>"))

(def app (-> routes
             (rmp/wrap-params)))

(defn -main [port]
  (ring/run-jetty app {:port (Integer/parseInt port)}))
