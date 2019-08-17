package com.charlieworld.housing.data

import com.charlieworld.housing.data.persistance.entities.{
  MonthlyCreditGuarantee,
  YearlyCreditGuarantee
}
import com.charlieworld.housing.data.persistance.tables.{
  MonthlyCreditGuaranteeTable,
  YearlyCreditGuaranteeTable
}
import com.charlieworld.housing.data.repositories.HousingFinanceRepositoryImpl
import com.charlieworld.housing.models.HousingFinanceDataReq
import monix.execution.Scheduler.Implicits.global
import org.mockito.MockitoSugar.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpecLike, Matchers}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

class HousingFinanceRepositorySpec extends Matchers with FlatSpecLike with MockitoSugar {

  final val mockMysql: Database = mock[Database]

  object impl extends HousingFinanceRepositoryImpl with MysqlDatabaseConfiguration {
    override val mysql: Database = mockMysql
  }

  "findYearlyCreditGuaranteeByYearAndInstituteId" should
    "return seq of yearlyCreditGuaranteeId when success to find" in {
    val instituteId = 1L
    val year = 2018
    val yearlyCreditGuaranteeId = 11L

    when {
      mockMysql.run(
        TableQuery[YearlyCreditGuaranteeTable]
          .filter { y ⇒
            y.instituteId === instituteId &&
            y.year === year
          }
          .map(_.yearlyCreditGuaranteeId)
          .take(1)
          .result
      )
    }.thenReturn(Future.successful(Seq(yearlyCreditGuaranteeId)))

    impl.findYearlyCreditGuaranteeByYearAndInstituteId(year, instituteId).foreach { result ⇒
      result shouldBe Seq(yearlyCreditGuaranteeId)
    }
  }

  it should "return empty seq when failed to find YearlyCreditGuarantee" in {
    val instituteId = 1L
    val year = 2017

    when {
      mockMysql.run(
        TableQuery[YearlyCreditGuaranteeTable]
          .filter { y ⇒
            y.instituteId === instituteId &&
            y.year === year
          }
          .map(_.yearlyCreditGuaranteeId)
          .take(1)
          .result
      )
    }.thenReturn(Future.successful(Seq.empty))

    impl.findYearlyCreditGuaranteeByYearAndInstituteId(year, instituteId).foreach { result ⇒
      result shouldBe Seq.empty
    }
  }

  "saveYearlyCreditGuarantee" should "return yearlyCreditGuaranteeId when it succeed" in {
    val instituteId = 1L
    val year = 2017
    val yearlyCreditGuaranteeId = 11L

    when {
      mockMysql.run(
        TableQuery[YearlyCreditGuaranteeTable]
          .returning(
            TableQuery[YearlyCreditGuaranteeTable].map(_.yearlyCreditGuaranteeId)
          )
          .forceInsert(
            YearlyCreditGuarantee(
              None,
              year,
              instituteId,
            )
          )
      )
    }.thenReturn(Future.successful(yearlyCreditGuaranteeId))

    impl.saveYearlyCreditGuarantee(year, instituteId).foreach { result ⇒
      result shouldBe yearlyCreditGuaranteeId
    }
  }

  "saveAllMonthlyCreditGuarantee" should "return () when it succeed" in {

    val housingFinanceDataReq: HousingFinanceDataReq =
      HousingFinanceDataReq(
        2019,
        1,
        1L,
        1000L,
      )
    val entities: Seq[HousingFinanceDataReq] = Seq(housingFinanceDataReq)
    val yearlyCreditGuaranteeId: Long = 1L

    when {
      mockMysql.run(
        TableQuery[MonthlyCreditGuaranteeTable]
          .returning(TableQuery[MonthlyCreditGuaranteeTable].map(_.monthlyCreditGuaranteeId))
          .forceInsertAll(
            entities.map(
              e ⇒
                MonthlyCreditGuarantee(
                  None,
                  yearlyCreditGuaranteeId,
                  e.month,
                  e.amount,
              )
            )
          )
      )
    }.thenReturn(Future.successful(Seq(yearlyCreditGuaranteeId)))

    impl.saveAllMonthlyCreditGuarantee(entities, yearlyCreditGuaranteeId).foreach { result ⇒
      result shouldBe Seq(yearlyCreditGuaranteeId)
    }
  }
}
