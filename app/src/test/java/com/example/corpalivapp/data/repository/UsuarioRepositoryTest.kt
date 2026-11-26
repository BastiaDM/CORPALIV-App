package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.remote.ApiService
import com.example.corpalivapp.data.remote.AuthRequest
import com.example.corpalivapp.data.remote.AuthResponse
import com.example.corpalivapp.data.remote.HistoriaDto
import com.example.corpalivapp.data.security.SessionManager
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UsuarioRepositoryTest {

    private val api = mockk<ApiService>()
    private val usuarios = UsuarioRepository(api)
    private val historias = HistoriasRepository(api)
    private val colaboraciones = ColaboracionRepository(api)

    @Before
    fun setUp() {
        mockkObject(SessionManager)
        every { SessionManager.guardarToken(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login correcto guarda el token y un token vacio falla`() = runTest {
        coEvery { api.login(any()) } returns AuthResponse("ok", "jwt123", "listo")

        val correcto = usuarios.login("  USER@Mail.com ", "clave1234")

        assertTrue(correcto.isSuccess)
        coVerify { api.login(AuthRequest("user@mail.com", "clave1234")) }
        verify(exactly = 1) { SessionManager.guardarToken("jwt123") }

        coEvery { api.login(any()) } returns AuthResponse("ok", "", "listo")

        assertFalse(usuarios.login("a@b.cl", "12345678").isSuccess)
        verify(exactly = 1) { SessionManager.guardarToken(any()) }
    }

    @Test
    fun `login y registro fallidos devuelven el mensaje correcto`() = runTest {
        coEvery { api.login(any()) } returns
                AuthResponse("error", "", "Correo o contraseña incorrectos.")
        assertEquals(
            "Correo o contraseña incorrectos.",
            usuarios.login("a@b.cl", "12345678").exceptionOrNull()?.message
        )

        coEvery { api.login(any()) } throws IOException("sin red")
        assertEquals(
            "No se pudo conectar con el servidor. Revisa tu conexión.",
            usuarios.login("a@b.cl", "12345678").exceptionOrNull()?.message
        )

        coEvery { api.register(any()) } returns AuthResponse("ok", null, "listo")
        assertTrue(usuarios.register("nuevo@mail.com", "clave1234").isSuccess)

        coEvery { api.register(any()) } returns AuthResponse("error", null, null)
        assertEquals(
            "No se pudo registrar el usuario.",
            usuarios.register("nuevo@mail.com", "clave1234").exceptionOrNull()?.message
        )
    }

    @Test
    fun `el repositorio de historias convierte y delega en la API`() = runTest {
        coEvery { api.getHistorias() } returns listOf(
            HistoriaDto(id = 1, titulo = "A", subtitulo = "s", texto = "t", imagenBase64 = "b64")
        )
        val lista = historias.obtenerHistorias()
        assertEquals(1, lista.size)
        assertEquals("b64", lista[0].imagenBase64)

        val nueva = HistoriaExito(id = 0, titulo = "T", subtitulo = "S", imagen = 0, texto = "X")
        val respuesta = HistoriaDto(id = 1, titulo = "T", subtitulo = "S", texto = "X")

        coEvery { api.crearHistoria(any()) } returns respuesta
        historias.agregar(nueva, "b64")
        coVerify { api.crearHistoria(match { it.id == null && it.imagenBase64 == "b64" }) }

        coEvery { api.actualizarHistoria(any(), any()) } returns respuesta
        historias.actualizar(nueva.copy(id = 5), null)
        coVerify { api.actualizarHistoria(5, match { it.id == 5 }) }

        coEvery { api.eliminarHistoria(any()) } just Runs
        historias.eliminar(3)
        coVerify { api.eliminarHistoria(3) }
    }

    @Test
    fun `el repositorio de colaboraciones delega en la API`() = runTest {
        val colaboracion = Colaboracion(
            id = 8,
            tipoColaborador = "Persona",
            nombre = "Ana",
            correo = "ana@mail.com",
            programa = "Taller Sociolaboral",
            tipoDonacion = "Única",
            monto = 5000
        )

        coEvery { api.getColaboraciones() } returns listOf(colaboracion)
        assertEquals(1, colaboraciones.obtenerTodas().size)

        coEvery { api.crearColaboracion(any()) } returns colaboracion
        assertEquals(8, colaboraciones.insertar(colaboracion).id)

        coEvery { api.eliminarColaboracion(any()) } just Runs
        colaboraciones.eliminar(colaboracion)
        coVerify { api.eliminarColaboracion(8) }
    }
}