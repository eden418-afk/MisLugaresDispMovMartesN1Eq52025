package com.example.mislugares

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.mislugares.casos_uso.CasosUsoLugar
import com.example.mislugares.databinding.VistaLugarBinding
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.Aplicacion
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.Date

class VistaLugarActivity : AppCompatActivity() {
    val RESULTADO_EDITAR = 1
    val RESULTADO_GALERIA = 2
    val RESULTADO_FOTO = 3

    private val lugares by lazy { (application as Aplicacion).lugares }
    private val usoLugar by lazy { CasosUsoLugar(this, lugares) }

    private var pos = 0
    private lateinit var lugar: Lugar

    private lateinit var binding: VistaLugarBinding

    /** 1) Registrar Photo Picker y manejar el resultado */
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            // 1) Copiamos la imagen a almacenamiento privado de la app
            val localUri = copiarAAlmacenPrivado(uri)

            // 2) Guardamos en el repositorio y mostramos en pantalla
            usoLugar.ponerFoto(pos, localUri?.toString(), binding.foto)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VistaLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        pos = intent.extras?.getInt("pos", 0) ?: 0
        lugar = lugares.elemento(pos)

        binding.galeria.setOnClickListener { seleccionarFotoDesdeGaleria() }

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
            tipo.isGone = true
        } else {
            tipo.isVisible = true
            tipo.text = lugar.direccion
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

        usoLugar.visualizarFoto(lugar, foto)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.vista_lugar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.accion_compartir -> {
                usoLugar.compartir(lugar)
                true
            }
            R.id.accion_llegar    -> {
                usoLugar.verMapa(lugar)
                true
            }
            R.id.accion_editar    -> {
                usoLugar.editar(pos, RESULTADO_EDITAR)
                true;
            }
            R.id.accion_borrar    -> {
                confirmarBorrado()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmarBorrado() {
        AlertDialog.Builder(this)
            .setTitle("Borrado de lugar")
            .setMessage("¿Estás seguro que quieres eliminar este lugar?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Confirmar") { _, _ ->
                usoLugar.borrar(pos)   // elimina y hace finish()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULTADO_EDITAR && resultCode == RESULT_OK) {
            lugar = lugares.elemento(pos)
            actualizaVistas()
        }
    }

    fun verMapa(view: View) = usoLugar.verMapa(lugar)
    fun llamarTelefono(view: View) = usoLugar.llamarTelefono(lugar)
    fun verPgWeb(view: View) = usoLugar.verPgWeb(lugar)
    private fun seleccionarFotoDesdeGaleria() {
        usoLugar.ponerDeGaleria(pickMedia)
    }

    /** Copia el contenido de [src] a filesDir y devuelve su Uri local (file://). */
    private fun copiarAAlmacenPrivado(src: android.net.Uri): android.net.Uri? {
        return try {
            val nombre = "foto_${System.currentTimeMillis()}.jpg"
            val destino = File(filesDir, nombre)

            contentResolver.openInputStream(src).use { input ->
                FileOutputStream(destino).use { output ->
                    if (input != null) input.copyTo(output)
                }
            }
            // Para uso interno basta file://; si quisieras compartirlo con otras apps, usa FileProvider
            android.net.Uri.fromFile(destino)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
