val scala3Version = "3.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "ulid-scala3",
    version := "0.1.0-SNAPSHOT",
    organization := "com.github.mutsuhiro6",
    scalaVersion := scala3Version,
    
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"
  )
