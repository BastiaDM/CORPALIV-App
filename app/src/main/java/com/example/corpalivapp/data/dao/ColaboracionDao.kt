
package com.example.corpalivapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.corpalivapp.data.model.Colaboracion
import kotlinx.coroutines.flow.Flow

@Dao
interface ColaboracionDao {

    @Query("SELECT * FROM colaboraciones ORDER BY fechaRegistro DESC")
    fun observarTodas(): Flow<List<Colaboracion>>

    @Insert
    suspend fun insertar(colaboracion: Colaboracion)

    @Delete
    suspend fun eliminar(colaboracion: Colaboracion)

    @Query("SELECT COUNT(*) FROM colaboraciones")
    suspend fun contarColaboraciones(): Int
}
