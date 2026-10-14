
package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.repository.HistoriasRepository

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.ui.res.painterResource
import com.example.corpalivapp.R

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale

@Composable
fun HistoriasScreen(
    modifier: Modifier = Modifier
) {
    val repository = remember { HistoriasRepository() }
    val historias = remember { repository.obtenerHistorias() }

    var historiaAbierta by remember {
        mutableStateOf<Int?>(null)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Historias de éxito",
            style = MaterialTheme.typography.headlineMedium
        )

        historias.forEach { historia ->
            HistoriaItem(
                historia = historia,
                expandida = historiaAbierta == historia.id,
                onClick = {
                    historiaAbierta =
                        if (historiaAbierta == historia.id) {
                            null
                        } else {
                            historia.id
                        }
                }
            )
        }
    }
}

@Composable
private fun HistoriaItem(
    historia: HistoriaExito,
    expandida: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = historia.titulo,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Image(
                        painter = painterResource(id = historia.imagen),
                        contentDescription = "Imagen de ${historia.titulo}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = historia.subtitulo,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = if (expandida) "▲" else "▼",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (expandida) {
                Text(
                    text = historia.texto,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
