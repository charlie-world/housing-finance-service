package com.charlieworld.housing.data.persistance.entities

case class Summary(
  summaryId: Option[Long],
  year: Int,
  instituteId: Long,
  sumAmount: Long,
  avgAmount: Long,
)
