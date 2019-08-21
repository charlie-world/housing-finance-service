package com.charlieworld.housing.exceptions

case class JsonParseException(msg: String) extends Exception {
  override def getMessage: String = msg
}
