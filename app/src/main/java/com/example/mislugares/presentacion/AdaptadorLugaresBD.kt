package com.example.mislugares.presentacion


import android.content.Context
import android.database.Cursor
import com.example.mislugares.datos.LugaresBD
import com.example.mislugares.modelo.Lugar

class AdaptadorLugaresBD(override val contexto: Context, override val lugares: LugaresBD, var cursor: Cursor?)
    : AdaptadorLugares(contexto = contexto, lugares = lugares) {

    fun lugarPosicion(posicion: Int): Lugar {
        val c = cursor
        if (c != null) {
            c.moveToPosition(posicion)
            return (lugares as LugaresBD).extraeLugar(c)
        }
        return Lugar.LUGAR_VACIO
    }

    fun idPosicion(posicion: Int): Int {
        val c = cursor

        c?.moveToPosition(posicion)

        if (c != null && c.count > 0) return c.getInt(0)
        else                         return -1
    }

    override fun onBindViewHolder(holder: ViewHolder, posicion: Int) {
        val lugar = lugarPosicion(posicion)
        holder.bind(lugar)
        holder.itemView.tag = posicion
        holder.itemView.setOnClickListener(onClick)
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    fun posicionId(id: Int): Int {
        var pos = 0
        while (pos < itemCount && idPosicion(pos) != id) pos++
        return if (pos >= itemCount) -1
        else                 pos
    }
}