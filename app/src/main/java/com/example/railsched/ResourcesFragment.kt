package com.example.railsched

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResourcesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resourceAdapter: ResourceAdapter
    private val resourceList = mutableListOf<Resource>() // Declare and initialize resourceList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resources, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add sample data
        resourceList.add(Resource("Rake 101", "Rake"))
        resourceList.add(Resource("Crew 201", "Crew"))

        // Set up adapter
        resourceAdapter = ResourceAdapter(resourceList)
        recyclerView.adapter = resourceAdapter

        // Add Resource Button
        val addResourceButton = view.findViewById<Button>(R.id.addResourceButton)
        addResourceButton.setOnClickListener {
            val dialog = AddResourceDialog { newResource ->
                resourceList.add(newResource) // Add the new resource to the list
                resourceAdapter.notifyDataSetChanged() // Refresh the RecyclerView
            }
            dialog.show(parentFragmentManager, "AddResourceDialog")
        }

        return view
    }
}