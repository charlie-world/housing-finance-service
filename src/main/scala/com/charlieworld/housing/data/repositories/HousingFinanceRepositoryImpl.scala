package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.data.MysqlDatabaseConfiguration
import com.charlieworld.housing.data.persistance.entities.{
  Institute,
  MonthlyCreditGuarantee,
  YearlyCreditGuarantee
}
import com.charlieworld.housing.data.persistance.tables.{
  InstituteTable,
  MonthlyCreditGuaranteeTable,
  YearlyCreditGuaranteeTable
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
            .join(TableQuery[YearlyCreditGuaranteeTable])
            .on(_.instituteId === _.instituteId)
            .sortBy(_._2.totalAmount.desc)
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
  ): Task[Seq[YearlyCreditGuarantee]] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[InstituteTable]
            .join(TableQuery[YearlyCreditGuaranteeTable])
            .on(_.instituteId === _.instituteId)
            .filter(_._1.instituteName === instituteName)
            .sortBy(_._2.averageAmount.asc)
            .map(_._2)
            .result
        )
      )
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

  override def saveYearlyCreditGuarantee(
    year: Int,
    instituteId: Long,
    totalAmount: Long,
    averageAmount: Long
  ): Task[Long] =
    Task.deferFuture(
      mysql.run(
        TableQuery[YearlyCreditGuaranteeTable]
          .returning(TableQuery[YearlyCreditGuaranteeTable].map(_.instituteId))
          .forceInsert(
            YearlyCreditGuarantee(
              None,
              year,
              instituteId,
              averageAmount,
              totalAmount,
            )
          )
      )
    )

  override def saveMonthlyCreditGuarantee(
    yearlyCreditGuaranteeId: Long,
    data: Seq[(Int, Long)]
  ): Task[Seq[Long]] =
    Task.deferFuture(
      mysql.run(
        TableQuery[MonthlyCreditGuaranteeTable]
          .returning(TableQuery[MonthlyCreditGuaranteeTable].map(_.monthlyCreditGuaranteeId))
          .forceInsertAll(
            data.map {
              case (month, amount) ⇒
                MonthlyCreditGuarantee(
                  None,
                  yearlyCreditGuaranteeId,
                  month,
                  amount,
                )
            }
          )
      )
    )
}
