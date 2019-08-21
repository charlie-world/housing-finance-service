package com.charlieworld.housing

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{HttpApp, Route}
import com.charlieworld.housing.data.MysqlDatabaseConfiguration
import com.charlieworld.housing.data.repositories.{HousingFinanceRepositoryImpl, UserRepositoryImpl}
import com.charlieworld.housing.routes.{HousingFinanceRoute, UserRoute}
import com.charlieworld.housing.services.{HousingFinanceServiceImpl, UserServiceImpl}
import com.charlieworld.housing.utils.{CryptoImpl, FileReadImpl}
import monix.execution.Scheduler
import slick.jdbc.MySQLProfile.api.Database

import scala.concurrent.ExecutionContext

object Application
  extends HttpApp
  with App
  with Authentication
  with HousingFinanceRoute
  with UserRoute
  with UserServiceImpl
  with CryptoImpl
  with UserRepositoryImpl
  with HousingFinanceServiceImpl
  with FileReadImpl
  with HousingFinanceRepositoryImpl
  with MysqlDatabaseConfiguration
  with AppSuite {

  implicit val actor: ActorSystem = ActorSystem("housing-finance-service")
  implicit val ec: ExecutionContext = actor.dispatcher
  implicit protected val s: Scheduler = Scheduler(ec)
  override protected val mysql: Database = Database.forConfig("mysql")

  override def routes: Route =
    pathEndOrSingleSlash {
      complete("housing finance service\n")
    } ~ pathPrefix("api") {
      pathPrefix("v1") {
        auth { _ =>
          housingFinanceRoutes
        } ~ userRoutes
      }
    }

  startServer("0.0.0.0", 8080)
}
