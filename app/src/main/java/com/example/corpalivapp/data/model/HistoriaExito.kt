package com.example.corpalivapp.data.model

import android.net.Uri
import com.example.corpalivapp.R

data class HistoriaExito(
    val id: Int = 0,

    val titulo: String,
    val subtitulo: String,

    val imagen: Int = R.drawable.placeholder,

    val texto: String,

    val imagenUri: Uri? = null,

    val imagenBase64: String? = null
)
