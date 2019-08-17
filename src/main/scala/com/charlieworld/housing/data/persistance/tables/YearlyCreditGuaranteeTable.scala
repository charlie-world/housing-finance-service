package com.charlieworld.housing.data.persistance.tables

import java.time.LocalDateTime

import com.charlieworld.housing.data.persistance.entities.YearlyCreditGuarantee
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class YearlyCreditGuaranteeTable(tag: Tag)
  extends Table[YearlyCreditGuarantee](tag, "yearly_credit_guarantee") {

  def yearlyCreditGuaranteeId: Rep[Long] =
    column[Long]("yearly_credit_guarantee_id", O.PrimaryKey, O.AutoInc)
  def year: Rep[Int] = column[Int]("year")
  def instituteId: Rep[Long] = column[Long]("institute_id")

  def createdAt: Rep[LocalDateTime] =
    column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))

  def updatedAt: Rep[LocalDateTime] =
    column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  def *(): ProvenShape[YearlyCreditGuarantee] =
    (yearlyCreditGuaranteeId.?, year, instituteId) <> (YearlyCreditGuarantee.tupled, YearlyCreditGuarantee.unapply)
}
