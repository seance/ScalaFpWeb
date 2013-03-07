Scala Web study
===============

A small study of various Scala web frameworks/toolkits for implementing a RESTful API, while keeping an eye open for FP (Functional Programming) opportunities.

The `frontend` directory contains a Bacon.js / Backbone.js based frontend for the Todos application, copied from [pyykkis](https://github.com/pyykkis)'s TodoMVC example. The other directories contain backend implementations using the various frameworks.

You can serve the frontend directory from any HTTP server on port 7000, or use curl to test the backends. Each backend exercises the basic todos API, with CORS and browser extensions for exploring composability.

Scalatra
--------

_"Scalatra is a simple, accessible and free web micro-framework. It combines the power of the JVM with the beauty and brevity of Scala, helping you quickly build high-performance web sites and APIs."_

### Factoids
* Strongly influenced by Sinatra framework for Ruby
* Servlet/Filter based - proven stability as part of core philosophy
* HTTP oriented, in design and implementation
* Mutable state based routing definition DSL
* Available for Scala 2.10
* Actively developed by team

### Opinions
Simple, effective and proven, yet rather only embraces Scala's OO nature without much FP utility. This is of course in line with Sinatra's Ruby language, but makes functional composition more difficult.

Finagle
-------

_"Finagle is an extensible network stack for the JVM, used to construct highly concurrent servers. Finagle implements uniform client and server APIs for several protocols, and is designed for high performance and concurrency. Most of Finagle’s code is protocol agnostic, simplifying the implementation of new protocols."_

### Factoids
* Protocol agnostic; not only HTTP
* Futures based on own abstraction; influenced the Scala 2.10 standard
* For now, only for Scala 2.9
* Developed by Twitter

### Opinions
Finagle's flexibility also means some extra complexity is accrued for implementing a simple HTTP/REST API. Some of Netty's Java idiosyncrasies bleed through to the otherwise nice clean Scala DSL.

Lift
----

_"Lift is the most powerful, most secure web framework available today. There are Seven Things that distinguish Lift from other web frameworks."_

### Factoids
* One of the most established Scala web frameworks
* Servlet/Filter based
* Emphasis on server templated web applications, with many supporting modules
* Own implementations for Actors, Futures, etc
* For now, only for Scala 2.9
* Developed by David Pollak & team

### Opinions
Conceived in the early days of Scala, Lift suffers somewhat from duplicating functionality refined in smaller, more focused libraries. Lift has however pioneered the way for later frameworks, not least with its popular JSON library. Lift's process for creating new projects and some of its syntactic and organizational choices may raise some eyebrows nowadays.

Play Framework 2.1
------------------

_"The High Velocity Web Framework For Java and Scala"_

### Factoids
* More a full platform than library - server, classloader, compiler
* Now part of the Typesafe Stack from Typesafe - the company with Scala's creator Martin Odersky
* Integrates with Akka, also part of the Typesafe Stack
* Incorporates advanced features, such as Web Sockets and Iteratee I/O
* Quick development turnaround with on-the-fly compile and redeploy
* Available for Scala 2.10
* Developed by Guillaume Bort & Typesafe

### Opinions
Play Framework elbowed its way into the JVM web scene quite spectacularly, winning solid recognition in the Scala world by being adopted as a part of the Typesafe umbrella in its version 2.x incarnation. Very practically oriented, Play's external routing DSL has some limitations compared with some of the latest crop of pure Scala frameworks, especially in functional composition.

Unfiltered
----------

_"Unfiltered is a toolkit for servicing HTTP requests in Scala. It provides a consistent vocabulary for handing requests on various server backends, without impeding direct access to their native interfaces."_

### Factoids
* Minimalistic HTTP toolkit; well suited for REST APIs
* Modular structure for request response cycle Servlets on Jetty, async with Netty
* Available for Scala 2.10
* Developed by Nathan Hamblen & contributors

### Opinions
Unfiltered's elegant and modular structure provides a highly composable set of abstractions. The DSL is very idiomatic Scala. With Unfiltered, you will however need to hand-pick your own stack for more expansive applications.

BlueEyes
--------

_"A lightweight Web 3.0 framework for Scala, featuring a purely asynchronous architecture, extremely high-performance, massive scalability, high usability, and a functional, composable design."_

### Factoids
* Full stack web framework for building RESTful services
* Unifies the programming model by taking the hardest path - async byte streams
* Inspired by Sinatra and Scalatra, but quite different
* Implementation utilizes some bleeding edge stuff e.g. from Scalaz
* Unfortunately, somewhat lacking in documentation and subject to rapid change
* For now, only for Scala 2.9
* Developed by John A. De Goes & contributors

### Opinions
BlueEyes' concept brings delicious FP to Scalatra's lightweight syntax. However, BlueEyes' "hard mode" asynchronous byte stream approach, combined with intricate type level manipulation unfortunately leads to somewhat brittle compositions, while necessitating an advanced level of understanding of Scala's type system. A more up-to-date and complete documentation would be a great boon to hopeful BlueEyes adopters.

Spray
-----

_"A suite of scala libraries for building and consuming RESTful web services on top of Akka: lightweight, asynchronous, non-blocking, actor-based, testable"_

### Factoids
* Especially suited for RESTful services
* Brings first-class integration with Akka as a key feature
* Borrows many concepts from BlueEyes
* Highly modular, exceptionally lucid structure
* Available for Scala 2.10
* Developed by Mathias Doenitz & contributors

### Opinions
Spray feels like BlueEyes' somewhat more mature sibling. Forgoing async byte streams as the one truth, Spray sidesteps some of the hard problems faced by BlueEyes. The project's neat modular structure lends to understanding it. Spray's model embraces FP - this is evident in how the main service abstraction builds a kind of a monadic IO action, encoded as a `RequestContext => Unit` (`main :: IO ()`, anyone?). The documentation is rather feature-oriented, while a good tutorial seems hard to find.
