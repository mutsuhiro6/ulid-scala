val scala3Version = "3.1.0"
val scala2Version = "2.13.7"

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.mutsuhiro6"
ThisBuild / homepage := Some(url("https://github.com/mutsuhiro6/ulid-scala3"))
ThisBuild /  licenses := List("The MIT License" -> url("http://opensource.org/licenses/MIT"))
ThisBuild / developers := List(
    Developer(
      id = "mutsuhiro6",
      name = "Mutsuhiro Iwamoto",
      email = "mutsuhiro6@icloud.com",
      url = url("https://github.com/mutsuhiro6")
    )
  )

lazy val lib = project
  .in(file("lib"))
  .settings(
    name := "ulid-scala3",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases")
    ),
    Test / publish / skip := true
  )

lazy val benchmark = project
  .in(file("benchmark"))
  .settings(
    name := "ulid-scala3-benchmark",
    publish / skip := true,
    libraryDependencies ++= Seq(
      "com.chatwork" % "scala-ulid_2.13" % "1.0.24",
      "org.wvlet.airframe" %% "airframe-ulid" % "21.12.1",
      "net.petitviolet" % "ulid4s_2.13" % "0.5.0",
      "de.huxhorn.sulky" % "de.huxhorn.sulky.ulid" % "8.3.0"
    ),
    scalaVersion := scala3Version,
    crossScalaVersions := Seq(scala3Version, scala2Version)
  )
  .enablePlugins(JmhPlugin)
  .dependsOn(lib)

lazy val exampleScala3 = project
  .in(file("example/scala_3"))
  .settings(
    name := "ulid-scala3-example_scala3",
    publish / skip := true,
    scalaVersion := scala3Version
  )
  .dependsOn(lib)

lazy val exampleScala213 = project
  .in(file("example/scala_2.13"))
  .settings(
    name := "ulid-scala3-example_scala2.13",
    publish / skip := true,
    scalaVersion := scala2Version,
    scalacOptions += "-Ytasty-reader"
  )
  .dependsOn(lib)

lazy val root = project
  .in(file("."))
  .settings(name := "ulid-scala3-root")
  .aggregate(lib)
