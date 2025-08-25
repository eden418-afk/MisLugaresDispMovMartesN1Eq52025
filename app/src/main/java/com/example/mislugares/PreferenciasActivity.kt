package com.example.mislugares

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.asteroides.presentacion.PreferenciasFragment

class PreferenciasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, PreferenciasFragment())
            .commit()
    }
}
