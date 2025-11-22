package com.example.mislugares.datos

import android.content.Context
import com.example.mislugares.modelo.Lugar

class LugaresLista : RepositorioLugares {

    private val listaLugares = mutableListOf<Lugar>()

    override fun elemento(posicion: Int, contexto: Context): Lugar {
        return listaLugares[posicion]
    }

    override fun añade(lugar: Lugar) {
        listaLugares.add(lugar)
    }

    override fun nuevo(): Int {
        val lugar = Lugar("Nuevo lugar")
        listaLugares.add(lugar)
        return listaLugares.size - 1
    }

    override fun borrar(id: Int) {
        listaLugares.removeAt(id)
    }

    override fun tamaño(contexto: Context): Int {
        return listaLugares.size
    }

    override fun actualiza(id: Int, lugar: Lugar) {
        listaLugares[id] = lugar
    }
}
