package com.charlieworld.housing.entities

case class YearlyTotalAmountResponse(year: Int, totalAmount: Long, detailAmount: Map[String, Long])
