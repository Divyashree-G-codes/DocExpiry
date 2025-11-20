package com.example.docexpiry.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import android.util.Log

/**
 * PermissionManager - Handles runtime permissions for DocExpiry
 *
 * Features:
 * - Check if permissions are granted
 * - Get list of permissions to request
 * - Handle runtime permission requests (Android 6.0+)
 * - Determine if permission rationale should be shown
 *
 * Permissions handled:
 * - CAMERA: For capturing card photos
 * - READ_EXTERNAL_STORAGE: For selecting images from gallery
 * - POST_NOTIFICATIONS: For sending notifications (Android 13+)
 *
 * Usage:
 * val permissionManager = PermissionManager(context)
 * if (!permissionManager.isCameraGranted()) {
 *     // Request permission
 * }
 */
class PermissionManager(private val context: Context) {

    companion object {
        private const val TAG = "PermissionManager"

        // Permission request codes
        const val REQUEST_CODE_CAMERA = 100
        const val REQUEST_CODE_STORAGE = 101
        const val REQUEST_CODE_NOTIFICATION = 102
        const val REQUEST_CODE_ALL = 103
    }

    /**
     * Check if camera permission is granted
     */
    fun isCameraGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if read external storage permission is granted
     */
    fun isStorageGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if notification permission is granted (Android 13+)
     */
    fun isNotificationGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Notifications are allowed by default on older Android versions
            true
        }
    }

    /**
     * Get array of permissions that need to be requested
     */
    fun getPermissionsToRequest(): Array<String> {
        val permissions = mutableListOf<String>()

        // Add camera permission if not granted
        if (!isCameraGranted()) {
            permissions.add(Manifest.permission.CAMERA)
        }

        // Add storage permission if not granted
        if (!isStorageGranted()) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // Add notification permission if not granted (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isNotificationGranted()) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        return permissions.toTypedArray()
    }

    /**
     * Get list of permissions that should not be requested (already permanently denied)
     */
    fun getPermanentlyDeniedPermissions(): List<String> {
        val denied = mutableListOf<String>()

        if (!isCameraGranted()) {
            denied.add(Manifest.permission.CAMERA)
        }
        if (!isStorageGranted()) {
            denied.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isNotificationGranted()) {
            denied.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        return denied
    }

    /**
     * Log permission status
     */
    fun logPermissionStatus() {
        Log.d(TAG, """
            Permission Status:
            - Camera: ${if (isCameraGranted()) "GRANTED" else "DENIED"}
            - Storage: ${if (isStorageGranted()) "GRANTED" else "DENIED"}
            - Notifications: ${if (isNotificationGranted()) "GRANTED" else "DENIED"}
        """.trimIndent())
    }

    /**
     * Request all required permissions at once
     * Call this method and handle the result in onRequestPermissionsResult
     */
    fun getRequiredPermissions(): Array<String> {
        val permissions = mutableListOf<String>()

        // Always request camera (required for card photos)
        if (!isCameraGranted()) {
            permissions.add(Manifest.permission.CAMERA)
        }

        // Always request storage (required for gallery access)
        if (!isStorageGranted()) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // Request notifications on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isNotificationGranted()) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        return permissions.toTypedArray()
    }
}

