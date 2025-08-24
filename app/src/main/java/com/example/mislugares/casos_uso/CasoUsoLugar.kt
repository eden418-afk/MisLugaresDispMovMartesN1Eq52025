package com.example.mislugares.casos_uso

import android.app.Activity
import android.content.Intent
import com.example.mislugares.MainActivity
import com.example.mislugares.datos.RepositorioLugares
import kotlin.jvm.java

class CasosUsoLugar(
    val actividad: Activity,
    val lugares: RepositorioLugares
) {
    // OPERACIONES BÁSICAS
    fun mostrar(pos: Int) {
        val i = Intent(actividad, MainActivity::class.java)
        i.putExtra("pos", pos)
        actividad.startActivity(i)
    }
}