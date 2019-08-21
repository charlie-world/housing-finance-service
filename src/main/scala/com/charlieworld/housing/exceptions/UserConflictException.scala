package com.charlieworld.housing.exceptions

case class UserConflictException(msg: String) extends Exception {
  override def getMessage: String = msg
}
