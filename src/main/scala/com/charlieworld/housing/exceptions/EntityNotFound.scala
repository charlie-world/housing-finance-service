package com.charlieworld.housing.exceptions

case class EntityNotFound(msg: String) extends Exception {
  override def getMessage(): String = msg
}
