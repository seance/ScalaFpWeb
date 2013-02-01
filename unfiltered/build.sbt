organization := "fpweb"

name := "unfiltered-todo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-filter" % "0.6.7",
  "net.databinder" %% "unfiltered-jetty" % "0.6.7",
  "net.databinder" %% "unfiltered-json" % "0.6.7"
)

resolvers ++= Seq(
  "java m2" at "http://download.java.net/maven/2"
)
