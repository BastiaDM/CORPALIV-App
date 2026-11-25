
package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.remote.ApiService
import com.example.corpalivapp.data.remote.RetrofitInstance
import com.example.corpalivapp.data.remote.toDto
import com.example.corpalivapp.data.remote.toModel

class HistoriasRepository(
    private val api: ApiService = RetrofitInstance.api
) {

    suspend fun obtenerHistorias(): List<HistoriaExito> =
        api.getHistorias().map { it.toModel() }

    suspend fun agregar(historia: HistoriaExito, imagenBase64: String?) {
        api.crearHistoria(historia.toDto(imagenBase64))
    }

    suspend fun actualizar(historia: HistoriaExito, imagenBase64: String?) {
        api.actualizarHistoria(historia.id, historia.toDto(imagenBase64))
    }

    suspend fun eliminar(id: Int) {
        api.eliminarHistoria(id)
    }
}
