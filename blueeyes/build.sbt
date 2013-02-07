organization := "fpweb"

name := "blueeyes-todo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "com.github.jdegoes" %% "blueeyes-core"  % "0.6.0",
  "com.github.jdegoes" %% "blueeyes-json"  % "0.6.0",
  "ch.qos.logback" % "logback-classic" % "1.0.0" % "runtime"
)

resolvers ++= Seq(
  "Sonatype" at "http://oss.sonatype.org/content/repositories/public",
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"
)
