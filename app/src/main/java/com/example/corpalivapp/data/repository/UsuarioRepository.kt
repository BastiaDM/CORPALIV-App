package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.dao.UsuarioDao
import com.example.corpalivapp.data.model.Usuario
import com.example.corpalivapp.data.security.PasswordHasher

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {
    suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        val normalizedEmail = email.trim().lowercase()

        if (usuarioDao.findByEmail(normalizedEmail) != null) {
            return Result.failure(
                IllegalArgumentException(
                    "Este correo ya está registrado."
                )
            )
        }

        val salt = PasswordHasher.newSalt()
        val hash = PasswordHasher.hash(password, salt)

        usuarioDao.insert(
            Usuario(
                email = normalizedEmail,
                passwordHash = hash,
                passwordSalt = salt
            )
        )

        return Result.success(Unit)
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        val normalizedEmail = email.trim().lowercase()
        val user = usuarioDao.findByEmail(normalizedEmail)

        if (user == null ||
            !PasswordHasher.verify(
                password,
                user.passwordSalt,
                user.passwordHash
            )
        ) {
            return Result.failure(
                IllegalArgumentException(
                    "Correo o contraseña incorrectos."
                )
            )
        }

        return Result.success(Unit)
    }
    suspend fun createDefaultUserIfNeeded() {
        val email = "testo@demo.com"

        if (usuarioDao.findByEmail(email) == null) {
            val password = "Test1234!"
            val salt = PasswordHasher.newSalt()
            val hash = PasswordHasher.hash(password, salt)

            usuarioDao.insert(
                Usuario(
                    email = email,
                    passwordHash = hash,
                    passwordSalt = salt
                )
            )
        }
    }
}
