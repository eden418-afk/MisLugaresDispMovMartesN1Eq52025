package com.example.mislugares.presentacion

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.mislugares.R

class PreferenciasFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferencias, rootKey)

        // Preferencia "maximo" (EditTextPreference)
        val fragmentos = findPreference<EditTextPreference>("maximo")

        fragmentos?.setOnPreferenceChangeListener { preference, newValue ->
            // newValue llega como String
            val valor = (newValue as? String)?.toIntOrNull()
            if (valor == null) {
                Toast.makeText(requireContext(), "Ha de ser un número", Toast.LENGTH_SHORT).show()
                return@setOnPreferenceChangeListener false
            }

            if (valor in 0..99) {
                // Actualiza el summary mostrando el valor
                preference.summary = "Limita el número de valores que se muestran ($valor)"
                true
            } else {
                Toast.makeText(requireContext(), "Valor máximo 99", Toast.LENGTH_SHORT).show()
                false
            }
        }

        // Inicializa el summary con el valor actual al abrir la pantalla
        fragmentos?.text?.toIntOrNull()?.let { v ->
            fragmentos.summary = "Limita el número de valores que se muestran ($v)"
        }
    }
}
