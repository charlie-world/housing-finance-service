package com.charlieworld.housing.data.persistance.tables

import java.time.LocalDateTime

import com.charlieworld.housing.data.persistance.entities.Institute
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.MySQLProfile.api._

class InstituteTable(tag: Tag) extends Table[Institute](tag, "institute") {
  def instituteId: Rep[Long] = column[Long]("institute_id", O.PrimaryKey, O.AutoInc)
  def instituteName: Rep[String] = column[String]("institute_name")
  def instituteCode: Rep[String] = column[String]("institute_code")

  def createdAt: Rep[LocalDateTime] =
    column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))

  def updatedAt: Rep[LocalDateTime] =
    column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  def *(): ProvenShape[Institute] =
    (instituteId.?, instituteName, instituteCode) <> (Institute.tupled, Institute.unapply)
}
