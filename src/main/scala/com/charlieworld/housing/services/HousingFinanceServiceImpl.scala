package com.charlieworld.housing.services

import com.charlieworld.housing.data.repositories.HousingFinanceRepository
import com.charlieworld.housing.entities.{
  HousingFinanceFileEntity,
  InstituteYearlyAmount,
  TopOneYearlyAmountResponse,
  YearlyAmountResponse,
  YearlyAvgAmountResponse,
  YearlyTotalAmountResponse
}
import com.charlieworld.housing.exceptions.EntityNotFound
import com.charlieworld.housing.utils.FileRead
import monix.eval.Task

import scala.math.round

trait HousingFinanceServiceImpl extends HousingFinanceService {
  this: HousingFinanceRepository with FileRead ⇒
  override def findMinAndMaxYearlyAvgAmount(instituteName: String): Task[YearlyAvgAmountResponse] =
    findMinMaxYearlyCreditGuaranteeAvgAmount(instituteName).map(
      data ⇒
        YearlyAvgAmountResponse(
          instituteName,
          data.map(d ⇒ YearlyAmountResponse(d.year, d.totalAmount))
      )
    )

  override def findTopOneYearlyAmount(): Task[TopOneYearlyAmountResponse] =
    findTopOneYearlyCreditGuaranteeTotalAmount().map {
      case Some(v) ⇒ TopOneYearlyAmountResponse(v)
      case None ⇒ throw EntityNotFound("Not found entities. func: [findTopOneYearlyAmount()]")
    }

  override def instituteYearlyAmountsToResponseModel(
    instituteYearlyAmounts: Seq[InstituteYearlyAmount]
  ): Seq[YearlyTotalAmountResponse] =
    instituteYearlyAmounts
      .groupBy(_.year)
      .map {
        case (y, insYearlyAmounts) ⇒
          YearlyTotalAmountResponse(
            y,
            insYearlyAmounts.map(_.amount).sum,
            insYearlyAmounts.map(e ⇒ e.instituteName → e.amount).toMap
          )
      }
      .toSeq
      .sortBy(_.year)

  override def saveYearlyAndMonthlyCreditGuarantee(
    instituteId: Long,
    instituteName: String,
    es: Seq[HousingFinanceFileEntity]
  ): Task[Seq[InstituteYearlyAmount]] =
    Task
      .gather(
        es.groupBy(_.year).map {
          case (y, ess) ⇒
            val sum = ess.map(_.amount).sum
            val avg = if (ess.isEmpty) 0 else round(sum.toFloat / ess.size)
            for {
              id ← saveYearlyCreditGuarantee(y, instituteId, sum, avg)
              _ ← saveMonthlyCreditGuarantee(id, ess.map(e ⇒ (e.month, e.amount)))
            } yield (y, sum)
        }
      )
      .map(_.map {
        case (year, sum) ⇒
          InstituteYearlyAmount(instituteName, year, sum)
      }.toSeq)

  override def saveHousingFinanceData(): Task[Seq[YearlyTotalAmountResponse]] =
    for {
      rows ← readFile("")
      entities ← Task.gather(rows.map(transformEntity))
      institutes ← findAllInstitute().map(_.map(i ⇒ i.instituteName → i.instituteId).toMap)
      instituteYearlyAmounts ← Task.gather(
        entities.groupBy(_.instituteName).map {
          case (insName, es) ⇒
            saveYearlyAndMonthlyCreditGuarantee(institutes(insName).get, insName, es)
        }
      )
      result = instituteYearlyAmountsToResponseModel(instituteYearlyAmounts.flatten.toSeq)
    } yield result
}
