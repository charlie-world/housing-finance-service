package com.charlieworld.housing.data.persistance.entities

case class MonthlyCreditGuarantee(
  monthlyCreditGuaranteeId: Option[Long],
  yearId: Long,
  month: Int,
  amount: Long,
)
