# mcp

Vibe-coded MCP server using Claude Code. 

## Journal

### Initial Prompts
I started with the following prompts:

```
generate an "estimate-cost" function in mcp.clj that accepts a string of text
and dollar cost per million tokens given as a BigDecimal, and returns a map with
the token count and total cost. The function should use the clojure "tolkien"
library to count the tokens in the string.
```


```
Update the estimate-cost function to convert the cost-per-million-tokens to a
BigDecimal. Assume it is a float or a double.
```

```
Create basic MCP (Model Context Protocol) server that exposes this
estimate-cost function as a tool for LLMs to use.
```


```
fix the dependency for the tolkien library by following the instructions on the
Github page https://github.com/lukaszkorecki/tolkien
```

### Manually Fixed Bugs:
- unnecessary casting of `cost-per-million` to string
- BigDecimal. instead of BigDecimal/valueOf 
- division yielded a clojure.lang.Ratio, which can't be given to BigDecimal/valueOf 
- unused import of clojure.core.async
- unused `create-notification` function

### Code Changes
Prompt code changes to make the signature of estimate-cost more versatile. 
I want to call it via exec from deps.edn or from code.

```
Change the estimate-cost function to accept a map rather than positional arguments. Update all calls to estimate-cost, including in deps.edn
```



