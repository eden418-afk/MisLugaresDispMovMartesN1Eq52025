package com.example.mislugares.casos_uso

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.mislugares.EdicionLugarActivity
import com.example.mislugares.MainActivity
import com.example.mislugares.VistaLugarActivity
import com.example.mislugares.datos.RepositorioLugares
import kotlin.jvm.java

class CasosUsoLugar(
    val actividad: Activity,
    val lugares: RepositorioLugares
) {
    // OPERACIONES BÁSICAS
    fun mostrar(pos: Int) {
        val i = Intent(actividad, VistaLugarActivity::class.java)
        i.putExtra("pos", pos)
        actividad.startActivity(i)
    }

    fun borrar(id: Int){
        if (id in 0 until lugares.tamaño()) {
            lugares.borrar(id)
            Toast.makeText(actividad, "Lugar eliminado", Toast.LENGTH_SHORT).show()
            actividad.finish() // cerramos la pantalla actual
        }
    }

    fun editar(pos: Int){
        val i = Intent(actividad, EdicionLugarActivity::class.java)
        i.putExtra("pos", pos)
        actividad.startActivity(i)
    }
}