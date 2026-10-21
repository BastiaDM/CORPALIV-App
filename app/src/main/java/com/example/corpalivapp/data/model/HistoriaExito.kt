package com.example.corpalivapp.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.corpalivapp.data.UriTypeConverter

@Entity(tableName = "historias_exito")
@TypeConverters(UriTypeConverter::class)
data class HistoriaExito(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,
    val subtitulo: String,
    val imagen: Int,
    val texto: String,
    val imagenUri: Uri? = null
)