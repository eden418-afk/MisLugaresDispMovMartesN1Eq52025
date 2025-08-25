package com.example.mislugares.casos_uso

import android.app.Activity
import android.content.Intent
import com.example.mislugares.AcercaDeActivity

class CasosUsoActividades (private val actividad: Activity) {
    fun lanzarAcercaDe() {
        actividad.startActivity(Intent(actividad, AcercaDeActivity::class.java))
    }
}