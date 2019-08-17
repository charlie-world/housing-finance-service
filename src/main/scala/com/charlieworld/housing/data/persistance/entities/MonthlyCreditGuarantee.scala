package com.charlieworld.housing.data.persistance.entities

case class MonthlyCreditGuarantee(
  monthlyCreditGuaranteeId: Option[Long],
  yearlyCreditGuaranteeId: Long,
  month: Int,
  amount: Long,
)
