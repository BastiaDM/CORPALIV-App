
package com.example.corpalivapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.corpalivapp.data.model.HistoriaExito
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoriaExitoDao {

    @Query("SELECT * FROM historias_exito ORDER BY id ASC")
    fun obtenerHistorias(): Flow<List<HistoriaExito>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregar(historia: HistoriaExito)

    @Update
    suspend fun actualizar(historia: HistoriaExito)

    @Query("DELETE FROM historias_exito WHERE id = :id")
    suspend fun eliminar(id: Int)

    @Query("SELECT COUNT(*) FROM historias_exito")
    suspend fun contarHistorias(): Int

}
