package com.example.railsched

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ForecastAdapter(private val forecastList: List<Forecast>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trainName: TextView = itemView.findViewById(R.id.trainName)
        val trainTime: TextView = itemView.findViewById(R.id.trainTime)
        val trainStatus: TextView = itemView.findViewById(R.id.trainStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val currentItem = forecastList[position]
        holder.trainName.text = currentItem.trainName
        holder.trainTime.text = currentItem.trainTime
        holder.trainStatus.text = currentItem.status
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}