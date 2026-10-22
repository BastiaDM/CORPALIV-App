
package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.viewmodel.ColaboracionViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdmColaboracionesScreen(
    modifier: Modifier = Modifier,
    viewModel: ColaboracionViewModel
) {
    val colaboraciones by viewModel.colaboraciones.collectAsState()

    var idBuscado by remember {
        mutableStateOf("")
    }

    var colaboracionSeleccionada by remember {
        mutableStateOf<Colaboracion?>(null)
    }

    // Filtra las colaboraciones según el ID escrito.
    // Permite coincidencias parciales mientras se escribe.
    val colaboracionesFiltradas = if (idBuscado.isBlank()) {
        colaboraciones
    } else {
        colaboraciones.filter {
            it.id.toString().contains(idBuscado.trim())
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Administrar colaboraciones",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Registros encontrados: ${colaboracionesFiltradas.size}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = idBuscado,
            onValueChange = {
                idBuscado = it.filter { caracter ->
                    caracter.isDigit()
                }
            },
            label = {
                Text("Buscar por ID")
            },
            placeholder = {
                Text("Ejemplo: 3")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider()

        if (colaboracionesFiltradas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (colaboraciones.isEmpty()) {
                        "No hay colaboraciones registradas."
                    } else {
                        "No se encontraron colaboraciones con ese ID."
                    },
                    fontWeight = FontWeight.Medium
                )

                if (colaboraciones.isEmpty()) {
                    Text(
                        text = "Cuando se registre una colaboración desde el formulario, aparecerá aquí."
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = colaboracionesFiltradas,
                    key = { it.id }
                ) { colaboracion ->
                    ColaboracionCard(
                        colaboracion = colaboracion,
                        alEliminar = {
                            colaboracionSeleccionada = colaboracion
                        }
                    )
                }
            }
        }
    }

    // Diálogo de confirmación antes de eliminar.
    if (colaboracionSeleccionada != null) {
        AlertDialog(
            onDismissRequest = {
                colaboracionSeleccionada = null
            },
            title = {
                Text("Eliminar colaboración")
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar el registro de " +
                            "${colaboracionSeleccionada?.nombre}? " +
                            "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        colaboracionSeleccionada?.let {
                            viewModel.eliminar(it)
                        }

                        colaboracionSeleccionada = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        colaboracionSeleccionada = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ColaboracionCard(
    colaboracion: Colaboracion,
    alEliminar: () -> Unit
) {
    val formatoMoneda = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = colaboracion.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "ID: ${colaboracion.id}"
            )

            Text(
                text = "Tipo: ${colaboracion.tipoColaborador}"
            )

            if (colaboracion.rut.isNotBlank()) {
                Text(
                    text = "RUT: ${colaboracion.rut}"
                )
            }

            Text(
                text = "Correo: ${colaboracion.correo}"
            )

            if (colaboracion.telefono.isNotBlank()) {
                Text(
                    text = "Teléfono: ${colaboracion.telefono}"
                )
            }

            HorizontalDivider()

            Text(
                text = "Programa: ${colaboracion.programa}"
            )

            Text(
                text = "Modalidad: ${colaboracion.tipoDonacion}"
            )

            Text(
                text = "Monto: ${formatoMoneda.format(colaboracion.monto)}",
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = alEliminar
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}
