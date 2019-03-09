import sbt._

object Dependencies {
  lazy val dependencies: Seq[ModuleID] =
    Seq(
      scalaReflect, scalatest, scalacheck, pprint, pureConfig, logback,
      cats, simulacrum, monocle, shapeless, kittens,
      betterFiles, circe, protobuf, avro4s, avroTools, monixKafka
    ).flatten

  lazy val scalaReflect: Seq[ModuleID] = Seq(
    "org.scala-lang" % "scala-reflect" % BuildProperties("scala.version")
  )

  lazy val scalatest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test, it"
  )

  lazy val scalacheck: Seq[ModuleID] = Seq(
    "org.scalacheck" %% "scalacheck" % "1.14.0" % "test, it"  
  )

  lazy val pprint: Seq[ModuleID] = Seq(
    "com.lihaoyi" %% "pprint" % "0.5.3"
  )
  
  lazy val pureConfig: Seq[ModuleID] = {
    val version = "0.10.2"

    Seq(
      "com.github.pureconfig" %% "pureconfig",
      "com.github.pureconfig" %% "pureconfig-http4s"
    ).map(_ % version)
  }

  lazy val logback: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )

  lazy val cats: Seq[ModuleID] = {
    val version = "1.6.0"

    Seq(
      "org.typelevel" %% "cats-laws",
      "org.typelevel" %% "cats-testkit"
    ).map(_ % version % "test, it") ++ Seq(
      "org.typelevel" %% "cats-core"
    ).map(_ % version) ++ Seq(
      "org.typelevel" %% "cats-effect" % "1.2.0"
    )
  }

  lazy val simulacrum: Seq[ModuleID] = Seq(
    "com.github.mpilquist" %% "simulacrum" % "0.15.0"
  )

  lazy val monocle: Seq[ModuleID] = {
    val version = "1.5.0"

    Seq(
      "com.github.julien-truffaut" %% "monocle-law"
    ).map(_ % version % "test, it") ++ Seq(
      "com.github.julien-truffaut" %% "monocle-core",
      "com.github.julien-truffaut" %% "monocle-macro",
      "com.github.julien-truffaut" %% "monocle-generic"
    ).map(_ % version)
  }

  lazy val shapeless: Seq[ModuleID] = Seq(
    "com.chuusai" %% "shapeless" % "2.3.3"
  )

  lazy val kittens: Seq[ModuleID] = Seq(
    "org.typelevel" %% "kittens" % "1.2.1"
  )

  lazy val betterFiles: Seq[ModuleID] = Seq(
    "com.github.pathikrit" %% "better-files" % "3.7.1"
  )

  lazy val circe: Seq[ModuleID] = {
    val version = "0.11.1"

    Seq(
      "io.circe" %% "circe-testing",
      "io.circe" %% "circe-literal"
    ).map(_ % version % "test, it") ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-generic-extras",
      "io.circe" %% "circe-parser",
      "io.circe" %% "circe-refined"
    ).map(_ % version)
  }

  lazy val protobuf: Seq[ModuleID] = Seq(
    "com.google.protobuf" % "protobuf-java" % "3.7.0-rc1" force()
  )
  
  lazy val avro4s: Seq[ModuleID] = {
    val version = "2.0.4"

    Seq(
      "com.sksamuel.avro4s" %% "avro4s-core",
      "com.sksamuel.avro4s" %% "avro4s-kafka"
    ).map(_ % version)
  }
  
  lazy val avroTools: Seq[ModuleID] = Seq(
    "org.apache.avro" % "avro-tools" % "1.8.2"
  )

  lazy val monixKafka: Seq[ModuleID] = Seq(
    "io.monix" %% "monix-kafka-1x" % "1.0.0-RC2"
  )
}