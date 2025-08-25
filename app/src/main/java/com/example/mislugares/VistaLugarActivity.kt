package com.example.mislugares

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.VistaLugarBinding
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.Aplicacion
import java.text.DateFormat
import java.util.Date

class VistaLugarActivity : AppCompatActivity() {

    private val lugares by lazy { (application as Aplicacion).lugares }
    private val usoLugar by lazy { CasosUsoLugar(this, lugares) }

    private var pos = 0
    private lateinit var lugar: Lugar

    private lateinit var binding: VistaLugarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VistaLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        pos = intent.extras?.getInt("pos", 0) ?: 0
        lugar = lugares.elemento(pos)

        actualizaVistas()
    }

    private fun actualizaVistas() = with(binding) {
        // Siempre visibles
        nombre.text = lugar.nombre
        logoTipo.setImageResource(lugar.tipoLugar.recurso)
        tipo.text = lugar.tipoLugar.texto
        valoracion.rating = lugar.valoracion
        valoracion.setOnRatingBarChangeListener { _, valor, _ ->
            lugar.valoracion = valor
        }

        // Dirección
        if (lugar.direccion.isNullOrBlank()) {
            direccion.isGone = true
        } else {
            direccion.isVisible = true
            direccion.text = lugar.direccion
        }

        // Teléfono
        if (lugar.telefono == 0) {
            telefono.isGone = true
        } else {
            telefono.isVisible = true
            telefono.text = lugar.telefono.toString()
        }

        // URL
        if (lugar.url.isNullOrBlank()) {
            url.isGone = true
        } else {
            url.isVisible = true
            url.text = lugar.url
        }

        // Comentario
        if (lugar.comentarios.isNullOrBlank()) {
            comentario.isGone = true
        } else {
            comentario.isVisible = true
            comentario.text = lugar.comentarios
        }

        // Fecha y hora
        if (lugar.fecha == 0L) {
            fecha.isGone = true
            hora.isGone = true
        } else {
            val d = Date(lugar.fecha)
            fecha.isVisible = true
            hora.isVisible = true
            fecha.text = DateFormat.getDateInstance().format(d)
            hora.text = DateFormat.getTimeInstance().format(d)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.vista_lugar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.accion_compartir -> true
            R.id.accion_llegar    -> true
            R.id.accion_editar    -> true
            R.id.accion_borrar    -> {
                usoLugar.borrar(pos)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
