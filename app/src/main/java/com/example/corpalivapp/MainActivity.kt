package com.example.corpalivapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.corpalivapp.data.AppDataBase
import com.example.corpalivapp.data.repository.UsuarioRepository
import com.example.corpalivapp.ui.App
import com.example.corpalivapp.ui.theme.MiAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDataBase.getInstance(applicationContext)
        val usuarioRepository = UsuarioRepository(database.usuarioDao())

        lifecycleScope.launch(Dispatchers.IO) {
            usuarioRepository.createDefaultUserIfNeeded()
        }

        setContent {
            MiAppTheme {
                App()
            }
        }
    }
}
