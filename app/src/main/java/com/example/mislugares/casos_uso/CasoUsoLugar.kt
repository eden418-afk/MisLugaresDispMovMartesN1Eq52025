package com.example.mislugares.casos_uso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.mislugares.EdicionLugarActivity
import com.example.mislugares.MainActivity
import com.example.mislugares.VistaLugarActivity
import com.example.mislugares.datos.LugaresDBAdapter
import com.example.mislugares.datos.RepositorioLugares
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.presentacion.Aplicacion
import java.io.File
import java.io.IOException
import kotlin.jvm.java

class CasosUsoLugar(
    val actividad: Activity,
    val lugares: RepositorioLugares
) {

    var uriUltimaFoto: Uri? = null

    // OPERACIONES BÁSICAS
    fun mostrar(pos: Int) {
        val i = Intent(actividad, VistaLugarActivity::class.java)
        i.putExtra("pos", pos)
        actividad.startActivity(i)
    }

    fun borrar(pos: Int) {
        val id = (lugares as LugaresDBAdapter).adaptador.idPosicion(pos)
        lugares.borrar(id)
        actividad.finish()
    }

    fun editar(pos: Int, id: Int, codigo: Int) {
        val i = Intent(actividad, EdicionLugarActivity::class.java)
        i.putExtra("pos", pos)
        i.putExtra("id", id)
        actividad.startActivityForResult(i, codigo)
    }

    // CORRECTO
    fun guardar(pos: Int, id: Int, lugar: Lugar) {
        if (id != -1) {
            lugares.actualiza(id, lugar)
        } else {
            lugares.actualiza(pos, lugar)
        }
    }


    // INTENCIONES
    fun compartir(lugar: Lugar) {
        val lat = lugar.posicion.latitud
        val lon = lugar.posicion.longitud
        val url = "https://www.google.com/maps/?q=$lat,$lon"

        val texto = "${lugar.nombre}\n${lugar.direccion}\n\nUbicación: $url"

        actividad.startActivity(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, texto)
            }
        )
    }


    fun llamarTelefono(lugar: Lugar) {
        if (lugar.telefono == 0) return
        val uri = Uri.parse("tel:${lugar.telefono}")
        val i = Intent(Intent.ACTION_CALL, uri) // antes: ACTION_DIAL
        actividad.startActivity(i)
    }

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
    fun ponerFoto(id: Long, uri: String?, imageView: ImageView? = null) {
        val lugar = lugares.elemento(id.toInt(), actividad)
        lugar.foto = uri ?: ""
        lugares.actualiza(id.toInt(), lugar)
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

    /** Abrir la cámara y guardar la foto en el almacenamiento privado de la app. */
    fun tomarFoto(codigoSolicitud: Int): Uri? {
        return try {
            val file = File.createTempFile(
                "img_${System.currentTimeMillis() / 1000}", ".jpg",
                actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
            uriUltimaFoto =
                if (Build.VERSION.SDK_INT >= 24)
                    FileProvider.getUriForFile(
                        actividad,
                        "${actividad.packageName}.fileprovider",   // <= authority
                        file
                    )
                else Uri.fromFile(file)

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            actividad.startActivityForResult(intent, codigoSolicitud)
            uriUltimaFoto
        } catch (ex: IOException) {
            Toast.makeText(actividad, "Error al crear fichero de imagen", Toast.LENGTH_LONG).show()
            null
        }
    }

    fun nuevo() {
        val _id = lugares.nuevo()  // 🔥 Inserta un registro vacío y devuelve su ID

        // Si hay posición actual, se guarda
        val posicion = (actividad.application as Aplicacion).posicionActual
        if (posicion != GeoPunto.SIN_POSICION) {
            val lugar = lugares.elemento(_id, actividad)
            lugar.posicion = posicion
            lugares.actualiza(_id, lugar)
        }

        // Lanza la actividad de edición pasando el ID
        val i = Intent(actividad, EdicionLugarActivity::class.java)
        i.putExtra("id", _id)
        actividad.startActivity(i)
    }

}