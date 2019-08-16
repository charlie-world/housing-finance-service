package com.charlieworld.housing

import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive1}
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import scala.util.{Failure, Success, Try}

trait Authentication {

  def verify(token: String): Try[Long] = Try {
    val algorithm = Algorithm.HMAC256(Authentication.SECRET)
    val verifier = JWT.require(algorithm).build
    val result = verifier.verify(token)
    result.getClaim("user_id").asLong()
  }

  private def extractBearerToken(authHeader: Option[Authorization]): Option[String] =
    authHeader.collect {
      case Authorization(OAuth2BearerToken(token)) => token
    }

  def auth: Directive1[Long] =
    optionalHeaderValueByType(classOf[Authorization]).map(extractBearerToken).flatMap {
      case Some(token) =>
        verify(token) match {
          case Success(userId) => provide(userId)
          case Failure(_)      => reject(AuthorizationFailedRejection)
        }
      case None => reject(AuthorizationFailedRejection)
    }
}

private object Authentication {
  final val SECRET = "charlie-world"
}
