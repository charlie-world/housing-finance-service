import Dependencies._

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.charlieworld",
      scalaVersion := "2.12.7"
    )
  ),
  name := "housing-finance-service",
  version := "1.0.0",
  scalacOptions ++= Seq(
    "-unchecked",
    "-feature",
    "-deprecation",
    "-language:postfixOps",
    "-language:implicitConversions"
  ),
  libraryDependencies ++= Seq(
    testDependencies,
    akkaHttpDependencies,
    serializationDependencies,
    coreDependencies,
    loggingDependencies,
    authenticationDependencies,
    databaseDependencies,
  ).flatten,
  scalafmtOnCompile := true,
  autoCompilerPlugins := true,
  assemblyJarName in assembly := "app.jar"
)
