package com.charlieworld.housing.services

import com.charlieworld.housing.data.persistance.entities.{CreditGuarantee, Institute, Summary}
import com.charlieworld.housing.entities.HousingFinanceFileEntity

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

  val fileRows = Seq(
    HousingFinanceFileEntity(instituteId, 2018, 1, 1000L),
    HousingFinanceFileEntity(instituteId, 2018, 2, 1000L),
    HousingFinanceFileEntity(instituteId, 2018, 3, 1000L),
    HousingFinanceFileEntity(instituteId, 2019, 1, 1000L),
  )

  val password = "ABCD"
  val email1 = "email1@email.com"
  val email2 = "email2@email.com"
  val jwt = "JWT_TOKEN_SAMPLE"
  val newJwt = "NEW_JWT_TOKEN_SAMPLE"
}
