package com.charlieworld.housing.services.mock

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByType, provide}
import com.charlieworld.housing.services.{AuthenticationService, Fixtures}

trait MockAuthenticationService extends AuthenticationService {
  override def createToken(userId: Long): String = Fixtures.jwt

  override def auth: Directive1[Long] =
    optionalHeaderValueByType(classOf[Authorization])
      .map {
        _.collect {
          case Authorization(OAuth2BearerToken(token)) ⇒ token
        }
      }
      .flatMap {
        case Some(token) if token == Fixtures.jwt ⇒ provide(1L)
        case _ ⇒ complete(StatusCodes.Unauthorized)
      }

  override def refresh: Directive1[String] = provide(Fixtures.newJwt)
}
