package com.charlieworld.housing.models

case class HousingFinanceDataReq(
  year: Int,
  month: Int,
  instituteId: Long,
  amount: Long,
)
