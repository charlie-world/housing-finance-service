package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.data.persistance.entities.{Institute, YearlyCreditGuarantee}
import monix.eval.Task

trait HousingFinanceRepository {
  def findTopOneYearlyCreditGuaranteeTotalAmount(): Task[Option[(Int, String)]]

  def findMinMaxYearlyCreditGuaranteeAvgAmount(
    instituteName: String
  ): Task[Seq[YearlyCreditGuarantee]]

  def findAllInstitute(): Task[Seq[Institute]]

  def saveYearlyCreditGuarantee(
    year: Int,
    instituteId: Long,
    totalAmount: Long,
    averageAmount: Long
  ): Task[Long]

  def saveMonthlyCreditGuarantee(
    yearlyCreditGuaranteeId: Long,
    data: Seq[(Int, Long)]
  ): Task[Seq[Long]]
}
