package com.charlieworld.housing.exceptions

case class JsonParseError(msg: String) extends Exception {
  override def getMessage: String = msg
}
