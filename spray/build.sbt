organization := "fpweb"

name := "spray-todo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.0"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.10" % "2.1.1",
  "io.spray" % "spray-can" % "1.1-M7",
  "io.spray" % "spray-routing" % "1.1-M7",
  "io.spray" % "spray-json_2.10" % "1.2.3"
)

javaOptions in run ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n")

fork in run := true