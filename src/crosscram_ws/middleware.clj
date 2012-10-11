(ns crosscram-ws.middleware
  (:require [clojure.string :as str]))

(def known-ct {"html" "text/html"
               "txt" "text/plain"
               "json" "application/json"})

(defn wrap-accept-from-extension
  "Alters the 'Accept' header if the resource has an extension that is a key in
the 'known-ct' map with the corresponding value."
  [handler]
  (fn [req]
    (let [uri (:uri req)
          dotparts (str/split uri #"\.")
          [uri ext] (if (> (count dotparts) 1)
                      [(str/join "." (butlast dotparts)) (last dotparts)]
                      [(first dotparts) nil])]
      (if-let [ct-from-ext (known-ct ext)]
        ;; the extension was found in known-ct; add the corresponding media type
        ;; to accept header
        (handler (-> req
                     (assoc-in [:headers "accept"] ct-from-ext)
                     (assoc :uri uri)))
        ;; extension was not found in known-ct; default to text/html
        ;; (ultimately, the Accept header should not be modified here, but this
        ;; middleware guarantees that the Accept header contains a single media
        ;; type, simplifying things downstream).
        (handler (assoc-in req [:headers "accept"] "text/html"))))))