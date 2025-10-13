package com.example.mislugares.presentacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mislugares.R
import com.example.mislugares.datos.RepositorioLugares
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar

class AdaptadorLugares(private val lugares: RepositorioLugares)
    : RecyclerView.Adapter<AdaptadorLugares.ViewHolder>() {

    /** Se establece desde la Activity */
    var onItemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.nombre)
        private val direccion: TextView = itemView.findViewById(R.id.direccion)
        private val foto: ImageView = itemView.findViewById(R.id.foto)
        private val valoracion: RatingBar = itemView.findViewById(R.id.valoracion)
        val distancia: TextView = itemView.findViewById(R.id.distancia)

        fun bind(lugar: Lugar) {
            nombre.text = lugar.nombre
            direccion.text = lugar.direccion
            foto.setImageResource(lugar.tipoLugar.recurso)
            foto.scaleType = ImageView.ScaleType.FIT_END
            valoracion.rating = lugar.valoracion

            val app = itemView.context.applicationContext as Aplicacion
            val pos = app.posicionActual
            if (pos == GeoPunto.SIN_POSICION || lugar.posicion == GeoPunto.SIN_POSICION) {
                distancia.text = "... Km"
            } else {
                val d = pos.distancia(lugar.posicion).toInt() // metros
                distancia.text = if (d < 2000) "$d m" else "${d / 1000} Km"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.elemento_lista, parent, false)
        val holder = ViewHolder(v)
        // Click en la fila -> devolvemos la posición al callback
        v.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) onItemClick?.invoke(pos)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lugares.elemento(position))
    }

    override fun getItemCount(): Int = lugares.tamaño()
}
