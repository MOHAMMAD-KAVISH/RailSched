package com.example.railsched

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.Async

class ScheduleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val scheduleList = mutableListOf<Schedule>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add sample data
        scheduleList.add(Schedule("Train 101", "10:00 AM", "Platform 1"))
        scheduleList.add(Schedule("Train 102", "11:00 AM", "Platform 2"))

        // Set up adapter
        scheduleAdapter = ScheduleAdapter(scheduleList)
        recyclerView.adapter = scheduleAdapter

        // Add Schedule Button
        val addScheduleButton = view.findViewById<Button>(R.id.addScheduleButton)
        addScheduleButton.setOnClickListener {
            val dialog = AddScheduleDialog { newSchedule ->
                scheduleList.add(newSchedule) // Add the new schedule to the list
                scheduleAdapter.notifyDataSetChanged() // Refresh the RecyclerView

                NotificationHelper(requireContext()).sendNotification(
                    "New Schedule Added",
                    "${newSchedule.trainName} at ${newSchedule.trainTime} on ${newSchedule.trainPlatform}"
                )
            }
            dialog.show(parentFragmentManager, "AddScheduleDialog")
            Toast.makeText(requireContext(), "Add Schedule Clicked", Toast.LENGTH_SHORT).show()
            // Navigate to AddScheduleActivity or show a dialog


        }

        return view
    }
}