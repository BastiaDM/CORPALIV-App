
package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.remote.ApiService
import com.example.corpalivapp.data.remote.RetrofitInstance

class ColaboracionRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    suspend fun obtenerTodas(): List<Colaboracion> =
        api.getColaboraciones()

    suspend fun insertar(colaboracion: Colaboracion): Colaboracion =
        api.crearColaboracion(colaboracion)

    suspend fun eliminar(colaboracion: Colaboracion) =
        api.eliminarColaboracion(colaboracion.id)
}
