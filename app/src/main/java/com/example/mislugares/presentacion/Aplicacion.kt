package com.example.mislugares.presentacion

import android.app.Application
import com.example.mislugares.datos.LugaresLista

class Aplicacion : Application() {
    val lugares = LugaresLista()
}