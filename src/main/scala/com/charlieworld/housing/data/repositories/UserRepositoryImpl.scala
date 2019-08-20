package com.charlieworld.housing.data.repositories

import java.util.UUID

import com.charlieworld.housing.data.MysqlDatabaseConfiguration
import com.charlieworld.housing.data.persistance.entities.User
import com.charlieworld.housing.data.persistance.tables.UserTable
import com.charlieworld.housing.exceptions.{LoginFailedException, UserConflictException}
import com.charlieworld.housing.utils.Crypto
import monix.eval.Task
import slick.jdbc.MySQLProfile.api._

trait UserRepositoryImpl extends UserRepository {
  this: MysqlDatabaseConfiguration with Crypto ⇒

  override def join(email: String, password: String): Task[Long] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[UserTable]
            .filter(_.email === email)
            .take(1)
            .result
        )
      )
      .flatMap {
        case Nil ⇒
          val encryptKey = UUID.randomUUID().toString.replace("-", "")
          Task.deferFuture(
            mysql.run(
              TableQuery[UserTable]
                .returning(TableQuery[UserTable].map(_.userId))
                .forceInsert(
                  User(
                    None,
                    email,
                    encrypt(password, encryptKey),
                    encryptKey,
                  )
                )
            )
          )
        case _ ⇒
          Task.raiseError(UserConflictException(s"Already exist email $email"))
      }

  override def login(email: String, password: String): Task[Long] =
    Task
      .deferFuture(
        mysql.run(
          TableQuery[UserTable]
            .filter(_.email === email)
            .take(1)
            .map(u ⇒ (u.userId, u.password, u.encryptKey))
            .result
        )
      )
      .flatMap(_.headOption match {
        case Some((uId, pw, encKey)) if pw == encrypt(password, encKey) ⇒ Task.pure(uId)
        case _ ⇒ Task.raiseError(LoginFailedException("Login failed"))
      })
}
