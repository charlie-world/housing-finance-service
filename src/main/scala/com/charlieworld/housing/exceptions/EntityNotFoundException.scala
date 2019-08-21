package com.charlieworld.housing.exceptions

case class EntityNotFoundException(msg: String) extends Exception {
  override def getMessage(): String = msg
}
