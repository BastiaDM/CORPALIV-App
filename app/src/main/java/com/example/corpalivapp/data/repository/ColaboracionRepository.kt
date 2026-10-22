
package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.dao.ColaboracionDao
import com.example.corpalivapp.data.model.Colaboracion
import kotlinx.coroutines.flow.Flow

class ColaboracionRepository(
    private val colaboracionDao: ColaboracionDao
) {
    fun observarTodas(): Flow<List<Colaboracion>> =
        colaboracionDao.observarTodas()

    suspend fun insertar(colaboracion: Colaboracion) =
        colaboracionDao.insertar(colaboracion)

    suspend fun actualizar(colaboracion: Colaboracion) =
        colaboracionDao.actualizar(colaboracion)

    suspend fun eliminar(colaboracion: Colaboracion) =
        colaboracionDao.eliminar(colaboracion)

    suspend fun inicializarColaboracionesSiVacias() {
        if (colaboracionDao.contarColaboraciones() == 0) {

            val colaboracionesPrueba = listOf(
                Colaboracion(
                    tipoColaborador = "Persona",
                    nombre = "Ana",
                    apellidos = "Perez",
                    rut = "11.111.111-1",
                    edad = 25,
                    correo = "ana.prueba@example.com",
                    telefono = "900000001",
                    programa = "Programa de Inclusión Laboral",
                    tipoDonacion = "Única",
                    monto = 5000,
                    medioPago = "Transferencia"
                ),

                Colaboracion(
                    tipoColaborador = "Persona",
                    nombre = "Carlos",
                    apellidos = "Soto",
                    rut = "12.345.678-5",
                    edad = 32,
                    correo = "carlos.prueba@example.com",
                    telefono = "900000002",
                    programa = "Taller Sociolaboral",
                    tipoDonacion = "Mensual",
                    monto = 10000,
                    medioPago = "Por definir"
                ),

                Colaboracion(
                    tipoColaborador = "Empresa",
                    nombre = "Empresa Demo",
                    rut = "76.123.456-K",
                    razonSocial = "Empresa Demo SpA",
                    nombreContacto = "Contacto de prueba",
                    correo = "empresa.prueba@example.com",
                    telefono = "900000003",
                    programa = "Taller Sociolaboral",
                    tipoDonacion = "Única",
                    monto = 20000,
                    medioPago = "Efectivo"
                )
            )

            colaboracionesPrueba.forEach { colaboracion ->
                colaboracionDao.insertar(colaboracion)
            }
        }
    }
}
