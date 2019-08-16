package com.charlieworld.housing.data.persistance.entities

case class YearlyCreditGuarantee(
  yearlyCreditGuaranteeId: Option[Long],
  year: Int,
  instituteId: Long,
)
