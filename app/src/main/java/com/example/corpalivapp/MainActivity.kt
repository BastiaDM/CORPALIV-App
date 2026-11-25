package com.example.corpalivapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.corpalivapp.data.security.SessionManager
import com.example.corpalivapp.ui.App
import com.example.corpalivapp.ui.theme.MiAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        SessionManager.init(applicationContext)

        setContent {
            MiAppTheme {
                App()
            }
        }
    }
}
