package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.data.persistance.entities.{Institute, YearlyCreditGuarantee}
import monix.eval.Task

trait HousingFinanceRepository {
  def findTopOneYearlyCreditGuaranteeTotalAmount(): Task[Option[(String, YearlyCreditGuarantee)]]

  def findMinMaxYearlyCreditGuaranteeAvgAmount(
    instituteName: String
  ): Task[Seq[YearlyCreditGuarantee]]

  def findInstitute(instituteName: String): Task[Option[Institute]]

  def saveYearlyCreditGuarantee(
    year: Int,
    instituteId: Long,
    totalAmount: Long,
    averageAmount: Long
  ): Task[Long]

  def saveMonthlyCreditGuarantee(
    month: Int,
    yearlyCreditGuaranteeId: Long,
    amount: Long
  ): Task[Long]
}
