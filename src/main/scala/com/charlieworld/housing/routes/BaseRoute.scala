package com.charlieworld.housing.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.charlieworld.housing.entities.{
  HousingFinanceDataResponse,
  TopOneYearlyAmountResponse,
  YearlyAvgAmountResponse
}
import monix.eval.Task
import monix.execution.Scheduler
import com.charlieworld.housing.serialization.JsonProtocol._

import scala.util.{Failure, Success}

trait BaseRoute extends Directives with SprayJsonSupport {

  def runComplete[_](task: Task[_])(implicit scheduler: Scheduler): Route =
    onComplete(task.runAsync) {
      case Success(result: HousingFinanceDataResponse) ⇒ complete(result)
      case Success(result: TopOneYearlyAmountResponse) ⇒ complete(result)
      case Success(result: YearlyAvgAmountResponse) ⇒ complete(result)
      case Failure(ex) ⇒ complete(StatusCodes.InternalServerError, ex.getMessage)
    }
}
