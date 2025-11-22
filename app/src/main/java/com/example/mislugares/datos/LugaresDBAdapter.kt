package com.example.mislugares.datos

import android.content.Context
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.AdaptadorLugaresBD

class LugaresDBAdapter (val contexto: Context): LugaresBD(contexto) {
    lateinit var adaptador: AdaptadorLugaresBD

    fun elementoPos(pos: Int) = adaptador.lugarPosicion(pos)

    override fun tamaño(contexto: Context): Int = adaptador.itemCount

    override fun actualiza(id:Int, lugar: Lugar) {
        super.actualiza(id,lugar)
        adaptador.cursor = extraeCursor(contexto)
        adaptador.notifyDataSetChanged()
    }
}