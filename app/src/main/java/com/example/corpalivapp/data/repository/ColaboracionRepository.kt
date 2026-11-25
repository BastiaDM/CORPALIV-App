
package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.remote.ApiService
import com.example.corpalivapp.data.remote.RetrofitInstance

// Las colaboraciones viven en el backend (colaboracion-service).
class ColaboracionRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    // Requiere sesion iniciada (contiene datos personales).
    suspend fun obtenerTodas(): List<Colaboracion> =
        api.getColaboraciones()

    // Es publico: cualquier persona puede enviar el formulario.
    suspend fun insertar(colaboracion: Colaboracion): Colaboracion =
        api.crearColaboracion(colaboracion)

    suspend fun eliminar(colaboracion: Colaboracion) =
        api.eliminarColaboracion(colaboracion.id)
}
