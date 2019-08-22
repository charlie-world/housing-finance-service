package com.charlieworld.housing.services

import com.charlieworld.housing.data.repositories.UserRepository
import com.charlieworld.housing.entities.JWTResponse
import monix.eval.Task

trait UserServiceImpl extends UserService {
  this: AuthenticationService with UserRepository ⇒

  override def signIn(email: String, password: String): Task[JWTResponse] =
    for {
      userId ← login(email, password)
    } yield JWTResponse(createToken(userId))

  override def signUp(email: String, password: String): Task[JWTResponse] =
    for {
      userId ← join(email, password)
    } yield JWTResponse(createToken(userId))
}
