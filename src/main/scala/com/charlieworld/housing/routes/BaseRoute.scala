package com.charlieworld.housing.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.charlieworld.housing.Logging
import com.charlieworld.housing.entities.{HousingFinanceDataResponse, JWTResponse, TopOneYearlyAmountResponse, YearlyAvgAmountResponse}
import com.charlieworld.housing.serialization.JsonProtocol._
import monix.eval.Task
import monix.execution.Scheduler

import scala.util.{Failure, Success}

trait BaseRoute extends Directives with SprayJsonSupport { this: Logging ⇒
  def runComplete[_](task: Task[_])(implicit scheduler: Scheduler): Route =
    onComplete(task.runAsync) {
      case Success(result: HousingFinanceDataResponse) ⇒ complete(result)
      case Success(result: TopOneYearlyAmountResponse) ⇒ complete(result)
      case Success(result: YearlyAvgAmountResponse) ⇒ complete(result)
      case Success(result: JWTResponse) ⇒ complete(result)
      case Failure(ex) ⇒
        logger.error("Exception thrown", ex)
        complete(StatusCodes.InternalServerError, ex.getMessage)
    }
}
