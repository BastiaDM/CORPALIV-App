package com.example.corpalivapp.data.remote

object ApiConfig {

    // URL del API Gateway del backend (corpaliv-backend, puerto 9090).
    //
    //  - Emulador de Android Studio: 10.0.2.2 es el "localhost" del PC en el que estes ejecutando.
    //  - Celular fisico: usar la IP de tu PC en la red Wi-Fi, por ejemplo "http://192.168.1.50:9090/"
    //    (y permite el puerto 9090 en el firewall de Windows).
    //
    // Debe terminar en "/".
    const val BASE_URL = "http://127.0.0.1:9090/"
}
