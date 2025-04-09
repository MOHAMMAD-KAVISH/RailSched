package com.example.railsched

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForecastFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var forecastAdapter: ForecastAdapter
    private val forecastList = mutableListOf<Forecast>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add sample data
        forecastList.add(Forecast("Train 101", "10:00 AM", "On Time"))
        forecastList.add(Forecast("Train 102", "11:00 AM", "Delayed"))

        // Set up adapter
        forecastAdapter = ForecastAdapter(forecastList)
        recyclerView.adapter = forecastAdapter

        // Generate Forecast Button
        val generateForecastButton = view.findViewById<Button>(R.id.generateForecastButton)
        generateForecastButton.setOnClickListener {
            Log.d("ForecastFragment", "Generate Forecast Button Clicked")

            // Example: Generate dummy forecast data
            val newForecastData = listOf(
                Forecast("Train 103", "12:00 PM", "On Time"),
                Forecast("Train 104", "01:00 PM", "Delayed")
            )
            forecastList.clear() // Clear the existing data
            forecastList.addAll(newForecastData) // Add new data
            forecastAdapter.notifyDataSetChanged() // Notify the adapter of data changes

            // Send a notification
            val notificationHelper = NotificationHelper(requireContext())
            notificationHelper.sendNotification("Forecast Updated", "New forecast data is available.")
            Log.d("ForecastFragment", "Notification sent")
        }

        return view
    }
}