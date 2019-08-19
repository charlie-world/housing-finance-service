package com.charlieworld.housing.data.persistance.entities

case class CreditGuarantee(
  creditGuaranteeId: Option[Long],
  instituteId: Long,
  year: Int,
  month: Int,
  amount: Long,
)
