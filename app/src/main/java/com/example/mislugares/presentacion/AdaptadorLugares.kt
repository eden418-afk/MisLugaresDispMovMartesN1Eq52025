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
import com.example.mislugares.modelo.Lugar

class AdaptadorLugares(private val lugares: RepositorioLugares)
    : RecyclerView.Adapter<AdaptadorLugares.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.nombre)
        private val direccion: TextView = itemView.findViewById(R.id.direccion)
        private val foto: ImageView = itemView.findViewById(R.id.foto)
        private val valoracion: RatingBar = itemView.findViewById(R.id.valoracion)

        fun bind(lugar: Lugar) {
            nombre.text = lugar.nombre
            direccion.text = lugar.direccion
            foto.setImageResource(lugar.tipoLugar.recurso)
            foto.scaleType = ImageView.ScaleType.FIT_END
            valoracion.rating = lugar.valoracion
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.elemento_lista, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lugar = lugares.elemento(position)
        holder.bind(lugar)
    }

    override fun getItemCount(): Int = lugares.tamaño()
}
