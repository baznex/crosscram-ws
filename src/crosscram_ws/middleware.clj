(ns crosscram-ws.middleware
  (:require [clojure.string :as str]))

(defn wrap-accept-from-extension
  [handler]
  (fn [req]
    (let [uri (:uri req)
          dotparts (str/split uri #"\.")
          [uri ext] (if (> (count dotparts) 1)
                      [(str/join "." (butlast dotparts)) (last dotparts)]
                      [(first dotparts) nil])]
      (if ext
        ;; there is an extension; if it's one we support, add it to accept header
        (let [known-ct {"html" "text/html"
                        "txt" "text/plain"
                        "json" "application/json"}
              ;; use the content-type associated with the extension. If none,
              ;; use "text/html" for the default
              accept (known-ct ext "text/html")
              req (-> req
                      (assoc-in [:headers "accept"] accept)
                      (assoc :uri uri))]
          (handler req))
        ;; no extension; default to text/html (ultimately, the Accept header
        ;; should not be modified here, but this middleware guarantees that the
        ;; Accept header contains a single media type, simplifying things
        ;; downstream).
        (handler (assoc-in req [:headers "accept"] "text/html"))))))