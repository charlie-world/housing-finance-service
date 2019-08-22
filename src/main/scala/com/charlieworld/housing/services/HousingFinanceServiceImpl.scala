package com.charlieworld.housing.services

import com.charlieworld.housing.data.persistance.entities.{Institute, Summary}
import com.charlieworld.housing.data.repositories.HousingFinanceRepository
import com.charlieworld.housing.entities.{
  HousingFinanceDataResponse,
  HousingFinanceFileEntity,
  TopOneYearlyAmountResponse,
  YearlyAmountResponse,
  YearlyAvgAmountResponse,
  YearlyTotalAmountResponse
}
import com.charlieworld.housing.exceptions.EntityNotFoundException
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
          data.map(d ⇒ YearlyAmountResponse(d.year, d.avgAmount))
      )
    )

  override def findTopOneYearlyAmount(): Task[TopOneYearlyAmountResponse] =
    findTopOneYearlyCreditGuaranteeTotalAmount().map {
      case Some(v) ⇒ TopOneYearlyAmountResponse(v)
      case None ⇒
        throw EntityNotFoundException("Not found entities. func: [findTopOneYearlyAmount()]")
    }

  override def upsertSummary(institueId: Long, year: Int): Task[Summary] =
    findCreditGuaranteeByInstituteIdAndYear(institueId, year)
      .flatMap { creditGuarantees ⇒
        val sum = creditGuarantees.map(_.amount).sum
        val avg = round(
          creditGuarantees.map(_.amount).sum.toDouble / creditGuarantees.size
        )
        findSummaryByInstituteId(institueId, year)
          .flatMap {
            case Some(s) ⇒
              updateSummary(s.summaryId.get, sum, avg)
                .map(_ ⇒ s.copy(sumAmount = sum, avgAmount = avg))
            case None ⇒
              saveSummaries(institueId, year, sum, avg)
                .map(id ⇒ Summary(Some(id), year, institueId, sum, avg))
          }
      }

  override def upsertCreditGuarantee(
    es: Seq[HousingFinanceFileEntity],
    institutes: Seq[Institute]
  ): Task[Seq[(Long, Int)]] =
    Task.gatherUnordered(
      es.groupBy(e ⇒ (e.instituteId, e.year))
        .map {
          case ((instituteId, year), ess) ⇒
            Task
              .gatherUnordered(
                ess.map(
                  h ⇒
                    findCreditGuaranteeByInstituteIdAndYearAndMonth(instituteId, year, h.month)
                      .flatMap {
                        case Some(c) ⇒
                          updateCreditGuarantee(c.creditGuaranteeId.get, h.amount)
                        case None ⇒
                          saveCreditGuarantee(instituteId, year, h.month, h.amount)
                    }
                )
              )
              .map(_ ⇒ (instituteId, year))
        }
    )

  override def summariesToResponse(
    summaries: Seq[Summary],
    institutes: Seq[Institute]
  ): HousingFinanceDataResponse =
    HousingFinanceDataResponse(
      HousingFinanceService.HOUSING_FINANCE_DATA_NAME,
      summaries
        .groupBy(_.year)
        .map {
          case (y, sumsGrouped) ⇒
            YearlyTotalAmountResponse(
              y,
              sumsGrouped.map(_.sumAmount).sum,
              sumsGrouped
                .flatMap(
                  s ⇒
                    institutes
                      .find(_.instituteId.contains(s.instituteId))
                      .map(_.instituteName → s.sumAmount)
                )
                .toMap
            )
        }
        .toSeq
        .sortBy(_.year)
    )

  override def saveHousingFinanceData(): Task[HousingFinanceDataResponse] =
    for {
      rows ← readFile("/data.csv")
      institutes ← findAllInstitute()
      entities ← Task.gatherUnordered(
        rows.map(transformEntity(_, institutes.flatMap(_.instituteId)))
      )
      insYears ← upsertCreditGuarantee(
        entities.flatten,
        institutes
      )
      summaries ← Task.gatherUnordered(insYears.map {
        case (insId, year) ⇒ upsertSummary(insId, year)
      })
    } yield summariesToResponse(summaries, institutes)
}
