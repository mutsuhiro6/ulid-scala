val scala3Version = "3.1.0"

lazy val lib = project
  .in(file("lib"))
  .settings(
    name := "ulid-scala3",
    version := "0.1.0-SNAPSHOT",
    organization := "com.github.mutsuhiro6",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"
  )

// For benchmark module
val scala2Version = "2.13.6"

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

lazy val example = project
  .in(file("example"))
  .settings(
    name := "ulid-scala3-example",
    publish / skip := true,
     libraryDependencies ++= Seq(
      "com.chatwork" % "scala-ulid_2.13" % "1.0.24",
      "org.wvlet.airframe" %% "airframe-ulid" % "21.12.1",
      "de.huxhorn.sulky" % "de.huxhorn.sulky.ulid" % "8.3.0"
    ),
    scalaVersion := scala3Version,
    crossScalaVersions := Seq(scala3Version, scala2Version)
  )
  .dependsOn(lib)

lazy val root = project
  .in(file("."))
  .settings(name := "ulid-scala3-root")
  .aggregate(lib)
