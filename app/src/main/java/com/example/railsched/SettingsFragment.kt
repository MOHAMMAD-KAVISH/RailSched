package com.example.railsched

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.materialswitch.MaterialSwitch
import java.util.Locale

class SettingsFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        sharedPref = requireContext().getSharedPreferences("rail_sched_prefs", Context.MODE_PRIVATE)

        // 🔙 Back Button -> Go to HomeFragment
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            //requireActivity().finish()
        }

        // 🌙 Dark Mode Switch
        val darkModeSwitch = view.findViewById<MaterialSwitch>(R.id.darkModeSwitch)
        val isDarkMode = sharedPref.getBoolean("dark_mode_enabled", false)
        darkModeSwitch.isChecked = isDarkMode
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("dark_mode_enabled", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // 🌐 Language Spinner
        val languageSpinner = view.findViewById<Spinner>(R.id.languageSpinner)
        val languageAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        )
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = languageAdapter

        val savedLang = sharedPref.getString("language", "English")
        val position = languageAdapter.getPosition(savedLang)
        if (position >= 0) languageSpinner.setSelection(position)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedLang = parent?.getItemAtPosition(pos).toString()
                if (selectedLang != savedLang) {
                    sharedPref.edit().putString("language", selectedLang).apply()
                    changeLanguage(selectedLang)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 🔒 Change Password
        view.findViewById<Button>(R.id.changePasswordButton).setOnClickListener {
            ChangePasswordDialog().show(parentFragmentManager, "ChangePasswordDialog")
        }

        // 🔔 Notification Preferences
        view.findViewById<Button>(R.id.notificationPreferencesButton).setOnClickListener {
            NotificationPreferencesDialog().show(parentFragmentManager, "NotificationPreferencesDialog")
        }

        // 🚪 Logout Button
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            // sharedPref.edit().clear().apply() // Uncomment if you want to clear saved login/session
            requireActivity().finish() // Closes the app or returns to login depending on your flow
        }

        return view
    }

    private fun changeLanguage(language: String) {
        val locale = when (language.lowercase(Locale.getDefault())) {
            "hindi" -> Locale("hi")
            "spanish" -> Locale("es")
            else -> Locale("en")
        }

        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        Toast.makeText(requireContext(), "Language set to $language", Toast.LENGTH_SHORT).show()
    }
}
