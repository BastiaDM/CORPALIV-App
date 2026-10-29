package com.example.corpalivapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.R
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    pantallaActual: String,
    onPantallaSeleccionada: (String) -> Unit,
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier.size(120.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = pantallaActual == "Home",
                    onClick = {
                        onPantallaSeleccionada("Home")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Historias") },
                    selected = pantallaActual == "Historias",
                    onClick = {
                        onPantallaSeleccionada("Historias")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Administrar historias") },
                    selected = pantallaActual == "Administrar historias",
                    onClick = {
                        onPantallaSeleccionada("Administrar historias")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Programas") },
                    selected = pantallaActual == "Programas",
                    onClick = {
                        onPantallaSeleccionada("Programas")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Usuario") },
                    selected = pantallaActual == "Usuario",
                    onClick = {
                        onPantallaSeleccionada("Usuario")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Formulario colaboradores") },
                    selected = pantallaActual == "Formulario colaboradores",
                    onClick = {
                        onPantallaSeleccionada("Formulario colaboradores")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Administrar colaboraciones") },
                    selected = pantallaActual == "Administrar colaboraciones",
                    onClick = {
                        onPantallaSeleccionada("Administrar colaboraciones")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        contenido {
            scope.launch {
                drawerState.open()
            }
        }
    }
}