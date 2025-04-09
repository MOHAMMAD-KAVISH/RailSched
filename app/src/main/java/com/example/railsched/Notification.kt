package com.example.railsched

data class Notification(
    val title: String, // Title of the notification (e.g., "Train 101 Delayed")
    val time: String   // Time of the notification (e.g., "10:00 AM")
)