
package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.viewmodel.ColaboracionViewModel

@Composable
fun FormularioScreen(
    modifier: Modifier = Modifier,
    viewModel: ColaboracionViewModel
) {
    var tipoColaborador by remember { mutableStateOf("") }

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }

    var razonSocial by remember { mutableStateOf("") }
    var nombreContacto by remember { mutableStateOf("") }

    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var programa by remember { mutableStateOf("") }
    var tipoColaboracion by remember { mutableStateOf("") }
    var tipoDonacion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var medioPago by remember { mutableStateOf("") }

    val mensaje by viewModel.mensaje.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Formulario de colaboración",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Completa tus datos y selecciona cómo deseas colaborar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SeccionFormulario(titulo = "1. Tipo de colaborador") {
            SelectorBotones(
                opciones = listOf("Persona", "Empresa"),
                seleccionado = tipoColaborador,
                alSeleccionar = { tipoColaborador = it }
            )
        }

        SeccionFormulario(titulo = "2. Datos del colaborador") {
            if (tipoColaborador.isBlank()) {
                Text(
                    text = "Selecciona Persona o Empresa para mostrar los demas campos.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (tipoColaborador == "Persona") {
                CampoFormulario("Nombre", nombre) { nombre = it }
                CampoFormulario("Apellidos", apellidos) { apellidos = it }
                CampoFormulario("RUT", rut) { rut = it }
                CampoFormulario(
                    etiqueta = "Edad",
                    valor = edad,
                    tipo = KeyboardType.Number
                ) { edad = it }
            }

            if (tipoColaborador == "Empresa") {
                CampoFormulario(
                    etiqueta = "Razón social",
                    valor = razonSocial
                ) { razonSocial = it }

                CampoFormulario("RUT de la empresa", rut) { rut = it }

                CampoFormulario(
                    etiqueta = "Nombre del contacto",
                    valor = nombreContacto
                ) { nombreContacto = it }
            }

            CampoFormulario(
                etiqueta = "Correo electrónico",
                valor = correo,
                tipo = KeyboardType.Email
            ) { correo = it }

            CampoFormulario(
                etiqueta = "Teléfono",
                valor = telefono,
                tipo = KeyboardType.Phone
            ) { telefono = it }
        }

        SeccionFormulario(titulo = "3. Programa al que deseas aportar") {
            SelectorBotones(
                opciones = listOf(
                    "Programa de Inclusión Laboral",
                    "Taller Sociolaboral"
                ),
                seleccionado = programa,
                alSeleccionar = { programa = it }
            )
        }

        SeccionFormulario(titulo = "4. Detalles de la donación") {
            Text(
                text = "Modalidad",
                fontWeight = FontWeight.Medium
            )

            SelectorBotones(
                opciones = listOf("Única", "Mensual"),
                seleccionado = tipoDonacion,
                alSeleccionar = { tipoDonacion = it }
            )

            CampoFormulario(
                etiqueta = "Monto en pesos chilenos (CLP)",
                valor = monto,
                tipo = KeyboardType.Number
            ) { monto = it }

            Text(
                text = "Medio de pago preferido",
                fontWeight = FontWeight.Medium
            )

            SelectorBotones(
                opciones = listOf(
                    "Transferencia",
                    "Efectivo",
                    "Por definir"
                ),
                seleccionado = medioPago,
                alSeleccionar = { medioPago = it }
            )

        }

        if (mensaje != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = mensaje!!,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Button(
            onClick = {
                val colaboracion = Colaboracion(
                    tipoColaborador = tipoColaborador,
                    nombre = if (tipoColaborador == "Empresa") {
                        razonSocial
                    } else {
                        nombre
                    },
                    apellidos = apellidos,
                    rut = rut,
                    edad = if (tipoColaborador == "Persona") {
                        edad.toIntOrNull()
                    } else {
                        null
                    },
                    razonSocial = razonSocial,
                    nombreContacto = nombreContacto,
                    correo = correo.trim(),
                    telefono = telefono,
                    programa = programa,
                    tipoDonacion = tipoDonacion,
                    monto = monto.toIntOrNull() ?: 0,
                    medioPago = medioPago
                )

                viewModel.guardar(colaboracion)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Registrar colaboración",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun SeccionFormulario(
    titulo: String,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider()

            contenido()
        }
    }
}

@Composable
private fun SelectorBotones(
    opciones: List<String>,
    seleccionado: String,
    alSeleccionar: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.chunked(2).forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                fila.forEach { opcion ->
                    FilterChip(
                        selected = seleccionado == opcion,
                        onClick = { alSeleccionar(opcion) },
                        label = {
                            Text(
                                text = opcion,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (fila.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    etiqueta: String,
    valor: String,
    tipo: KeyboardType = KeyboardType.Text,
    alCambiar: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        keyboardOptions = KeyboardOptions(keyboardType = tipo),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
