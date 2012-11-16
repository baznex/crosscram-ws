(defproject crosscram-ws "1.0.0-SNAPSHOT"
  :description "A web interface for Crosscram"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [ring/ring-core "1.1.6"]
                 [cheshire "4.0.3"]
                 [crosscram "0.0.1-SNAPSHOT"]
                 [hiccup "1.0.1"]
                 [org.clojure/tools.namespace "0.2.1"]
                 [org.clojure/java.classpath "0.2.0"]]
  :main crosscram-ws.server)