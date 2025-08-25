package com.example.mislugares

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.VistaLugarBinding
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.Aplicacion
import java.text.DateFormat
import java.util.Date

class VistaLugarActivity : AppCompatActivity() {

    // Repo y casos de uso (usando el singleton que hicimos)
    private val lugares by lazy { (application as Aplicacion).lugares }
    private val usoLugar by lazy { CasosUsoLugar(this, lugares) }

    private var pos = 0
    private lateinit var lugar: Lugar

    private lateinit var binding: VistaLugarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VistaLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pos = intent.extras?.getInt("pos", 0) ?: 0
        lugar = lugares.elemento(pos)

        actualizaVistas()
    }

    private fun actualizaVistas() = with(binding) {
        nombre.text = lugar.nombre
        logoTipo.setImageResource(lugar.tipoLugar.recurso)
        tipo.text = lugar.tipoLugar.texto
        direccion.text = lugar.direccion
        telefono.text = lugar.telefono.toString()
        url.text = lugar.url
        comentario.text = lugar.comentarios
        fecha.text = DateFormat.getDateInstance().format(Date(lugar.fecha))
        hora.text = DateFormat.getTimeInstance().format(Date(lugar.fecha))
        valoracion.rating = lugar.valoracion
        valoracion.setOnRatingBarChangeListener { _, valor, _ ->
            lugar.valoracion = valor
        }
    }
}
