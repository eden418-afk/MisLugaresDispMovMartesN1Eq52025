package com.example.asteroides.presentacion

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.mislugares.R

class PreferenciasFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferencias, rootKey)

        val maximo = findPreference<EditTextPreference>("maximo")

        // 1) Validación y actualización del summary cuando el usuario cambia el valor
        maximo?.setOnPreferenceChangeListener { pref, newValue ->
            val valor = (newValue as? String)?.toIntOrNull()
            if (valor == null) {
                Toast.makeText(requireContext(), "Ha de ser un número", Toast.LENGTH_SHORT).show()
                return@setOnPreferenceChangeListener false
            }
            if (valor in 0..99) {
                pref.summary = "Limita el número de valores que se muestran ($valor)"
                true
            } else {
                Toast.makeText(requireContext(), "Valor máximo 99", Toast.LENGTH_SHORT).show()
                false
            }
        }

        // 2) Inicializar el summary con el valor ya guardado (cuando abrimos la pantalla)
        actualizarSummaryMaximo()
    }

    override fun onResume() {
        super.onResume()
        // Reafirma el summary si el fragmento se recrea (por ejemplo, rotación)
        actualizarSummaryMaximo()
    }

    private fun actualizarSummaryMaximo() {
        val maximo = findPreference<EditTextPreference>("maximo") ?: return
        val valor = maximo.text?.toIntOrNull()
        if (valor != null) {
            maximo.summary = "Limita el número de valores que se muestran ($valor)"
        } else {
            maximo.summary = "Limita el número de valores que se muestran"
        }
    }
}
