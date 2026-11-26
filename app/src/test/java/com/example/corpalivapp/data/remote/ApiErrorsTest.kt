package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.model.HistoriaExito
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ApiErrorsTest {

    private fun errorHttp(codigo: Int, cuerpo: String = ""): HttpException {
        val body = cuerpo.toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(codigo, body))
    }

    @Test
    fun `errores de red y de sesion muestran mensajes claros`() {
        assertEquals(
            "No se pudo conectar con el servidor. Revisa tu conexión.",
            IOException("sin red").mensajeUsuario()
        )
        assertEquals(
            "Debes iniciar sesión (pestaña Usuario) para realizar esta acción.",
            errorHttp(401).mensajeUsuario()
        )
        assertEquals(
            "Debes iniciar sesión (pestaña Usuario) para realizar esta acción.",
            errorHttp(403).mensajeUsuario()
        )
    }

    @Test
    fun `errores del servidor usan su mensaje o uno generico`() {
        assertEquals(
            "La edad es obligatoria.",
            errorHttp(400, """{"message":"La edad es obligatoria."}""").mensajeUsuario()
        )
        assertEquals("La solicitud no es válida.", errorHttp(404).mensajeUsuario())
        assertEquals(
            "El servidor respondió con un error (500).",
            errorHttp(500).mensajeUsuario()
        )
    }

    @Test
    fun `errores propios de la app usan su mensaje o uno por defecto`() {
        assertEquals(
            "No se pudo leer la imagen.",
            IllegalStateException("No se pudo leer la imagen.").mensajeUsuario()
        )
        assertEquals(
            "No se pudo completar la operación.",
            IllegalArgumentException().mensajeUsuario()
        )
        assertEquals("Ocurrió un error inesperado.", RuntimeException("x").mensajeUsuario())
    }

    @Test
    fun `los DTO de historias se convierten a modelo y de vuelta`() {
        val modelo = HistoriaDto(
            id = null, titulo = "T", subtitulo = "S", texto = "X", imagenBase64 = "b64"
        ).toModel()
        assertEquals(0, modelo.id)
        assertEquals("b64", modelo.imagenBase64)

        val nueva = HistoriaExito(
            id = 0, titulo = "T", subtitulo = "S", imagen = 0, texto = "X"
        ).toDto("b64")
        assertNull(nueva.id)
        assertEquals("b64", nueva.imagenBase64)

        val existente = HistoriaExito(
            id = 7, titulo = "T", subtitulo = "S", imagen = 0, texto = "X"
        ).toDto(null)
        assertEquals(7, existente.id)
        assertNull(existente.imagenBase64)
    }
}