
package com.example.corpalivapp.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.ImagenUtils
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.remote.mensajeUsuario
import com.example.corpalivapp.data.repository.HistoriasRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

data class ErroresHistoria(
    val titulo: String? = null,
    val subtitulo: String? = null,
    val texto: String? = null
) {
    val esValida: Boolean
        get() = titulo == null &&
                subtitulo == null &&
                texto == null
}

class HistoriasViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: HistoriasRepository = HistoriasRepository()
) : AndroidViewModel(application) {

    private val _historias = MutableStateFlow<List<HistoriaExito>>(emptyList())
    val historias: StateFlow<List<HistoriaExito>> = _historias.asStateFlow()

    // Mensaje de error de red/servidor para mostrar en pantalla.
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargar()
    }

    // Descarga las historias desde el backend (publico, no requiere sesion).
    fun cargar() {
        viewModelScope.launch {
            try {
                _historias.value = repository.obtenerHistorias()
                _error.value = null
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("HistoriasViewModel", "Error al cargar historias", e)
                _error.value = e.mensajeUsuario()
            }
        }
    }

    // Comprueba que los datos de la historia sean válidos.
    fun validarHistoria(
        titulo: String,
        subtitulo: String,
        texto: String
    ): ErroresHistoria {

        return ErroresHistoria(
            titulo = if (titulo.isBlank()) {
                "El título es obligatorio"
            } else {
                null
            },

            subtitulo = if (subtitulo.isBlank()) {
                "El subtítulo es obligatorio"
            } else {
                null
            },

            texto = if (texto.isBlank()) {
                "El texto completo es obligatorio"
            } else {
                null
            }
        )
    }

    fun agregar(historia: HistoriaExito) {
        ejecutar {
            repository.agregar(historia, imagenParaEnviar(historia))
        }
    }

    fun actualizar(historia: HistoriaExito) {
        ejecutar {
            repository.actualizar(historia, imagenParaEnviar(historia))
        }
    }

    fun eliminar(id: Int) {
        ejecutar {
            repository.eliminar(id)
        }
    }

    // Foto nueva -> se reduce y se convierte a Base64; si no hay foto nueva, null.
    private suspend fun imagenParaEnviar(historia: HistoriaExito): String? {
        val uri: Uri = historia.imagenUri ?: return null

        val base64 = withContext(Dispatchers.IO) {
            ImagenUtils.uriABase64(getApplication<Application>(), uri)
        }

        return base64 ?: throw IllegalStateException(
            "No se pudo leer la imagen seleccionada."
        )
    }

    // Ejecuta una operacion contra el backend y luego recarga la lista.
    private fun ejecutar(accion: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                accion()
                _error.value = null
                _historias.value = repository.obtenerHistorias()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("HistoriasViewModel", "Error al ejecutar operación", e)
                _error.value = e.mensajeUsuario()
            }
        }
    }
}
