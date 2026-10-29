
package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.AppDataBase
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.repository.HistoriasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

class HistoriasViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDataBase.getInstance(application)

    private val repository = HistoriasRepository(
        database.historiaExitoDao()
    )

    val historias = repository.obtenerHistorias().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            repository.inicializarHistoriasSiVacias()
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
        viewModelScope.launch {
            repository.agregar(historia)
        }
    }

    fun actualizar(historia: HistoriaExito) {
        viewModelScope.launch {
            repository.actualizar(historia)
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            repository.eliminar(id)
        }
    }
}
