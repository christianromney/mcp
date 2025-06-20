(ns achilles.mcp-server
  (:require [clojure.data.json :as json]
            [clojure.core.async :as async]
            [achilles.mcp-core :as mcp-core])
  (:gen-class))

(defn read-message
  "Read a JSON-RPC message from stdin"
  []
  (when-let [line (read-line)]
    (try
      (json/read-str line :key-fn keyword)
      (catch Exception _e
        {:error {:code -32700 :message "Parse error"}}))))

(defn write-message
  "Write a JSON-RPC message to stdout"
  [message]
  (println (json/write-str message))
  (flush))

(defn start-server
  "Start the MCP server main loop"
  []
  (loop []
    (when-let [request (read-message)]
      (if (:error request)
        (write-message request)
        (let [response (mcp-core/handle-request request)]
          (when response
            (write-message response))))
      (recur))))

(defn -main
  "Main entry point for MCP server"
  [& _args]
  (start-server))
