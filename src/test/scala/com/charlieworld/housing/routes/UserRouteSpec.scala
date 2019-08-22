package com.charlieworld.housing.routes

import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import ch.qos.logback.classic.{Logger => LogbackLogger}
import com.charlieworld.housing.entities.{JWTResponse, UserRequest}
import com.charlieworld.housing.routes.mock.MockUserService
import com.charlieworld.housing.serialization.JsonProtocol._
import com.charlieworld.housing.services.Fixtures
import com.charlieworld.housing.services.mock.MockAuthenticationService
import com.charlieworld.housing.{AppSuite, Logging}
import monix.execution.Scheduler
import org.scalatest.{FlatSpecLike, Matchers}
import org.slf4j.{Logger, LoggerFactory}
import spray.json._

class UserRouteSpec
  extends Matchers
  with FlatSpecLike
  with ScalatestRouteTest
  with UserRoute
  with Logging
  with MockAuthenticationService
  with MockUserService
  with AppSuite {

  override val logger: Logger = LoggerFactory.getLogger(getClass)

  override implicit protected val s: Scheduler = monix.execution.Scheduler.global

  "POST /users" should "return jwt token if it succeed to sign up" in {
    val entity = HttpEntity(
      ContentTypes.`application/json`,
      UserRequest(Fixtures.email1, Fixtures.password).toJson.compactPrint,
    )

    Post("/users").withEntity(entity) ~> userRoutes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[JWTResponse] shouldBe JWTResponse(Fixtures.jwt)
    }
  }

  it should "return 500 error with error message if it failed to sign up" in {
    val entity = HttpEntity(
      ContentTypes.`application/json`,
      UserRequest(Fixtures.email2, Fixtures.password).toJson.compactPrint,
    )

    Post("/users").withEntity(entity) ~> userRoutes ~> check {
      status shouldBe StatusCodes.InternalServerError
      responseAs[String] shouldBe s"Already exist email ${Fixtures.email2}"
    }
  }

  "POST /users/login" should "return jwt token if it succeed to sign in" in {
    val userId = 1L
    val entity = HttpEntity(
      ContentTypes.`application/json`,
      UserRequest(Fixtures.email1, Fixtures.password).toJson.compactPrint,
    )
    val jwtToken = createToken(userId)
    val header = Authorization(OAuth2BearerToken(jwtToken))

    Post("/users/login").withEntity(entity).withHeaders(header) ~> userRoutes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[JWTResponse] shouldBe JWTResponse(Fixtures.jwt)
    }
  }

  it should "return 500 error with error message if it failed to sign in" in {
    val userId = 1L
    val entity = HttpEntity(
      ContentTypes.`application/json`,
      UserRequest(Fixtures.email2, Fixtures.password).toJson.compactPrint,
    )
    val jwtToken = createToken(userId)
    val header = Authorization(OAuth2BearerToken(jwtToken))

    Post("/users/login").withEntity(entity).withHeaders(header) ~> userRoutes ~> check {
      status shouldBe StatusCodes.InternalServerError
      responseAs[String] shouldBe "Login failed"
    }
  }

  it should "return 401 unauthorized if it failed to jwt auth" in {
    val header = Authorization(OAuth2BearerToken("INVALID"))

    Post("/users/login").withHeaders(header) ~> userRoutes ~> check {
      status shouldBe StatusCodes.Unauthorized
    }
  }

  "GET /users/refresh" should "return new jwt token if it succeed to auth" in {
    val userId = 1L
    val jwtToken = createToken(userId)
    val header = Authorization(OAuth2BearerToken(jwtToken))

    Get("/users/refresh").withHeaders(header) ~> userRoutes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[JWTResponse] shouldBe JWTResponse(Fixtures.newJwt)
    }
  }
}
