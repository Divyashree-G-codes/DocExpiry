package com.example.docexpiry.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.docexpiry.MainActivity
import com.example.docexpiry.R
import java.util.*

/**
 * NotificationManager - Handles all notification operations for DocExpiry
 *
 * Features:
 * - Send notifications for cards expiring within 30 days
 * - Schedule alarms to send notifications periodically
 * - Handle notification channel creation (Android 8.0+)
 * - Request notification permissions (Android 13+)
 * - Reschedule notifications on device boot
 *
 * Usage:
 * NotificationManager.getInstance(context).scheduleExpiryNotifications()
 */
class NotificationManager(private val context: Context) {

    companion object {
        private const val TAG = "NotificationManager"

        // Notification channels
        const val CHANNEL_ID_EXPIRY = "expiry_notifications"
        const val CHANNEL_NAME_EXPIRY = "Card Expiry Alerts"
        const val CHANNEL_ID_GENERAL = "general_notifications"
        const val CHANNEL_NAME_GENERAL = "General Notifications"

        // Notification IDs
        const val NOTIFICATION_ID_EXPIRY = 1001
        const val NOTIFICATION_ID_GENERAL = 1002

        // Request codes for alarms
        const val REQUEST_CODE_EXPIRY_CHECK = 2001

        // Days before expiry to show notification
        const val DAYS_BEFORE_EXPIRY = 30

        private var instance: NotificationManager? = null

        fun getInstance(context: Context): NotificationManager {
            return instance ?: NotificationManager(context.applicationContext).also { instance = it }
        }
    }

    init {
        createNotificationChannels()
    }

    /**
     * Create notification channels for Android 8.0+
     * Required for notifications to work on modern Android versions
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Expiry alerts channel - High importance
            val expiryChannel = NotificationChannel(
                CHANNEL_ID_EXPIRY,
                CHANNEL_NAME_EXPIRY,
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for cards expiring soon"
                enableVibration(true)
                enableLights(true)
            }

            // General notifications channel - Default importance
            val generalChannel = NotificationChannel(
                CHANNEL_ID_GENERAL,
                CHANNEL_NAME_GENERAL,
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General app notifications"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(expiryChannel)
            notificationManager.createNotificationChannel(generalChannel)

            Log.d(TAG, "Notification channels created")
        }
    }

    /**
     * Show notification for cards expiring soon
     *
     * @param expiringCardCount Number of cards expiring within 30 days
     * @param cardNames List of card names expiring soon
     */
    fun showExpiryNotification(expiringCardCount: Int, cardNames: List<String>) {
        try {
            if (expiringCardCount == 0) return

            // Build notification message
            val title = if (expiringCardCount == 1) {
                "1 Card Expiring Soon"
            } else {
                "$expiringCardCount Cards Expiring Soon"
            }

            val message = when {
                expiringCardCount == 1 -> cardNames.firstOrNull() ?: "Your card is expiring"
                expiringCardCount <= 3 -> cardNames.joinToString(", ")
                else -> "${cardNames.take(3).joinToString(", ")} and ${expiringCardCount - 3} more"
            }

            // Create intent to open MainActivity when tapped
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID_EXPIRY,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Build notification
            val notification = NotificationCompat.Builder(context, CHANNEL_ID_EXPIRY)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build()

            // Show notification
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_EXPIRY, notification)
            Log.d(TAG, "Expiry notification shown: $expiringCardCount cards")
        } catch (e: Exception) {
            Log.e(TAG, "Error showing expiry notification", e)
        }
    }

    /**
     * Show general notification
     */
    fun showGeneralNotification(title: String, message: String) {
        try {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID_GENERAL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_GENERAL, notification)
            Log.d(TAG, "General notification shown: $title")
        } catch (e: Exception) {
            Log.e(TAG, "Error showing general notification", e)
        }
    }

    /**
     * Schedule daily alarm to check for expiring cards
     * This alarm will trigger every day at 9 AM
     */
    fun scheduleExpiryNotifications() {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                ?: return

            // Create intent for broadcast receiver
            val intent = Intent(context, ExpiryNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_EXPIRY_CHECK,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Schedule for 9 AM daily
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)

                // If it's already past 9 AM, schedule for tomorrow
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            // Set repeating alarm
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Log.d(TAG, "Expiry notifications scheduled for ${calendar.time}")
            } catch (e: SecurityException) {
                Log.w(TAG, "SCHEDULE_EXACT_ALARM permission not available, using setAndAllowWhileIdle")
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling expiry notifications", e)
        }
    }

    /**
     * Cancel scheduled expiry notifications
     */
    fun cancelExpiryNotifications() {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                ?: return

            val intent = Intent(context, ExpiryNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_EXPIRY_CHECK,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)
            Log.d(TAG, "Expiry notifications cancelled")
        } catch (e: Exception) {
            Log.e(TAG, "Error cancelling expiry notifications", e)
        }
    }

    /**
     * Check for cards expiring within DAYS_BEFORE_EXPIRY days
     * and show notification if any are found
     */
    suspend fun checkAndNotifyExpiringCards() {
        try {
            val dao = com.example.docexpiry.data.AppDatabase.getInstance(context).cardDao()
            val allCards = dao.getAllSync() ?: emptyList()

            val now = System.currentTimeMillis()
            val thirtyDaysMillis = DAYS_BEFORE_EXPIRY * 24 * 60 * 60 * 1000L

            // Filter cards expiring within 30 days
            val expiringCards = allCards.filter { card ->
                val daysToExpiry = (card.expiryDate - now) / (24 * 60 * 60 * 1000)
                daysToExpiry in 0..DAYS_BEFORE_EXPIRY
            }

            if (expiringCards.isNotEmpty()) {
                val cardNames = expiringCards.map { it.type }
                showExpiryNotification(expiringCards.size, cardNames)
                Log.d(TAG, "${expiringCards.size} cards expiring soon")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking expiring cards", e)
        }
    }
}

