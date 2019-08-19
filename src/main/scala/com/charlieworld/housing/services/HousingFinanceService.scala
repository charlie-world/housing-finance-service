package com.charlieworld.housing.services

import com.charlieworld.housing.data.persistance.entities.{Institute, Summary}
import com.charlieworld.housing.entities.{
  HousingFinanceDataResponse,
  HousingFinanceFileEntity,
  TopOneYearlyAmountResponse,
  YearlyAvgAmountResponse
}
import monix.eval.Task

trait HousingFinanceService {
  def findTopOneYearlyAmount(): Task[TopOneYearlyAmountResponse]
  def findMinAndMaxYearlyAvgAmount(instituteName: String): Task[YearlyAvgAmountResponse]

  def saveHousingFinanceData(): Task[HousingFinanceDataResponse]

  def summariesToResponse(
    summaries: Seq[Summary],
    institutes: Seq[Institute]
  ): HousingFinanceDataResponse

  def upsertCreditGuarantee(
    es: Seq[HousingFinanceFileEntity],
    institutes: Seq[Institute]
  ): Task[Seq[(Long, Int)]]

  def upsertSummary(institueId: Long, year: Int): Task[Summary]
}

object HousingFinanceService {
  final val HOUSING_FINANCE_DATA_NAME = "주택금융 공급현황"
}
