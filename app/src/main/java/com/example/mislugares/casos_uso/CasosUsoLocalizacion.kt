package com.example.mislugares.casos_uso

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.mislugares.presentacion.Aplicacion
import com.example.mislugares.presentacion.AdaptadorLugares
import com.example.mislugares.modelo.GeoPunto
import kotlin.math.max

class CasosUsoLocalizacion(
    private val actividad: Activity,
    private val codigoPermiso: Int, private val adaptador: AdaptadorLugares
) : LocationListener {

    private val manejadorLoc =
        actividad.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var mejorLoc: Location? = null
    private val app get() = (actividad.application as Aplicacion)

    init {
        ultimaLocalizacion()
    }


    fun hayPermisoLocalizacion() =
        ActivityCompat.checkSelfPermission(
            actividad, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun solicitarPermiso(permiso: String, justificacion: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)) {
            AlertDialog.Builder(actividad)
                .setTitle("Solicitud de permiso")
                .setMessage(justificacion)
                .setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(actividad, arrayOf(permiso), codigoPermiso)
                }.show()
        } else {
            ActivityCompat.requestPermissions(actividad, arrayOf(permiso), codigoPermiso)
        }
    }


    @SuppressLint("MissingPermission")
    fun ultimaLocalizacion() {
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(LocationManager.GPS_PROVIDER))
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
            }
        } else {
            solicitarPermiso(
                Manifest.permission.ACCESS_FINE_LOCATION,
                "Sin el permiso localización no puedo mostrar la distancia a los lugares."
            )
        }
    }


    fun permisoConcedido() {
        ultimaLocalizacion()
        activarProveedores()
        adaptador.notifyDataSetChanged()
    }

    @SuppressLint("MissingPermission")
    private fun activarProveedores() {
        if (!hayPermisoLocalizacion()) return

        if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manejadorLoc.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 10_000L, 5f, this, Looper.getMainLooper()
            )
        }
        if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manejadorLoc.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 20_000L, 10f, this, Looper.getMainLooper()
            )
        }
    }

    fun activar()  { if (hayPermisoLocalizacion()) activarProveedores() }
    fun desactivar() { if (hayPermisoLocalizacion()) manejadorLoc.removeUpdates(this) }



    override fun onLocationChanged(location: Location) {
        actualizaMejorLocaliz(location)
        adaptador.notifyDataSetChanged()
    }

    override fun onProviderDisabled(provider: String) { activarProveedores() }
    override fun onProviderEnabled(provider: String)  { activarProveedores() }
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) { activarProveedores() }



    private val DOS_MINUTOS = 2 * 60 * 1000L

    private fun actualizaMejorLocaliz(loc: Location?) {
        if (loc == null) return
        if (mejorLoc == null ||
            loc.accuracy <= 2 * (mejorLoc!!.accuracy) ||
            (loc.time - mejorLoc!!.time) > DOS_MINUTOS
        ) {
            mejorLoc = loc
            (actividad.application as Aplicacion).posicionActual =
                GeoPunto(loc.latitude, loc.longitude)
            adaptador.notifyDataSetChanged()
        }
    }
}