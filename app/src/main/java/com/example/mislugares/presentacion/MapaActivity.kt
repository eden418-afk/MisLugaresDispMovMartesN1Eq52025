package com.example.mislugares.presentacion

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mislugares.R
import com.example.mislugares.VistaLugarActivity
import com.example.mislugares.modelo.GeoPunto
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mapa: GoogleMap
    private val lugares by lazy { (application as Aplicacion).lugares }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa)

        // Obtén el fragment y pide el mapa de forma asíncrona
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // opcional: título en la app bar
        supportActionBar?.title = getString(R.string.app_name)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap
        mapa.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Capa "Mi ubicación" (solo si el permiso está concedido)
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            mapa.isMyLocationEnabled = true
            mapa.uiSettings.isZoomControlsEnabled = true
            mapa.uiSettings.isCompassEnabled = true
        } else {
            // Si aún no lo has pedido en la app, simplemente no la actives aquí
            mapa.uiSettings.isZoomControlsEnabled = true
            mapa.uiSettings.isCompassEnabled = true
        }

        // 1) Centra la cámara en el primer lugar (si existe)
        if (lugares.tamaño() > 0) {
            val p = lugares.elemento(0).posicion
            if (p != GeoPunto.SIN_POSICION) {
                val latLng = LatLng(p.latitud, p.longitud)
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            }
        }

        // 2) Añade un marcador por cada lugar, usando su icono escalado
        for (i in 0 until lugares.tamaño()) {
            val lugar = lugares.elemento(i)
            val p = lugar.posicion
            if (p != null && p.latitud != 0.0) {
                val grande = BitmapFactory.decodeResource(resources, lugar.tipoLugar.recurso)
                val icono = Bitmap.createScaledBitmap(grande, grande.width / 7, grande.height / 7, false)

                val marker = mapa.addMarker(
                    MarkerOptions()
                        .position(LatLng(p.latitud, p.longitud))
                        .title(lugar.nombre)
                        .snippet(lugar.direccion)
                        .icon(BitmapDescriptorFactory.fromBitmap(icono))
                )
                marker?.tag = i           // ← guarda la posición del lugar
            }
        }

        mapa.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        for (pos in 0 until lugares.tamaño()) {
            if (lugares.elemento(pos).nombre == marker.title) {
                val intent = Intent(this, VistaLugarActivity::class.java)
                intent.putExtra("pos", pos)
                startActivity(intent)
                break
            }
        }
    }
}
