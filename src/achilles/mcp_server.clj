(ns achilles.mcp-server
  (:require [clojure.data.json :as json]
            [clojure.core.async :as async]
            [achilles.mcp :as mcp])
  (:gen-class))

(def server-info
  {:name "Token Cost Estimator MCP Server"
   :version "1.0.0"})

(def protocol-version "2024-11-05")

(def tools
  {"estimate_cost"
   {:name "estimate_cost"
    :description "Estimate cost based on text token count and cost per million tokens"
    :inputSchema {:type "object"
                  :properties {:text {:type "string"
                                      :description "Text to analyze for token count"}
                               :cost_per_million_tokens {:type "number"
                                                         :description "Cost per million tokens as a number"}}
                  :required ["text" "cost_per_million_tokens"]}}})

(defn create-response
  "Create a JSON-RPC response"
  ([id result]
   {:jsonrpc "2.0"
    :id id
    :result result})
  ([id error-code error-message]
   {:jsonrpc "2.0"
    :id id
    :error {:code error-code
            :message error-message}}))

(defn create-notification
  "Create a JSON-RPC notification"
  [method params]
  {:jsonrpc "2.0"
   :method method
   :params params})

(defmulti handle-request
  "Handle incoming MCP requests based on method"
  :method)

(defmethod handle-request "initialize"
  [{:keys [id params]}]
  (create-response id
                   {:protocolVersion protocol-version
                    :capabilities {:tools {:listChanged false}}
                    :serverInfo server-info}))

(defmethod handle-request "initialized"
  [{:keys [id]}]
  (when id (create-response id {})))

(defmethod handle-request "tools/list"
  [{:keys [id]}]
  (create-response id {:tools (vals tools)}))

(defmethod handle-request "tools/call"
  [{:keys [id params]}]
  (try
    (let [{:keys [name arguments]} params]
      (case name
        "estimate_cost"
        (let [{:keys [text cost_per_million_tokens]} arguments
              result (mcp/estimate-cost {:text text :cost-per-million cost_per_million_tokens})]
          (create-response id
                           {:content [{:type "text"
                                       :text (format "Token count: %d\nTotal cost: $%.6f"
                                                     (:token-count result)
                                                     (:total-cost result))}]}))
        (create-response id -32601 (str "Unknown tool: " name))))
    (catch Exception e
      (create-response id -32000 (str "Tool execution error: " (.getMessage e))))))

(defmethod handle-request :default
  [{:keys [id method]}]
  (create-response id -32601 (str "Unknown method: " method)))

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
        (let [response (handle-request request)]
          (when response
            (write-message response))))
      (recur))))

(defn -main
  "Main entry point for MCP server"
  [& _args]
  (start-server))
