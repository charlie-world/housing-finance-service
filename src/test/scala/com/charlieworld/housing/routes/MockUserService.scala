package com.charlieworld.housing.routes

import com.charlieworld.housing.entities.JWTResponse
import com.charlieworld.housing.exceptions.{LoginFailedException, UserConflictException}
import com.charlieworld.housing.services.{Fixtures, UserService}
import monix.eval.Task

trait MockUserService extends UserService {
  override def signUp(email: String, password: String): Task[JWTResponse] =
    if (email == Fixtures.email1) Task.pure(JWTResponse(Fixtures.jwt))
    else Task.raiseError(UserConflictException(s"Already exist email $email"))

  override def refresh(userId: Long): Task[JWTResponse] =
    Task.pure(JWTResponse(Fixtures.newJwt))

  override def signIn(email: String, password: String): Task[JWTResponse] =
    if (email == Fixtures.email1) Task.pure(JWTResponse(Fixtures.jwt))
    else Task.raiseError(LoginFailedException("Login failed"))
}
