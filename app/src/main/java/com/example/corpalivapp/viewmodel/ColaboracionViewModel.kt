
package com.example.corpalivapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.repository.ColaboracionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ColaboracionErrores(
    val tipoColaborador: String? = null,
    val nombre: String? = null,
    val apellidos: String? = null,
    val rut: String? = null,
    val edad: String? = null,
    val razonSocial: String? = null,
    val nombreContacto: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
    val programa: String? = null,
    val monto: String? = null,
    val medioPago: String? = null
)

class ColaboracionViewModel(
    private val repository: ColaboracionRepository
) : ViewModel() {

    private val _colaboraciones =
        MutableStateFlow<List<Colaboracion>>(emptyList())

    val colaboraciones: StateFlow<List<Colaboracion>> =
        _colaboraciones.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)

    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _errores = MutableStateFlow(ColaboracionErrores())

    val errores: StateFlow<ColaboracionErrores> =
        _errores.asStateFlow()

    private val programasValidos = listOf(
        "Programa de Inclusión Laboral",
        "Taller Sociolaboral"
    )

    private val mediosPagoValidos = listOf(
        "Transferencia",
        "Tarjeta Credito/Debito",
        "Por definir"
    )

    init {
        viewModelScope.launch {
            repository.inicializarColaboracionesSiVacias()
        }

        viewModelScope.launch {
            repository.observarTodas().collect { lista ->
                _colaboraciones.value = lista
            }
        }
    }

    // Validación individual para mostrar errores mientras se escribe.
    fun validarCampo(
        campo: String,
        valor: String,
        tipoDonacion: String = "Única"
    ): String? {
        val error = when (campo) {
            "tipoColaborador" -> {
                if (valor !in listOf("Persona", "Empresa")) {
                    "Selecciona Persona o Empresa."
                } else null
            }

            "nombre" -> validarNombre(valor, "El nombre")
            "apellidos" -> validarNombre(valor, "Los apellidos")
            "razonSocial" -> validarRazonSocial(valor)
            "nombreContacto" ->
                validarNombre(valor, "El nombre del contacto")
            "rut" -> validarRutMensaje(valor)
            "edad" -> validarEdad(valor)
            "correo" -> validarCorreo(valor)
            "telefono" -> validarTelefono(valor)
            "programa" -> {
                if (valor !in programasValidos) {
                    "Selecciona un programa válido."
                } else null
            }

            "monto" -> validarMonto(valor, tipoDonacion)
            "medioPago" -> {
                if (valor !in mediosPagoValidos) {
                    "Selecciona un medio de pago."
                } else null
            }
            else -> null
        }

        val anteriores = _errores.value

        _errores.value = when (campo) {
            "tipoColaborador" ->
                anteriores.copy(tipoColaborador = error)
            "nombre" -> anteriores.copy(nombre = error)
            "apellidos" -> anteriores.copy(apellidos = error)
            "razonSocial" -> anteriores.copy(razonSocial = error)
            "nombreContacto" ->
                anteriores.copy(nombreContacto = error)
            "rut" -> anteriores.copy(rut = error)
            "edad" -> anteriores.copy(edad = error)
            "correo" -> anteriores.copy(correo = error)
            "telefono" -> anteriores.copy(telefono = error)
            "programa" -> anteriores.copy(programa = error)
            "monto" -> anteriores.copy(monto = error)
            "medioPago" -> anteriores.copy(medioPago = error)
            else -> anteriores
        }

        return error
    }

    // Valida todos los campos antes de guardar.
    fun validarFormulario(colaboracion: Colaboracion): Boolean {
        val esPersona = colaboracion.tipoColaborador == "Persona"
        val esEmpresa = colaboracion.tipoColaborador == "Empresa"

        val nuevosErrores = ColaboracionErrores(
            tipoColaborador = if (esPersona || esEmpresa) {
                null
            } else {
                "Selecciona Persona o Empresa."
            },

            nombre = if (esPersona) {
                validarNombre(colaboracion.nombre, "El nombre")
            } else null,

            apellidos = if (esPersona) {
                validarNombre(colaboracion.apellidos, "Los apellidos")
            } else null,

            rut = validarRutMensaje(colaboracion.rut),

            edad = if (esPersona) {
                colaboracion.edad?.let {
                    validarEdad(it.toString())
                } ?: "La edad es obligatoria."
            } else null,

            razonSocial = if (esEmpresa) {
                validarRazonSocial(colaboracion.razonSocial)
            } else null,

            nombreContacto = if (esEmpresa) {
                validarNombre(
                    colaboracion.nombreContacto,
                    "El nombre del contacto"
                )
            } else null,

            correo = validarCorreo(colaboracion.correo),
            telefono = validarTelefono(colaboracion.telefono),

            programa = if (colaboracion.programa in programasValidos) {
                null
            } else {
                "Selecciona un programa válido."
            },

            monto = validarMonto(
                colaboracion.monto.toString(),
                colaboracion.tipoDonacion
            ),

            medioPago = if (colaboracion.medioPago in mediosPagoValidos) {
                null
            } else {
                "Selecciona un medio de pago."
            }
        )

        _errores.value = nuevosErrores
        return nuevosErrores == ColaboracionErrores()
    }

    private fun validarNombre(
        valor: String,
        etiqueta: String
    ): String? {
        return when {
            valor.isBlank() -> "$etiqueta es obligatorio."
            valor.trim().length < 2 ->
                "$etiqueta debe tener al menos 2 caracteres."
            valor.any { it.isDigit() } ->
                "$etiqueta no puede contener números."
            else -> null
        }
    }

    private fun validarRazonSocial(valor: String): String? {
        return when {
            valor.isBlank() -> "La razón social es obligatoria."
            valor.trim().length < 2 ->
                "La razón social debe tener al menos 2 caracteres."
            else -> null
        }
    }

    private fun validarEdad(valor: String): String? {
        val edad = valor.toIntOrNull()

        return when {
            valor.isBlank() -> "La edad es obligatoria."
            edad == null -> "Ingresa una edad válida."
            edad !in 1..120 ->
                "La edad debe estar entre 1 y 120 años."
            else -> null
        }
    }

    private fun validarCorreo(valor: String): String? {
        return when {
            valor.isBlank() -> "El correo electrónico es obligatorio."
            !Patterns.EMAIL_ADDRESS.matcher(valor.trim()).matches() ->
                "Ingresa un correo electrónico válido."
            else -> null
        }
    }

    private fun validarTelefono(valor: String): String? {
        val telefono = valor.trim()

        // El teléfono es opcional.
        if (telefono.isBlank()) return null

        val digitos = telefono.count { it.isDigit() }
        val formato = Regex("""^\+?[0-9][0-9\s().-]*[0-9]$""")

        return when {
            !formato.matches(telefono) ->
                "El teléfono contiene caracteres no válidos."
            digitos !in 7..15 ->
                "El teléfono debe tener entre 7 y 15 dígitos."
            else -> null
        }
    }

    private fun validarMonto(
        valor: String,
        tipoDonacion: String
    ): String? {
        val monto = valor.toLongOrNull()

        return when {
            valor.isBlank() -> "El monto es obligatorio."
            monto == null -> "Ingresa un monto numérico válido."
            monto < 0 -> "El monto no puede ser negativo."
            monto > 1_000_000_000L ->
                "El monto supera el límite máximo permitido."
            tipoDonacion !in listOf("Única", "Mensual") ->
                "Selecciona una modalidad de donación válida."
            tipoDonacion == "Única" && monto < 1000L ->
                "La donación única debe ser de al menos $1.000."
            tipoDonacion == "Mensual" && monto < 500L ->
                "La donación mensual debe ser de al menos $500."
            else -> null
        }
    }

    private fun validarRutMensaje(valor: String): String? {
        return when {
            valor.isBlank() -> "El RUT es obligatorio."
            !rutValido(valor) ->
                "El RUT no es válido. Revisa el número y su dígito verificador."
            else -> null
        }
    }

    private fun rutValido(rut: String): Boolean {
        val entrada = rut.trim().uppercase()

        val formato = Regex(
            """(?:\d{6,8}|\d{1,3}(?:\.\d{3}){1,2})-?[0-9K]"""
        )

        if (!formato.matches(entrada)) return false

        val limpio = entrada.replace(".", "").replace(" ", "")

        val cuerpo: String
        val digitoIngresado: Char

        if (limpio.contains("-")) {
            if (limpio.count { it == '-' } != 1) return false

            val partes = limpio.split("-")
            if (partes.size != 2) return false
            if (partes[0].isEmpty() || partes[1].length != 1) {
                return false
            }

            cuerpo = partes[0]
            digitoIngresado = partes[1][0]
        } else {
            if (limpio.length < 7) return false

            cuerpo = limpio.dropLast(1)
            digitoIngresado = limpio.last()
        }

        if (cuerpo.length !in 6..8 || !cuerpo.all { it.isDigit() }) {
            return false
        }

        if (!digitoIngresado.isDigit() && digitoIngresado != 'K') {
            return false
        }

        var suma = 0
        var multiplicador = 2

        for (digito in cuerpo.reversed()) {
            suma += digito.digitToInt() * multiplicador
            multiplicador++

            if (multiplicador > 7) {
                multiplicador = 2
            }
        }

        val resultado = 11 - (suma % 11)

        val digitoEsperado = when (resultado) {
            11 -> '0'
            10 -> 'K'
            else -> resultado.digitToChar()
        }

        return digitoIngresado == digitoEsperado
    }

    fun guardar(colaboracion: Colaboracion) {
        if (!validarFormulario(colaboracion)) {
            _mensaje.value = "Revisa los campos marcados antes de continuar."
            return
        }

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.insertar(colaboracion)
                }

                _mensaje.value = "Colaboración registrada correctamente."
                _errores.value = ColaboracionErrores()
            } catch (e: Exception) {
                _mensaje.value = "No se pudo guardar la colaboración."
            }
        }
    }


    fun eliminar(colaboracion: Colaboracion) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.eliminar(colaboracion)
                }

                _mensaje.value = "Colaboración eliminada."
            } catch (e: Exception) {
                _mensaje.value = "No se pudo eliminar la colaboración."
            }
        }
    }
}
