(ns achilles.mcp
  (:require [tolkien.core :as token]
            [clojure.pprint :refer [pprint]])
  (:gen-class))

(def ^:const ClaudeSonnetCost
  "Cost in dollars per 1MM tokens for Claude Sonnet 4."
  3.00)

(defn estimate-cost
  "Estimates cost for text based on token count and cost per million tokens.
  Args:
    {:keys [text cost-per-million]} - Map containing:
      text - (String) The text for which to count tokens.
      cost-per-million - (double) Cost per million tokens in dollars.
  
  Returns:
    Map with :token-count and :total-cost keys"
  [{:keys [text cost-per-million]}]
  (let [token-count (token/count-tokens "gpt-3.5-turbo" text)
        cost-bd    (BigDecimal/valueOf cost-per-million)
        cost-ratio (BigDecimal/valueOf (/ token-count 1000000.0))
        total-cost (.multiply cost-bd cost-ratio)]
    {:token-count token-count
     :total-cost total-cost}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [text (first args)]
    (pprint (estimate-cost {:text text :cost-per-million ClaudeSonnetCost}))))
