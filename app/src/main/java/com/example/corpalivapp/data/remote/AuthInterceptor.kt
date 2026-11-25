package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.security.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

// Agrega "Authorization: Bearer <token>" a las peticiones que lo necesitan.
class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = SessionManager.token()

        val enviaToken = !token.isNullOrBlank() && !esRutaPublica(original)
        val request = if (enviaToken) {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }

        val response = chain.proceed(request)

        // Token vencido o invalido: se descarta para que la app pida iniciar sesion de nuevo.
        if (enviaToken && response.code == 401) {
            SessionManager.cerrarSesion()
        }
        return response
    }

    // Debe coincidir con las rutas publicas del api-gateway. Si se envia un token vencido a una
    // ruta publica, Spring Security responde 401 igual, por eso en estas rutas no se envia.
    private fun esRutaPublica(request: Request): Boolean {
        val ruta = request.url.encodedPath
        return ruta.startsWith("/auth/") ||
                (request.method == "GET" && ruta.startsWith("/historias")) ||
                (request.method == "POST" && ruta == "/colaboraciones")
    }
}
