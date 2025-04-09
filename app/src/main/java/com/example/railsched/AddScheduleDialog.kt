package com.example.railsched

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleDialog(private val onScheduleAdded: (Schedule) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_add_schedule_dialog, null)

        val trainNameEditText = view.findViewById<EditText>(R.id.trainNameEditText)
        val trainDateEditText = view.findViewById<EditText>(R.id.trainDateEditText)
        val trainTimeEditText = view.findViewById<EditText>(R.id.trainTimeEditText)
        val trainPlatformEditText = view.findViewById<EditText>(R.id.trainPlatformEditText)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        trainDateEditText.setOnClickListener {
            DatePickerDialog(requireContext(),
                { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    trainDateEditText.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        trainTimeEditText.setOnClickListener {
            TimePickerDialog(requireContext(),
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    trainTimeEditText.setText(timeFormat.format(calendar.time))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Train Schedule")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val name = trainNameEditText.text.toString().trim()
                val date = trainDateEditText.text.toString().trim()
                val time = trainTimeEditText.text.toString().trim()
                val platform = trainPlatformEditText.text.toString().trim()

                if (name.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && platform.isNotEmpty()) {
                    val schedule = Schedule(
                        name,
                        "$date at $time",
                        platform
                    )
                    onScheduleAdded(schedule)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
