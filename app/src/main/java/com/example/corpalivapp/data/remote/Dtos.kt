package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.model.HistoriaExito

// ---------- Autenticacion ----------

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val status: String? = null,
    val token: String? = null,
    val message: String? = null
)

// ---------- Historias de exito ----------

// La imagen viaja como texto Base64 (el backend no guarda Uris del telefono).
data class HistoriaDto(
    val id: Int? = null,
    val titulo: String,
    val subtitulo: String,
    val texto: String,
    val imagenBase64: String? = null
)

fun HistoriaDto.toModel(): HistoriaExito = HistoriaExito(
    id = id ?: 0,
    titulo = titulo,
    subtitulo = subtitulo,
    texto = texto,
    imagenBase64 = imagenBase64
)

// imagenBase64: null = "no cambiar la imagen" (el servidor conserva la que tenia).
fun HistoriaExito.toDto(imagenBase64: String?): HistoriaDto = HistoriaDto(
    id = if (id == 0) null else id,
    titulo = titulo,
    subtitulo = subtitulo,
    texto = texto,
    imagenBase64 = imagenBase64
)
