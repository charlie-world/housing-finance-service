package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.entities.{MonthlyInstituteAmount, YearlyInstituteAmount}
import monix.eval.Task

trait HousingFinanceRepository {
  def saveAll(entities: Seq[_]): Task[_]
  def findTop1YearlyInstituteAmount(): Task[Option[YearlyInstituteAmount]]

  def findAllYearlyAmountAverageByInstituteName(
    instituteName: String
  ): Task[Seq[MonthlyInstituteAmount]]
}
