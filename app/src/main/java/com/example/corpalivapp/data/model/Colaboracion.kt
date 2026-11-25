
package com.example.corpalivapp.data.model

// Ahora se guarda en el backend (colaboracion-service). Los nombres de los campos
// coinciden con el JSON del servidor. El servidor asigna "id" y "fechaRegistro".
data class Colaboracion(
    val id: Int = 0,

    val tipoColaborador: String,
    val nombre: String,
    val apellidos: String = "",
    val rut: String = "",
    val edad: Int? = null,

    val razonSocial: String = "",
    val nombreContacto: String = "",

    val correo: String,
    val telefono: String = "",

    val programa: String,
    val tipoDonacion: String,
    val monto: Int,

    val medioPago: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)
