package com.example.docexpiry

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.docexpiry.permissions.PermissionManager

/**
 * BaseActivity - Base class for all activities in DocExpiry
 *
 * Features:
 * - Permission management helpers
 * - Request permissions with callback support
 * - Check permission status
 * - Provides a common overflow menu (logout) for all activities except registration
 */
open class BaseActivity : AppCompatActivity() {

    protected lateinit var permissionManager: PermissionManager
    private var permissionCallback: ((permission: String, granted: Boolean) -> Unit)? = null

    override fun onResume() {
        super.onResume()
        // Initialize permission manager on resume
        permissionManager = PermissionManager(this)
        permissionManager.logPermissionStatus()

        // (Toolbar injection moved to onCreateOptionsMenu so menu is attached correctly before inflation)
    }

    /**
     * Inflate a common menu for activities. By default we show the logout menu item.
     * If the current activity is `RegistrationActivity`, we inflate a different menu
     * that offers a registration shortcut instead of logout.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Ensure an app bar (Toolbar) exists so overflow menu shows on Activities without an ActionBar
        try {
            val root = findViewById<android.view.View>(android.R.id.content) as? android.view.ViewGroup
            root?.let { r ->
                val existing = r.findViewById<androidx.appcompat.widget.Toolbar>(R.id.overflow_toolbar)
                if (existing == null) {
                    // Insert a small Toolbar at the top of the content so activities without an ActionBar get the overflow
                    val toolbar = androidx.appcompat.widget.Toolbar(this).apply {
                        id = R.id.overflow_toolbar
                        layoutParams = android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, androidx.appcompat.widget.Toolbar.LayoutParams.WRAP_CONTENT)
                        // Make background transparent to avoid visual changes
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    }
                    // Add toolbar as first child
                    r.addView(toolbar, 0)
                    try {
                        setSupportActionBar(toolbar)
                        // Ensure overflow icon is visible regardless of theme
                        toolbar.overflowIcon = androidx.core.content.ContextCompat.getDrawable(this, android.R.drawable.ic_menu_more)
                        // Give the toolbar some elevation so it sits above other headers
                        val density = resources.displayMetrics.density
                        toolbar.elevation = 8f * density
                        toolbar.minimumHeight = (56 * density).toInt()
                        toolbar.bringToFront()
                        // Use an appcompat popup theme so overflow menu items are styled correctly
                        try {
                            val popupResName = "ThemeOverlay_AppCompat_Light"
                            val popupResId = resources.getIdentifier(popupResName, "style", packageName)
                            if (popupResId != 0) toolbar.popupTheme = popupResId
                        } catch (_: Exception) { /* ignore if style not available */ }
                        // Refresh the options menu so the items are attached to the newly set ActionBar
                        try { supportInvalidateOptionsMenu() } catch (_: Exception) { }
                    } catch (e: Exception) {
                        android.util.Log.w("BaseActivity", "setSupportActionBar failed: ${'$'}{e.message}")
                    }
                } else {
                    // If there's an existing toolbar, ensure it's set as support action bar
                    try {
                        setSupportActionBar(existing)
                        existing.overflowIcon = androidx.core.content.ContextCompat.getDrawable(this, android.R.drawable.ic_menu_more)
                        try {
                            val popupResName = "ThemeOverlay_AppCompat_Light"
                            val popupResId = resources.getIdentifier(popupResName, "style", packageName)
                            if (popupResId != 0) existing.popupTheme = popupResId
                        } catch (_: Exception) {}
                        try { existing.elevation = 8f * resources.displayMetrics.density } catch (_: Exception) {}
                        try { existing.minimumHeight = (56 * resources.displayMetrics.density).toInt() } catch (_: Exception) {}
                        try { existing.bringToFront() } catch (_: Exception) {}
                        try { supportInvalidateOptionsMenu() } catch (_: Exception) {}
                    } catch (e: Exception) { /* ignore */ }
                }
            }
        } catch (e: Exception) {
            android.util.Log.w("BaseActivity", "Toolbar injection failed in onCreateOptionsMenu: ${'$'}{e.message}")
        }

        return try {
            val menuName = if (this is RegistrationActivity) "menu_overflow_register" else "menu_overflow_common"
            val menuRes = resources.getIdentifier(menuName, "menu", packageName)
            if (menuRes != 0) {
                menuInflater.inflate(menuRes, menu)
                true
            } else {
                android.util.Log.w("BaseActivity", "Menu resource not found: $menuName")
                false
            }
        } catch (e: Exception) {
            android.util.Log.w("BaseActivity", "Failed to inflate menu: ${'$'}{e.message}")
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return try {
            val logoutId = resources.getIdentifier("action_logout", "id", packageName)
            val regId = resources.getIdentifier("action_open_registration", "id", packageName)

            when (item.itemId) {
                logoutId -> {
                    // Only unset the "registered" flag so saved personal details remain (prefill preserved)
                    val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                    prefs.edit().putBoolean("registered", false).apply()

                    // Navigate to registration screen and clear activity stack
                    val i = Intent(this, RegistrationActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(i)
                    finish()
                    true
                }
                regId -> {
                    // Open the registration form (useful as a shortcut)
                    startActivity(Intent(this, RegistrationActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } catch (e: Exception) {
            android.util.Log.w("BaseActivity", "Menu action failed: ${'$'}{e.message}")
            super.onOptionsItemSelected(item)
        }
    }

    /**
     * Request a single permission with callback
     */
    protected fun requestPermission(
        permission: String,
        requestCode: Int,
        callback: ((granted: Boolean) -> Unit)? = null
    ) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            callback?.invoke(true)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }

    /**
     * Request multiple permissions with callback
     */
    protected fun requestPermissions(
        permissions: Array<String>,
        requestCode: Int,
        callback: ((allGranted: Boolean, grantResults: IntArray) -> Unit)? = null
    ) {
        val ungrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (ungrantedPermissions.isEmpty()) {
            callback?.invoke(true, IntArray(permissions.size) { PackageManager.PERMISSION_GRANTED })
        } else {
            ActivityCompat.requestPermissions(this, ungrantedPermissions, requestCode)
        }
    }

    /**
     * Override in subclasses to handle permission results
     * Example:
     * override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
     *     when (requestCode) {
     *         PermissionManager.REQUEST_CODE_CAMERA -> handleCameraPermission(grantResults[0])
     *     }
     * }
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionCallback?.let { callback ->
            for (i in permissions.indices) {
                callback(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED)
            }
        }
    }

    /**
     * Check if all required permissions are granted
     */
    protected fun areAllPermissionsGranted(): Boolean {
        val requiredPermissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Check if notification permission is granted (Android 13+)
     */
    protected fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}
