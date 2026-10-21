
package com.example.corpalivapp.ui.screens.historias

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.corpalivapp.data.CamaraUriFactory
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.ui.components.ImagenInteligente

@Composable
fun FormularioHistoriaScreen(
    historia: HistoriaExito?,
    modifier: Modifier = Modifier,
    onCancelar: () -> Unit,
    onGuardar: (HistoriaExito) -> Unit
) {
    val context = LocalContext.current

    var titulo by remember(historia?.id) {
        mutableStateOf(historia?.titulo ?: "")
    }

    var subtitulo by remember(historia?.id) {
        mutableStateOf(historia?.subtitulo ?: "")
    }

    var texto by remember(historia?.id) {
        mutableStateOf(historia?.texto ?: "")
    }

    var imagenUri by remember(historia?.id) {
        mutableStateOf(historia?.imagenUri)
    }

    var intentoGuardar by remember(historia?.id) {
        mutableStateOf(false)
    }

    var uriPendienteCamara by remember {
        mutableStateOf<Uri?>(null)
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriSeleccionado ->
        if (uriSeleccionado != null) {
            imagenUri = uriSeleccionado
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && uriPendienteCamara != null) {
            imagenUri = uriPendienteCamara
        } else {
            uriPendienteCamara = null
        }
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            try {
                val uri = CamaraUriFactory.crearUri(context)
                uriPendienteCamara = uri
                camaraLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error al abrir la cámara: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Debes conceder el permiso de cámara en Ajustes.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun tomarFotografia() {
        val permisoConcedido = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (permisoConcedido) {
            try {
                val uri = CamaraUriFactory.crearUri(context)
                uriPendienteCamara = uri
                camaraLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error al abrir la cámara: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
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
            text = "Imagen de la historia",
            style = MaterialTheme.typography.titleMedium
        )

        ImagenInteligente(
            historia = historia?.copy(imagenUri = imagenUri)
                ?: if (imagenUri != null) {
                    HistoriaExito(
                        id = 0,
                        titulo = titulo,
                        subtitulo = subtitulo,
                        imagen = com.example.corpalivapp.R.drawable.placeholder,
                        texto = texto,
                        imagenUri = imagenUri
                    )
                } else {
                    null
                }
        )

        Button(
            onClick = {
                galeriaLauncher.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Collections,
                contentDescription = "Galería"
            )
            Text("  Elegir desde galería")
        }

        OutlinedButton(
            onClick = {
                tomarFotografia()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Cámara"
            )
            Text("  Tomar fotografía")
        }

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
                                ?: com.example.corpalivapp.R.drawable.placeholder,
                            texto = texto.trim(),
                            imagenUri = imagenUri
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
