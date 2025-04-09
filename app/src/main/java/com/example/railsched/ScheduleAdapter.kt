package com.example.railsched

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(private val scheduleList: List<Schedule>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    // ViewHolder class
    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trainName: TextView = itemView.findViewById(R.id.trainName)
        val trainTime: TextView = itemView.findViewById(R.id.trainTime)
        val trainPlatform: TextView = itemView.findViewById(R.id.trainPlatform)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        // Inflate the item layout
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        // Bind data to views
        val currentItem = scheduleList[position]
        holder.trainName.text = currentItem.trainName
        holder.trainTime.text = currentItem.trainTime
        holder.trainPlatform.text = currentItem.trainPlatform
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}