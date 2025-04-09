package com.example.railsched

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var registerText: TextView
    private lateinit var passwordToggle: ImageView
    private lateinit var rememberMeCheckBox: CheckBox

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences("rail_sched_prefs", Context.MODE_PRIVATE)

        // Auto-login if user was already logged in
        if (sharedPref.getBoolean("is_logged_in", false)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        registerText = findViewById(R.id.registerText)
        passwordToggle = findViewById(R.id.passwordToggle)
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox)

        // Load last used email
        val lastEmail = sharedPref.getString("last_logged_email", "")
        emailEditText.setText(lastEmail)

        // Load saved password for that email
        if (!lastEmail.isNullOrEmpty()) {
            val savedPassword = sharedPref.getString("password_for_$lastEmail", "")
            passwordEditText.setText(savedPassword)
        }

        // Load checkbox state
        rememberMeCheckBox.isChecked = sharedPref.getBoolean("remember_me", false)

        // Listen to email input change: auto-fill password if stored
        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val typedEmail = emailEditText.text.toString()
                val savedPass = sharedPref.getString("password_for_$typedEmail", "")
                passwordEditText.setText(savedPass)
            }
        }

        // Toggle password visibility
        passwordToggle.setOnClickListener { togglePasswordVisibility() }

        // Login action
        loginButton.setOnClickListener { validateLogin() }

        // Forgot password
        forgotPassword.setOnClickListener { showForgotPasswordDialog() }

        // Register screen
        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24)
        } else {
            passwordEditText.transformationMethod = SingleLineTransformationMethod.getInstance()
            passwordToggle.setImageResource(R.drawable.baseline_visibility_off_24)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun validateLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Enter a valid email"
            showCustomToast("Invalid email format!", false)
            return
        }

        if (password.isEmpty() || password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters"
            showCustomToast("Password too short!", false)
            return
        }

        // Simulate login success
        showCustomToast("Login Successful!", true)

        // Save preferences
        with(sharedPref.edit()) {
            putBoolean("is_logged_in", true)
            putString("last_logged_email", email)

            if (rememberMeCheckBox.isChecked) {
                putString("password_for_$email", password)
                putBoolean("remember_me", true)
            } else {
                remove("password_for_$email")
                putBoolean("remember_me", false)
            }

            apply()
        }

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Forgot Password?")
        builder.setMessage("Enter your registered email to receive a reset link.")

        val input = EditText(this).apply {
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setPadding(40, 20, 40, 20)
        }
        builder.setView(input)

        builder.setPositiveButton("Submit") { _, _ ->
            val email = input.text.toString().trim()
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showCustomToast("Invalid email!", false)
            } else {
                showCustomToast("Password reset link sent!", true)
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        alertDialog.show()
    }

    private fun showCustomToast(message: String, isSuccess: Boolean) {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.custom_toast, findViewById(android.R.id.content), false)

        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)

        toastText.text = message
        toastIcon.setImageResource(if (isSuccess) R.drawable.baseline_check_circle_24 else R.drawable.baseline_error_24)
        toastIcon.setColorFilter(Color.WHITE)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}
