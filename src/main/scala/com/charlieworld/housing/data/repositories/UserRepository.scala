package com.charlieworld.housing.data.repositories

import monix.eval.Task

trait UserRepository {
  def login(email: String, password: String): Task[Long]
  def join(email: String, password: String): Task[Long]
}
