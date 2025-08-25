package com.example.mislugares

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.ActivityMainBinding
import com.example.mislugares.presentacion.AdaptadorLugares
import com.example.mislugares.presentacion.Aplicacion
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val lugares by lazy { (application as Aplicacion).lugares }
    private val adaptador by lazy { AdaptadorLugares(lugares) }
    val usoLugar by lazy { CasosUsoLugar(this, lugares) }

    private lateinit var casosAct: com.example.mislugares.casos_uso.CasosUsoActividades

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        casosAct = com.example.mislugares.casos_uso.CasosUsoActividades(this)

        val recycler: RecyclerView = binding.recyclerView
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adaptador

        adaptador.onItemClick = { pos ->
            // Abre la pantalla de detalle del lugar seleccionado
            usoLugar.mostrar(pos)
            // (equivalente a crear un Intent y pasar "pos")
        }

        // Toolbar
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                lanzarPreferencias()
                true
            }
            R.id.acercaDe -> {
                casosAct.lanzarAcercaDe()
                true
            }
            R.id.menu_buscar -> {
                lanzarVistaLugar()
                true;
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun lanzarVistaLugar(view: View? = null) {
        val entrada = EditText(this)
        entrada.setText("0")
        AlertDialog.Builder(this)
            .setTitle("Selección de lugar")
            .setMessage("indica su id:")
            .setView(entrada)
            .setPositiveButton("Ok") { dialog, whichButton ->
                val id = Integer.parseInt(entrada.text.toString())
                usoLugar.mostrar(id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    fun lanzarPreferencias(view: View? =null){
        casosAct.lanzarPreferencias()
    }
}
