package com.charlieworld.housing.data.persistance.tables

import java.time.LocalDateTime

import com.charlieworld.housing.data.persistance.entities.User
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.MySQLProfile.api._

class UserTable(tag: Tag) extends Table[User](tag, "user") {
  def userId: Rep[Long] = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
  def email: Rep[String] = column[String]("email")
  def password: Rep[String] = column[String]("password")
  def encryptKey: Rep[String] = column[String]("encrypt_key")

  def createdAt: Rep[LocalDateTime] =
    column[LocalDateTime]("created_at", O.Default(LocalDateTime.now()))

  def updatedAt: Rep[LocalDateTime] =
    column[LocalDateTime]("updated_at", O.Default(LocalDateTime.now()))

  def *(): ProvenShape[User] =
    (userId.?, email, password, encryptKey) <> (User.tupled, User.unapply)
}
