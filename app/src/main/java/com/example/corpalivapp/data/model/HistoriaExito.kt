package com.example.corpalivapp.data.model

import android.net.Uri
import com.example.corpalivapp.R

// Modelo que usa la interfaz. En el backend se guarda como HistoriaDto (ver data/remote/Dtos.kt).
data class HistoriaExito(
    val id: Int = 0,

    val titulo: String,
    val subtitulo: String,

    // Imagen por defecto cuando la historia no tiene foto.
    val imagen: Int = R.drawable.placeholder,

    val texto: String,

    // Foto recien elegida (galeria/camara) que todavia no se ha enviado al servidor.
    val imagenUri: Uri? = null,

    // Foto que ya esta guardada en el servidor (JPEG en Base64).
    val imagenBase64: String? = null
)
