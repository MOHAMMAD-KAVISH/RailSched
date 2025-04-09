package com.example.railsched

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class NotificationPreferencesDialog : DialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var delayNotificationsCheckBox: CheckBox
    private lateinit var scheduleUpdatesCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_notification_preferences_dialog, null)

        delayNotificationsCheckBox = view.findViewById(R.id.delayNotificationsCheckBox)
        scheduleUpdatesCheckBox = view.findViewById(R.id.scheduleUpdatesCheckBox)

        // Load saved preferences
        loadSavedPreferences()

        return AlertDialog.Builder(requireContext())
            .setTitle("Notification Preferences")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                savePreferences()
                Toast.makeText(requireContext(), "Preferences saved", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private fun loadSavedPreferences() {
        delayNotificationsCheckBox.isChecked = sharedPreferences.getBoolean(
            "delay_notifications",
            true // Default value if not found
        )
        scheduleUpdatesCheckBox.isChecked = sharedPreferences.getBoolean(
            "schedule_updates",
            true // Default value if not found
        )
    }

    private fun savePreferences() {
        sharedPreferences.edit().apply {
            putBoolean("delay_notifications", delayNotificationsCheckBox.isChecked)
            putBoolean("schedule_updates", scheduleUpdatesCheckBox.isChecked)
            apply() // Asynchronous save
        }
    }

    companion object {
        const val TAG = "NotificationPreferencesDialog"
    }
}