# What is it?
A categorical description of the service, for example: “{{name}} is a logging service...”.

# What problem(s) does it strive to solve?
This is the most important part of the document! We need to build services that solve our problems. A good service is clearly pointed at solving one or a few problems. When summarizing a service that you did not build, try to put yourself in the shoes of those who did. What was their mission, what problem(s) were they taking on, and how did they intend to distinguish their solution from others? Try to avoid this being a list of features without connecting those features to problems.

# Core abstractions
What is it about? A good service has a few clearly defined abstractions around which you can build an understanding of its facilities.

# Primary operations
Similarly, a good service has a few well-defined operations (endpoints, Kafka topics, etc) that can be composed into solutions.

# Architectural Components
Are there multiple processes, services or components involved, and how do they fit together? Are there environmental (AWS) or supportive (Kafka, Zookeeper) requirements? Diagrams are welcome here.


# Is it simple?
In the ["Simple Made Easy"](https://www.youtube.com/watch?v=LKtk3HCgTa8) sense: is the service complex? Is it stateful? Is it free of [dependency cycles](https://en.wikipedia.org/wiki/Circular_dependency) with other services?

# Fundamental tradeoffs <!-- Optional -->
Every project has benefits but no project is free of tradeoffs. We need to be explicit about tradeoffs in order to make good decisions, and to prepare compensations. Tradeoffs should be expressed in terms of “gives up X to get Y”, and not just a list of negatives.

## Benefits

## Risks

# Unknowns <!-- Optional -->
After having done the research to prepare this summary, what are the key aspects that are unknown, e.g. performance characteristics under load etc.

# Key indicators for use <!-- Optional -->
In what circumstances is this service a good fit and why?

# Key indicators against use <!-- Optional -->
In what circumstances is this service a poor fit and why?

# Alternatives <!-- Optional -->
Are there alternative services in the same space? In what ways do they differ, e.g. by taking on a slightly different problem set, emphasizing different aspects, or making different tradeoffs?

# Other relevant characteristics <!-- Optional -->
Are there operational (latency, scalability, security) aspects which should be highlighted?

# Resources
Please include links to any high level overviews, design documents, or presentations so people can dive deeper.
