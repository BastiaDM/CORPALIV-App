
package com.example.corpalivapp.data.repository

import com.example.corpalivapp.R
import com.example.corpalivapp.data.dao.HistoriaExitoDao
import com.example.corpalivapp.data.model.HistoriaExito
import kotlinx.coroutines.flow.Flow

class HistoriasRepository(
    private val historiaExitoDao: HistoriaExitoDao
) {

    fun obtenerHistorias(): Flow<List<HistoriaExito>> {
        return historiaExitoDao.obtenerHistorias()
    }

    suspend fun inicializarHistoriasSiVacias() {
        if (historiaExitoDao.contarHistorias() == 0) {
            historiaExitoDao.agregar(
                HistoriaExito(
                    titulo = "Tests historia 1",
                    subtitulo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    imagen = R.drawable.placeholder,
                    texto = "attis nec leo in, posuere tristique justo. Integer sollicitudin " +
                            "ipsum et ex eleifend, malesuada lacinia lectus interdum. Nam at lorem " +
                            "sapien. Mauris vel pharetra arcu. Nam quis posuere purus, ac laoreet turpis."
                )
            )

            historiaExitoDao.agregar(
                HistoriaExito(
                    titulo = "Tests historia 2",
                    subtitulo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    imagen = R.drawable.placeholder,
                    texto = "attis nec leo in, posuere tristique justo. Integer sollicitudin " +
                            "ipsum et ex eleifend, malesuada lacinia lectus interdum. Nam at lorem " +
                            "sapien. Mauris vel pharetra arcu. Nam quis posuere purus, ac laoreet turpis."
                )
            )

            historiaExitoDao.agregar(
                HistoriaExito(
                    titulo = "Tests historia 3",
                    subtitulo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    imagen = R.drawable.placeholder,
                    texto = "attis nec leo in, posuere tristique justo. Integer sollicitudin " +
                            "ipsum et ex eleifend, malesuada lacinia lectus interdum. Nam at lorem " +
                            "sapien. Mauris vel pharetra arcu. Nam quis posuere purus, ac laoreet turpis."
                )
            )
        }
    }

    suspend fun agregar(historia: HistoriaExito) {
        historiaExitoDao.agregar(historia)
    }

    suspend fun actualizar(historia: HistoriaExito) {
        historiaExitoDao.actualizar(historia)
    }

    suspend fun eliminar(id: Int) {
        historiaExitoDao.eliminar(id)
    }
}
