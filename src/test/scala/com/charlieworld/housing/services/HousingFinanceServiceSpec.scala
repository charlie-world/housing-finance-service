package com.charlieworld.housing.services

import com.charlieworld.housing.entities._
import monix.execution.Scheduler.Implicits.global
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class HousingFinanceServiceSpec extends Matchers with FlatSpecLike {

  object impl extends HousingFinanceServiceImpl with MockFileRead with MockRepository

  implicit val timeout: Duration = 5 seconds

  "findMinAndMaxYearlyAvgAmount" should "return YearlyAvgAmountResponse with min and max values of amount" in {
    Await.result(
      impl.findMinAndMaxYearlyAvgAmount("주택금융공사").runAsync,
      timeout
    ) shouldBe YearlyAvgAmountResponse(
      "주택금융공사",
      Fixtures.summaries.map(y ⇒ YearlyAmountResponse(y.year, y.avgAmount))
    )
  }

  "findTopOneYearlyAmount" should "return TopOneYearlyAmountResponse" in {
    Await.result(
      impl.findTopOneYearlyAmount().runAsync,
      timeout
    ) shouldBe TopOneYearlyAmountResponse(2019, "주택금융공사")
  }

  "summariesToResponse" should "return YearlyTotalAmountResponse which transformed from Summary" in {
    impl.summariesToResponse(Fixtures.summaries, Fixtures.allInstitutes) shouldBe HousingFinanceDataResponse(
      HousingFinanceService.HOUSING_FINANCE_DATA_NAME,
      Seq(
        YearlyTotalAmountResponse(2018, 3000L, Map("주택금융공사" → 3000L)),
        YearlyTotalAmountResponse(2019, 1000L, Map("주택금융공사" → 1000L)),
      )
    )
  }

  it should "return empty seq when instituteYearlyAmounts is empty seq" in {
    impl.summariesToResponse(Seq.empty, Fixtures.allInstitutes) shouldBe HousingFinanceDataResponse(
      HousingFinanceService.HOUSING_FINANCE_DATA_NAME,
      Seq.empty
    )
  }

  "upsertCreditGuarantee" should "return upserted institute id and year" in {
    Await.result(
      impl.upsertCreditGuarantee(Fixtures.fileRows, Fixtures.allInstitutes).runAsync,
      timeout
    ) shouldBe Seq((1L, 2019), (1L, 2018))
  }

  "upsertSummary" should "return summary if success to upsert summary" in {
    Await.result(
      impl.upsertSummary(Fixtures.instituteId, 2018).runAsync,
      timeout
    ) shouldBe Fixtures.summaries.head
  }
}
