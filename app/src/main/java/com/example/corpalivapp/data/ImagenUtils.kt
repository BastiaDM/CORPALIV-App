package com.example.corpalivapp.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImagenUtils {

    fun uriABase64(
        context: Context,
        uri: Uri,
        maxLado: Int = 800,
        calidad: Int = 80
    ): String? {
        return try {
            val resolver = context.contentResolver

            val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, medidas)
            }
            if (medidas.outWidth <= 0 || medidas.outHeight <= 0) return null

            var muestreo = 1
            while (medidas.outWidth / muestreo > maxLado * 2 ||
                medidas.outHeight / muestreo > maxLado * 2
            ) {
                muestreo *= 2
            }
            val opciones = BitmapFactory.Options().apply { inSampleSize = muestreo }
            val original = resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, opciones)
            } ?: return null

            val rotacion = resolver.openInputStream(uri)?.use { leerRotacion(it) } ?: 0
            val escala = minOf(1f, maxLado.toFloat() / maxOf(original.width, original.height))
            val matriz = Matrix().apply {
                postScale(escala, escala)
                if (rotacion != 0) postRotate(rotacion.toFloat())
            }
            val finalBitmap = Bitmap.createBitmap(
                original, 0, 0, original.width, original.height, matriz, true
            )

            val salida = ByteArrayOutputStream()
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, calidad, salida)

            if (finalBitmap !== original) original.recycle()
            finalBitmap.recycle()

            Base64.encodeToString(salida.toByteArray(), Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    private fun leerRotacion(entrada: InputStream): Int {
        val exif = ExifInterface(entrada)
        return when (
            exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}
