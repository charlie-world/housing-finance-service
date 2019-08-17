package com.charlieworld.housing

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpecLike, Matchers}
import akka.http.scaladsl.server.Directives._
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.{Config, ConfigFactory}

class AuthenticationSpec extends Matchers with FlatSpecLike with ScalatestRouteTest with Authentication {

  override def testConfig: Config = ConfigFactory.load("/application.conf")

  val routes: Route =
    pathEndOrSingleSlash {
      auth { userId ⇒
        complete(userId.toString)
      }
    }

  "auth function" should "return user_id if jwt token was verified" in {
    val userId = "1"
    val jwtToken = AuthenticationSpec.createToken(userId)
    Get("/").withHeaders(Authorization(OAuth2BearerToken(jwtToken))) ~> routes ~> check {
      status should equal(StatusCodes.OK)
      entityAs[String] shouldBe userId
    }
  }

  it should "return reject if jwt token was invalid" in {
    val invalidJwtToken = "INVALID_TOKEN"
    Get("/").withHeaders(Authorization(OAuth2BearerToken(invalidJwtToken))) ~> routes ~> check {
      status shouldBe StatusCodes.Unauthorized
    }
  }
}

object AuthenticationSpec {
  def createToken(userId: String): String = {
    val algorithm = Algorithm.HMAC256("charlie-world")
    JWT.create().withClaim("user_id", userId).sign(algorithm)
  }
}
