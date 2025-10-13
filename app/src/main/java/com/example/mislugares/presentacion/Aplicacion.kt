package com.example.mislugares.presentacion

import android.app.Application
import com.example.mislugares.datos.LugaresLista
import com.example.mislugares.datos.RepositorioLugares
import com.example.mislugares.modelo.GeoPunto

class Aplicacion : Application() {

    lateinit var lugares: RepositorioLugares

    var posicionActual: GeoPunto = GeoPunto.SIN_POSICION

    override fun onCreate() {
        super.onCreate()
        val repo = LugaresLista()
        repo.añadeEjemplos()
        lugares = repo
    }
}