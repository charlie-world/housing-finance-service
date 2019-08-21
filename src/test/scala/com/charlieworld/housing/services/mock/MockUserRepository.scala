package com.charlieworld.housing.services.mock

import com.charlieworld.housing.data.repositories.UserRepository
import com.charlieworld.housing.exceptions.{LoginFailedException, UserConflictException}
import com.charlieworld.housing.services.Fixtures
import monix.eval.Task

trait MockUserRepository extends UserRepository {
  override def join(email: String, password: String): Task[Long] =
    if (email == Fixtures.email1 && password == Fixtures.password) Task.pure(1L)
    else Task.raiseError(UserConflictException(s"Already exist email $email"))

  override def login(email: String, password: String): Task[Long] =
    if (email == Fixtures.email1) Task.pure(1L)
    else Task.raiseError(LoginFailedException("Login failed"))
}
