package com.charlieworld.housing.routes

import akka.http.scaladsl.server.Route
import com.charlieworld.housing.{AppSuite, Authentication}
import com.charlieworld.housing.entities.UserRequest
import com.charlieworld.housing.services.UserService
import com.charlieworld.housing.serialization.JsonProtocol.UserRequestJsonFormat

trait UserRoute extends BaseRoute {
  this: UserService with AppSuite with Authentication ⇒

  def userRoutes: Route =
    pathPrefix("users") {
      (post & pathEnd & entity(as[UserRequest])) { req ⇒
        runComplete(signUp(req.email, req.password))
      } ~
        auth { userId ⇒
          (path("login") & post & entity(as[UserRequest])) { req ⇒
            runComplete(signIn(req.email, req.password))
          } ~ (path("refresh") & get) {
            runComplete(refresh(userId))
          }
        }
    }
}
