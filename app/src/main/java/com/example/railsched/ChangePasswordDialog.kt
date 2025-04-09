package com.example.railsched

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment

class ChangePasswordDialog(private val onPasswordChanged: () -> Unit = {}) : DialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(
            "user_prefs",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_change_password_dialog, null)

        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText)
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)

        setupTextWatchers()

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.change_password_title)
            .setView(view)
            .setPositiveButton(R.string.change) { _, _ -> processPasswordChange() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
            }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateInputs()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        listOf(currentPasswordEditText, newPasswordEditText, confirmPasswordEditText).forEach {
            it.addTextChangedListener(textWatcher)
        }
    }

    private fun validateInputs(): Boolean {
        val newPassword = newPasswordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        return when {
            newPassword.length < 6 -> {
                newPasswordEditText.error = getString(R.string.password_too_short)
                false
            }
            newPassword != confirmPassword -> {
                confirmPasswordEditText.error = getString(R.string.passwords_mismatch)
                false
            }
            else -> true
        }
    }

    private fun processPasswordChange() {
        val currentPassword = currentPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()
        val savedPassword = sharedPreferences.getString("user_password", null)

        if (currentPassword != savedPassword) {
            currentPasswordEditText.error = getString(R.string.incorrect_password)
            return
        }

        sharedPreferences.edit {
            putString("user_password", newPassword)
        }

        Toast.makeText(
            requireContext(),
            R.string.password_change_success,
            Toast.LENGTH_SHORT
        ).show()

        onPasswordChanged()
        dismiss()
    }

    companion object {
        const val TAG = "ChangePasswordDialog"
    }
}