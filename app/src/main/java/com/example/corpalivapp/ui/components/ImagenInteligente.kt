package com.example.corpalivapp.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.corpalivapp.R
import com.example.corpalivapp.data.model.HistoriaExito
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImagenInteligente(
    historia: HistoriaExito?,
    modifier: Modifier = Modifier
) {
    val imagenServidor by produceState<ImageBitmap?>(
        initialValue = null,
        key1 = historia?.imagenBase64
    ) {
        val base64 = historia?.imagenBase64
        value = if (base64.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.Default) {
                try {
                    val bytes = Base64.decode(base64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        val bitmap = imagenServidor

        if (historia?.imagenUri != null) {
            AsyncImage(
                model = historia.imagenUri,
                contentDescription = "Imagen de la historia",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
        } else if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Imagen de la historia",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = historia?.imagen ?: R.drawable.placeholder),
                contentDescription = "Imagen provisional",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
