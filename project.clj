(defproject crosscram-ws "1.0.0-SNAPSHOT"
  :description "A web interface for Crosscram"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "1.1.3"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [crosscram "0.0.1-SNAPSHOT"]]
  :main crosscram-ws.server)