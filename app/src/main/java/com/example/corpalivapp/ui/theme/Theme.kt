package com.example.corpalivapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = Principal,
    onPrimary = Blanco,

    secondary = Claro,
    onSecondary = TextoOscuro,

    tertiary = Oscuro,
    onTertiary = Blanco,

    background = FondoClaro,
    onBackground = TextoOscuro,

    surface = Blanco,
    onSurface = TextoOscuro,

    error = Error,
    onError = Blanco
)

@Composable
fun MiAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}