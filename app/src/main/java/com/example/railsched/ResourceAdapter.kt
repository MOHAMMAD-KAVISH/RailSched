package com.example.railsched

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResourceAdapter(private val resourceList: List<Resource>) : RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    inner class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resourceName: TextView = itemView.findViewById(R.id.resourceName)
        val resourceType: TextView = itemView.findViewById(R.id.resourceType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_resource, parent, false)
        return ResourceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val currentItem = resourceList[position]
        holder.resourceName.text = currentItem.resourceName
        holder.resourceType.text = currentItem.resourceType
    }

    override fun getItemCount(): Int {
        return resourceList.size
    }
}