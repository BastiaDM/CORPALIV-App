package com.example.corpalivapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.corpalivapp.data.model.Usuario

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): Usuario?

    @Insert
    suspend fun insert(user: Usuario)
}