package com.example.corpalivapp.data

import android.net.Uri
import androidx.room.TypeConverter

class UriTypeConverter {

    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(valor: String?): Uri? {
        return valor?.let { Uri.parse(it) }
    }
}