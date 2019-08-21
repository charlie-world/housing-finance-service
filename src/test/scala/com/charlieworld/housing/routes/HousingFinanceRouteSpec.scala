package com.charlieworld.housing.routes

import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.charlieworld.housing.AppSuite
import com.charlieworld.housing.entities.{
  HousingFinanceDataResponse,
  TopOneYearlyAmountResponse,
  YearlyAmountResponse,
  YearlyAvgAmountResponse,
  YearlyTotalAmountResponse
}
import com.charlieworld.housing.routes.mock.MockHousingFinanceService
import monix.execution.Scheduler
import org.scalatest.{FlatSpecLike, Matchers}
import com.charlieworld.housing.serialization.JsonProtocol._

class HousingFinanceRouteSpec
  extends Matchers
  with FlatSpecLike
  with ScalatestRouteTest
  with HousingFinanceRoute
  with AppSuite
  with MockHousingFinanceService {

  override implicit protected val s: Scheduler = monix.execution.Scheduler.global

  "GET /housing-finance/most-supported-institute" should "return most supported institute with year" in {
    Get("/housing-finance/most-supported-institute") ~> housingFinanceRoutes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[TopOneYearlyAmountResponse] shouldBe TopOneYearlyAmountResponse(2019, "국민은행")
    }
  }

  "GET /housing-finance/institute/min-and-max-annual-amount?institute-name=국민은행" should "return min and max annual amount of institute" in {
    val uri = Uri("/housing-finance/institute/min-and-max-annual-amount").withQuery(
      Query(Map("institute-name" -> "국민은행"))
    )
    Get(uri) ~> housingFinanceRoutes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[YearlyAvgAmountResponse] shouldBe YearlyAvgAmountResponse(
        "국민은행",
        Seq(
          YearlyAmountResponse(2017, 60L),
          YearlyAmountResponse(2019, 120L),
        )
      )
    }
  }

  "POST /housing-finance/init" should "return " in {
    Post("/housing-finance/init") ~> housingFinanceRoutes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[HousingFinanceDataResponse] shouldBe HousingFinanceDataResponse(
        "test",
        Seq(
          YearlyTotalAmountResponse(2018, 100L, Map("국민은행" -> 100L))
        )
      )
    }
  }
}
