package com.charlieworld.housing.utils

import java.util.Base64

import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}

trait CryptoImpl extends Crypto {

  private final val CHARSET = "UTF-8"
  private final val iv: String = "0" * 16
  private final val method = "AES/CBC/PKCS5Padding"

  override def encrypt(plain: String, encryptKey: String): String = {
    val keySpec = new SecretKeySpec(encryptKey.getBytes(CHARSET), "AES")
    val cipher = Cipher.getInstance(method)
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(CHARSET)))
    val encrypted = cipher.doFinal(plain.getBytes(CHARSET))
    new String(Base64.getEncoder.encode(encrypted))
  }
}
