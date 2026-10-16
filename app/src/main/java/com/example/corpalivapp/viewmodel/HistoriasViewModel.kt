package com.example.corpalivapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.repository.HistoriasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HistoriasViewModel : ViewModel() {

    private val repository = HistoriasRepository()

    private val _historias = MutableStateFlow(
        repository.obtenerHistorias()
    )

    val historias = _historias.asStateFlow()

    fun agregar(historia: HistoriaExito) {
        val nuevoId = (_historias.value.maxOfOrNull { it.id } ?: 0) + 1

        val nuevaHistoria = historia.copy(id = nuevoId)

        _historias.value = _historias.value + nuevaHistoria
    }

    fun actualizar(historia: HistoriaExito) {
        _historias.value = _historias.value.map { actual ->
            if (actual.id == historia.id) {
                historia
            } else {
                actual
            }
        }
    }

    fun eliminar(id: Int) {
        _historias.value = _historias.value.filter { it.id != id }
    }
}