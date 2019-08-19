package com.charlieworld.housing.data.persistance.tables

import java.time.LocalDateTime

import com.charlieworld.housing.data.persistance.entities.CreditGuarantee
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class CreditGuaranteeTable(tag: Tag) extends Table[CreditGuarantee](tag, "credit_guarantee") {

  def creditGuaranteeId: Rep[Long] =
    column[Long]("credit_guarantee_id", O.PrimaryKey, O.AutoInc)
  def instituteId: Rep[Long] = column[Long]("institute_id")
  def year: Rep[Int] = column[Int]("year")
  def month: Rep[Int] = column[Int]("month")
  def amount: Rep[Long] = column[Long]("amount")

  def createdAt: Rep[LocalDateTime] =
    column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))

  def updatedAt: Rep[LocalDateTime] =
    column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  def *(): ProvenShape[CreditGuarantee] =
    (
      creditGuaranteeId.?,
      instituteId,
      year,
      month,
      amount,
    ) <> (CreditGuarantee.tupled, CreditGuarantee.unapply)
}
