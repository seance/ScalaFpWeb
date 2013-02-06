organization := "fpweb"

name := "unfiltered-todo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
  "net.databinder" % "unfiltered-filter_2.10" % "0.6.7",
  "net.databinder" % "unfiltered-jetty_2.10" % "0.6.7",
  "net.databinder" % "unfiltered-json_2.10" % "0.6.7"
)

resolvers ++= Seq(
  "java m2" at "http://download.java.net/maven/2"
)
