package com.example.mislugares.modelo

import com.example.mislugares.R

enum class TipoLugar(val texto: String, val recurso: Int) {
    OTROS("Otros", R.drawable.ic_almacenamiento_up),
    RESTAURANTE("Restaurante", R.drawable.ic_restaurante_up),
    BAR("Bar", R.drawable.ic_bar_up),
    COPAS("Copas", R.drawable.ic_cafe_up),
    ESPECTACULO("Espectáculo", R.drawable.ic_drama_up),
    HOTEL("Hotel", R.drawable.ic_hospital_up),
    COMPRAS("Compras", R.drawable.ic_compras_up),
    EDUCACION("Educación", R.drawable.ic_graduate_hat),
    DEPORTE("Deporte", R.drawable.ic_nadar_up),
    NATURALEZA("Naturaleza", R.drawable.ic_mountain_up),
    GASOLINERA("Gasolinera", R.drawable.ic_gasolinera_up);

    companion object {
        fun getNombres(): Array<String> =
            values().map { it.texto }.toTypedArray()
    }

}
