package com.charlieworld.housing.services

import com.charlieworld.housing.entities.JWTResponse
import monix.eval.Task

trait UserService {
  def signIn(email: String, password: String): Task[JWTResponse]
  def signUp(email: String, password: String): Task[JWTResponse]
}
