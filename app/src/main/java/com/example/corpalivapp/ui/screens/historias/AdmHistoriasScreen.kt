package com.example.corpalivapp.ui.screens.historias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.viewmodel.HistoriasViewModel

@Composable
fun AdmHistoriasScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoriasViewModel = viewModel()
) {
    val historias by viewModel.historias.collectAsState()

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var historiaEnEdicion by remember {
        mutableStateOf<HistoriaExito?>(null)
    }

    var historiaPorEliminar by remember {
        mutableStateOf<HistoriaExito?>(null)
    }


    if (mostrarFormulario) {
        FormularioHistoriaScreen(
            historia = historiaEnEdicion,
            modifier = modifier,
            onCancelar = {
                mostrarFormulario = false
                historiaEnEdicion = null
            },
            onGuardar = { historia ->
                if (historiaEnEdicion == null) {
                    viewModel.agregar(historia)
                } else {
                    viewModel.actualizar(historia)
                }

                mostrarFormulario = false
                historiaEnEdicion = null
            }
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Administrar historias",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Total de historias: ${historias.size}"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Historia",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Acciones",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            historias.forEach { historia ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = historia.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = historia.subtitulo,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = "ID: ${historia.id}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    historiaEnEdicion = historia
                                    mostrarFormulario = true
                                }
                            ) {
                                Text("Editar")
                            }

                            OutlinedButton(
                                onClick = {
                                    historiaPorEliminar = historia
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    historiaEnEdicion = null
                    mostrarFormulario = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar historia")
            }
        }
    }

    historiaPorEliminar?.let { historia ->
        AlertDialog(
            onDismissRequest = {
                historiaPorEliminar = null
            },
            title = {
                Text("Eliminar historia")
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar \"${historia.titulo}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminar(historia.id)
                        historiaPorEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        historiaPorEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}