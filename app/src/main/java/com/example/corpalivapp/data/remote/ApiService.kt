package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.model.Colaboracion
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body body: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: AuthRequest): AuthResponse

    @GET("historias")
    suspend fun getHistorias(): List<HistoriaDto>

    @POST("historias")
    suspend fun crearHistoria(@Body historia: HistoriaDto): HistoriaDto

    @PUT("historias/{id}")
    suspend fun actualizarHistoria(
        @Path("id") id: Int,
        @Body historia: HistoriaDto
    ): HistoriaDto

    @DELETE("historias/{id}")
    suspend fun eliminarHistoria(@Path("id") id: Int)

    @GET("colaboraciones")
    suspend fun getColaboraciones(): List<Colaboracion>

    @POST("colaboraciones")
    suspend fun crearColaboracion(@Body colaboracion: Colaboracion): Colaboracion

    @DELETE("colaboraciones/{id}")
    suspend fun eliminarColaboracion(@Path("id") id: Int)
}
