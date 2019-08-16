package com.charlieworld.housing.data.repositories

import com.charlieworld.housing.data.MysqlDatabaseConfiguration
import com.charlieworld.housing.data.persistance.tables.{
  InstituteTable,
  MonthlyCreditGuaranteeTable,
  YearlyCreditGuaranteeTable
}
import com.charlieworld.housing.entities.{MonthlyInstituteAmount, YearlyInstituteAmount}
import monix.eval.Task
import slick.jdbc.MySQLProfile.api._

trait HousingFinanceRepositoryImpl extends HousingFinanceRepository {
  this: MysqlDatabaseConfiguration =>

  override def findAllYearlyAmountAverageByInstituteName(
    instituteName: String
  ): Task[Seq[MonthlyInstituteAmount]] =
    Task
      .deferFuture(
        mysql
          .run(
            TableQuery[MonthlyCreditGuaranteeTable]
              .groupBy(_.yearlyCreditGuaranteeId)
              .map {
                case (yearlyCreditGuaranteeId, q) =>
                  (
                    yearlyCreditGuaranteeId,
                    q.map(_.amount).avg.getOrElse(0L),
                  )
              }
              .sortBy(_._2.desc)
              .flatMap {
                case (yearlyCreditGuaranteeId, averageAmount) =>
                  TableQuery[YearlyCreditGuaranteeTable]
                    .join(TableQuery[InstituteTable])
                    .on(_.instituteId === _.instituteId)
                    .filter {
                      case (yearly, inst) ⇒
                        inst.instituteName === instituteName &&
                          yearly.yearlyCreditGuaranteeId === yearlyCreditGuaranteeId
                    }
                    .map {
                      case (yearly, institute) =>
                        (
                          yearly.year,
                          institute.instituteName,
                          institute.instituteCode,
                          averageAmount,
                        ) <> (MonthlyInstituteAmount.tupled, MonthlyInstituteAmount.unapply)
                    }
              }
              .result
          )
      )

  override def findTop1YearlyInstituteAmount(): Task[Option[YearlyInstituteAmount]] =
    Task
      .deferFuture(
        mysql
          .run(
            TableQuery[MonthlyCreditGuaranteeTable]
              .groupBy(_.yearlyCreditGuaranteeId)
              .map {
                case (yearlyCreditGuaranteeId, q) =>
                  (
                    yearlyCreditGuaranteeId,
                    q.map(_.amount).sum.getOrElse(0L),
                  )
              }
              .sortBy(_._2.desc)
              .take(1)
              .flatMap {
                case (yearlyCreditGuaranteeId, totalAmount) =>
                  TableQuery[YearlyCreditGuaranteeTable]
                    .join(TableQuery[InstituteTable])
                    .on(_.instituteId === _.instituteId)
                    .filter(_._1.yearlyCreditGuaranteeId === yearlyCreditGuaranteeId)
                    .map {
                      case (yearly, institute) =>
                        (
                          yearly.year,
                          institute.instituteName,
                          institute.instituteCode,
                          totalAmount,
                        ) <> (YearlyInstituteAmount.tupled, YearlyInstituteAmount.unapply)
                    }
                    .take(1)
              }
              .result
          )
      )
      .map(_.headOption)

  override def saveAll(entities: Seq[_]): Task[_] = ???
}
