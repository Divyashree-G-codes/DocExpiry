package com.example.docexpiry

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.docexpiry.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        if (prefs.getBoolean("registered", false)) {
            // User already registered, go to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Setup login button
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateCredentials(email, password)) {
                // For demo purposes, accept any valid email/password
                // In production, validate against backend
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                // Go to registration page
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            }
        }

        // Setup sign up button
        binding.btnSignUp.setOnClickListener {
            // Go directly to registration for new users
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        // Forgot password
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password reset feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        var isValid = true

        // Validate email
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter a valid email"
            isValid = false
        } else {
            binding.etEmail.error = null
        }

        // Validate password
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.etPassword.error = null
        }

        return isValid
    }
}
