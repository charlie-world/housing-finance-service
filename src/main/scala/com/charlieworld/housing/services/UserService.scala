package com.charlieworld.housing.services

import monix.eval.Task

trait UserService {
  def signin(): Task[_]
  def signup(): Task[_]
  def refresh(): Task[_]
}
