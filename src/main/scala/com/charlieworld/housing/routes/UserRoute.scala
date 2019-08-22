package com.charlieworld.housing.routes

import akka.http.scaladsl.server.Route
import com.charlieworld.housing.{AppSuite, Logging}
import com.charlieworld.housing.entities.{JWTResponse, UserRequest}
import com.charlieworld.housing.services.{AuthenticationService, UserService}
import com.charlieworld.housing.serialization.JsonProtocol.UserRequestJsonFormat
import monix.eval.Task

trait UserRoute extends BaseRoute {
  this: UserService with AppSuite with AuthenticationService with Logging ⇒

  def userRoutes: Route =
    pathPrefix("users") {
      (post & pathEnd & entity(as[UserRequest])) { req ⇒
        runComplete(signUp(req.email, req.password))
      } ~ refresh { token ⇒
        (path("refresh") & get) {
          runComplete(Task.pure(JWTResponse(token)))
        }
      } ~ auth { _ ⇒
        (path("login") & post & entity(as[UserRequest])) { req ⇒
          runComplete(signIn(req.email, req.password))
        }
      }
    }
}
