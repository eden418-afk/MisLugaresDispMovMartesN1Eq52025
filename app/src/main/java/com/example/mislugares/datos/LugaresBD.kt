package com.example.mislugares.datos

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.preference.PreferenceManager
import com.example.mislugares.modelo.GeoPunto
import com.example.mislugares.modelo.Lugar
import com.example.mislugares.modelo.TipoLugar
import com.example.mislugares.presentacion.Aplicacion
import android.database.SQLException

open class LugaresBD(
    contexto: Context,
    nombre: String = "lugares",
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = 1
) : RepositorioLugares, SQLiteOpenHelper(contexto, nombre, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE lugares (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "direccion TEXT, " +
                    "latitud REAL, " +
                    "longitud REAL, " +
                    "tipo INTEGER, " +
                    "foto TEXT, " +
                    "telefono INTEGER, " +
                    "url TEXT, " +
                    "comentarios TEXT, " +
                    "fecha LONG, " +
                    "valoracion REAL)"
        )
        db.execSQL("INSERT INTO lugares VALUES (null, 'Holiday Inn Monterrey Norte', "+
                "'Ave. Universidad Norte # 101. Col. Anáhuac, El Roble, 66450 Monterrey, N.L.', 25.74178893441391, -100.30295341104559, "+
                "${TipoLugar.HOTEL.ordinal}, '', 81580000, 'https://www.ihg.com/holidayinn/hotels/us/en/monterrey/mtymx/hoteldetail?cm_mmc=GoogleMaps-_-HI-_-MX-_-MTYMX', "+
                "'Muy buen hotel.', ${System.currentTimeMillis()}, 3.0)")

        db.execSQL("INSERT INTO lugares VALUES (null, 'Facultad de Ingeniería Mecánica y Eléctrica', "+
                "'Pedro de Alba SN, Niños Héroes, Ciudad Universitaria, 66455 San Nicolás de los Garza, N.L.', 25.725471427465795, -100.31339035286359, "+
                "${TipoLugar.EDUCACION.ordinal}, '', 83294020, 'https://www.fime.uanl.mx/', "+
                "'La mejor facultad de ingenieria.', ${System.currentTimeMillis()}, 5.0)")

        db.execSQL("INSERT INTO lugares VALUES (null, 'Parque Fundidora', "+
                "'Adolfo Prieto S/N, Obrera, 64010 Monterrey, N.L.', 25.67882830602361, -100.28413784580258, "+
                "${TipoLugar.NATURALEZA.ordinal}, '', 12345, 'https://www.parquefundidora.org/', "+
                "'Espectacular parque turistico.', ${System.currentTimeMillis()}, 4.0)")

        db.execSQL("INSERT INTO lugares VALUES (null, 'Punto Valle', "+
                "'Rio Missouri 555, Del Valle, 66220 San Pedro Garza García, N.L.', 25.658885184387064, -100.35488631193765, "+
                "${TipoLugar.COMPRAS.ordinal}, '', 12345, 'https://puntovalle.com/', "+
                "'El tipico centro comercial.', ${System.currentTimeMillis()}, 2.0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }


    override fun elemento(posicion: Int, contexto: Context): Lugar {
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM lugares WHERE _id = $posicion", null
        )

        try {
            return if (cursor.moveToNext()) {
                extraeLugar(cursor)
            } else {
                throw SQLException("Error al acceder al elemento _id = $posicion")
            }
        } catch (e: Exception) {
            throw e
        } finally {
            cursor?.close()
        }
    }

    override fun tamaño(contexto: Context): Int {
        val cursor = extraeCursor(contexto)
        val tamanyo = cursor.count
        cursor.close()
        return tamanyo
    }


    fun extraeLugar(cursor: Cursor) = Lugar(
        nombre = cursor.getString(1),
        direccion = cursor.getString(2),
        posicion = GeoPunto(cursor.getDouble(3), cursor.getDouble(4)),
        tipoLugar = TipoLugar.values()[cursor.getInt(5)],
        foto = cursor.getString(6),
        telefono = cursor.getInt(7),
        url = cursor.getString(8),
        comentarios = cursor.getString(9),
        fecha = cursor.getLong(10),
        valoracion = cursor.getFloat(11)
    )

    fun extraeCursor(contexto: Context): Cursor {
        val pref = PreferenceManager.getDefaultSharedPreferences(contexto)
        var consulta = when (pref.getString("orden", "0")) {
            "0" -> "SELECT * FROM lugares"
            "1" -> "SELECT * FROM lugares ORDER BY valoracion DESC"
            else -> {
                val lon = (contexto.getApplicationContext() as Aplicacion)
                    .posicionActual.longitud
                val lat = (contexto.getApplicationContext() as Aplicacion)
                    .posicionActual.latitud
                "SELECT * FROM lugares ORDER BY " +
                        "($lon - longitud)*($lon - longitud) + " +
                        "($lat - latitud)*($lat - latitud)"
            }
        }
        consulta += " LIMIT ${pref.getString("maximo", "12")}"
        return readableDatabase.rawQuery(consulta, null)
    }

    override fun añade(lugar: Lugar) {
    }

    override fun nuevo(): Int {
        var _id = -1
        val lugar = Lugar(nombre = "")

        writableDatabase.execSQL(
            "INSERT INTO lugares (nombre, direccion, longitud, latitud, tipo, foto, telefono, url, comentarios, fecha, valoracion) " +
                    "VALUES ('', '', ${lugar.posicion.longitud}, ${lugar.posicion.latitud}, ${lugar.tipoLugar.ordinal}, '', 0, '', '', ${lugar.fecha}, 0)"
        )

        val c = readableDatabase.rawQuery(
            "SELECT _id FROM lugares WHERE fecha = ${lugar.fecha}", null
        )

        if (c.moveToNext()) {
            _id = c.getInt(0)
        }
        c.close()
        return _id
    }

    override fun borrar(id: Int) {
        val db = getWritableDatabase()
        db.delete("lugares", "_id = ?", arrayOf(id.toString()))
    }


    override fun actualiza(id:Int, lugar:Lugar) = with(lugar) {
        writableDatabase.execSQL("UPDATE lugares SET " +
                "nombre = '$nombre', direccion = '$direccion', " +
                "longitud = ${posicion.longitud}, latitud = ${posicion.latitud}, " +
                "tipo = ${tipoLugar.ordinal}, foto = '$foto', telefono = $telefono, " +
                "url = '$url', comentarios = '$comentarios', fecha = $fecha, " +
                "valoracion = $valoracion WHERE _id = $id")
    }

    fun elementoPorId(id: Int, contexto: Context): Lugar {
        val c = readableDatabase.rawQuery(
            "SELECT * FROM lugares WHERE _id = ?",
            arrayOf(id.toString())
        )
        c.use {
            if (it.moveToFirst()) return extraeLugar(it)
            throw SQLException("No existe lugar con _id = $id")
        }
    }

    fun elementoPorPos(pos: Int, contexto: Context): Lugar {
        val c = readableDatabase.rawQuery(
            "SELECT * FROM lugares ORDER BY _id ASC LIMIT 1 OFFSET ?",
            arrayOf(pos.toString())
        )
        c.use {
            if (it.moveToFirst()) return extraeLugar(it)
            throw SQLException("No existe elemento en la posición = $pos")
        }
    }

}