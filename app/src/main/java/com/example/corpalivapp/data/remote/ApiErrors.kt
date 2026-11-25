package com.example.corpalivapp.data.remote

import com.google.gson.JsonParser
import java.io.IOException
import retrofit2.HttpException

fun Throwable.mensajeUsuario(): String = when (this) {
    is HttpException -> when (code()) {
        401, 403 -> "Debes iniciar sesión (pestaña Usuario) para realizar esta acción."
        400, 404 -> mensajeDelServidor() ?: "La solicitud no es válida."
        else -> "El servidor respondió con un error (${code()})."
    }
    is IOException -> "No se pudo conectar con el servidor. Revisa tu conexión."
    // Errores propios de la app (por ejemplo, no se pudo leer la imagen).
    is IllegalStateException, is IllegalArgumentException ->
        message ?: "No se pudo completar la operación."
    else -> "Ocurrió un error inesperado."
}

private fun HttpException.mensajeDelServidor(): String? {
    return try {
        val cuerpo = response()?.errorBody()?.string()
        if (cuerpo.isNullOrBlank()) {
            null
        } else {
            JsonParser.parseString(cuerpo).asJsonObject.get("message")?.asString
        }
    } catch (e: Exception) {
        null
    }
}
