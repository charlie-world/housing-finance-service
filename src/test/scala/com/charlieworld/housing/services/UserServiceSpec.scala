package com.charlieworld.housing.services

import com.charlieworld.housing.entities.JWTResponse
import com.charlieworld.housing.exceptions.{LoginFailedException, UserConflictException}
import com.charlieworld.housing.services.mock.{MockAuthenticationService, MockUserRepository}
import monix.execution.Scheduler.Implicits.global
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class UserServiceSpec extends Matchers with FlatSpecLike {

  object impl extends UserServiceImpl with MockAuthenticationService with MockUserRepository

  final val timeout: Duration = 5 seconds

  "signIn" should "return jwt token when it was succeed" in {
    Await.result(
      impl.signIn(Fixtures.email1, Fixtures.password).runAsync,
      timeout
    ) shouldBe JWTResponse(Fixtures.jwt)
  }

  it should "throw login failed exception" in {
    a[LoginFailedException] shouldBe thrownBy {
      Await.result(
        impl.signIn(Fixtures.email2, Fixtures.password).runAsync,
        timeout
      )
    }
  }

  "signUp" should "return jwt token when it was succeed" in {
    Await.result(
      impl.signUp(Fixtures.email1, Fixtures.password).runAsync,
      timeout
    ) shouldBe JWTResponse(Fixtures.jwt)
  }

  it should "throw user conflict exception" in {
    a[UserConflictException] shouldBe thrownBy {
      Await.result(
        impl.signUp(Fixtures.email2, Fixtures.password).runAsync,
        timeout
      )
    }
  }
}
