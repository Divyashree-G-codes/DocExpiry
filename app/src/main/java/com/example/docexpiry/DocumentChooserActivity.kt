package com.example.docexpiry

import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.docexpiry.data.AppDatabase
import com.example.docexpiry.data.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DocumentChooserActivity : BaseActivity() {

    private val docs = listOf(
        DocItem("Aadhaar", "logo_aadhaar", "Aadhaar"),
        DocItem("PAN", "logo_pan", "PAN"),
        DocItem("Driving License", "logo_driving", "Driving License"),
        DocItem("Passport", "logo_passport", "Passport"),
        DocItem("Voter ID", "logo_voter", "Voter ID"),
        DocItem("Birth Certificate", "logo_birth", "Birth Certificate"),
        DocItem("Academic Certificate", "logo_academic", "Academic Certificate"),
        DocItem("Ration Card", "logo_ration", "Ration Card"),
        DocItem("Other", "logo_other", "Other")
    )

    data class DocItem(val type: String, val drawableName: String, val label: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If user not registered, go to registration
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        if (!prefs.getBoolean("registered", false)) {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_document_chooser)

        // Wire visible logout icon if present
        try {
            val ivLogout = findViewById<android.widget.ImageView>(R.id.ivLogoutVisible)
            ivLogout?.setOnClickListener {
                // reuse BaseActivity logout handling: clear registered flag and go to Registration
                val prefsLocal = getSharedPreferences("prefs", MODE_PRIVATE)
                prefsLocal.edit().putBoolean("registered", false).apply()
                val i = Intent(this, RegistrationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(i)
                finish()
            }
        } catch (e: Exception) {
            android.util.Log.w("DocumentChooser", "Logout wiring failed: ${'$'}{e.message}")
        }

        // Populate grid; refactored into refreshGrid() to allow reloading after add/edit
        refreshGrid()

        // If there are no saved cards (fresh user), immediately prompt to add Aadhaar
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(applicationContext)
            val dao = db.cardDao()
            val all = dao.getAllSync() ?: listOf()
            if (all.isEmpty()) {
                // Launch Add Aadhaar as the first required document
                launch(Dispatchers.Main) {
                    try {
                        startActivity(AddEditCardActivity.newIntent(this@DocumentChooserActivity, 0L).apply {
                            putExtra("prefill_type", "Aadhaar")
                        })
                    } catch (e: Exception) {
                        android.util.Log.w("DocumentChooser", "Auto-open Aadhaar failed: ${'$'}{e.message}")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh grid to reflect any newly added/edited cards
        refreshGrid()
    }

    private fun refreshGrid() {
        val grid = findViewById<GridLayout>(R.id.gridDocs)
        grid.removeAllViews()

        val db = AppDatabase.getInstance(applicationContext)
        val dao = db.cardDao()

        var firstFront: View? = null
        var firstBack: View? = null

        docs.forEachIndexed { index, item ->
            // Card container
            val card = CardView(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8, 8, 8, 8)
                }
                radius = 12f
                cardElevation = 4f
            }

            // Flip container (front + back)
            val flipContainer = FrameLayout(this).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            }

            // Front view (logo + label)
            val front = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(12, 12, 12, 12)
                tag = "front"
            }
            val ivFront = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(160, 100)
                scaleType = ImageView.ScaleType.FIT_CENTER
                contentDescription = item.label
            }

            // Resolve drawable by trying several candidate names (allows using different filenames)
            fun resolveDrawableName(vararg names: String): Int {
                for (n in names) {
                    val id = resources.getIdentifier(n, "drawable", packageName)
                    if (id != 0) return id
                }
                return 0
            }

            val alternatives = when (item.type) {
                "Aadhaar" -> listOf("aadhaar_logo", "aadhaar", "logo_aadhaar")
                "PAN" -> listOf("pan_logo", "logo_pan")
                "Driving License" -> listOf("driving_logo", "dl_logo", "drivinglicense_logo", "logo_driving_license")
                "Passport" -> listOf("passport_logo", "logo_passport")
                "Voter ID" -> listOf("voter_logo", "voterid_logo", "logo_voter_id")
                "Birth Certificate" -> listOf("birthcertificate_logo", "birth_certificate", "logo_birth_certificate", "logo_birth")
                "Academic Certificate" -> listOf("academic_logo", "logo_academic_certificate")
                "Ration Card" -> listOf("ration_logo", "logo_ration_card")
                else -> listOf("logo_other", "others_logo")
            }
            val resIdFront = resolveDrawableName(item.drawableName, *alternatives.toTypedArray())
            if (resIdFront != 0) ivFront.setImageResource(resIdFront) else ivFront.setImageResource(R.drawable.ic_aadhaar)
            val tvFront = TextView(this).apply {
                text = item.label
                textSize = 14f
                gravity = Gravity.CENTER
                setPadding(0, 8, 0, 0)
            }
            front.addView(ivFront)
            front.addView(tvFront)

            // Back view (details + action buttons)
            val back = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(12, 12, 12, 12)
                visibility = View.GONE
                tag = "back"
            }
            val tvBackTitle = TextView(this).apply {
                text = item.label
                textSize = 14f
                gravity = Gravity.CENTER
            }
            val tvBackSubtitle = TextView(this).apply {
                text = getString(R.string.tap_to_add_manage)
                textSize = 12f
                gravity = Gravity.CENTER
                setPadding(0, 6, 0, 8)
            }

            val actionRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }
            val btnLeft = TextView(this).apply {
                text = getString(R.string.add)
                setPadding(12, 8, 12, 8)
                setTextColor(ContextCompat.getColor(this@DocumentChooserActivity, R.color.design_default_color_primary))
                setBackgroundResource(android.R.drawable.btn_default)
            }
            val btnRight = TextView(this).apply {
                text = getString(R.string.share)
                setPadding(12, 8, 12, 8)
                setBackgroundResource(android.R.drawable.btn_default)
                setPadding(24, 8, 24, 8)
            }
            actionRow.addView(btnLeft)
            actionRow.addView(btnRight)

            back.addView(tvBackTitle)
            back.addView(tvBackSubtitle)
            back.addView(actionRow)

            flipContainer.addView(front)
            flipContainer.addView(back)
            card.addView(flipContainer)

            // Determine if a saved card exists for this type and update back actions
            lifecycleScope.launch(Dispatchers.IO) {
                val all = dao.getAllSync() ?: listOf()
                val found = all.firstOrNull { it.type.equals(item.type, ignoreCase = true) }
                launch(Dispatchers.Main) {
                    if (found != null) {
                        // show Edit and Share
                        btnLeft.text = getString(R.string.edit)
                        btnLeft.setOnClickListener {
                            openEdit(found.id)
                        }
                        btnRight.text = getString(R.string.share)
                        btnRight.setOnClickListener {
                            shareCard(found)
                        }

                        // Update front tile to display saved card summary (photo or number)
                        try {
                            if (!found.photoUri.isNullOrEmpty()) {
                                com.bumptech.glide.Glide.with(this@DocumentChooserActivity)
                                    .load(found.photoUri)
                                    .centerCrop()
                                    .into(ivFront)
                            } else {
                                // show placeholder and number text
                                ivFront.setImageResource(R.drawable.placeholder_user)
                            }

                            val displayText = when {
                                !found.number.isNullOrBlank() -> found.number
                                !found.ownerName.isNullOrBlank() -> found.ownerName
                                else -> item.label
                            }
                            tvFront.text = displayText
                        } catch (e: Exception) {
                            android.util.Log.w("DocumentChooser", "Failed to update front tile: ${'$'}{e.message}")
                        }

                    } else {
                        // no saved card -> Add
                        btnLeft.text = getString(R.string.add)
                        btnLeft.setOnClickListener {
                            // Special handling for Driving License: ask for subtype
                            if (item.type == "Driving License") {
                                val options = arrayOf("2 wheeler", "3 wheeler", "4 wheeler", "Other")
                                androidx.appcompat.app.AlertDialog.Builder(this@DocumentChooserActivity)
                                    .setTitle("Select vehicle subtype")
                                    .setItems(options) { _, which ->
                                        val subtype = options[which]
                                        openAddEditWithSubtype(item.type, subtype)
                                    }
                                    .setNegativeButton("Cancel", null)
                                    .show()
                            } else {
                                openAddEdit(item.type)
                            }
                        }
                        btnRight.text = "" // hide
                        btnRight.visibility = View.GONE
                    }
                }
            }

            // Flip on front click
            front.setOnClickListener {
                // If a saved card exists for this type, open manage screen directly
                lifecycleScope.launch(Dispatchers.IO) {
                    val allCards = dao.getAllSync() ?: listOf()
                    val foundCard = allCards.firstOrNull { it.type.equals(item.type, ignoreCase = true) }
                    if (foundCard != null) {
                        // Open MainActivity (Manage) with focus
                        launch(Dispatchers.Main) {
                            val i = Intent(this@DocumentChooserActivity, MainActivity::class.java).apply {
                                putExtra("focus_type", item.type)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            }
                            startActivity(i)
                        }
                    } else {
                        // No existing card: flip to show Add action
                        launch(Dispatchers.Main) {
                            flip(front, back)
                        }
                    }
                }
            }
            // Flip back to front when tapping back (but keep actions clickable)
            back.setOnClickListener {
                flip(back, front)
            }

            grid.addView(card)
        }

        // After grid is populated, flip the first tile to show its back (actions) so user sees the first document immediately
        if (firstFront != null && firstBack != null) {
            grid.post {
                try {
                    val f = firstFront
                    val b = firstBack
                    if (f != null && b != null) flip(f, b)
                } catch (e: Exception) {
                    android.util.Log.w("DocumentChooser", "Auto-flip first tile failed: ${'$'}{e.message}")
                }
            }
        }
    }

    private fun openAddEditWithSubtype(type: String, subtype: String) {
        try {
            val intent = AddEditCardActivity.newIntent(this, 0L).apply {
                putExtra("prefill_type", type)
                putExtra("prefill_subtype", subtype)
            }
            startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("DocumentChooser", "Error opening add/edit with subtype: ${'$'}{e.message}", e)
        }
    }

    private fun flip(from: View, to: View) {
        val outAnim = android.animation.ObjectAnimator.ofFloat(from, "rotationY", 0f, 90f).setDuration(180)
        val inAnim = android.animation.ObjectAnimator.ofFloat(to, "rotationY", -90f, 0f).setDuration(180)
        outAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                from.visibility = View.GONE
                to.visibility = View.VISIBLE
                inAnim.start()
            }
        })
        outAnim.start()
    }

    private fun openAddEdit(type: String) {
        val intent = AddEditCardActivity.newIntent(this, 0L).apply { putExtra("prefill_type", type) }
        startActivity(intent)
    }

    private fun shareCard(card: Card) {
        // Share the document as text (type + number) as requested.
        val shareText = "${'$'}{card.type}: ${'$'}{card.number}"
        val share = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(share, "Share document"))
    }

    private fun openEdit(id: Long) {
        startActivity(AddEditCardActivity.newIntent(this, id))
    }
}
