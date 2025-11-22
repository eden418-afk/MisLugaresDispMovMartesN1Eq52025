package com.example.mislugares

import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.mislugares.casos_uso.CasosUsoLocalizacion
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.ActivityMainBinding
import com.example.mislugares.presentacion.AdaptadorLugaresBD
import com.example.mislugares.presentacion.Aplicacion
import com.example.mislugares.presentacion.MapaActivity
import com.google.android.material.snackbar.Snackbar

private const val SOLICITUD_PERMISO_LOCALIZACION = 1
private const val RESULTADO_PREFERENCIAS = 0

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private val lugares by lazy { (application as Aplicacion).lugares }
    private val adaptador by lazy { (application as Aplicacion).adaptador }
    val usoLugar by lazy { CasosUsoLugar(this, lugares) }

    private lateinit var casosAct: com.example.mislugares.casos_uso.CasosUsoActividades

    private lateinit var usoLocalizacion: CasosUsoLocalizacion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        casosAct = com.example.mislugares.casos_uso.CasosUsoActividades(this)
        usoLocalizacion = CasosUsoLocalizacion(
            actividad = this,
            codigoPermiso = SOLICITUD_PERMISO_LOCALIZACION,
            adaptador = adaptador
        )

        val recycler: RecyclerView = binding.recyclerView
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adaptador

        adaptador.onClick = {
            val pos = it.tag as Int
            usoLugar.mostrar(pos)
        }

        setSupportActionBar(binding.toolbar)
        binding.fab.setOnClickListener {
            val intent = Intent(this, EdicionLugarActivity::class.java)
            intent.putExtra("id", -1L)
            startActivity(intent)
        }
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
            R.id.menu_mapa -> {
                startActivity(Intent(this, MapaActivity::class.java))
                true
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

    fun lanzarPreferencias(view: View? = null) {
        casosAct.lanzarPreferencias(RESULTADO_PREFERENCIAS)
    }

    override fun onResume() {
        super.onResume()
        usoLocalizacion.activar()
        adaptador.cursor = lugares.extraeCursor(this)
        adaptador.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        usoLocalizacion.desactivar()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION &&
            grantResults.size == 1 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            usoLocalizacion.permisoConcedido()
        }
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULTADO_PREFERENCIAS) {
            adaptador.cursor = lugares.extraeCursor(this)
            adaptador.notifyDataSetChanged()
        }
    }
}