package com.charlieworld.housing.services

import com.charlieworld.housing.data.persistance.entities.{Institute, YearlyCreditGuarantee}
import com.charlieworld.housing.data.repositories.HousingFinanceRepository
import monix.eval.Task

trait MockRepository extends HousingFinanceRepository {
  override def findAllInstitute(): Task[Seq[Institute]] = Task.pure(Fixtures.allInstitutes)

  override def findMinMaxYearlyCreditGuaranteeAvgAmount(
    instituteName: String
  ): Task[Seq[YearlyCreditGuarantee]] =
    Task.pure(Fixtures.bankYearlyCreditGuaratee)

  override def findTopOneYearlyCreditGuaranteeTotalAmount(): Task[Option[(Int, String)]] =
    Task.pure(Some(2019, "주택금융공사"))

  override def saveMonthlyCreditGuarantee(
    yearlyCreditGuaranteeId: Long,
    data: Seq[(Int, Long)]
  ): Task[Seq[Long]] =
    if (yearlyCreditGuaranteeId == 1L) Task.pure(Seq(20L, 21L, 22L, 23L))
    else Task.raiseError(Fixtures.saveMonthlyException)

  override def saveYearlyCreditGuarantee(
    year: Int,
    instituteId: Long,
    totalAmount: Long,
    averageAmount: Long
  ): Task[Long] =
    if (instituteId == 1L) { Task.pure(1L) } else if (instituteId == 2L) { Task.pure(2L) } else
      Task.raiseError(Fixtures.saveYearlyException)
}
