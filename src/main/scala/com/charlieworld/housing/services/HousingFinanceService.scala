package com.charlieworld.housing.services

import com.charlieworld.housing.entities.{
  TopOneYearlyAmountResponse,
  YearlyAvgAmountResponse,
  YearlyTotalAmountResponse
}
import monix.eval.Task

trait HousingFinanceService {
  def saveHousingFinanceData(): Task[Seq[YearlyTotalAmountResponse]]
  def findTopOneYearlyAmount(): Task[TopOneYearlyAmountResponse]
  def findMinAndMAxYearlyAvgAmount(instituteName: String): Task[YearlyAvgAmountResponse]
}
