package com.example.mislugares

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.EdicionLugarBinding
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar
import com.example.mislugares.presentacion.Aplicacion

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

        setSupportActionBar(binding.toolbar)

        pos = intent.extras?.getInt("pos", 0) ?: 0
        lugar = lugares.elemento(pos)

        // Spinner de tipos
        val adaptador = ArrayAdapter(
            this@EdicionLugarActivity,
            android.R.layout.simple_spinner_item,
            TipoLugar.getNombres()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.tipoLugar.adapter = adaptador

        actualizaVistas()
    }

    private fun actualizaVistas() = with(binding) {
        nombre.setText(lugar.nombre)
        direccion.setText(lugar.direccion)
        telefono.setText(if (lugar.telefono == 0) "" else lugar.telefono.toString())
        url.setText(lugar.url)
        comentario.setText(lugar.comentarios)
        tipoLugar.setSelection(lugar.tipoLugar.ordinal)
    }

    // MENÚ
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edicion_lugar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.accion_cancelar -> {
            finish()
            true
        }
        R.id.accion_guardar -> {
            guardarCambios()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun guardarCambios() = with(binding) {
        val nuevoLugar = Lugar(
            nombre = nombre.text.toString(),
            direccion = direccion.text.toString(),
            posicion = lugar.posicion, // conserva lo que ya tuviera
            tipoLugar = TipoLugar.values()[tipoLugar.selectedItemPosition],
            foto = lugar.foto,
            telefono = telefono.text.toString().toIntOrNull() ?: 0,
            url = url.text.toString(),
            comentarios = comentario.text.toString(),
            fecha = lugar.fecha,
            valoracion = lugar.valoracion
        )
        usoLugar.guardar(pos, nuevoLugar)
        setResult(RESULT_OK)
        finish()
    }
}
