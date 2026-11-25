package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.remote.ApiService
import com.example.corpalivapp.data.remote.AuthRequest
import com.example.corpalivapp.data.remote.RetrofitInstance
import com.example.corpalivapp.data.remote.mensajeUsuario
import com.example.corpalivapp.data.security.SessionManager
import kotlin.coroutines.cancellation.CancellationException

class UsuarioRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        val normalizedEmail = email.trim().lowercase()

        return try {
            val respuesta = api.register(AuthRequest(normalizedEmail, password))

            if (respuesta.status == "ok") {
                Result.success(Unit)
            } else {
                Result.failure(
                    IllegalArgumentException(
                        respuesta.message ?: "No se pudo registrar el usuario."
                    )
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(IllegalStateException(e.mensajeUsuario()))
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        val normalizedEmail = email.trim().lowercase()

        return try {
            val respuesta = api.login(AuthRequest(normalizedEmail, password))
            val token = respuesta.token

            if (respuesta.status == "ok" && !token.isNullOrBlank()) {
                SessionManager.guardarToken(token)
                Result.success(Unit)
            } else {
                Result.failure(
                    IllegalArgumentException(
                        respuesta.message ?: "Correo o contraseña incorrectos."
                    )
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(IllegalStateException(e.mensajeUsuario()))
        }
    }
}
