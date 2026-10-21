package com.example.corpalivapp.data.model

import android.net.Uri

data class HistoriaExito(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagen: Int,
    val texto: String,
    val imagenUri: Uri? = null
)
