package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.entities.{MonthlyInstituteAmount, YearlyInstituteAmount}
import com.charlieworld.housing.models.HousingFinanceDataReq
import monix.eval.Task

trait HousingFinanceRepository {
  def saveAll(entities: Seq[HousingFinanceDataReq]): Task[_]
  def findTop1YearlyInstituteAmount(): Task[Option[YearlyInstituteAmount]]

  def findAllYearlyAmountAverageByInstituteName(
    instituteName: String
  ): Task[Seq[MonthlyInstituteAmount]]
  def findYearlyCreditGuaranteeByYearAndInstituteId(year: Int, instituteId: Long): Task[Seq[Long]]

  def saveAllMonthlyCreditGuarantee(
    entities: Seq[HousingFinanceDataReq],
    yearlyCreditGuaranteeId: Long
  ): Task[Seq[Long]]
  def saveYearlyCreditGuarantee(year: Int, instituteId: Long): Task[Long]
}
