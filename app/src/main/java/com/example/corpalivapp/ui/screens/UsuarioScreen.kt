package com.example.corpalivapp.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.viewmodel.EstadoAutenticacion
import com.example.corpalivapp.viewmodel.UsuarioViewModel

@Composable
fun UsuarioScreen(
    modifier: Modifier = Modifier,
    viewModel: UsuarioViewModel
) {
    var isRegister by remember { mutableStateOf(false) }

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val estado by viewModel.estado.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()

    val progresoAnimado by animateFloatAsState(
        targetValue = progreso.coerceIn(0f, 100f) / 100f,
        animationSpec = tween(durationMillis = 400),
        label = "progresoLogin"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRegister) "Crear cuenta" else "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = viewModel::actualizarEmail,
            label = { Text("Correo electrónico") },
            singleLine = true,
            isError = errores.email != null,
            supportingText = {
                errores.email?.let { Text(it) }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            enabled = estado != EstadoAutenticacion.LOADING,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = viewModel::actualizarPassword,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = errores.password != null,
            supportingText = {
                errores.password?.let { Text(it) }
            },
            enabled = estado != EstadoAutenticacion.LOADING,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        AnimatedContent(
            targetState = estado,
            label = "resultadoAutenticacion"
        ) { estadoActual ->
            when (estadoActual) {
                EstadoAutenticacion.IDLE -> {
                    Spacer(Modifier.height(8.dp))
                }

                EstadoAutenticacion.LOADING -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { progresoAnimado },
                                modifier = Modifier.fillMaxSize(),
                                strokeWidth = 7.dp,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )

                            Text(
                                text = "${(progresoAnimado * 100).toInt()}%",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                        Text("Procesando...")
                    }
                }

                EstadoAutenticacion.SUCCESS -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Operación exitosa",
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(64.dp)
                        )

                        Text(
                            text = mensaje.orEmpty(),
                            color = Color(0xFF16A34A)
                        )
                    }
                }

                EstadoAutenticacion.ERROR -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Error",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(64.dp)
                        )

                        Text(
                            text = mensaje.orEmpty(),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.autenticar(esRegistro = isRegister)
            },
            enabled = estado != EstadoAutenticacion.LOADING,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegister) "Registrarse" else "Entrar")
        }

        TextButton(
            onClick = {
                isRegister = !isRegister
                viewModel.limpiarResultado()
            },
            enabled = estado != EstadoAutenticacion.LOADING,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isRegister)
                    "¿Ya tienes cuenta? Inicia sesión"
                else
                    "¿No tienes cuenta? Regístrate"
            )
        }
    }
}