package com.example.corpalivapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.ui.screens.AdmColaboracionesScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.corpalivapp.data.AppDataBase
import com.example.corpalivapp.data.repository.UsuarioRepository
import com.example.corpalivapp.ui.components.AppDrawer
import com.example.corpalivapp.ui.components.AppTopBar
import com.example.corpalivapp.ui.screens.HomeScreen
import com.example.corpalivapp.ui.screens.historias.HistoriasScreen
import com.example.corpalivapp.ui.screens.historias.AdmHistoriasScreen
import com.example.corpalivapp.ui.screens.ProgramasScreen
import com.example.corpalivapp.ui.screens.UsuarioScreen

import com.example.corpalivapp.data.repository.ColaboracionRepository
import com.example.corpalivapp.ui.screens.FormularioScreen
import com.example.corpalivapp.viewmodel.ColaboracionViewModel

import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var pantallaActual by remember {
        mutableStateOf("Home")
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val database = remember {
        AppDataBase.getInstance(context)
    }

    val usuarioRepository = remember {
        UsuarioRepository(database.usuarioDao())
    }

    val colaboracionViewModel: ColaboracionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                if (modelClass.isAssignableFrom(ColaboracionViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ColaboracionViewModel(
                        ColaboracionRepository(database.colaboracionDao())
                    ) as T
                }

                throw IllegalArgumentException("ViewModel desconocido")
            }
        }
    )

    AppDrawer(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = {
            pantallaActual = it
        }
    ) { abrirMenu ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppTopBar(
                    onMenuClick = {
                        abrirMenu()
                    }
                )
            }
        ) { innerPadding ->
            when (pantallaActual) {
                "Home" -> {
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                "Historias" -> {
                    HistoriasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                //temporalmente en este menu, futuramente en admin
                "Administrar historias" -> {
                    AdmHistoriasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                "Programas" -> {
                    ProgramasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                "Usuario" -> {
                    UsuarioScreen(
                        modifier = Modifier.padding(innerPadding),

                        onLogin = { email, password ->
                            scope.launch {
                                val resultado = withContext(Dispatchers.IO) {
                                    usuarioRepository.login(email, password)
                                }

                                resultado
                                    .onSuccess {
                                        Toast.makeText(
                                            context,
                                            "Inicio de sesión exitoso",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    .onFailure { error ->
                                        Toast.makeText(
                                            context,
                                            error.message ?: "Error al iniciar sesión",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                        },

                        onRegister = { email, password ->
                            scope.launch {
                                val resultado = withContext(Dispatchers.IO) {
                                    usuarioRepository.register(email, password)
                                }

                                resultado
                                    .onSuccess {
                                        Toast.makeText(
                                            context,
                                            "Usuario registrado correctamente",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    .onFailure { error ->
                                        Toast.makeText(
                                            context,
                                            error.message ?: "Error al registrar usuario",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                        }
                    )
                }
                "Formulario colaboradores" -> {
                    FormularioScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = colaboracionViewModel
                    )
                }
                "Administrar colaboraciones" -> {
                    AdmColaboracionesScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = colaboracionViewModel
                    )
                }
            }
        }
    }
}