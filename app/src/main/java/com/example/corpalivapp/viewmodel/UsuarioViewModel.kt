package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.AppDataBase
import com.example.corpalivapp.data.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

enum class EstadoAutenticacion {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}

data class UsuarioErrores(
    val email: String? = null,
    val password: String? = null
)

class UsuarioViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = UsuarioRepository(
        AppDataBase.getInstance(application).usuarioDao()
    )

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _errores = MutableStateFlow(UsuarioErrores())
    val errores = _errores.asStateFlow()

    private val _estado = MutableStateFlow(EstadoAutenticacion.IDLE)
    val estado = _estado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso = _progreso.asStateFlow()

    fun actualizarEmail(valor: String) {
        _email.value = valor
        _errores.value = _errores.value.copy(email = null)
    }

    fun actualizarPassword(valor: String) {
        _password.value = valor
        _errores.value = _errores.value.copy(password = null)
    }

    fun autenticar(esRegistro: Boolean) {
        if (_estado.value == EstadoAutenticacion.LOADING) return

        val correo = _email.value.trim()
        val clave = _password.value

        val emailValido = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        ).matches(correo)

        val errorEmail = if (emailValido) {
            null
        } else {
            "Introduce un correo válido."
        }

        val errorPassword = if (clave.length >= 8) {
            null
        } else {
            "La contraseña debe tener al menos 8 caracteres."
        }

        _errores.value = UsuarioErrores(
            email = errorEmail,
            password = errorPassword
        )

        if (errorEmail != null || errorPassword != null) {
            _estado.value = EstadoAutenticacion.IDLE
            _mensaje.value = null
            return
        }

        viewModelScope.launch {
            _estado.value = EstadoAutenticacion.LOADING
            _mensaje.value = null
            _progreso.value = 20f

            try {
                val resultado = withContext(Dispatchers.IO) {
                    if (esRegistro) {
                        repositorio.register(correo, clave)
                    } else {
                        repositorio.login(correo, clave)
                    }
                }

                resultado.fold(
                    onSuccess = {
                        _progreso.value = 100f
                        _estado.value = EstadoAutenticacion.SUCCESS
                        _mensaje.value = if (esRegistro) {
                            "Usuario registrado correctamente."
                        } else {
                            "Inicio de sesión exitoso."
                        }
                    },
                    onFailure = { error ->
                        _estado.value = EstadoAutenticacion.ERROR
                        _mensaje.value = error.message
                            ?: "No se pudo completar la operación."
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _estado.value = EstadoAutenticacion.ERROR
                _mensaje.value = "Ocurrió un error inesperado."
            }
        }
    }

    fun limpiarResultado() {
        if (_estado.value != EstadoAutenticacion.LOADING) {
            _estado.value = EstadoAutenticacion.IDLE
            _mensaje.value = null
            _progreso.value = 0f
        }
    }
}