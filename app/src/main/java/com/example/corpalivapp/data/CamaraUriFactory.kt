
package com.example.corpalivapp.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object CamaraUriFactory {

    fun crearUri(context: Context): Uri {
        val directorio = File(context.filesDir, "historias")

        if (!directorio.exists() && !directorio.mkdirs()) {
            throw IllegalStateException(
                "No se pudo crear el directorio de imágenes"
            )
        }

        val archivo = File.createTempFile(
            "historia_",
            ".jpg",
            directorio
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
    }
}
