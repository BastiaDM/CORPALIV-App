
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var aporteMensual by remember { mutableStateOf(false) }
    var monto by remember { mutableStateOf("") }
    var medioPago by remember { mutableStateOf("") }

    var intentoRegistrar by remember { mutableStateOf(false) }

    val tipoDonacion = if (aporteMensual) "Mensual" else "Única"

    val mensaje by viewModel.mensaje.collectAsState()
    val errores by viewModel.errores.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "¡Eres genial! \n\nGracias por tu compromiso con la Discapacidad Múltiple",
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
                error = errores.tipoColaborador,
                mostrarError = intentoRegistrar,
                alSeleccionar = {
                    tipoColaborador = it
                    viewModel.validarCampo("tipoColaborador", it)
                }
            )
        }

        SeccionFormulario(titulo = "2. Datos del colaborador") {
            if (tipoColaborador.isBlank()) {
                Text(
                    text = "Selecciona Persona o Empresa para mostrar los demás campos.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (tipoColaborador == "Persona") {
                CampoFormulario(
                    etiqueta = "Nombre",
                    valor = nombre,
                    error = errores.nombre,
                    mostrarErrores = intentoRegistrar
                ) {
                    nombre = it
                    viewModel.validarCampo("nombre", it)
                }

                CampoFormulario(
                    etiqueta = "Apellidos",
                    valor = apellidos,
                    error = errores.apellidos,
                    mostrarErrores = intentoRegistrar
                ) {
                    apellidos = it
                    viewModel.validarCampo("apellidos", it)
                }

                CampoFormulario(
                    etiqueta = "RUT",
                    valor = rut,
                    error = errores.rut,
                    mostrarErrores = intentoRegistrar
                ) {
                    rut = it
                    viewModel.validarCampo("rut", it)
                }

                CampoFormulario(
                    etiqueta = "Edad",
                    valor = edad,
                    tipo = KeyboardType.Number,
                    error = errores.edad,
                    mostrarErrores = intentoRegistrar
                ) {
                    edad = it
                    viewModel.validarCampo("edad", it)
                }
            }

            if (tipoColaborador == "Empresa") {
                CampoFormulario(
                    etiqueta = "Razón social",
                    valor = razonSocial,
                    error = errores.razonSocial,
                    mostrarErrores = intentoRegistrar
                ) {
                    razonSocial = it
                    viewModel.validarCampo("razonSocial", it)
                }

                CampoFormulario(
                    etiqueta = "RUT de la empresa",
                    valor = rut,
                    error = errores.rut,
                    mostrarErrores = intentoRegistrar
                ) {
                    rut = it
                    viewModel.validarCampo("rut", it)
                }

                CampoFormulario(
                    etiqueta = "Nombre del contacto",
                    valor = nombreContacto,
                    error = errores.nombreContacto,
                    mostrarErrores = intentoRegistrar
                ) {
                    nombreContacto = it
                    viewModel.validarCampo("nombreContacto", it)
                }
            }

            CampoFormulario(
                etiqueta = "Correo electrónico",
                valor = correo,
                tipo = KeyboardType.Email,
                error = errores.correo,
                mostrarErrores = intentoRegistrar
            ) {
                correo = it
                viewModel.validarCampo("correo", it)
            }

            CampoFormulario(
                etiqueta = "Teléfono (opcional)",
                valor = telefono,
                tipo = KeyboardType.Phone,
                error = errores.telefono,
                mostrarErrores = intentoRegistrar
            ) {
                telefono = it
                viewModel.validarCampo("telefono", it)
            }
        }

        SeccionFormulario(titulo = "3. Programa al que deseas aportar") {
            SelectorBotones(
                opciones = listOf(
                    "Programa de Inclusión Laboral",
                    "Taller Sociolaboral"
                ),
                seleccionado = programa,
                error = errores.programa,
                mostrarError = intentoRegistrar,
                alSeleccionar = {
                    programa = it
                    viewModel.validarCampo("programa", it)
                }
            )
        }

        SeccionFormulario(titulo = "4. Detalles de la donación") {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("¿Quieres hacerte socio con un aporte mensual?")

                    Text(
                        text = if (aporteMensual) {
                            "Tu aporte será mensual."
                        } else {
                            "Realizarás un aporte único."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = aporteMensual,
                    onCheckedChange = {
                        aporteMensual = it
                        viewModel.validarCampo(
                            campo = "monto",
                            valor = monto,
                            tipoDonacion = if (it) "Mensual" else "Única"
                        )
                    }
                )
            }

            CampoFormulario(
                etiqueta = "Monto en pesos chilenos (CLP)",
                valor = monto,
                tipo = KeyboardType.Number,
                error = errores.monto,
                mostrarErrores = intentoRegistrar
            ) {
                monto = it
                viewModel.validarCampo(
                    campo = "monto",
                    valor = it,
                    tipoDonacion = tipoDonacion
                )
            }

            Text(
                text = "Medio de pago preferido",
                fontWeight = FontWeight.Medium
            )

            SelectorBotones(
                opciones = listOf(
                    "Transferencia",
                    "Tarjeta Credito/Debito",
                    "Por definir"
                ),
                seleccionado = medioPago,
                error = errores.medioPago,
                mostrarError = intentoRegistrar,
                alSeleccionar = {
                    medioPago = it
                    viewModel.validarCampo("medioPago", it)
                }
            )
        }

        if (mensaje != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
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
                intentoRegistrar = true

                val colaboracion = Colaboracion(
                    tipoColaborador = tipoColaborador,
                    nombre = if (tipoColaborador == "Empresa") {
                        razonSocial.trim()
                    } else {
                        nombre.trim()
                    },
                    apellidos = apellidos.trim(),
                    rut = rut.trim(),
                    edad = if (tipoColaborador == "Persona") {
                        edad.toIntOrNull()
                    } else {
                        null
                    },
                    razonSocial = razonSocial.trim(),
                    nombreContacto = nombreContacto.trim(),
                    correo = correo.trim(),
                    telefono = telefono.trim(),
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
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
    error: String?,
    mostrarError: Boolean,
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

        if (mostrarError && error != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    etiqueta: String,
    valor: String,
    tipo: KeyboardType = KeyboardType.Text,
    error: String?,
    mostrarErrores: Boolean,
    alCambiar: (String) -> Unit
) {
    var tocado by remember(etiqueta) {
        mutableStateOf(false)
    }

    val mostrarEstado = tocado || mostrarErrores
    val tieneError = mostrarEstado && error != null
    val esValido = mostrarEstado && error == null && valor.isNotBlank()

    OutlinedTextField(
        value = valor,
        onValueChange = {
            tocado = true
            alCambiar(it)
        },
        label = { Text(etiqueta) },
        keyboardOptions = KeyboardOptions(keyboardType = tipo),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = tieneError,
        trailingIcon = {
            when {
                tieneError -> {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Campo inválido",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                esValido -> {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Campo válido",
                        tint = Color(0xFF2E7D32)
                    )
                }
            }
        },
        supportingText = {
            if (tieneError) {
                Text(
                    text = error.orEmpty(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
