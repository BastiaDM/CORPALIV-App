package com.example.corpalivapp.data.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {

    private const val ITERATIONS = 600_000
    private const val KEY_LENGTH = 256

    fun newSalt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)

        return Base64.encodeToString(
            salt,
            Base64.NO_WRAP
        )
    }

    fun hash(password: String, saltBase64: String): String {
        val salt = Base64.decode(saltBase64, Base64.NO_WRAP)
        val spec = PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATIONS,
            KEY_LENGTH
        )

        return try {
            val key = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .encoded

            Base64.encodeToString(key, Base64.NO_WRAP)
        } finally {
            spec.clearPassword()
        }
    }

    fun verify(
        password: String,
        salt: String,
        expectedHash: String
    ): Boolean {
        val calculatedHash = hash(password, salt)

        return java.security.MessageDigest.isEqual(
            Base64.decode(calculatedHash, Base64.NO_WRAP),
            Base64.decode(expectedHash, Base64.NO_WRAP)
        )
    }
}
