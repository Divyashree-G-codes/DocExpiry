package com.example.docexpiry

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.docexpiry.data.AppDatabase
import com.example.docexpiry.data.Card
import com.example.docexpiry.databinding.ActivityAddEditCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

// Activity to add or edit a card, with dynamic authority spinner and image picking
class AddEditCardActivity : BaseActivity() {

    private lateinit var binding: ActivityAddEditCardBinding
    private val dao by lazy { AppDatabase.getInstance(applicationContext).cardDao() }

    private var currentPhotoUri: String? = null
    private var editingId: Long = 0L

    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var cameraUri: Uri? = null

    companion object {
        fun newIntent(ctx: Context, id: Long) = Intent(ctx, AddEditCardActivity::class.java).putExtra("id", id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityAddEditCardBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            android.util.Log.e("AddEditCardActivity", "Error inflating layout: ${'$'}{e.message}", e)
            Toast.makeText(this, "Error loading form", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        try {
            // Activity Result API for gallery pick
            pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                try {
                    uri?.let {
                        // Copy selected image into app cache so we don't rely on external URI permissions
                        val imagesDir = File(cacheDir, "images")
                        if (!imagesDir.exists()) imagesDir.mkdirs()
                        val outFile = File(imagesDir, "picked_${'$'}{System.currentTimeMillis()}.jpg")

                        contentResolver.openInputStream(it)?.use { input ->
                            FileOutputStream(outFile).use { out ->
                                input.copyTo(out)
                                out.flush()
                            }
                        }

                        // Save absolute path (ImageShareUtils will convert file path to FileProvider uri when sharing)
                        currentPhotoUri = outFile.absolutePath

                        // Set preview using Uri.fromFile
                        binding.imagePreview.setImageURI(android.net.Uri.fromFile(outFile))
                        Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AddEditCardActivity", "Error selecting image: ${'$'}{e.message}", e)
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
                }
            }

            // Create a temporary file Uri via FileProvider for camera
            takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                try {
                    if (success && cameraUri != null) {
                        currentPhotoUri = cameraUri.toString()
                        binding.imagePreview.setImageURI(cameraUri)
                        Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show()
                    } else if (!success) {
                        Toast.makeText(this, "Photo capture cancelled", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AddEditCardActivity", "Error capturing image: ${'$'}{e.message}", e)
                    Toast.makeText(this, "Error capturing photo", Toast.LENGTH_SHORT).show()
                }
            }

            // Populate card types
            val cardTypes = listOf("Aadhaar", "PAN", "Voter ID", "Driving License", "Passport", "Birth Certificate", "Ration Card", "Academic Certificate")
            val typeAdapter = SimpleSpinnerAdapter(this, cardTypes)
            binding.spinnerType.adapter = typeAdapter
            typeAdapter.attachListener(binding.spinnerType)

             // Map of authorities by type (extendable)
             val authorityMap = mapOf(
                "Aadhaar" to listOf("UIDAI"),
                "PAN" to listOf("IT Department"),
                "Passport" to listOf("Passport Office"),
                "Driving License" to listOf("RTO"),
                "Voter ID" to listOf("Election Commission"),
                "Birth Certificate" to listOf("Municipal Corporation"),
                "Ration Card" to listOf("Food Dept"),
                "Academic Certificate" to listOf("University")
            )

             typeAdapter.onItemSelected = object : SimpleSpinnerAdapter.OnItemSelected {
                override fun onSelected(pos: Int, value: String) {
                    try {
                        val authList = authorityMap[value] ?: listOf("Authority")
                        val aAdapter = SimpleSpinnerAdapter(this@AddEditCardActivity, authList)
                        binding.spinnerAuthority.adapter = aAdapter
                        aAdapter.attachListener(binding.spinnerAuthority)
                    } catch (e: Exception) {
                        android.util.Log.e("AddEditCardActivity", "Error updating authority: ${'$'}{e.message}", e)
                    }
                }
             }

            // Apply prefill (if provided) after authorityMap/onItemSelected is in place so authority spinner updates
            val prefillType = intent.getStringExtra("prefill_type")
            val prefillSubtype = intent.getStringExtra("prefill_subtype")
            if (editingId == 0L && !prefillType.isNullOrBlank()) {
                val pos = typeAdapter.positionOf(prefillType)
                if (pos >= 0) binding.spinnerType.setSelection(pos)
                // populate authority for the prefilled type
                val authList = authorityMap[prefillType] ?: listOf("Authority")
                val aAdapter = SimpleSpinnerAdapter(this@AddEditCardActivity, authList)
                binding.spinnerAuthority.adapter = aAdapter
                aAdapter.attachListener(binding.spinnerAuthority)
                // If subtype is provided, store it in etNumber tag/hint to be visible
                if (!prefillSubtype.isNullOrBlank()) {
                    binding.etNumber.hint = "Subtype: ${'$'}prefillSubtype"
                    binding.etNumber.tag = prefillSubtype
                }
            }

            // Date pickers with error handling
            binding.etIssued.setOnClickListener {
                try {
                    pickDate { ms ->
                        binding.etIssued.setText(java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(ms)))
                        binding.etIssued.tag = ms
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AddEditCardActivity", "Error picking issued date: ${'$'}{e.message}", e)
                    Toast.makeText(this, "Error selecting date", Toast.LENGTH_SHORT).show()
                }
            }

            binding.etExpiry.setOnClickListener {
                try {
                    pickDate { ms ->
                        binding.etExpiry.setText(java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(ms)))
                        binding.etExpiry.tag = ms
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AddEditCardActivity", "Error picking expiry date: ${'$'}{e.message}", e)
                    Toast.makeText(this, "Error selecting date", Toast.LENGTH_SHORT).show()
                }
            }

            // Image pick buttons with error handling
            binding.btnPick.setOnClickListener {
                try {
                    pickImageLauncher.launch("image/*")
                } catch (e: Exception) {
                    android.util.Log.e("AddEditCardActivity", "Error launching gallery: ${'$'}{e.message}", e)
                    Toast.makeText(this, "Error opening gallery", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnCamera.setOnClickListener {
                try {
                    launchCamera()
                } catch (e: Exception) {
                    android.util.Log.e("AddEditCardActivity", "Error launching camera: ${'$'}{e.message}", e)
                    Toast.makeText(this, "Error opening camera", Toast.LENGTH_SHORT).show()
                }
            }

            // --------- NEW: Auto-fill owner details from Registration shared prefs for new cards ---------
            val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
            val regName = prefs.getString("user_name", null)
            val regAddress = prefs.getString("user_address", null)
            val regDobMillis = prefs.getString("user_dob_millis", null)?.toLongOrNull() ?: 0L
            val regDobStr = prefs.getString("user_dob", null)

            // If adding a new card (id == 0), pre-fill owner fields but allow editing
            editingId = intent.getLongExtra("id", 0L)
            if (editingId == 0L) {
                regName?.let {
                    if (it.isNotBlank()) {
                        binding.etName.setText(it)
                    }
                }
                regAddress?.let {
                    if (it.isNotBlank()) {
                        binding.etAddress.setText(it)
                    }
                }
                if (regDobMillis > 0L) {
                    binding.etDob.setText(java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(regDobMillis)))
                    binding.etDob.tag = regDobMillis
                } else if (!regDobStr.isNullOrBlank()) {
                    binding.etDob.setText(regDobStr)
                }
                // Allow DOB editing via date picker
                binding.etDob.isFocusableInTouchMode = true
                binding.etDob.isClickable = true
                binding.etDob.setOnClickListener {
                    try {
                        pickDate { ms ->
                            binding.etDob.setText(java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(ms)))
                            binding.etDob.tag = ms
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AddEditCardActivity", "Error opening DOB picker: ${e.message}", e)
                    }
                }
            }
            // -------------------------------------------------------------------------------------------

            // If editing, load existing
            if (editingId != 0L) {
                lifecycleScope.launch {
                    try {
                        val card = dao.getById(editingId)
                        card?.let { populateForEdit(it) }
                    } catch (e: Exception) {
                        android.util.Log.e("AddEditCardActivity", "Error loading card: ${e.message}", e)
                        android.widget.Toast.makeText(this@AddEditCardActivity, "Error loading document", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.btnSave.setOnClickListener {
                try {
                    val type = binding.spinnerType.selectedItem?.toString() ?: ""
                    val authority = binding.spinnerAuthority.selectedItem?.toString() ?: ""
                    val number = binding.etNumber.text.toString().trim()
                    val owner = binding.etName.text.toString().trim()
                    val ownerAddress = binding.etAddress.text.toString().trim()
                    // ownerDob stored as tag (millis) when available
                    val ownerDob = (binding.etDob.tag as? Long) ?: run {
                         // try parse if text available
                         val txt = binding.etDob.text.toString().trim()
                         try {
                             val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                             sdf.parse(txt)?.time ?: 0L
                         } catch (e: Exception) { 0L }
                     }
                    val issued = binding.etIssued.tag as? Long ?: 0L
                    val expiry = binding.etExpiry.tag as? Long ?: 0L

                    // Basic validation
                    if (type.isEmpty()) {
                        Toast.makeText(this, "Please select document type", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (number.isEmpty()) {
                        binding.etNumber.error = "Number required"
                        return@setOnClickListener
                    }
                    if (owner.isEmpty()) {
                        binding.etName.error = "Name required"
                        return@setOnClickListener
                    }
                    if (issued == 0L || expiry == 0L) {
                        Toast.makeText(this, "Please select issued and expiry dates", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Logical validation: expiry must be after issued
                    if (expiry < issued) {
                        Toast.makeText(this, "Expiry date must be after issued date", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    // Type-specific validation
                    if (type == "PAN" && !isValidPan(number)) {
                        binding.etNumber.error = "Invalid PAN format (e.g. ABCDE1234F)"
                        return@setOnClickListener
                    }
                    if (type == "Aadhaar" && !isValidAadhaar(number)) {
                        binding.etNumber.error = "Invalid Aadhaar (12 digits)"
                        return@setOnClickListener
                    }

                    val card = Card(
                        id = if (editingId == 0L) 0 else editingId,
                        type = type,
                        number = number,
                        ownerName = owner,
                        ownerAddress = if (ownerAddress.isEmpty()) null else ownerAddress,
                        ownerDob = ownerDob,
                        authority = authority,
                        issuedDate = issued,
                        expiryDate = expiry,
                        photoUri = currentPhotoUri
                    )

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            if (editingId == 0L) {
                                dao.insert(card)
                            } else {
                                dao.update(card)
                            }
                            runOnUiThread {
                                Toast.makeText(this@AddEditCardActivity, "Document saved successfully", Toast.LENGTH_SHORT).show()
                                // If this was a newly added Aadhaar, redirect user to the Manage (Main) screen
                                if (editingId == 0L && type == "Aadhaar") {
                                    val i = android.content.Intent(this@AddEditCardActivity, MainActivity::class.java).apply {
                                        // Clear task so Manage becomes the foreground flow
                                        flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    startActivity(i)
                                    finish()
                                    return@runOnUiThread
                                }
                                finish()
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("AddEditCardActivity", "Error saving card: ${e.message}", e)
                            runOnUiThread {
                                Toast.makeText(this@AddEditCardActivity, "Error saving document", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AddEditCardActivity", "Error in save button: ${e.message}", e)
                    Toast.makeText(this, "Error saving document", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("AddEditCardActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing form", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun populateForEdit(card: Card) {
        val typeAdapter = binding.spinnerType.adapter as SimpleSpinnerAdapter
        binding.spinnerType.setSelection(typeAdapter.positionOf(card.type))
        binding.etNumber.setText(card.number)
        // If the etNumber tag contains a subtype (from prefill), don't overwrite
        if (binding.etNumber.tag == null) binding.etName.setText(card.ownerName)
        // Populate owner address and DOB when editing
        binding.etAddress.setText(card.ownerAddress ?: "")
        if (card.ownerDob > 0L) {
            binding.etDob.setText(java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date(card.ownerDob)))
            binding.etDob.tag = card.ownerDob
        } else {
            binding.etDob.setText("")
        }
        binding.etIssued.setText(Date(card.issuedDate).toString())
        binding.etIssued.tag = card.issuedDate
        binding.etExpiry.setText(Date(card.expiryDate).toString())
        binding.etExpiry.tag = card.expiryDate
        currentPhotoUri = card.photoUri

        // Load photo thumbnail safely
        try {
            if (currentPhotoUri != null) {
                val path = currentPhotoUri!!
                val uri = when {
                    path.startsWith("content://") -> Uri.parse(path)
                    path.startsWith("file://") -> Uri.parse(path)
                    File(path).exists() -> android.net.Uri.fromFile(File(path))
                    else -> null
                }
                if (uri != null) {
                    binding.imagePreview.setImageURI(uri)
                } else {
                    binding.imagePreview.setImageResource(R.drawable.placeholder_user)
                }
            } else {
                binding.imagePreview.setImageResource(R.drawable.placeholder_user)
            }
        } catch (e: Exception) {
            android.util.Log.w("AddEditCardActivity", "populateForEdit image load failed: ${e.message}")
            binding.imagePreview.setImageResource(R.drawable.placeholder_user)
        }
    }

    private fun pickDate(onPicked: (Long) -> Unit) {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            c.set(y, m, d, 0, 0, 0)
            onPicked(c.timeInMillis)
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun launchCamera() {
        // create temporary file in cache and request URI via FileProvider
        val f = File(cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        f.createNewFile()
        val uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", f)
        cameraUri = uri
        takePictureLauncher.launch(uri)
    }

    private fun isValidPan(pan: String): Boolean {
        // PAN: 5 letters, 4 digits, 1 letter
        val regex = Regex("[A-Z]{5}[0-9]{4}[A-Z]")
        return regex.matches(pan.uppercase(Locale.getDefault()))
    }

    private fun isValidAadhaar(aadhaar: String): Boolean {
        // Aadhaar: 12 digits
        val digitsOnly = aadhaar.replace("\\s".toRegex(), "")
        val regex = Regex("\\d{12}")
        return regex.matches(digitsOnly)
    }
}
