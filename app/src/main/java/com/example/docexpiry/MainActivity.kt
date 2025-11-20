package com.example.docexpiry

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.docexpiry.data.AppDatabase
import com.example.docexpiry.data.Card
import com.example.docexpiry.databinding.ActivityMainBinding
import com.example.docexpiry.notifications.NotificationManager
import com.example.docexpiry.permissions.PermissionManager
import com.example.docexpiry.utils.ImageShareUtils
import java.util.*

// Main dashboard showing all cards, search, filter and sort
// Extends BaseActivity for permission management support
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CardAdapter
    private lateinit var notificationManager: NotificationManager
    private val viewModel: CardListViewModel by viewModels {
        CardListViewModel.Factory(AppDatabase.getInstance(applicationContext).cardDao())
    }

    // Keep the latest list from DB so we can apply combined filters + sorts
    private var allCards: List<Card> = listOf()

    // Use Activity Result API instead of deprecated startActivityForResult
    // private val VIEW_IMAGE_REQUEST = 501

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize permission manager
        permissionManager = PermissionManager(this)

        // Initialize notification manager and schedule expiry checks
        notificationManager = NotificationManager.getInstance(this)
        notificationManager.scheduleExpiryNotifications()

        // Request required permissions using PermissionManager
        val perms = permissionManager.getRequiredPermissions()
        if (perms.isNotEmpty()) {
            requestPermissions(perms, PermissionManager.REQUEST_CODE_ALL) { allGranted, _ ->
                if (!allGranted) android.util.Log.w("MainActivity", "Some permissions were not granted")
            }
        }

        // If user not registered, navigate to RegistrationActivity
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        if (!prefs.getBoolean("registered", false)) {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
            return
        }

        // Display user info in header
        val userName = prefs.getString("user_name", "User") ?: "User"
        binding.tvUserName.text = userName

        // Logout is handled via the overflow menu in BaseActivity (menu_overflow_common)

        // Register launcher to receive results from FullImageActivity
        val viewImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val dataIntent = result.data
                if (dataIntent != null) {
                    val extras = dataIntent.extras
                    if (extras != null) {
                        val deletedId = extras.getLong("deletedItemId", -1L)
                        if (deletedId != -1L) {
                            val cardToDelete = allCards.find { it.id == deletedId }
                            if (cardToDelete != null) {
                                viewModel.delete(cardToDelete)
                            }
                        } else {
                            val editedId = extras.getLong("editedItemId", -1L)
                            if (editedId != -1L) {
                                // LiveData will refresh the list - nothing more to do here
                            }
                        }
                    }
                }
            }
        }

        // Update adapter to handle image view action and start full image activity via launcher
        try {
            adapter = CardAdapter { card, action ->
                try {
                    when (action) {
                        CardAdapter.Action.VIEW -> startActivity(CardDetailActivity.newIntent(this, card.id))
                        CardAdapter.Action.EDIT -> startActivity(AddEditCardActivity.newIntent(this, card.id))
                        CardAdapter.Action.DELETE -> viewModel.delete(card)
                        CardAdapter.Action.SHARE -> shareCard(card)
                        CardAdapter.Action.VIEW_IMAGE -> {
                            // Precompute formatted DOB string (if present) to keep Intent construction tidy
                            val ownerDobStr = if (card.ownerDob > 0L) {
                                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                sdf.format(java.util.Date(card.ownerDob))
                            } else null

                            val i = Intent(this, FullImageActivity::class.java).apply {
                                putExtra("photoUri", card.photoUri)
                                putExtra("itemId", card.id)
                                putExtra("ownerName", card.ownerName)
                                putExtra("ownerAddress", card.ownerAddress)
                                ownerDobStr?.let { putExtra("ownerDob", it) }
                                putExtra("cardNumber", card.number)
                                putExtra("authority", card.authority)
                                putExtra("issuedDate", card.issuedDate)
                                putExtra("expiryDate", card.expiryDate)
                            }
                            viewImageLauncher.launch(i)
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("MainActivity", "Error handling action: ${'$'}{e.message}", e)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error creating adapter: ${'$'}{e.message}", e)
            return
        }

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        // Setup filter & sort spinners with null checks
        val cardTypes = listOf("All", "Aadhaar", "PAN", "Voter ID", "Driving License", "Passport", "Birth Certificate", "Ration Card", "Academic Certificate")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cardTypes)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterType.adapter = filterAdapter

        // Issued range spinner
        val issuedRanges = listOf("All time", "Last 30 days", "Last year", "Older")
        val issuedAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, issuedRanges)
        issuedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIssuedRange.adapter = issuedAdapter

        val sortOptions = listOf("Expiry: Nearest", "Expiry: Furthest", "Issued: Newest", "Issued: Oldest", "Type A-Z")
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSort.adapter = sortAdapter

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spinnerFilterType.onItemSelectedListener = spinnerListener
        binding.spinnerSort.onItemSelectedListener = spinnerListener
        binding.spinnerIssuedRange.onItemSelectedListener = spinnerListener

        // Observe cards and update local list + UI with error handling
        try {
            viewModel.cards.observe(this) { list ->
                allCards = list ?: listOf()
                val pluralSuffix = if (allCards.size != 1) "s" else ""
                binding.tvDocCount.text = getString(R.string.doc_count, allCards.size, pluralSuffix)
                applyFilters()

                // If activity was opened with a focus_type (from DocumentChooser), preselect and focus that filter
                val focusType = intent.getStringExtra("focus_type")
                if (!focusType.isNullOrBlank()) {
                    try {
                        val idx = (binding.spinnerFilterType.adapter as? ArrayAdapter<String>)?.getPosition(focusType) ?: -1
                         if (idx >= 0) {
                             binding.spinnerFilterType.setSelection(idx)
                             // remove the extra so subsequent resumes don't keep forcing it
                             intent.removeExtra("focus_type")
                             applyFilters()
                         }
                    } catch (e: Exception) {
                        android.util.Log.w("MainActivity", "Failed to apply focus_type: ${'$'}{e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error observing cards: ${'$'}{e.message}", e)
        }

        // SearchView text changes: update adapter filter (use binding.searchBar)
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchBar.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                try {
                    applyFilters()
                } catch (e: Exception) {
                    android.util.Log.e("MainActivity", "Error in search: ${'$'}{e.message}", e)
                }
                return true
            }
        })

        // Initial filter application
        applyFilters()

        // Wire up FAB to open AddEditCardActivity
        try {
            binding.fabAdd.setOnClickListener {
                try {
                    startActivity(AddEditCardActivity.newIntent(this, 0L))
                } catch (e: Exception) {
                    android.util.Log.e("MainActivity", "Failed to launch AddEditCardActivity: ${'$'}{e.message}", e)
                }
            }
        } catch (e: Exception) {
            android.util.Log.w("MainActivity", "FAB not found or error wiring it: ${'$'}{e.message}")
        }
    }

    // Handle back press: if search view is focused, clear focus; otherwise, perform default back action
    override fun onBackPressed() {
        if (binding.searchBar.hasFocus()) {
            binding.searchBar.clearFocus()
            return
        }
        super.onBackPressed()
    }

    // Menu (logout) handled by BaseActivity

    // Apply filters and sorting to the card list based on spinner selections
    private fun applyFilters() {
        try {
            // Get selected filter type, issued range, and sort order
            val filterType = binding.spinnerFilterType.selectedItem.toString()
            val issuedRange = binding.spinnerIssuedRange.selectedItem.toString()
            val sortOrder = binding.spinnerSort.selectedItem.toString()

            // Filter by card type
            val filteredList = when (filterType) {
                "All" -> allCards
                else -> allCards.filter { it.type == filterType }
            }

            // Further filter by issued date range
            val dateFilteredList = when (issuedRange) {
                "All time" -> filteredList
                "Last 30 days" -> filteredList.filter { it.issuedDate >= System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000 }
                "Last year" -> filteredList.filter { it.issuedDate >= System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000 }
                "Older" -> filteredList.filter { it.issuedDate < System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000 }
                else -> filteredList
            }

            // Sort the list based on the selected order
            val sortedList = when (sortOrder) {
                "Expiry: Nearest" -> dateFilteredList.sortedBy { it.expiryDate }
                "Expiry: Furthest" -> dateFilteredList.sortedByDescending { it.expiryDate }
                "Issued: Newest" -> dateFilteredList.sortedByDescending { it.issuedDate }
                "Issued: Oldest" -> dateFilteredList.sortedBy { it.issuedDate }
                "Type A-Z" -> dateFilteredList.sortedBy { it.type }
                else -> dateFilteredList
            }

            // Update the adapter with the filtered and sorted list
            adapter.submitList(sortedList)
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error applying filters: ${'$'}{e.message}", e)
        }
    }

    // Share card details as text
    private fun shareCard(card: Card) {
        try {
            val shareText = StringBuilder().apply {
                appendLine("${card.type} Details:")
                appendLine("Name: ${card.ownerName}")
                appendLine("Number: ${card.number}")
                appendLine("Issued: ${formatDate(card.issuedDate)}")
                appendLine("Expires: ${formatDate(card.expiryDate)}")
                appendLine("Authority: ${card.authority}")
                appendLine("Address: ${card.ownerAddress}")
                appendLine()
                append(getString(R.string.app_name) + " - Manage your documents securely.")
            }.toString()
            // Use ImageShareUtils to share text (and image if needed)
            ImageShareUtils.shareText(this, shareText, card.photoUri)
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error sharing card: ${'$'}{e.message}", e)
        }
    }

    // Format date from millis to dd/MM/yyyy string
    private fun formatDate(millis: Long): String {
        return try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            sdf.format(java.util.Date(millis))
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error formatting date: ${'$'}{e.message}", e)
            millis.toString() // Fallback to millis string
        }
    }
}
