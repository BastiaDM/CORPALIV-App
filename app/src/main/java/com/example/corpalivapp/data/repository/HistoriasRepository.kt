package com.example.corpalivapp.data.repository

import com.example.corpalivapp.data.model.HistoriaExito
import com.example.corpalivapp.R
class HistoriasRepository {

    fun obtenerHistorias(): List<HistoriaExito> {
        return listOf(
            HistoriaExito(
                id = 1,
                titulo = "Tests historia 1",
                subtitulo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                imagen = R.drawable.placeholder,
                texto = "attis nec leo in, posuere tristique justo. Integer sollicitudin " +
                        "ipsum et ex eleifend, malesuada lacinia lectus interdum. Nam at lorem " +
                        "sapien. Mauris vel pharetra arcu. Nam quis posuere purus, ac laoreet turpis."
            ),
            HistoriaExito(
                id = 2,
                titulo = "Tests historia 1",
                subtitulo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                imagen = R.drawable.placeholder,
                texto = "attis nec leo in, posuere tristique justo. Integer sollicitudin " +
                        "ipsum et ex eleifend, malesuada lacinia lectus interdum. Nam at lorem " +
                        "sapien. Mauris vel pharetra arcu. Nam quis posuere purus, ac laoreet turpis."
            ),
            HistoriaExito(
                id = 3,
                titulo = "Tests historia 1",
                subtitulo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                imagen = R.drawable.placeholder,
                texto = "attis nec leo in, posuere tristique justo. Integer sollicitudin " +
                        "ipsum et ex eleifend, malesuada lacinia lectus interdum. Nam at lorem " +
                        "sapien. Mauris vel pharetra arcu. Nam quis posuere purus, ac laoreet turpis."
            )
        )
    }
}
