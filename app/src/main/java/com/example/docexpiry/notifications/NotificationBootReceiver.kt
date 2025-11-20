package com.example.docexpiry.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * NotificationBootReceiver - Handles device boot completion
 *
 * This receiver reschedules expiry notification alarms when device restarts
 * since alarms are cleared when device is turned off
 */
class NotificationBootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationBootReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        // Check if this is boot completion intent
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Device boot completed, rescheduling alarms")

            // Reschedule expiry notifications
            NotificationManager.getInstance(context).scheduleExpiryNotifications()
        }
    }
}

