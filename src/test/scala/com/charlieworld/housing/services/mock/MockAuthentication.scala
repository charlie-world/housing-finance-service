package com.charlieworld.housing.services.mock

import com.charlieworld.housing.Authentication

trait MockAuthentication extends Authentication {
  override def createToken(userId: Long): String = s"$userId-${userId.hashCode()}"
}
