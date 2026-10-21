package com.example.corpalivapp.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.corpalivapp.R
import com.example.corpalivapp.data.model.HistoriaExito

@Composable
fun ImagenInteligente(
    historia: HistoriaExito?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (historia?.imagenUri != null) {
            AsyncImage(
                model = historia.imagenUri,
                contentDescription = "Imagen de la historia",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
        } else if (historia != null) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = historia.imagen),
                contentDescription = "Imagen de la historia",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Imagen provisional",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
