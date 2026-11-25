
package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.remote.ApiService
import com.example.corpalivapp.data.remote.RetrofitInstance
import com.example.corpalivapp.data.remote.toDto
import com.example.corpalivapp.data.remote.toModel

// Las historias viven en el backend (historia-service).
class HistoriasRepository(
    private val api: ApiService = RetrofitInstance.api
) {

    // Es publico: se pueden ver las historias sin iniciar sesion.
    suspend fun obtenerHistorias(): List<HistoriaExito> =
        api.getHistorias().map { it.toModel() }

    // Crear, editar y eliminar requieren sesion iniciada.
    // imagenBase64: null = no cambiar la imagen que ya tiene la historia.
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
