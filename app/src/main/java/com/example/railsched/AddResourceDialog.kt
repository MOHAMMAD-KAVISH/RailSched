package com.example.railsched

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class AddResourceDialog(private val onResourceAdded: (Resource) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_add_resource_dialog, null)

        val resourceNameEditText = view.findViewById<EditText>(R.id.resourceNameEditText)
        val resourceTypeEditText = view.findViewById<EditText>(R.id.resourceTypeEditText)

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Resource")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val resourceName = resourceNameEditText.text.toString()
                val resourceType = resourceTypeEditText.text.toString()

                if (resourceName.isNotEmpty() && resourceType.isNotEmpty()) {
                    val resource = Resource(resourceName, resourceType)
                    onResourceAdded(resource) // Pass the new resource back to the caller
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}