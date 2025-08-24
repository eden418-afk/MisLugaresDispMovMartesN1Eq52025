package com.example.mislugares

import android.content.Context


object Singleton {
    private var _lugares: LugaresLista? = null

    fun inicializa(context: Context) {
        if (_lugares == null) {
            synchronized(this) {
                if (_lugares == null) {
                    _lugares = LugaresLista()
                }
            }
        }
    }

    val lugares: LugaresLista
        get() = _lugares
            ?: error("LugaresSingleton no inicializado. Llama a inicializa(context) primero.")
}
