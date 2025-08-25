package com.example.mislugares

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.EdicionLugarBinding
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar
import com.example.mislugares.presentacion.Aplicacion
import java.text.DateFormat
import java.util.Date

class EdicionLugarActivity : AppCompatActivity() {

    private val lugares by lazy { (application as Aplicacion).lugares }
    private val usoLugar by lazy { CasosUsoLugar(this, lugares) }

    private var pos = 0
    private lateinit var lugar: Lugar


    private lateinit var binding: EdicionLugarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EdicionLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        pos = intent.extras?.getInt("pos", 0) ?: 0
        lugar = lugares.elemento(pos)

        actualizaVistas()
    }

    private fun actualizaVistas() = with(binding) {
        nombre.setText(lugar.nombre)
        direccion.setText(lugar.direccion)
        telefono.setText(if (lugar.telefono == 0) "" else lugar.telefono.toString())
        url.setText(lugar.url)
        comentario.setText(lugar.comentarios)

        val adaptador = ArrayAdapter<String>(
            this@EdicionLugarActivity,
            R.layout.simple_spinner_item,
            TipoLugar.getNombres()
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tipoLugar.adapter = adaptador
        binding.tipoLugar.setSelection(lugar.tipoLugar.ordinal)
    }
}