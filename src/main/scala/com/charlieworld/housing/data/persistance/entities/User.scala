package com.charlieworld.housing.data.persistance.entities

case class User(
  userId: Option[Long],
  email: String,
  password: String,
  encryptKey: String,
)
