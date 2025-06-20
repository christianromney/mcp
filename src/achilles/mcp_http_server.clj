(ns achilles.mcp-http-server
  (:require [clojure.data.json :as json]
            [ring.adapter.jetty :as jetty]
            [achilles.mcp-core :as mcp-core])
  (:gen-class))

(defn parse-json-body
  "Parse JSON from request body"
  [request]
  (try
    (when-let [body (:body request)]
      (let [body-str (if (string? body)
                       body
                       (slurp body))]
        (json/read-str body-str :key-fn keyword)))
    (catch Exception e
      {:error {:code -32700 :message "Parse error"}})))

(defn create-json-response
  "Create HTTP response with JSON body"
  [status body]
  {:status status
   :headers {"Content-Type" "application/json"
             "Access-Control-Allow-Origin" "*"
             "Access-Control-Allow-Methods" "POST, OPTIONS"
             "Access-Control-Allow-Headers" "Content-Type"}
   :body (json/write-str body)})

(defn mcp-handler
  "Handle MCP requests over HTTP"
  [request]
  (case (:request-method request)
    :options
    {:status 200
     :headers {"Access-Control-Allow-Origin" "*"
               "Access-Control-Allow-Methods" "POST, OPTIONS"
               "Access-Control-Allow-Headers" "Content-Type"}}
    
    :post
    (let [json-request (parse-json-body request)]
      (if (:error json-request)
        (create-json-response 400 json-request)
        (let [response (mcp-core/handle-request json-request)]
          (if response
            (create-json-response 200 response)
            (create-json-response 204 {})))))
    
    (create-json-response 405 {:error {:code -32601 :message "Method not allowed"}})))

(defn start-server
  "Start the HTTP MCP server"
  [& {:keys [port] :or {port 3000}}]
  (println (format "Starting MCP HTTP server on port %d..." port))
  (jetty/run-jetty mcp-handler {:port port :join? true}))

(defn -main
  "Main entry point for HTTP MCP server"
  [& args]
  (let [port (if (seq args)
               (Integer/parseInt (first args))
               3000)]
    (start-server :port port)))