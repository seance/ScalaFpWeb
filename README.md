Scala Web study
===============

A small study of various Scala web frameworks/toolkits for implementing a RESTful API, while keeping an eye open for FP (Functional Programming) opportunities.

The `frontend` directory contains a Bacon.js / Backbone.js based frontend for the Todos application, copied from [pyykkis](https://github.com/pyykkis)'s TodoMVC example. The other directories contain backend implementations using the various frameworks.

You can serve the frontend directory from any HTTP server on port 7000, or use curl to test the backends. Each backend exercises the basic todos API, with CORS and browser extensions for exploring composability.

Scalatra
--------------------

_"Scalatra is a simple, accessible and free web micro-framework. It combines the power of the JVM with the beauty and brevity of Scala, helping you quickly build high-performance web sites and APIs."_

### Factoids
* Strongly influenced by Sinatra framework for Ruby
* Servlet/Filter based - proven stability as part of core philosophy
* HTTP oriented, in design and implementation
* Mutable state based routing definition DSL

### Opinions
Simple, effective and proven, yet rather only embraces Scala's OO nature without much FP utility. This is of course in line with Sinatra's Ruby language, but makes functional composition more difficult.

Finagle
--------------------

_"Finagle is an extensible network stack for the JVM, used to construct highly concurrent servers. Finagle implements uniform client and server APIs for several protocols, and is designed for high performance and concurrency. Most of Finagle’s code is protocol agnostic, simplifying the implementation of new protocols."_

### Factoids
### Opinions
