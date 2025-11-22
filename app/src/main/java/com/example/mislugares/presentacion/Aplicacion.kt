package com.example.mislugares.presentacion


import android.app.Application
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.datos.LugaresDBAdapter
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.presentacion.AdaptadorLugaresBD

class Aplicacion : Application() {

    val lugares = LugaresDBAdapter(this);

    val adaptador by lazy {
        AdaptadorLugaresBD(
            contexto = this,
            lugares = lugares,
            cursor = lugares.extraeCursor(this)
        )
    }

    var posicionActual: GeoPunto = GeoPunto.SIN_POSICION

    override fun onCreate() {
        super.onCreate()
        lugares.adaptador = adaptador
    }
}