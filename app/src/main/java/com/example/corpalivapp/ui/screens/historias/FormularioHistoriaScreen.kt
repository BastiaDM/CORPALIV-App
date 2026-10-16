package com.example.corpalivapp.ui.screens.historias

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.R
import com.example.corpalivapp.data.model.HistoriaExito

@Composable
fun FormularioHistoriaScreen(
    historia: HistoriaExito?,
    modifier: Modifier = Modifier,
    onCancelar: () -> Unit,
    onGuardar: (HistoriaExito) -> Unit
) {
    var titulo by remember(historia?.id) {
        mutableStateOf(historia?.titulo ?: "")
    }

    var subtitulo by remember(historia?.id) {
        mutableStateOf(historia?.subtitulo ?: "")
    }

    var texto by remember(historia?.id) {
        mutableStateOf(historia?.texto ?: "")
    }

    var intentoGuardar by remember(historia?.id) {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (historia == null) {
                "Agregar historia"
            } else {
                "Editar historia"
            },
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = intentoGuardar && titulo.isBlank(),
            supportingText = {
                if (intentoGuardar && titulo.isBlank()) {
                    Text("El título es obligatorio")
                }
            }
        )

        OutlinedTextField(
            value = subtitulo,
            onValueChange = { subtitulo = it },
            label = { Text("Subtítulo") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            isError = intentoGuardar && subtitulo.isBlank(),
            supportingText = {
                if (intentoGuardar && subtitulo.isBlank()) {
                    Text("El subtítulo es obligatorio")
                }
            }
        )

        Text(
            text = "Imagen provisional",
            style = MaterialTheme.typography.titleMedium
        )

        Image(
            painter = painterResource(
                id = historia?.imagen ?: R.drawable.placeholder
            ),
            contentDescription = "Imagen de la historia",
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Por ahora se utiliza una imagen local provisional."
        )

        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Texto completo") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            isError = intentoGuardar && texto.isBlank(),
            supportingText = {
                if (intentoGuardar && texto.isBlank()) {
                    Text("El texto completo es obligatorio")
                }
            }
        )

        Button(
            onClick = {
                intentoGuardar = true

                if (
                    titulo.isNotBlank() &&
                    subtitulo.isNotBlank() &&
                    texto.isNotBlank()
                ) {
                    onGuardar(
                        HistoriaExito(
                            id = historia?.id ?: 0,
                            titulo = titulo.trim(),
                            subtitulo = subtitulo.trim(),
                            imagen = historia?.imagen
                                ?: R.drawable.placeholder,
                            texto = texto.trim()
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        TextButton(
            onClick = onCancelar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}