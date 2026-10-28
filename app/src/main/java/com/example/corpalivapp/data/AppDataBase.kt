
package com.example.corpalivapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.corpalivapp.data.dao.ColaboracionDao
import com.example.corpalivapp.data.dao.HistoriaExitoDao
import com.example.corpalivapp.data.dao.UsuarioDao
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.model.Usuario

@Database(
    entities = [
        Usuario::class,
        HistoriaExito::class,
        Colaboracion::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun historiaExitoDao(): HistoriaExitoDao

    abstract fun colaboracionDao(): ColaboracionDao

    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        // Migración de la versión 1 a la versión 2.
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS historias_exito (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        titulo TEXT NOT NULL,
                        subtitulo TEXT NOT NULL,
                        imagen INTEGER NOT NULL,
                        texto TEXT NOT NULL,
                        imagenUri TEXT
                    )
                    """.trimIndent()
                )
            }
        }

        // Migración de la versión 2 a la versión 3.
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS colaboraciones (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        tipoColaborador TEXT NOT NULL,
                        nombre TEXT NOT NULL,
                        apellidos TEXT NOT NULL,
                        rut TEXT NOT NULL,
                        edad INTEGER,
                        razonSocial TEXT NOT NULL,
                        nombreContacto TEXT NOT NULL,
                        correo TEXT NOT NULL,
                        telefono TEXT NOT NULL,
                        programa TEXT NOT NULL,
                        tipoDonacion TEXT NOT NULL,
                        monto INTEGER NOT NULL,
                        medioPago TEXT NOT NULL,
                        fechaRegistro INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
