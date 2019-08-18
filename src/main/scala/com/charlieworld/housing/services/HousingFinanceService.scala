package com.charlieworld.housing.services

import com.charlieworld.housing.entities.{
  HousingFinanceFileEntity,
  InstituteYearlyAmount,
  TopOneYearlyAmountResponse,
  YearlyAvgAmountResponse,
  YearlyTotalAmountResponse
}
import monix.eval.Task

trait HousingFinanceService {
  def saveHousingFinanceData(): Task[Seq[YearlyTotalAmountResponse]]
  def findTopOneYearlyAmount(): Task[TopOneYearlyAmountResponse]
  def findMinAndMaxYearlyAvgAmount(instituteName: String): Task[YearlyAvgAmountResponse]

  def instituteYearlyAmountsToResponseModel(
    instituteYearlyAmounts: Seq[InstituteYearlyAmount]
  ): Seq[YearlyTotalAmountResponse]

  def saveYearlyAndMonthlyCreditGuarantee(
    instituteId: Long,
    instituteName: String,
    es: Seq[HousingFinanceFileEntity]
  ): Task[Seq[InstituteYearlyAmount]]
}
