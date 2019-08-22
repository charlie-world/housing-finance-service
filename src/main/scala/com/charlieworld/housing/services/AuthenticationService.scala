package com.charlieworld.housing.services

import akka.http.scaladsl.server.Directive1

trait AuthenticationService {
  def createToken(userId: Long): String

  def auth: Directive1[Long]

  def refresh: Directive1[String]
}

private object AuthenticationService {
  final val SECRET = "charlie-world"
}
