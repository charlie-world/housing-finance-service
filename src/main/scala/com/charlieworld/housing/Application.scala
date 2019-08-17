package com.charlieworld.housing

import akka.http.scaladsl.server.{HttpApp, Route}

object Application extends HttpApp with App with Authentication {
  override def routes: Route =
    pathEndOrSingleSlash {
      complete("housing finance service\n")
    }

  startServer("0.0.0.0", 8080)
}
