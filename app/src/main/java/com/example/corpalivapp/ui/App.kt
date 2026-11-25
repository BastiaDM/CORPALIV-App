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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.example.corpalivapp.viewmodel.UsuarioViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var pantallaActual by remember {
        mutableStateOf("Home")
    }
    val colaboracionViewModel: ColaboracionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                if (modelClass.isAssignableFrom(ColaboracionViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ColaboracionViewModel(
                        ColaboracionRepository()
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
                        modifier = Modifier.padding(innerPadding),
                        onPantallaSeleccionada = { it ->
                            pantallaActual = it
                        }
                    )
                }

                "Programas" -> {
                    ProgramasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                "Usuario" -> {
                    val usuarioViewModel: UsuarioViewModel = viewModel()

                    UsuarioScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = usuarioViewModel
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