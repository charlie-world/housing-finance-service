package com.charlieworld.housing.services

import java.time.{LocalDateTime, ZoneId, ZoneOffset}
import java.util.Date

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByType, provide}
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import scala.util.{Failure, Success, Try}

trait AuthenticationServiceImpl extends AuthenticationService {

  private def verify(token: String): Try[Long] = Try {
    val algorithm = Algorithm.HMAC256(AuthenticationService.SECRET)
    val verifier = JWT.require(algorithm).build
    val result = verifier.verify(token)
    result.getClaim("user_id").asString().toLong
  }

  private def extractUserId(token: String): Try[Long] = Try {
    JWT.decode(token).getClaim("user_id").asString().toLong
  }

  private def extractBearerToken(authHeader: Option[Authorization]): Option[String] =
    authHeader.collect {
      case Authorization(OAuth2BearerToken(token)) ⇒ token
    }

  override def createToken(userId: Long): String = {
    val algorithm = Algorithm.HMAC256(AuthenticationService.SECRET)
    val nowUTC = LocalDateTime.now(ZoneId.of("UTC"))
    JWT
      .create()
      .withClaim("user_id", userId.toString)
      .withExpiresAt(Date.from(nowUTC.plusHours(1).toInstant(ZoneOffset.UTC)))
      .sign(algorithm)
  }

  override def auth: Directive1[Long] =
    optionalHeaderValueByType(classOf[Authorization]).map(extractBearerToken).flatMap {
      case Some(token) ⇒
        verify(token) match {
          case Success(userId) ⇒ provide(userId)
          case Failure(_) ⇒ complete(StatusCodes.Unauthorized)
        }
      case None ⇒ complete(StatusCodes.Unauthorized)
    }

  override def refresh: Directive1[String] =
    optionalHeaderValueByType(classOf[Authorization]).map(extractBearerToken).flatMap {
      case Some(token) ⇒
        extractUserId(token) match {
          case Success(userId) ⇒ provide(createToken(userId))
          case Failure(_) ⇒ complete(StatusCodes.Unauthorized)
        }
      case None ⇒ complete(StatusCodes.Unauthorized)
    }
}
