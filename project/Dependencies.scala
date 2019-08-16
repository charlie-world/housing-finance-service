import sbt._

object Dependencies {
  private lazy val scalaTestVersion = "3.0.5"
  private lazy val monixVersion = "2.3.0"
  private lazy val akkaHttpVersion = "10.0.11"
  private lazy val akkaVersion = "2.5.11"
  private lazy val slf4jVersion = "1.7.25"
  private lazy val jwtAuthVersion = "3.8.2"
  private lazy val slickVersion = "3.3.0"
  private lazy val mysqlConnectorVersion = "8.0.17"
  
  lazy val testDependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest"
  ).map(_ % scalaTestVersion)

  lazy val akkaHttpDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http",
  ).map(_ % akkaHttpVersion)

  lazy val serializationDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http-spray-json"
  ).map(_ % akkaHttpVersion)

  lazy val coreDependencies: Seq[ModuleID] = Seq(
    "io.monix" %% "monix",
    "io.monix" %% "monix-cats"
  ).map(_ % monixVersion)
  
  lazy val loggingDependencies: Seq[ModuleID] = Seq(
    "org.slf4j" % "slf4j-api"
  ).map(_ % slf4jVersion)

  lazy val authenticationDependencies: Seq[ModuleID] = Seq(
    "com.auth0" % "java-jwt"
  ).map(_ % jwtAuthVersion)

  lazy val databaseDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.slick" %% "slick",
    "com.typesafe.slick" %% "slick-hikaricp"
  ).map(_ % slickVersion) ++ Seq(
    "mysql" % "mysql-connector-java"
  ).map(_ % mysqlConnectorVersion)
}
