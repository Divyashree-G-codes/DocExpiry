package com.example.docexpiry.notifications;

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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0011\u0010\u0007\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\u0006H\u0002J\u0006\u0010\n\u001a\u00020\u0006J\u001c\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ\u0016\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0015"}, d2 = {"Lcom/example/docexpiry/notifications/NotificationManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "cancelExpiryNotifications", "", "checkAndNotifyExpiringCards", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createNotificationChannels", "scheduleExpiryNotifications", "showExpiryNotification", "expiringCardCount", "", "cardNames", "", "", "showGeneralNotification", "title", "message", "Companion", "app_debug"})
public final class NotificationManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "NotificationManager";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_ID_EXPIRY = "expiry_notifications";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_NAME_EXPIRY = "Card Expiry Alerts";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_ID_GENERAL = "general_notifications";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_NAME_GENERAL = "General Notifications";
    public static final int NOTIFICATION_ID_EXPIRY = 1001;
    public static final int NOTIFICATION_ID_GENERAL = 1002;
    public static final int REQUEST_CODE_EXPIRY_CHECK = 2001;
    public static final int DAYS_BEFORE_EXPIRY = 30;
    @org.jetbrains.annotations.Nullable
    private static com.example.docexpiry.notifications.NotificationManager instance;
    @org.jetbrains.annotations.NotNull
    public static final com.example.docexpiry.notifications.NotificationManager.Companion Companion = null;
    
    public NotificationManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    /**
     * Create notification channels for Android 8.0+
     * Required for notifications to work on modern Android versions
     */
    private final void createNotificationChannels() {
    }
    
    /**
     * Show notification for cards expiring soon
     *
     * @param expiringCardCount Number of cards expiring within 30 days
     * @param cardNames List of card names expiring soon
     */
    public final void showExpiryNotification(int expiringCardCount, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> cardNames) {
    }
    
    /**
     * Show general notification
     */
    public final void showGeneralNotification(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String message) {
    }
    
    /**
     * Schedule daily alarm to check for expiring cards
     * This alarm will trigger every day at 9 AM
     */
    public final void scheduleExpiryNotifications() {
    }
    
    /**
     * Cancel scheduled expiry notifications
     */
    public final void cancelExpiryNotifications() {
    }
    
    /**
     * Check for cards expiring within DAYS_BEFORE_EXPIRY days
     * and show notification if any are found
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object checkAndNotifyExpiringCards(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/example/docexpiry/notifications/NotificationManager$Companion;", "", "()V", "CHANNEL_ID_EXPIRY", "", "CHANNEL_ID_GENERAL", "CHANNEL_NAME_EXPIRY", "CHANNEL_NAME_GENERAL", "DAYS_BEFORE_EXPIRY", "", "NOTIFICATION_ID_EXPIRY", "NOTIFICATION_ID_GENERAL", "REQUEST_CODE_EXPIRY_CHECK", "TAG", "instance", "Lcom/example/docexpiry/notifications/NotificationManager;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.docexpiry.notifications.NotificationManager getInstance(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}