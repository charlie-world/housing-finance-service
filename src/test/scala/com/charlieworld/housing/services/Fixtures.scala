package com.charlieworld.housing.services

import com.charlieworld.housing.data.persistance.entities.{CreditGuarantee, Institute, Summary}
import com.charlieworld.housing.entities.{HousingFinanceFileEntity, InstituteYearlyAmount}

object Fixtures {

  val instituteId: Long = 1L
  val instituteName: String = "주택금융공사"

  val summaries = Seq(
    Summary(Some(1L), 2018, instituteId, 3000L, 1000L),
    Summary(Some(2L), 2019, instituteId, 1000L, 1000L),
  )

  val creditGuarantees = Seq(
    CreditGuarantee(Some(1L), instituteId, 2018, 1, 1000L),
    CreditGuarantee(Some(2L), instituteId, 2018, 2, 1000L),
    CreditGuarantee(Some(3L), instituteId, 2018, 3, 1000L),
    CreditGuarantee(Some(3L), instituteId, 2019, 1, 1000L),
  )

  val allInstitutes = Seq(
    Institute(Some(instituteId), instituteName, "HFS12")
  )

  val instituteYearlyAmounts: Seq[InstituteYearlyAmount] = Seq(
    InstituteYearlyAmount("A", 2018, 1000L),
    InstituteYearlyAmount("B", 2018, 1200L),
    InstituteYearlyAmount("C", 2018, 900L),
    InstituteYearlyAmount("A", 2019, 800L),
    InstituteYearlyAmount("B", 2019, 1000L),
    InstituteYearlyAmount("C", 2019, 700L),
    InstituteYearlyAmount("A", 2020, 200L),
    InstituteYearlyAmount("B", 2020, 100L),
  )

  val fileRows = Seq(
    HousingFinanceFileEntity(instituteName, 2018, 1, 1000L),
    HousingFinanceFileEntity(instituteName, 2018, 2, 1000L),
    HousingFinanceFileEntity(instituteName, 2018, 3, 1000L),
    HousingFinanceFileEntity(instituteName, 2019, 1, 1000L),
  )

  val saveYearlyException = new Exception("Not Implemented")
  val saveMonthlyException = new Exception("Not Implemented")
}
