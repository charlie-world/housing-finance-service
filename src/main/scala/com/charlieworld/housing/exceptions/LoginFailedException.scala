package com.charlieworld.housing.exceptions

case class LoginFailedException(msg: String) extends Exception {
  override def getMessage: String = msg
}
