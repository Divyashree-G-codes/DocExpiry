package com.example.docexpiry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.docexpiry.databinding.ActivityFullImageBinding

class FullImageActivity : BaseActivity() {

    private lateinit var binding: ActivityFullImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Expect either a resource id (resId) or a photoUri string
        val resId = intent.getIntExtra("resId", -1)
        val photoUri = intent.getStringExtra("photoUri")
        val itemId = intent.getLongExtra("itemId", -1L)
        // Optional details passed by caller
        val ownerName = intent.getStringExtra("ownerName")
        val ownerAddress = intent.getStringExtra("ownerAddress") // may be null
        val ownerDob = intent.getStringExtra("ownerDob") // may be null
        val cardNumber = intent.getStringExtra("cardNumber")
        val authority = intent.getStringExtra("authority")
        val issued = intent.getLongExtra("issuedDate", -1L)
        val expiry = intent.getLongExtra("expiryDate", -1L)

        // Prefer registered user info from SharedPreferences when available
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val regName = prefs.getString("user_name", null)
        val regAddress = prefs.getString("user_address", null)
        val regDob = prefs.getString("user_dob", null)

        // Load image into view
        if (resId != -1) {
            binding.ivFull.setImageResource(resId)
        } else if (!photoUri.isNullOrEmpty()) {
            try {
                com.bumptech.glide.Glide.with(this)
                    .load(photoUri)
                    .into(binding.ivFull)
            } catch (e: Exception) {
                binding.ivFull.setImageResource(R.drawable.placeholder_user)
            }
        } else {
            binding.ivFull.setImageResource(R.drawable.placeholder_user)
        }

        // Flip to details when image tapped
        binding.ivFull.setOnClickListener { flipToBack() }
        binding.tvFlipHint.setOnClickListener { flipToFront() }

        // Prepare display strings
        val displayName = when {
            !regName.isNullOrBlank() -> regName
            !ownerName.isNullOrBlank() -> ownerName
            else -> ""
        }
        val displayAddress = when {
            !regAddress.isNullOrBlank() -> regAddress
            !ownerAddress.isNullOrBlank() -> ownerAddress
            else -> ""
        }
        val displayDob = when {
            !regDob.isNullOrBlank() -> regDob
            !ownerDob.isNullOrBlank() -> ownerDob
            else -> ""
        }

        binding.tvDetailName.text = displayName
        binding.tvDetailAddress.text = displayAddress
        binding.tvDetailDOB.text = displayDob
        binding.tvDetailNumber.text = cardNumber ?: ""
        binding.tvDetailAuthority.text = authority ?: ""
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        // Use proper Kotlin string templates so the formatted date is evaluated
        binding.tvDetailIssued.text = if (issued > 0) "Issued: ${sdf.format(java.util.Date(issued))}" else ""
        binding.tvDetailExpiry.text = if (expiry > 0) "Expiry: ${sdf.format(java.util.Date(expiry))}" else ""

        // Register launcher and edit from details side (same as front edit)
        val editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val out = Intent().apply { putExtra("editedItemId", itemId) }
                setResult(Activity.RESULT_OK, out)
                finish()
            }
        }

        binding.btnEditDetails.setOnClickListener {
            if (itemId != -1L) {
                editLauncher.launch(AddEditCardActivity.newIntent(this, itemId))
            }
        }

        // Delete action from details side
        binding.btnDeleteDetails.setOnClickListener {
            val res = Intent().apply { putExtra("deletedItemId", itemId) }
            setResult(Activity.RESULT_OK, res)
            finish()
        }

    }

    private fun flipToBack() {
        val front = findViewById<android.view.View>(R.id.frontSide)
        val back = findViewById<android.view.View>(R.id.backSide)
        // Set camera distance for 3D effect
        val scale = resources.displayMetrics.density
        front.cameraDistance = 8000 * scale
        back.cameraDistance = 8000 * scale

        front.pivotX = (front.width / 2).toFloat()
        front.pivotY = (front.height / 2).toFloat()
        back.pivotX = (back.width / 2).toFloat()
        back.pivotY = (back.height / 2).toFloat()

        front.animate().rotationY(90f).setDuration(300).withEndAction {
            front.visibility = android.view.View.GONE
            back.visibility = android.view.View.VISIBLE
            back.rotationY = -90f
            back.animate().rotationY(0f).setDuration(300).start()
        }.start()
    }

    private fun flipToFront() {
        val front = findViewById<android.view.View>(R.id.frontSide)
        val back = findViewById<android.view.View>(R.id.backSide)
        val scale = resources.displayMetrics.density
        front.cameraDistance = 8000 * scale
        back.cameraDistance = 8000 * scale

        back.pivotX = (back.width / 2).toFloat()
        back.pivotY = (back.height / 2).toFloat()
        front.pivotX = (front.width / 2).toFloat()
        front.pivotY = (front.height / 2).toFloat()

        back.animate().rotationY(90f).setDuration(300).withEndAction {
            back.visibility = android.view.View.GONE
            front.visibility = android.view.View.VISIBLE
            front.rotationY = -90f
            front.animate().rotationY(0f).setDuration(300).start()
        }.start()
    }
}
