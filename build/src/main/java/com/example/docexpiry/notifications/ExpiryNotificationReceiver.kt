package com.example.docexpiry.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * ExpiryNotificationReceiver - Handles broadcast for expiry check alarm
 *
 * This receiver is triggered by AlarmManager every day to check
 * for cards expiring within 30 days and send notifications
 */
class ExpiryNotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ExpiryNotificationReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        Log.d(TAG, "Expiry check alarm triggered")

        // Run expiry check in background thread
        GlobalScope.launch {
            try {
                NotificationManager.getInstance(context).checkAndNotifyExpiringCards()
            } catch (e: Exception) {
                Log.e(TAG, "Error checking expiring cards", e)
            }
        }
    }
}

