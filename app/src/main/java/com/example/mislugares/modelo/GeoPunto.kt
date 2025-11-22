package com.example.mislugares.modelo

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class GeoPunto(var latitud: Double, var longitud: Double) {

    companion object {
        val SIN_POSICION = GeoPunto(0.0, 0.0)
    }

    fun distancia(punto: GeoPunto): Double {
        val RADIO_TIERRA = 6_371_000.0 // en metros

        val dLat = Math.toRadians(latitud - punto.latitud)
        val dLon = Math.toRadians(longitud - punto.longitud)
        val lat1 = Math.toRadians(punto.latitud)
        val lat2 = Math.toRadians(latitud)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return c * RADIO_TIERRA
    }
}
