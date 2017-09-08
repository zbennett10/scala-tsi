// import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport.scalafmtTestOnCompile
import sbt.Keys.scalacOptions

name := "scala-ts-compiler"
version := "0.1-SNAPSHOT"

// TODO: Different versions between 2.11 and 2.12
lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-deprecation",
  "-Xlint",
  "-encoding", "UTF8",
  "-target:jvm-1.8",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-language:experimental.macros"
) //++
  // Scala 2.11 only settings
  // Seq("-Ydelambdafy:method", "-Ybackend:GenBCode","-Xsource:2.12", "-Ywarn-unused", "-Ywarn-unused-import")

lazy val commonSettings = Seq(
  scalaVersion := "2.12.3",
  organization := "nl.codestar",
  scalacOptions ++= compilerOptions,
  // Code formatting
  //scalafmtOnCompile in Compile := true,
  //scalafmtTestOnCompile in Compile := true
)

lazy val macros = (project in file("macros"))
    .settings(
      commonSettings,
      libraryDependencies += Def.setting { "org.scala-lang" % "scala-reflect" % scalaVersion.value }.value
    )

lazy val root = (project in file("."))
    .dependsOn(macros)
    .settings(
      commonSettings,
      libraryDependencies ++= dependencies
    )

lazy val plugin = (project in file("plugin"))
    .dependsOn(macros, root)
    .settings(
      commonSettings ++ (sbtPlugin := true),
      libraryDependencies += "org.clapper" %% "classutil" % "1.1.2"
    )

lazy val dependencies = Seq(
  "com.github.scopt" %% "scopt"          % "3.7.0",
  "org.scalatest"    %% "scalatest"      % "3.0.1"            % "test"
)

addCommandAlias("generate-typescript", "runMain GenerateTypescript")
