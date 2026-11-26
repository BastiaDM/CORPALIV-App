package com.example.corpalivapp.viewmodel

import android.app.Application
import com.example.corpalivapp.MainDispatcherRule
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.remote.ApiService
import com.example.corpalivapp.data.remote.AuthResponse
import com.example.corpalivapp.data.repository.ColaboracionRepository
import com.example.corpalivapp.data.repository.HistoriasRepository
import com.example.corpalivapp.data.repository.UsuarioRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import java.io.IOException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ColaboracionViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val app = mockk<Application>(relaxed = true)
    private val api = mockk<ApiService>()
    private val colaboracionRepo = mockk<ColaboracionRepository>()
    private val historiasRepo = mockk<HistoriasRepository>()

    private val sinConexion = "No se pudo conectar con el servidor. Revisa tu conexión."

    private fun personaValida() = Colaboracion(
        tipoColaborador = "Persona",
        nombre = "Ana",
        apellidos = "Pérez",
        rut = "12.345.678-5",
        edad = 30,
        correo = "ana@mail.com",
        programa = "Programa de Inclusión Laboral",
        tipoDonacion = "Única",
        monto = 5000,
        medioPago = "Transferencia"
    )

    private fun empresaValida() = Colaboracion(
        tipoColaborador = "Empresa",
        nombre = "Empresa SA",
        razonSocial = "Empresa SA",
        nombreContacto = "Juan Soto",
        rut = "6000000-K",
        correo = "contacto@empresa.cl",
        programa = "Taller Sociolaboral",
        tipoDonacion = "Mensual",
        monto = 1000,
        medioPago = "Por definir"
    )

    private fun historia(id: Int) = HistoriaExito(
        id = id, titulo = "Titulo", subtitulo = "Sub", imagen = 0, texto = "Texto"
    )

    private fun errorHttp(codigo: Int) =
        HttpException(Response.error<Any>(codigo, "".toResponseBody()))

    @Test
    fun `colaboracion valida cada campo del formulario`() {
        val vm = ColaboracionViewModel(colaboracionRepo)

        assertNull(vm.validarCampo("tipoColaborador", "Persona"))
        assertEquals("Selecciona Persona o Empresa.", vm.validarCampo("tipoColaborador", "Otro"))

        assertEquals("El nombre es obligatorio.", vm.validarCampo("nombre", ""))
        assertEquals("El nombre no puede contener números.", vm.validarCampo("nombre", "Ana2"))
        assertNull(vm.validarCampo("nombre", "Ana"))
        assertNull(vm.validarCampo("apellidos", "Pérez"))
        assertNull(vm.validarCampo("nombreContacto", "Juan Soto"))

        assertEquals("La razón social es obligatoria.", vm.validarCampo("razonSocial", ""))
        assertNull(vm.validarCampo("razonSocial", "Empresa SA"))

        assertEquals("El correo electrónico es obligatorio.", vm.validarCampo("correo", ""))
        assertEquals("Ingresa un correo electrónico válido.", vm.validarCampo("correo", "malo"))
        assertNull(vm.validarCampo("correo", "ana@mail.com"))

        assertNull(vm.validarCampo("telefono", ""))
        assertEquals(
            "El teléfono contiene caracteres no válidos.",
            vm.validarCampo("telefono", "abc123456")
        )
        assertEquals(
            "El teléfono debe tener entre 7 y 15 dígitos.",
            vm.validarCampo("telefono", "12345")
        )
        assertNull(vm.validarCampo("telefono", "+56 9 1234 5678"))

        assertEquals("La edad es obligatoria.", vm.validarCampo("edad", ""))
        assertEquals("Ingresa una edad válida.", vm.validarCampo("edad", "abc"))
        assertEquals("La edad debe estar entre 1 y 120 años.", vm.validarCampo("edad", "150"))
        assertNull(vm.validarCampo("edad", "30"))

        assertEquals("El RUT es obligatorio.", vm.validarCampo("rut", ""))
        assertEquals(
            "El RUT no es válido. Revisa el número y su dígito verificador.",
            vm.validarCampo("rut", "12.345.678-9")
        )
        assertNull(vm.validarCampo("rut", "12.345.678-5"))
        assertNull(vm.validarCampo("rut", "12345678-5"))
        assertNull(vm.validarCampo("rut", "6000000-K"))

        assertEquals("Selecciona un programa válido.", vm.validarCampo("programa", "Otro"))
        assertNull(vm.validarCampo("programa", "Taller Sociolaboral"))
        assertEquals("Selecciona un medio de pago.", vm.validarCampo("medioPago", ""))
        assertNull(vm.validarCampo("medioPago", "Transferencia"))

        assertEquals("El monto es obligatorio.", vm.validarCampo("monto", ""))
        assertEquals("Ingresa un monto numérico válido.", vm.validarCampo("monto", "abc"))
        assertEquals("El monto no puede ser negativo.", vm.validarCampo("monto", "-5"))
        assertNotNull(vm.validarCampo("monto", "999", "Única"))
        assertNull(vm.validarCampo("monto", "1000", "Única"))
        assertNotNull(vm.validarCampo("monto", "499", "Mensual"))
        assertNull(vm.validarCampo("monto", "500", "Mensual"))

        vm.validarCampo("rut", "123")
        assertNotNull(vm.errores.value.rut)
    }

    @Test
    fun `colaboracion valida el formulario y se comunica con el backend`() {
        val vm = ColaboracionViewModel(colaboracionRepo)

        assertTrue(vm.validarFormulario(personaValida()))
        assertTrue(vm.validarFormulario(empresaValida()))
        assertFalse(vm.validarFormulario(personaValida().copy(edad = null)))
        assertEquals("La edad es obligatoria.", vm.errores.value.edad)

        vm.guardar(personaValida().copy(correo = "malo"))
        assertEquals("Revisa los campos marcados antes de continuar.", vm.mensaje.value)
        coVerify(exactly = 0) { colaboracionRepo.insertar(any()) }

        coEvery { colaboracionRepo.insertar(any()) } returns personaValida()
        vm.guardar(personaValida())
        coVerify(exactly = 1) { colaboracionRepo.insertar(any()) }
        assertEquals("Colaboración registrada correctamente.", vm.mensaje.value)

        coEvery { colaboracionRepo.insertar(any()) } throws IOException("sin red")
        vm.guardar(personaValida())
        assertEquals(sinConexion, vm.mensaje.value)

        coEvery { colaboracionRepo.obtenerTodas() } returns listOf(personaValida())
        vm.cargar()
        assertEquals(1, vm.colaboraciones.value.size)
        assertNull(vm.errorLista.value)

        coEvery { colaboracionRepo.obtenerTodas() } throws errorHttp(401)
        vm.cargar()
        assertEquals(
            "Debes iniciar sesión (pestaña Usuario) para realizar esta acción.",
            vm.errorLista.value
        )

        coEvery { colaboracionRepo.obtenerTodas() } returns emptyList()
        coEvery { colaboracionRepo.eliminar(any()) } just Runs
        vm.eliminar(personaValida())
        coVerify { colaboracionRepo.eliminar(any()) }
        assertEquals("Colaboración eliminada.", vm.mensaje.value)
    }

    @Test
    fun `historias carga valida y ejecuta sus operaciones`() {
        coEvery { historiasRepo.obtenerHistorias() } returns emptyList()
        val vm = HistoriasViewModel(app, historiasRepo)

        coEvery { historiasRepo.obtenerHistorias() } returns listOf(historia(1), historia(2))
        vm.cargar()
        assertEquals(2, vm.historias.value.size)
        assertNull(vm.error.value)

        coEvery { historiasRepo.obtenerHistorias() } throws IOException("sin red")
        vm.cargar()
        assertEquals(sinConexion, vm.error.value)

        val errores = vm.validarHistoria("", " ", "")
        assertEquals("El título es obligatorio", errores.titulo)
        assertEquals("El subtítulo es obligatorio", errores.subtitulo)
        assertEquals("El texto completo es obligatorio", errores.texto)
        assertFalse(errores.esValida)
        assertTrue(vm.validarHistoria("Titulo", "Sub", "Texto").esValida)

        coEvery { historiasRepo.obtenerHistorias() } returns emptyList()
        coEvery { historiasRepo.agregar(any(), any()) } just Runs
        coEvery { historiasRepo.actualizar(any(), any()) } just Runs
        coEvery { historiasRepo.eliminar(any()) } just Runs

        val nueva = historia(0)
        vm.agregar(nueva)
        coVerify(exactly = 1) { historiasRepo.agregar(nueva, null) }

        val editada = historia(4)
        vm.actualizar(editada)
        coVerify(exactly = 1) { historiasRepo.actualizar(editada, null) }

        vm.eliminar(4)
        coVerify(exactly = 1) { historiasRepo.eliminar(4) }

        coEvery { historiasRepo.agregar(any(), any()) } throws IllegalStateException("No se pudo guardar")
        vm.agregar(nueva)
        assertEquals("No se pudo guardar", vm.error.value)
    }

    @Test
    fun `usuario valida los datos y autentica contra el backend`() {
        val vm = UsuarioViewModel(app, UsuarioRepository(api))

        vm.actualizarEmail("malo")
        vm.actualizarPassword("123")
        vm.autenticar(esRegistro = false)
        assertEquals("Introduce un correo válido.", vm.errores.value.email)
        assertEquals("La contraseña debe tener al menos 8 caracteres.", vm.errores.value.password)
        assertEquals(EstadoAutenticacion.IDLE, vm.estado.value)
        coVerify(exactly = 0) { api.login(any()) }

        vm.actualizarEmail("ana@mail.com")
        vm.actualizarPassword("12345678")
        assertNull(vm.errores.value.email)

        coEvery { api.login(any()) } returns AuthResponse("ok", "jwt123", "listo")
        vm.autenticar(esRegistro = false)
        assertEquals(EstadoAutenticacion.SUCCESS, vm.estado.value)
        assertEquals("Inicio de sesión exitoso.", vm.mensaje.value)

        vm.limpiarResultado()
        assertEquals(EstadoAutenticacion.IDLE, vm.estado.value)
        assertNull(vm.mensaje.value)

        coEvery { api.login(any()) } returns
                AuthResponse("error", "", "Correo o contraseña incorrectos.")
        vm.autenticar(esRegistro = false)
        assertEquals(EstadoAutenticacion.ERROR, vm.estado.value)
        assertEquals("Correo o contraseña incorrectos.", vm.mensaje.value)

        coEvery { api.login(any()) } throws IOException("sin red")
        vm.autenticar(esRegistro = false)
        assertEquals(sinConexion, vm.mensaje.value)

        coEvery { api.register(any()) } returns AuthResponse("ok", null, "listo")
        vm.autenticar(esRegistro = true)
        assertEquals("Usuario registrado correctamente.", vm.mensaje.value)
    }
}