name := "finagle-todo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "com.twitter" %% "finagle-http" % "6.2.0",
    "net.liftweb" %% "lift-json" % "2.5-RC1"
)
