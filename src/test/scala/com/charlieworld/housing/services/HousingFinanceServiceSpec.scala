package com.charlieworld.housing.services

import com.charlieworld.housing.entities._
import monix.execution.Scheduler.Implicits.global
import org.scalatest.{FlatSpecLike, Matchers}

import scala.util.{Failure, Success}

class HousingFinanceServiceSpec extends Matchers with FlatSpecLike {

  object impl extends HousingFinanceServiceImpl with MockFileRead with MockRepository

  "findMinAndMaxYearlyAvgAmount" should "return YearlyAvgAmountResponse with min and max values of amount" in {
    impl.findMinAndMaxYearlyAvgAmount("주택금융공사").foreach { result ⇒
      result shouldBe YearlyAvgAmountResponse(
        "주택금융공사",
        Fixtures.bankYearlyCreditGuaratee.map(y ⇒ YearlyAmountResponse(y.year, y.totalAmount))
      )
    }
  }

  "findTopOneYearlyAmount" should "return TopOneYearlyAmountResponse" in {
    impl.findTopOneYearlyAmount().foreach { result ⇒
      result shouldBe TopOneYearlyAmountResponse(2019, "주택금융공사")
    }
  }

  "instituteYearlyAmountsToResponseModel" should "return YearlyTotalAmountResponse which transformed from InstituteYearlyAmount" in {
    impl.instituteYearlyAmountsToResponseModel(Fixtures.instituteYearlyAmounts) shouldBe Seq(
      YearlyTotalAmountResponse(2018, 3100L, Map("A" → 1000L, "B" → 1200L, "C" → 900L)),
      YearlyTotalAmountResponse(2019, 2500L, Map("A" → 800L, "B" → 1000L, "C" → 700L)),
      YearlyTotalAmountResponse(2020, 300L, Map("A" → 200L, "B" → 100L)),
    )
  }

  it should "return empty seq when instituteYearlyAmounts is empty seq" in {
    impl.instituteYearlyAmountsToResponseModel(Seq.empty) shouldBe Seq.empty
  }

  "saveYearlyAndMonthlyCreditGuarantee" should "return seq of InstituteYearlyAmount" in {
    impl
      .saveYearlyAndMonthlyCreditGuarantee(
        Fixtures.instituteId,
        Fixtures.instituteName,
        Fixtures.fileRows
      )
      .foreach { result ⇒
        result shouldBe Seq(InstituteYearlyAmount(Fixtures.instituteName, 2018, 500L))
      }
  }

  it should "return empty seq if es is empty" in {
    impl
      .saveYearlyAndMonthlyCreditGuarantee(Fixtures.instituteId, Fixtures.instituteName, Seq.empty)
      .foreach { result ⇒
        result shouldBe Seq.empty
      }
  }

  it should "throw saveMonthlyException" in {
    impl
      .saveYearlyAndMonthlyCreditGuarantee(2L, Fixtures.instituteName, Seq.empty)
      .runAsync
      .onComplete {
        case Success(_) ⇒ ()
        case Failure(exception) ⇒ exception shouldBe Fixtures.saveMonthlyException
      }
  }

  it should "throw saveYearlyException" in {
    impl
      .saveYearlyAndMonthlyCreditGuarantee(3L, Fixtures.instituteName, Seq.empty)
      .runAsync
      .onComplete {
        case Success(_) ⇒ ()
        case Failure(exception) ⇒ exception shouldBe Fixtures.saveYearlyException
      }
  }
}
