
package com.example.corpalivapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.corpalivapp.data.dao.HistoriaExitoDao
import com.example.corpalivapp.data.dao.UsuarioDao
import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.data.model.Usuario

@Database(
    entities = [Usuario::class, HistoriaExito::class],
    version = 2,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun historiaExitoDao(): HistoriaExitoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        // * Migración de la versión 1 a la versión 2.
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

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
