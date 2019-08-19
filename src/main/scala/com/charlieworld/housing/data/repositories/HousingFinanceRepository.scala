package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.data.persistance.entities.{CreditGuarantee, Institute, Summary}
import monix.eval.Task

trait HousingFinanceRepository {
  def findTopOneYearlyCreditGuaranteeTotalAmount(): Task[Option[(Int, String)]]

  def findMinMaxYearlyCreditGuaranteeAvgAmount(
    instituteName: String
  ): Task[Seq[Summary]]
  def findAllInstitute(): Task[Seq[Institute]]

  def findSummaryByInstituteId(instituteId: Long, year: Int): Task[Option[Summary]]

  def saveCreditGuarantee(
    instituteId: Long,
    year: Int,
    month: Int,
    amount: Long
  ): Task[CreditGuarantee]

  def saveSummaries(instituteId: Long, year: Int, sumAmount: Long, avgAmount: Long): Task[Summary]

  def updateSummary(summaryId: Long, sumAmount: Long, avgAmount: Long): Task[_]

  def updateCreditGuarantee(creditGuaranteeId: Long, amount: Long): Task[_]

  def findCreditGuaranteeByInstituteIdAndYearAndMonth(
    instituteId: Long,
    year: Int,
    month: Int
  ): Task[Option[CreditGuarantee]]

  def findCreditGuaranteeByInstituteIdAndYear(
    instituteId: Long,
    year: Int
  ): Task[Seq[CreditGuarantee]]
}
