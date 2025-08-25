package com.example.mislugares.modelo

import com.example.mislugares.R

enum class TipoLugar(val texto: String, val recurso: Int) {
    OTROS("Otros", R.drawable.ic_graduate_hat),
    RESTAURANTE("Restaurante", R.drawable.ic_graduate_hat),
    BAR("Bar", R.drawable.ic_graduate_hat),
    COPAS("Copas", R.drawable.ic_graduate_hat),
    ESPECTACULO("Espectáculo", R.drawable.ic_graduate_hat),
    HOTEL("Hotel", R.drawable.ic_graduate_hat),
    COMPRAS("Compras", R.drawable.ic_graduate_hat),
    EDUCACION("Educación", R.drawable.ic_graduate_hat),
    DEPORTE("Deporte", R.drawable.ic_graduate_hat),
    NATURALEZA("Naturaleza", R.drawable.ic_graduate_hat),
    GASOLINERA("Gasolinera", R.drawable.ic_graduate_hat);

    companion object {
        fun getNombres(): Array<String> =
            values().map { it.texto }.toTypedArray()
    }

}
