package com.charlieworld.housing.services

import com.charlieworld.housing.data.persistance.entities.{Institute, YearlyCreditGuarantee}
import com.charlieworld.housing.entities.{HousingFinanceFileEntity, InstituteYearlyAmount}

object Fixtures {

  val instituteId: Long = 1L
  val instituteName: String = "주택금융공사"

  val bankYearlyCreditGuaratee = Seq(
    YearlyCreditGuarantee(Some(1L), 2005, 1L, 10, 100),
    YearlyCreditGuarantee(Some(2L), 2006, 1L, 10, 100),
    YearlyCreditGuarantee(Some(3L), 2007, 1L, 10, 100),
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
    HousingFinanceFileEntity(instituteName, 2018, 1, 100L),
    HousingFinanceFileEntity(instituteName, 2018, 2, 100L),
    HousingFinanceFileEntity(instituteName, 2018, 3, 100L),
    HousingFinanceFileEntity(instituteName, 2018, 4, 100L),
    HousingFinanceFileEntity(instituteName, 2018, 5, 100L),
  )

  val saveYearlyException = new Exception("Not Implemented")
  val saveMonthlyException = new Exception("Not Implemented")
}
