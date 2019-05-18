import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._
import Dependencies._
import sbt._

lazy val IT = config("it") extend Test

lazy val root = project("kafka-demo", file("."))

def project(id: String, base: File): Project =
  Project(id, base)
    .enablePlugins(JavaAppPackaging)
    .configs(IT)
    .settings(description := "Kafka Demo in Scala")
    .settings(promptTheme := com.scalapenos.sbt.prompt.PromptThemes.ScalapenosTheme)
    .settings(inConfig(IT)(Defaults.testSettings))
    .settings(Defaults.itSettings)
    .settings(javaOptions in Test ++= Seq("-Dconfig.resource=application.test.conf"))
    .settings(javaOptions in IT ++= Seq("-Dconfig.resource=application.it.conf"))
    .settings(
      resolvers ++= Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots"),
        Resolver.bintrayRepo("cakesolutions", "maven"),
        "Artima Maven Repository" at "http://repo.artima.com/releases",
        "jitpack" at "https://jitpack.io",
        "Confluent Platform Maven" at "http://packages.confluent.io/maven/"
      ),
      scalaVersion := BuildProperties("scala.version"),
      sbtVersion := BuildProperties("sbt.version"),
      organization := "com.backwards",
      name := id,
      autoStartServer := false,
      addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9"),
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
      libraryDependencies ++= dependencies,
      fork in Test := true,
      fork in IT := true,
      fork in run := true,
      javaOptions ++= Seq("-Xss256M", "-Xmx1G"),
      scalacOptions ++= Seq("-Ypartial-unification")
    )