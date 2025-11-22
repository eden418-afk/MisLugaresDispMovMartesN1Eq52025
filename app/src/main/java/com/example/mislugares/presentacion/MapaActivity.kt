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
    private val usoLugar by lazy { com.example.mislugares.casos_uso.CasosUsoLugar(this, lugares) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap
        mapa.mapType = GoogleMap.MAP_TYPE_NORMAL

        val permisoFine = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        mapa.uiSettings.isZoomControlsEnabled = true
        mapa.uiSettings.isCompassEnabled = true
        if (permisoFine) mapa.isMyLocationEnabled = true

        // === Cargar datos desde cursor (usa _id real) ===
        val cursor = lugares.extraeCursor(this)
        cursor.use { c ->
            if (c.moveToFirst()) {
                // Centrar en el primer lugar con posición válida
                val primero = lugares.extraeLugar(c)
                if (primero.posicion != GeoPunto.SIN_POSICION) {
                    val latLng = LatLng(primero.posicion.latitud, primero.posicion.longitud)
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                }
            }

            // Reiniciar y recorrer para crear marcadores
            c.moveToPosition(-1)
            while (c.moveToNext()) {
                val lugar = lugares.extraeLugar(c)
                val pos = lugar.posicion
                if (pos != GeoPunto.SIN_POSICION) {
                    // Obtén el _id verdadero de la fila (columna 0 por tu esquema)
                    val idCol = c.getColumnIndex("_id")
                    val id = if (idCol >= 0) c.getInt(idCol) else null

                    // Icono escalado (simple)
                    val base = BitmapFactory.decodeResource(resources, lugar.tipoLugar.recurso)
                    val icono: Bitmap = Bitmap.createScaledBitmap(
                        base, base.width / 7, base.height / 7, true
                    )

                    val marker = mapa.addMarker(
                        MarkerOptions()
                            .position(LatLng(pos.latitud, pos.longitud))
                            .title(lugar.nombre)
                            .snippet(lugar.direccion)
                            .icon(BitmapDescriptorFactory.fromBitmap(icono))
                    )
                    // Guarda el _id (no el índice) para usarlo al abrir la vista
                    if (id != null) marker?.tag = id
                }
            }
        }

        mapa.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        val id = marker.tag as? Int ?: return
        // Si ya usas CasosUsoLugar, esto te abre la vista por _id real:
        usoLugar.mostrar(id)

        // Alternativa si prefieres lanzar la Activity directamente:
        // val intent = Intent(this, VistaLugarActivity::class.java)
        // intent.putExtra("id", id)   // Asegúrate de que VistaLugarActivity acepte "id"
        // startActivity(intent)
    }
}
