package com.example.mislugares.presentacion

import android.app.Application
import com.example.mislugares.datos.LugaresLista
import com.example.mislugares.datos.RepositorioLugares

class Aplicacion : Application() {

    lateinit var lugares: RepositorioLugares

    override fun onCreate() {
        super.onCreate()
        val repo = LugaresLista()
        repo.añadeEjemplos()
        lugares = repo
    }
}