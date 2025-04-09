package com.example.railsched

data class Forecast(
    val trainName: String, // Name of the train (e.g., "Train 101")
    val trainTime: String, // Time of the train (e.g., "10:00 AM")
    val status: String     // Status of the train (e.g., "On Time", "Delayed")
)