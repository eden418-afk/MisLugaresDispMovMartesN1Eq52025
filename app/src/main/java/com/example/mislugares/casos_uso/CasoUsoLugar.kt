package com.example.mislugares.casos_uso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mislugares.EdicionLugarActivity
import com.example.mislugares.MainActivity
import com.example.mislugares.VistaLugarActivity
import com.example.mislugares.datos.RepositorioLugares
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
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

    fun editar(pos: Int, codigoSolicitud: Int){
        val i = Intent(actividad, EdicionLugarActivity::class.java)
        i.putExtra("pos", pos)
        actividad.startActivityForResult(i, codigoSolicitud)
    }

    fun guardar(id: Int, nuevoLugar: Lugar){
        lugares.actualiza(id, nuevoLugar);
    }

    // INTENCIONES
    fun compartir(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${lugar.nombre} - ${lugar.url}")
        }
    )

    fun llamarTelefono(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_DIAL, Uri.parse("tel:${lugar.telefono}"))
    )

    fun verPgWeb(lugar: Lugar) = actividad.startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse(lugar.url))
    )

    fun verMapa(lugar: Lugar) {
        val lat = lugar.posicion.latitud
        val lon = lugar.posicion.longitud
        val uri = if (lugar.posicion != GeoPunto.SIN_POSICION)
            Uri.parse("geo:$lat,$lon")
        else
            Uri.parse("geo:0,0?q=${lugar.direccion}")
        actividad.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    fun ponerDeGaleria(launcher: ActivityResultLauncher<PickVisualMediaRequest>) {
        launcher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    /** Guarda la URI de la foto en el lugar y (opcional) la pinta en el ImageView. */
    fun ponerFoto(pos: Int, uri: String?, imageView: ImageView? = null) {
        val lugar = lugares.elemento(pos)
        lugar.foto = uri ?: ""
        lugares.actualiza(pos, lugar)          // << persiste en tu repositorio (memoria/BD)
        imageView?.let { visualizarFoto(lugar, it) }
    }

    /** Muestra la foto del lugar (si hay), o limpia el ImageView. */
    fun visualizarFoto(lugar: Lugar, imageView: ImageView) {
        val u = lugar.foto
        if (!u.isNullOrEmpty()) {
            imageView.setImageURI(Uri.parse(u))
        } else {
            imageView.setImageDrawable(null)   // o un placeholder si prefieres
        }
    }
}