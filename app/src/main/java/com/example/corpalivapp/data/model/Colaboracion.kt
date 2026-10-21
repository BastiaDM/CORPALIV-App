
package com.example.corpalivapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colaboraciones")
data class Colaboracion(
    @PrimaryKey(autoGenerate = true)
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
    val tipoColaboracion: String,
    val tipoDonacion: String,
    val monto: Int,

    val medioPago: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)
