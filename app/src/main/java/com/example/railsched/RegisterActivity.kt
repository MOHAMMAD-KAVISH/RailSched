package com.example.railsched

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var passwordToggle: ImageView
    private lateinit var passwordStrengthIndicator: ProgressBar
    private lateinit var passwordStrengthText: TextView

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI Components
        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginRedirectText = findViewById(R.id.loginRedirectText)
        passwordToggle = findViewById(R.id.passwordToggle) // Use the ImageView
        passwordStrengthIndicator = findViewById(R.id.passwordStrengthIndicator)
        passwordStrengthText = findViewById(R.id.passwordStrengthText)

        // Handle password visibility toggle
        passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        // Register button click event
        registerButton.setOnClickListener {
            validateRegistration()
        }

        // Navigate back to Login Page
        loginRedirectText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Password Strength Listener
        passwordEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                updatePasswordStrength(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Toggle password visibility
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24)
        } else {
            passwordEditText.transformationMethod = SingleLineTransformationMethod.getInstance()
            confirmPasswordEditText.transformationMethod = SingleLineTransformationMethod.getInstance()
            passwordToggle.setImageResource(R.drawable.baseline_visibility_off_24)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length)
        confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
    }

    // Validate Registration Input
    private fun validateRegistration() {
        val username = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (username.isEmpty()) {
            usernameEditText.error = "Username is required!"
            return
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Enter a valid email!"
            return
        }

        if (password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters!"
            return
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match!"
            return
        }

        // Simulate Registration Success
        showSuccessDialog()
    }

    // Show Success Dialog
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registration Successful")
        builder.setMessage("Your account has been created successfully!")

        builder.setPositiveButton("Login Now") { _, _ ->
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        alertDialog.show()
    }

    // Update Password Strength Indicator
    private fun updatePasswordStrength(password: String) {
        val strength = getPasswordStrength(password)
        passwordStrengthIndicator.progress = strength
        passwordStrengthText.text = when {
            strength < 30 -> "Weak"
            strength < 60 -> "Moderate"
            else -> "Strong"
        }
        passwordStrengthText.setTextColor(
            when {
                strength < 30 -> Color.RED
                strength < 60 -> Color.YELLOW
                else -> Color.GREEN
            }
        )
    }

    // Calculate Password Strength
    private fun getPasswordStrength(password: String): Int {
        var score = 0
        if (password.length >= 6) score += 20
        if (password.length >= 10) score += 20
        if (password.any { it.isUpperCase() }) score += 20
        if (password.any { it.isDigit() }) score += 20
        if (password.any { !it.isLetterOrDigit() }) score += 20
        return score
    }
}