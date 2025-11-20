package com.example.docexpiry.permissions;

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
 *    // Request permission
 * }
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J\u0011\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\u0002\u0010\nJ\u0011\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u0006\u0010\u000f\u001a\u00020\rJ\u0006\u0010\u0010\u001a\u00020\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/example/docexpiry/permissions/PermissionManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getPermanentlyDeniedPermissions", "", "", "getPermissionsToRequest", "", "()[Ljava/lang/String;", "getRequiredPermissions", "isCameraGranted", "", "isNotificationGranted", "isStorageGranted", "logPermissionStatus", "", "Companion", "app_debug"})
public final class PermissionManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "PermissionManager";
    public static final int REQUEST_CODE_CAMERA = 100;
    public static final int REQUEST_CODE_STORAGE = 101;
    public static final int REQUEST_CODE_NOTIFICATION = 102;
    public static final int REQUEST_CODE_ALL = 103;
    @org.jetbrains.annotations.NotNull
    public static final com.example.docexpiry.permissions.PermissionManager.Companion Companion = null;
    
    public PermissionManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    /**
     * Check if camera permission is granted
     */
    public final boolean isCameraGranted() {
        return false;
    }
    
    /**
     * Check if read external storage permission is granted
     */
    public final boolean isStorageGranted() {
        return false;
    }
    
    /**
     * Check if notification permission is granted (Android 13+)
     */
    public final boolean isNotificationGranted() {
        return false;
    }
    
    /**
     * Get array of permissions that need to be requested
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String[] getPermissionsToRequest() {
        return null;
    }
    
    /**
     * Get list of permissions that should not be requested (already permanently denied)
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getPermanentlyDeniedPermissions() {
        return null;
    }
    
    /**
     * Log permission status
     */
    public final void logPermissionStatus() {
    }
    
    /**
     * Request all required permissions at once
     * Call this method and handle the result in onRequestPermissionsResult
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String[] getRequiredPermissions() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/example/docexpiry/permissions/PermissionManager$Companion;", "", "()V", "REQUEST_CODE_ALL", "", "REQUEST_CODE_CAMERA", "REQUEST_CODE_NOTIFICATION", "REQUEST_CODE_STORAGE", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}