package com.charlieworld.housing.routes

import akka.http.scaladsl.server.Route
import com.charlieworld.housing.AppSuite
import com.charlieworld.housing.services.HousingFinanceService

trait HousingFinanceRoute extends BaseRoute {
  this: HousingFinanceService with AppSuite ⇒

  def housingFinanceRoutes: Route =
    pathPrefix("housing-finance") {
      pathPrefix("most-supported-institute") {
        (get & pathEnd) {
          runComplete(findTopOneYearlyAmount())
        }
      } ~ pathPrefix("institute") {
        pathPrefix("min-and-max-annual-amount") {
          (get & pathEnd & parameter("institute-name")) { instituteName ⇒
            runComplete(findMinAndMaxYearlyAvgAmount(instituteName))
          }
        }
      } ~ pathPrefix("init") {
        (post & pathEnd) {
          runComplete(saveHousingFinanceData())
        }
      }
    }
}
