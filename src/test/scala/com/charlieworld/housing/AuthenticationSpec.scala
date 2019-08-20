package com.charlieworld.housing

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpecLike, Matchers}

class AuthenticationSpec
  extends Matchers
  with FlatSpecLike
  with ScalatestRouteTest
  with Authentication {

  val routes: Route =
    pathEndOrSingleSlash {
      auth { userId ⇒
        complete(userId.toString)
      }
    }

  "auth function" should "return user_id if jwt token was verified" in {
    val userId = 1L
    val jwtToken = createToken(userId)
    Get("/").withHeaders(Authorization(OAuth2BearerToken(jwtToken))) ~> routes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe userId.toString
    }
  }

  it should "return reject if jwt token was invalid" in {
    val invalidJwtToken = "INVALID_TOKEN"
    Get("/").withHeaders(Authorization(OAuth2BearerToken(invalidJwtToken))) ~> routes ~> check {
      status shouldBe StatusCodes.Unauthorized
    }
  }
}
