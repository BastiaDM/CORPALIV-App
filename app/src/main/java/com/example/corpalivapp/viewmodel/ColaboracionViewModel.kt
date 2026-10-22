
package com.example.corpalivapp.viewmodel

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

class ColaboracionViewModel(
    private val repository: ColaboracionRepository
) : ViewModel() {

    private val _colaboraciones =
        MutableStateFlow<List<Colaboracion>>(emptyList())

    val colaboraciones: StateFlow<List<Colaboracion>> =
        _colaboraciones.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)

    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

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

    fun guardar(colaboracion: Colaboracion) {
        val error = validar(colaboracion)

        if (error != null) {
            _mensaje.value = error
            return
        }

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.insertar(colaboracion)
                }

                _mensaje.value = "Colaboración registrada correctamente."
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

    fun actualizar(colaboracion: Colaboracion) {
        val error = validar(colaboracion)

        if (error != null) {
            _mensaje.value = error
            return
        }

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.actualizar(colaboracion)
                }

                _mensaje.value = "Colaboración actualizada."
            } catch (e: Exception) {
                _mensaje.value = "No se pudo actualizar la colaboración."
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    private fun validar(colaboracion: Colaboracion): String? {

        // Validar tipo de colaborador
        if (colaboracion.tipoColaborador !in listOf("Persona", "Empresa")) {
            return "Selecciona Persona o Empresa."
        }

        // Validar datos según el tipo
        if (colaboracion.tipoColaborador == "Persona") {
            if (colaboracion.nombre.isBlank()) {
                return "Ingresa el nombre."
            }

            if (colaboracion.apellidos.isBlank()) {
                return "Ingresa los apellidos."
            }

            if (colaboracion.edad == null ||
                colaboracion.edad !in 1..120
            ) {
                return "La edad debe ser un número entre 1 y 120."
            }
        }

        if (colaboracion.tipoColaborador == "Empresa") {
            if (colaboracion.razonSocial.isBlank()) {
                return "Ingresa la razón social."
            }

            if (colaboracion.nombreContacto.isBlank()) {
                return "Ingresa el nombre del contacto."
            }
        }

        // Validar RUT chileno
        if (!rutValido(colaboracion.rut)) {
            return "El RUT no es válido. Revisa el número y su dígito verificador."
        }

        // Validar correo electrónico
        if (colaboracion.correo.isBlank() ||
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(colaboracion.correo.trim())
                .matches()
        ) {
            return "Ingresa un correo electrónico válido."
        }

        val telefono = colaboracion.telefono.trim()

        if (telefono.isNotBlank()) {
            val formatoTelefono = Regex(
                "^\\+?[0-9][0-9\\s().-]{5,23}[0-9]$"
            )

            val cantidadDigitos = telefono.count { it.isDigit() }

            if (!formatoTelefono.matches(telefono) ||
                cantidadDigitos !in 7..15
            ) {
                return "Ingresa un teléfono válido con entre 7 y 15 dígitos."
            }
        }


        // Validar programa
        if (colaboracion.programa !in listOf(
                "Programa de Inclusión Laboral",
                "Taller Sociolaboral"
            )
        ) {
            return "Selecciona un programa válido."
        }

        // Validar modalidad
        if (colaboracion.tipoDonacion !in listOf("Única", "Mensual")) {
            return "Selecciona una modalidad de donación."
        }

        // Validar el monto máximo
        if (colaboracion.monto > 1_000_000_000) {
            return "El monto ingresado supera el límite máximo permitido para una colaboración."
        }

        // Validar el monto mínimo según la modalidad
        when (colaboracion.tipoDonacion) {
            "Única" -> {
                if (colaboracion.monto < 1000) {
                    return "La donación única debe ser de al menos $1.000."
                }
            }

            "Mensual" -> {
                if (colaboracion.monto < 500) {
                    return "La donación mensual debe ser de al menos $500."
                }
            }

            else -> {
                return "Selecciona una modalidad de donación válida."
            }
        }

        // Validar medio de pago
        if (colaboracion.medioPago !in listOf(
                "Transferencia",
                "Efectivo",
                "Por definir"
            )
        ) {
            return "Selecciona un medio de pago."
        }

        return null
    }

    private fun rutValido(rut: String): Boolean {

        val entrada = rut.trim().uppercase()

        val formatoValido = Regex(
            """(?:\d{6,8}|\d{1,3}(?:\.\d{3}){1,2})-?[0-9K]"""
        )

        if (!formatoValido.matches(entrada)) {
            return false
        }

        val limpio = entrada
            .replace(".", "")
            .replace(" ", "")

        val cuerpo: String
        val digitoIngresado: Char

        if (limpio.contains("-")) {
            // Debe tener un solo guion.
            if (limpio.count { it == '-' } != 1) return false

            val partes = limpio.split("-")

            if (partes.size != 2) return false
            if (partes[0].isEmpty() || partes[1].length != 1) return false

            cuerpo = partes[0]
            digitoIngresado = partes[1][0]
        } else {
            // Sin guion, el último carácter corresponde al dígito verificador.
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

        // Algoritmo chileno del módulo 11.
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
}
