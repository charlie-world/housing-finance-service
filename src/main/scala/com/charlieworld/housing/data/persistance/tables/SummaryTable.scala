package com.charlieworld.housing.data.persistance.tables

import java.time.LocalDateTime

import com.charlieworld.housing.data.persistance.entities.Summary
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.MySQLProfile.api._

class SummaryTable(tag: Tag) extends Table[Summary](tag, "summary") {
  def summaryId: Rep[Long] = column[Long]("summary_id", O.PrimaryKey, O.AutoInc)
  def year: Rep[Int] = column[Int]("year")
  def instituteId: Rep[Long] = column[Long]("institute_id")
  def sumAmount: Rep[Long] = column[Long]("sum_amount")
  def avgAmount: Rep[Long] = column[Long]("avg_amount")

  def createdAt: Rep[LocalDateTime] =
    column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))

  def updatedAt: Rep[LocalDateTime] =
    column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  def *(): ProvenShape[Summary] =
    (
      summaryId.?,
      year,
      instituteId,
      sumAmount,
      avgAmount,
    ) <> (Summary.tupled, Summary.unapply)
}
