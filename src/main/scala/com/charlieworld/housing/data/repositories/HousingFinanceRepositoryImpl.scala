package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.data.MysqlDatabaseConfiguration
import com.charlieworld.housing.data.persistance.entities.{CreditGuarantee, Institute, Summary}
import com.charlieworld.housing.data.persistance.tables.{
  CreditGuaranteeTable,
  InstituteTable,
  SummaryTable
}
import monix.eval.Task
import slick.jdbc.MySQLProfile.api._

trait HousingFinanceRepositoryImpl extends HousingFinanceRepository {
  this: MysqlDatabaseConfiguration ⇒

  override def findTopOneYearlyCreditGuaranteeTotalAmount(): Task[Option[(Int, String)]] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[InstituteTable]
            .join(TableQuery[SummaryTable])
            .on(_.instituteId === _.instituteId)
            .sortBy(_._2.sumAmount.desc)
            .take(1)
            .map {
              case (ins, yearly) ⇒
                (yearly.year, ins.instituteName)
            }
            .result
        )
      )
      .map(_.headOption)

  override def findMinMaxYearlyCreditGuaranteeAvgAmount(
    instituteName: String
  ): Task[Seq[Summary]] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[InstituteTable]
            .join(TableQuery[SummaryTable])
            .on(_.instituteId === _.instituteId)
            .filter(_._1.instituteName === instituteName)
            .sortBy(_._2.avgAmount.asc)
            .map(_._2)
            .result
        )
      )
      .map(_.toList)
      .map {
        case Nil ⇒ Seq.empty
        case head :: tail ⇒ Seq(Some(head), tail.lastOption).flatten
      }

  override def findAllInstitute(): Task[Seq[Institute]] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[InstituteTable].result
        )
      )

  override def findSummaryByInstituteId(instituteId: Long, year: Int): Task[Option[Summary]] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[SummaryTable].filter { s ⇒
            s.instituteId === instituteId &&
            s.year === year
          }.result
        )
      )
      .map(_.headOption)

  override def saveCreditGuarantee(
    instituteId: Long,
    year: Int,
    month: Int,
    amount: Long
  ): Task[Long] =
    Task.deferFuture(
      mysql.run(
        TableQuery[CreditGuaranteeTable]
          .returning(TableQuery[CreditGuaranteeTable].map(_.creditGuaranteeId))
          .forceInsert(
            CreditGuarantee(
              None,
              instituteId,
              year,
              month,
              amount,
            )
          )
      )
    )

  override def saveSummaries(
    instituteId: Long,
    year: Int,
    sumAmount: Long,
    avgAmount: Long
  ): Task[Long] =
    Task.deferFuture(
      mysql.run(
        TableQuery[SummaryTable]
          .returning(TableQuery[SummaryTable].map(_.summaryId))
          .forceInsert(
            Summary(
              None,
              year,
              instituteId,
              sumAmount,
              avgAmount,
            )
          )
      )
    )

  override def findCreditGuaranteeByInstituteIdAndYearAndMonth(
    instituteId: Long,
    year: Int,
    month: Int
  ): Task[Option[CreditGuarantee]] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[CreditGuaranteeTable]
            .filter { c ⇒
              c.instituteId === instituteId &&
              c.year === year &&
              c.month === month
            }
            .take(1)
            .result
        )
      )
      .map(_.headOption)

  override def updateSummary(summaryId: Long, sumAmount: Long, avgAmount: Long): Task[_] =
    Task.deferFuture(
      mysql.run(
        TableQuery[SummaryTable]
          .filter(_.summaryId === summaryId)
          .map(s ⇒ (s.sumAmount, s.avgAmount))
          .update(sumAmount, avgAmount)
      )
    )

  override def updateCreditGuarantee(creditGuaranteeId: Long, amount: Long): Task[_] =
    Task.deferFuture(
      mysql.run(
        TableQuery[CreditGuaranteeTable]
          .filter(_.creditGuaranteeId === creditGuaranteeId)
          .map(_.amount)
          .update(amount)
      )
    )

  override def findCreditGuaranteeByInstituteIdAndYear(
    instituteId: Long,
    year: Int
  ): Task[Seq[CreditGuarantee]] =
    Task.deferFuture(
      mysql.run(
        TableQuery[CreditGuaranteeTable].filter { c ⇒
          c.instituteId === instituteId &&
          c.year === year
        }.result
      )
    )
}
