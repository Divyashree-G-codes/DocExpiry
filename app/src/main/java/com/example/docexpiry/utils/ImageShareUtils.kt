package com.example.docexpiry.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ImageShareUtils {
    /**
     * Returns a content:// Uri suitable for sharing via FileProvider when possible.
     * - Accepts content://, file://, and absolute file paths.
     * - If input is an http/https URL, returns null (not handled here).
     */
    fun getShareableImageUri(context: Context, input: String): Uri? {
        try {
            // If already content uri, return as-is
            if (input.startsWith("content://")) return Uri.parse(input)
            // If http/https, not sharable via FileProvider (could be downloaded in future)
            if (input.startsWith("http://") || input.startsWith("https://")) return null
            // If file:// or a raw path, build File object
            val path = if (input.startsWith("file://")) {
                Uri.parse(input).path ?: input.removePrefix("file://")
            } else input

            val f = File(path)
            if (!f.exists()) return null

            val authority = context.packageName + ".fileprovider"
            return FileProvider.getUriForFile(context, authority, f)
        } catch (e: Exception) {
            android.util.Log.w("ImageShareUtils", "getShareableImageUri failed: ${e.message}")
            return null
        }
    }

    /**
     * Shares text (and optional image) using Android's share intent.
     * @param context Context to use for starting the activity
     * @param text Text to share
     * @param imageUri Optional image Uri to share
     */
    fun shareText(context: Context, text: String, imageUri: String?) {
        val intent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, text)
            type = if (imageUri != null) "image/*" else "text/plain"
            if (imageUri != null) {
                val uri = getShareableImageUri(context, imageUri)
                if (uri != null) {
                    putExtra(android.content.Intent.EXTRA_STREAM, uri)
                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }
        }
        val chooser = android.content.Intent.createChooser(intent, "Share via")
        context.startActivity(chooser)
    }
}
