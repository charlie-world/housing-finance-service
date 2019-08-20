package com.charlieworld.housing.utils

trait Crypto {
  def encrypt(plain: String, encryptKey: String): String
}
