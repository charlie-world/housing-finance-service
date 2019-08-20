package com.charlieworld.housing.services

import com.charlieworld.housing.entities.JWTResponse
import com.charlieworld.housing.exceptions.{LoginFailedException, UserConflictException}
import org.scalatest.{FlatSpecLike, Matchers}
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration._

class UserServiceSpec extends Matchers with FlatSpecLike {

  object impl extends UserServiceImpl with MockAuthentication with MockUserRepository

  final val timeout: Duration = 5 seconds

  "signIn" should "return jwt token when it was succeed" in {
    Await.result(
      impl.signIn(Fixtures.email1, Fixtures.password).runAsync,
      timeout
    ) shouldBe JWTResponse("1-1")
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
    ) shouldBe JWTResponse("1-1")
  }

  it should "throw user conflict exception" in {
    a[UserConflictException] shouldBe thrownBy {
      Await.result(
        impl.signUp(Fixtures.email2, Fixtures.password).runAsync,
        timeout
      )
    }
  }

  "refresh" should "return a new jwt token" in {
    Await.result(
      impl.refresh(2L).runAsync,
      timeout
    ) shouldBe JWTResponse("2-2")
  }
}
