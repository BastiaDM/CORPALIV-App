package com.example.corpalivapp.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onMenuClick: () -> Unit
) {
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Magenta
    ),
        navigationIcon = {
            TextButton(
                onClick = onMenuClick
            ) {
                Text("MENU",color=Color.White, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
            }
        },
        title = {
            Text("")
        }
    )
}