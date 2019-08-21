package com.charlieworld.housing.services.mock

import com.charlieworld.housing.data.persistance.entities.{CreditGuarantee, Institute, Summary}
import com.charlieworld.housing.data.repositories.HousingFinanceRepository
import com.charlieworld.housing.services.Fixtures
import monix.eval.Task

trait MockHousingFinanceRepository extends HousingFinanceRepository {
  override def findAllInstitute(): Task[Seq[Institute]] = Task.pure(Fixtures.allInstitutes)

  override def findMinMaxYearlyCreditGuaranteeAvgAmount(
    instituteName: String
  ): Task[Seq[Summary]] =
    Task.pure(Fixtures.summaries)

  override def findTopOneYearlyCreditGuaranteeTotalAmount(): Task[Option[(Int, String)]] =
    Task.pure(Some(2019, "주택금융공사"))

  override def findCreditGuaranteeByInstituteIdAndYear(
    instituteId: Long,
    year: Int
  ): Task[Seq[CreditGuarantee]] =
    Task.pure(Fixtures.creditGuarantees.filter(_.year == year))

  override def findCreditGuaranteeByInstituteIdAndYearAndMonth(
    instituteId: Long,
    year: Int,
    month: Int
  ): Task[Option[CreditGuarantee]] =
    Task.pure(Fixtures.creditGuarantees.find { c ⇒
      c.year == year && c.instituteId == instituteId
    })

  override def findSummaryByInstituteId(instituteId: Long, year: Int): Task[Option[Summary]] =
    Task.pure(Fixtures.summaries.find { s ⇒
      s.instituteId == instituteId && s.year == year
    })

  override def saveCreditGuarantee(
    instituteId: Long,
    year: Int,
    month: Int,
    amount: Long
  ): Task[Long] =
    Task.pure(Fixtures.creditGuarantees.find { c ⇒
      c.year == year && c.instituteId == instituteId
    }.get.creditGuaranteeId.get)

  override def saveSummaries(
    instituteId: Long,
    year: Int,
    sumAmount: Long,
    avgAmount: Long
  ): Task[Long] =
    Task.pure(Fixtures.summaries.find { s ⇒
      s.instituteId == instituteId && s.year == year
    }.get.summaryId.get)

  override def updateCreditGuarantee(creditGuaranteeId: Long, amount: Long): Task[_] = Task.unit

  override def updateSummary(summaryId: Long, sumAmount: Long, avgAmount: Long): Task[_] = Task.unit
}
