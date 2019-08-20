package com.charlieworld.housing.routes

import com.charlieworld.housing.data.persistance.entities.{Institute, Summary}
import com.charlieworld.housing.entities.{
  HousingFinanceDataResponse,
  HousingFinanceFileEntity,
  TopOneYearlyAmountResponse,
  YearlyAmountResponse,
  YearlyAvgAmountResponse,
  YearlyTotalAmountResponse
}
import com.charlieworld.housing.services.HousingFinanceService
import monix.eval.Task

trait MockHousingFinanceService extends HousingFinanceService {
  override def findMinAndMaxYearlyAvgAmount(instituteName: String): Task[YearlyAvgAmountResponse] =
    Task.pure(
      YearlyAvgAmountResponse(
        "국민은행",
        Seq(
          YearlyAmountResponse(2017, 60L),
          YearlyAmountResponse(2019, 120L),
        )
      )
    )

  override def findTopOneYearlyAmount(): Task[TopOneYearlyAmountResponse] =
    Task.pure(TopOneYearlyAmountResponse(2019, "국민은행"))

  override def saveHousingFinanceData(): Task[HousingFinanceDataResponse] =
    Task.pure(
      HousingFinanceDataResponse(
        "test",
        Seq(
          YearlyTotalAmountResponse(2018, 100L, Map("국민은행" -> 100L))
        )
      )
    )

  override def summariesToResponse(
    summaries: Seq[Summary],
    institutes: Seq[Institute]
  ): HousingFinanceDataResponse = ???

  override def upsertCreditGuarantee(
    es: Seq[HousingFinanceFileEntity],
    institutes: Seq[Institute]
  ): Task[Seq[(Long, Int)]] = ???

  override def upsertSummary(institueId: Long, year: Int): Task[Summary] = ???
}
