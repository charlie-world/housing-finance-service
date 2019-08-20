package com.charlieworld.housing.utils

import org.scalatest.{FlatSpecLike, Matchers}

class CryptoSpec extends Matchers with FlatSpecLike {

  object impl extends CryptoImpl

  "encrypt" should "return cipher text" in {
    val encryptKey = "1234567890ABCDEF1234567890ABCDEF"
    val plainText = "test"
    impl.encrypt(plainText, encryptKey) shouldBe "aoorg0q+GrzdNWjDMCAnWQ=="
  }
}
