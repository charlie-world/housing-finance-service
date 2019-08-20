package com.charlieworld.housing.entities

case class TopOneYearlyAmountResponse(year: Int, bank: String)

object TopOneYearlyAmountResponse {

  def apply(input: (Int, String)): TopOneYearlyAmountResponse =
    TopOneYearlyAmountResponse(input._1, input._2)
}
