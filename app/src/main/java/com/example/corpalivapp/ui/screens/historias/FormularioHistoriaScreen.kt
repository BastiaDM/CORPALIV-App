
package com.example.corpalivapp.ui.screens.historias

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.R
import com.example.corpalivapp.data.CamaraUriFactory
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.ui.components.ImagenInteligente
import com.example.corpalivapp.viewmodel.ErroresHistoria
import com.example.corpalivapp.viewmodel.HistoriasViewModel

@Composable
fun FormularioHistoriaScreen(
    historia: HistoriaExito?,
    modifier: Modifier = Modifier,
    viewModel: HistoriasViewModel = viewModel(),
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

    var errores by remember(historia?.id) {
        mutableStateOf(ErroresHistoria())
    }

    var uriPendienteCamara by remember {
        mutableStateOf<Uri?>(null)
    }

    // Abre la galería y obtiene la imagen seleccionada.
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriSeleccionado ->
        if (uriSeleccionado != null) {
            imagenUri = uriSeleccionado
        }
    }

    // Abre la cámara y recibe el resultado de la fotografía.
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && uriPendienteCamara != null) {
            imagenUri = uriPendienteCamara
        } else {
            uriPendienteCamara = null
        }
    }

    // Solicita el permiso para utilizar la cámara.
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

    // Comprueba el permiso y abre la cámara.
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

        // Campo título.
        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it

                if (intentoGuardar) {
                    errores = viewModel.validarHistoria(
                        titulo = it,
                        subtitulo = subtitulo,
                        texto = texto
                    )
                }
            },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errores.titulo != null,
            supportingText = {
                errores.titulo?.let { mensaje ->
                    Text(mensaje)
                }
            }
        )

        // Campo subtítulo.
        OutlinedTextField(
            value = subtitulo,
            onValueChange = {
                subtitulo = it

                if (intentoGuardar) {
                    errores = viewModel.validarHistoria(
                        titulo = titulo,
                        subtitulo = it,
                        texto = texto
                    )
                }
            },
            label = { Text("Subtítulo") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            isError = errores.subtitulo != null,
            supportingText = {
                errores.subtitulo?.let { mensaje ->
                    Text(mensaje)
                }
            }
        )

        Text(
            text = "Imagen de la historia",
            style = MaterialTheme.typography.titleMedium
        )

        // Muestra la imagen seleccionada o la imagen de la historia existente.
        ImagenInteligente(
            historia = historia?.copy(imagenUri = imagenUri)
                ?: if (imagenUri != null) {
                    HistoriaExito(
                        id = 0,
                        titulo = titulo,
                        subtitulo = subtitulo,
                        imagen = R.drawable.placeholder,
                        texto = texto,
                        imagenUri = imagenUri
                    )
                } else {
                    null
                }
        )

        // Seleccionar una imagen de la galería.
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

        // Tomar una fotografía.
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

        // Campo texto completo.
        OutlinedTextField(
            value = texto,
            onValueChange = {
                texto = it

                if (intentoGuardar) {
                    errores = viewModel.validarHistoria(
                        titulo = titulo,
                        subtitulo = subtitulo,
                        texto = it
                    )
                }
            },
            label = { Text("Texto completo") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            isError = errores.texto != null,
            supportingText = {
                errores.texto?.let { mensaje ->
                    Text(mensaje)
                }
            }
        )

        // Validar y guardar.
        Button(
            onClick = {
                intentoGuardar = true

                errores = viewModel.validarHistoria(
                    titulo = titulo,
                    subtitulo = subtitulo,
                    texto = texto
                )

                if (errores.esValida) {
                    onGuardar(
                        HistoriaExito(
                            id = historia?.id ?: 0,
                            titulo = titulo.trim(),
                            subtitulo = subtitulo.trim(),
                            imagen = historia?.imagen
                                ?: R.drawable.placeholder,
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
