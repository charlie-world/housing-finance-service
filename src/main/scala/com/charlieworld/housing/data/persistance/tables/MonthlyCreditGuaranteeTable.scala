package com.charlieworld.housing.data.persistance.tables

import java.time.LocalDateTime

import com.charlieworld.housing.data.persistance.entities.MonthlyCreditGuarantee
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class MonthlyCreditGuaranteeTable(tag: Tag)
  extends Table[MonthlyCreditGuarantee](tag, "monthly_credit_guarantee") {

  def monthlyCreditGuaranteeId: Rep[Long] =
    column[Long]("monthly_credit_guarantee_id", O.PrimaryKey, O.AutoInc)
  def yearlyCreditGuaranteeId: Rep[Long] = column[Long]("yearly_credit_guarantee_id")
  def month: Rep[Int] = column[Int]("month")
  def amount: Rep[Long] = column[Long]("amount")

  def createdAt: Rep[LocalDateTime] =
    column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))

  def updatedAt: Rep[LocalDateTime] =
    column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  def *(): ProvenShape[MonthlyCreditGuarantee] =
    (monthlyCreditGuaranteeId.?, yearlyCreditGuaranteeId, month, amount) <> (MonthlyCreditGuarantee.tupled, MonthlyCreditGuarantee.unapply)
}
