# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Testing
- Run all tests: `clojure -T:build test`
- Tests use `cognitect.test-runner` and are located in the `test/` directory

### Building
- Run CI pipeline (tests + build uberjar): `clojure -T:build ci`
- Built JAR location: `target/mcp-0.1.0-SNAPSHOT.jar`
- Run built JAR: `java -jar target/mcp-0.1.0-SNAPSHOT.jar`

### Running the Application
- Via exec function: `clojure -X:run-x` (default) or `clojure -X:run-x :name '"CustomName"'`
- Via main function: `clojure -M:run-m` (default) or `clojure -M:run-m CustomName`

### MCP Server
- Start MCP server: `clojure -M:mcp-server`
- The server exposes an `estimate_cost` tool that calculates token costs for text

## Project Structure

This is a Clojure application using `tools.build` for build automation and `tools.deps` for dependency management.

### Key Components
- **Main namespace**: `achilles.mcp` - Contains the estimate-cost` function, and `-main` entry point
- **MCP server**: `achilles.mcp-server` - Model Context Protocol server exposing estimate-cost as a tool for LLMs
- **Build configuration**: `build.clj` - Defines test and CI pipeline functions using `clojure.tools.build.api`
- **Dependencies**: `deps.edn` - Standard tools.deps configuration with aliases for running, testing, building, and MCP server

### Architecture
The project follows standard Clojure project conventions:
- Source code in `src/achilles/` 
- Tests in `test/achilles/` mirroring source structure
- Build automation in `build.clj` with separate test and CI functions
- The application can be invoked either as an executable function (`:exec-fn`) or traditional main (`:main-opts`)

### Build System
Uses `clojure.tools.build` with two key build functions:
- `test`: Runs test suite using cognitect test-runner
- `ci`: Full pipeline that runs tests, cleans target, compiles, and builds uberjar
