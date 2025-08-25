package com.example.mislugares

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.ActivityMainBinding
import com.example.mislugares.presentacion.Aplicacion
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val lugares by lazy { (application as Aplicacion).lugares }
    val usoLugar by lazy { CasosUsoLugar(this, lugares) }

    private lateinit var casosAct: com.example.mislugares.casos_uso.CasosUsoActividades

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        casosAct = com.example.mislugares.casos_uso.CasosUsoActividades(this)

        // Toolbar
        setSupportActionBar(binding.toolbar)

        // Listeners de los botones ahora presentes en el layout de la Activity
        binding.button.setOnClickListener {
            Toast.makeText(this, "Mostrar Lugares", Toast.LENGTH_SHORT).show()
        }

        binding.button2.setOnClickListener {
            Toast.makeText(this, "Preferencias", Toast.LENGTH_SHORT).show()
        }

        binding.button4.setOnClickListener {
            casosAct.lanzarAcercaDe()
        }

        binding.button3.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
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

    fun lanzarVistaLugar(view: View? = null){
        usoLugar.mostrar(0)
    }
}
