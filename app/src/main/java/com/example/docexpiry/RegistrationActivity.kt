package com.example.docexpiry

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.docexpiry.databinding.ActivityRegistrationBinding
import java.text.SimpleDateFormat
import java.util.*

// Enhanced registration screen with name, DOB, and address
class RegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedDate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logos moved to DocumentChooserActivity; no quick-action logos on registration page

        // Load shared prefs and redirect if already registered
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        if (prefs.getBoolean("registered", false)) {
            // Already registered -> go to main
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Prefill registration fields with any saved personal details (do NOT clear them on logout elsewhere)
        val savedName = prefs.getString("user_name", null)
        val savedDob = prefs.getString("user_dob", null)
        val savedDobMillis = prefs.getString("user_dob_millis", null)
        val savedAddress = prefs.getString("user_address", null)
        if (!savedName.isNullOrBlank()) binding.etName.setText(savedName)
        if (!savedDob.isNullOrBlank()) binding.etDob.setText(savedDob)
        if (!savedAddress.isNullOrBlank()) binding.etAddress.setText(savedAddress)
        if (!savedDobMillis.isNullOrBlank()) {
            try {
                selectedDate = savedDobMillis.toLong()
            } catch (_: Exception) { /* ignore parse errors */ }
        }

        // DOB picker
        binding.etDob.setOnClickListener {
            showDatePicker()
        }
        binding.etDob.isFocusable = false
        binding.etDob.isClickable = true

        // Register button
        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                saveUserData(prefs)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.timeInMillis
                binding.etDob.setText(dateFormat.format(Date(selectedDate)))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun validateForm(): Boolean {
        val name = binding.etName.text.toString().trim()
        val dob = binding.etDob.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        var isValid = true

        // Validate name
        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            isValid = false
        } else if (name.length < 2) {
            binding.etName.error = "Name must be at least 2 characters"
            isValid = false
        } else {
            binding.etName.error = null
        }

        // Validate DOB
        if (dob.isEmpty()) {
            binding.etDob.error = "Date of birth is required"
            isValid = false
        } else {
            binding.etDob.error = null
        }

        // Validate address
        if (address.isEmpty()) {
            binding.etAddress.error = "Address is required"
            isValid = false
        } else if (address.length < 5) {
            binding.etAddress.error = "Address must be at least 5 characters"
            isValid = false
        } else {
            binding.etAddress.error = null
        }

        return isValid
    }

    private fun saveUserData(prefs: android.content.SharedPreferences) {
        val name = binding.etName.text.toString().trim()
        val dob = binding.etDob.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        try {
            prefs.edit()
                .putBoolean("registered", true)
                .putString("user_name", name)
                .putString("user_dob", dob)
                .putString("user_dob_millis", selectedDate.toString())
                .putString("user_address", address)
                .apply()

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            // After registration, always go to the official document chooser page
            val i = Intent(this, DocumentChooserActivity::class.java).apply {
                // Clear the existing task so DocumentChooserActivity becomes the first/root screen
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(i)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving data: ${'$'}{e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAddEditWithPrefill(type: String, subtype: String? = null) {
        try {
            val intent = AddEditCardActivity.newIntent(this, 0L).apply {
                putExtra("prefill_type", type)
                if (subtype != null) putExtra("prefill_subtype", subtype)
            }
            startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("RegistrationActivity", "Error opening add/edit: ${e.message}", e)
        }
    }
}
