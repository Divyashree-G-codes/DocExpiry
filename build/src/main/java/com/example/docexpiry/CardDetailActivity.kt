package com.example.docexpiry

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationSet
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.docexpiry.data.AppDatabase
import com.example.docexpiry.data.Card
import com.example.docexpiry.databinding.ActivityCardDetailBinding
import com.example.docexpiry.utils.ImageShareUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * CardDetailActivity - Displays detailed view of a single government card
 *
 * Features:
 * - Flip animation between front (photo) and back (details) views
 * - Share card details using Android share intent
 * - Edit card functionality
 * - Delete card with confirmation dialog
 * - Smooth Material Design animations
 *
 * The card is passed via Intent extras:
 * - cardId (Long): ID of the card in Room database
 */
class CardDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityCardDetailBinding
    private var card: Card? = null
    private var isFront = true // Track which side of card is showing
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get card ID from intent
        val cardId = intent.getLongExtra("cardId", -1L)
        if (cardId == -1L) {
            Toast.makeText(this, "Invalid card", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load card from database
        loadCard(cardId)

        // Setup click listeners
        setupClickListeners()
    }

    /**
     * Load card data from Room database
     */
    private fun loadCard(cardId: Long) {
        GlobalScope.launch {
            try {
                val dao = AppDatabase.getInstance(applicationContext).cardDao()
                card = dao.getById(cardId)

                runOnUiThread {
                    if (card != null) {
                        displayCard()
                    } else {
                        Toast.makeText(
                            this@CardDetailActivity,
                            "Card not found",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                Log.e("CardDetailActivity", "Error loading card", e)
                runOnUiThread {
                    Toast.makeText(
                        this@CardDetailActivity,
                        "Error loading card",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    /**
     * Display card details on front and back sides
     */
    private fun displayCard() {
        card?.let { c ->
            // Front side - Display card type and photo
            binding.tvFrontType.text = c.type

            // Load card photo using Glide
            if (c.photoUri != null) {
                Glide.with(this)
                    .load(c.photoUri)
                    .into(binding.ivCardImage)

                // Make image tappable to open full screen viewer
                binding.ivCardImage.setOnClickListener {
                    val i = Intent(this, FullImageActivity::class.java).apply {
                        putExtra("photoUri", c.photoUri)
                        putExtra("itemId", c.id)
                    }
                    startActivity(i)
                }
            } else {
                // Default placeholder if no photo
                binding.ivCardImage.setImageResource(R.drawable.placeholder_user)
            }

            // Back side - Display all details
            binding.tvBackNumber.text = "Card #: ${c.number}"
            binding.tvBackOwner.text = "Name: ${c.ownerName}"
            binding.tvBackAuth.text = "Authority: ${c.authority}"
            binding.tvBackIssued.text = "Issued: ${dateFormat.format(Date(c.issuedDate))}"
            binding.tvBackExpiry.text = "Expires: ${dateFormat.format(Date(c.expiryDate))}"

            // Disable flip animation initially (set both sides visible for setup)
            binding.frontSide.alpha = 1f
            binding.backSide.alpha = 0f
        }
    }

    /**
     * Setup click listeners for card container and action buttons
     */
    private fun setupClickListeners() {
        // Flip animation on card tap
        binding.cardViewContainer.setOnClickListener {
            animateCardFlip()
        }

        // Share button - Share card details via Android share intent
        binding.btnShare.setOnClickListener {
            shareCard()
        }

        // Edit button - Navigate to edit screen
        binding.btnEdit.setOnClickListener {
            editCard()
        }

        // Delete button - Show confirmation and delete
        binding.btnDelete.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    /**
     * Animate card flip between front and back
     * Uses ObjectAnimator for smooth Y-axis rotation
     */
    private fun animateCardFlip() {
        card?.let { _ ->
            // Determine which side to hide and show
            val fromSide = if (isFront) binding.frontSide else binding.backSide
            val toSide = if (isFront) binding.backSide else binding.frontSide

            // Flip animation duration
            val duration = 300L

            // Hide current side (fade out)
            val hideAnimator = ObjectAnimator.ofFloat(fromSide, "alpha", 1f, 0f)
            hideAnimator.duration = duration / 2

            // Show new side (fade in)
            val showAnimator = ObjectAnimator.ofFloat(toSide, "alpha", 0f, 1f)
            showAnimator.duration = duration / 2
            showAnimator.startDelay = duration / 2

            // Play animations
            hideAnimator.start()
            showAnimator.start()

            // Toggle state
            isFront = !isFront
        }
    }

    /**
     * Share card details using Android share intent
     * Shares card type, number, owner name, and expiry date
     */
    private fun shareCard() {
        card?.let { c ->
            // Build share message
            val shareText = """
                Government Card Details
                
                Type: ${c.type}
                Card Number: ${c.number}
                Owner: ${c.ownerName}
                Authority: ${c.authority}
                Issued: ${dateFormat.format(Date(c.issuedDate))}
                Expires: ${dateFormat.format(Date(c.expiryDate))}
                
                Shared via DocExpiry - DigiLocker Style Card Manager
            """.trimIndent()

            try {
                val photoUriString = c.photoUri
                if (!photoUriString.isNullOrEmpty()) {
                    val uri = ImageShareUtils.getShareableImageUri(this, photoUriString)
                    if (uri != null) {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "image/*"
                            putExtra(Intent.EXTRA_STREAM, uri as android.os.Parcelable)
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        shareIntent.clipData = android.content.ClipData.newUri(contentResolver, "Image", uri)
                        startActivity(Intent.createChooser(shareIntent, "Share Card"))
                        return
                    }
                }
            } catch (e: Exception) {
                android.util.Log.w("CardDetailActivity", "Image share failed: ${e.message}")
            }

            // Fallback to text-only share
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share Card"))
        }
    }

    /**
     * Navigate to edit screen with card data
     */
    private fun editCard() {
        card?.let { c ->
            val intent = Intent(this, AddEditCardActivity::class.java).apply {
                putExtra("cardId", c.id)
                putExtra("cardType", c.type)
                putExtra("cardNumber", c.number)
                putExtra("ownerName", c.ownerName)
                putExtra("authority", c.authority)
                putExtra("issuedDate", c.issuedDate)
                putExtra("expiryDate", c.expiryDate)
                putExtra("photoUri", c.photoUri)
            }
            startActivityForResult(intent, EDIT_REQUEST_CODE)
        }
    }

    /**
     * Show delete confirmation dialog
     */
    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this).apply {
            setTitle("Delete Card")
            setMessage("Are you sure you want to delete this card?")
            setPositiveButton("Delete") { _, _ ->
                deleteCard()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    /**
     * Delete card from database
     */
    private fun deleteCard() {
        card?.let { c ->
            GlobalScope.launch {
                try {
                    val dao = AppDatabase.getInstance(applicationContext).cardDao()
                    dao.delete(c)

                    runOnUiThread {
                        Toast.makeText(
                            this@CardDetailActivity,
                            "Card deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                } catch (e: Exception) {
                    Log.e("CardDetailActivity", "Error deleting card", e)
                    runOnUiThread {
                        Toast.makeText(
                            this@CardDetailActivity,
                            "Error deleting card",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Handle result from edit activity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Reload card after edit
            card?.let { loadCard(it.id) }
        }
    }

    companion object {
        private const val EDIT_REQUEST_CODE = 100

        fun newIntent(context: android.content.Context, cardId: Long): Intent {
            return Intent(context, CardDetailActivity::class.java).apply {
                putExtra("cardId", cardId)
            }
        }
    }
}
