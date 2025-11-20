package com.example.docexpiry;

/**
 * BaseActivity - Base class for all activities in DocExpiry
 *
 * Features:
 * - Permission management helpers
 * - Request permissions with callback support
 * - Check permission status
 * - Provides a common overflow menu (logout) for all activities except registration
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\tH\u0004J\b\u0010\u0013\u001a\u00020\tH\u0004J\u0012\u0010\u0014\u001a\u00020\t2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J-\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u001c2\u000e\u0010\u001d\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0016\u00a2\u0006\u0002\u0010!J\b\u0010\"\u001a\u00020\u000bH\u0014J?\u0010#\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u001c2%\b\u0002\u0010$\u001a\u001f\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010%H\u0004J_\u0010&\u001a\u00020\u000b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\u001e2\u0006\u0010\u001b\u001a\u00020\u001c2:\b\u0002\u0010$\u001a4\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\'\u0012\u0013\u0012\u00110 \u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u001f\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0004H\u0004\u00a2\u0006\u0002\u0010(R@\u0010\u0003\u001a4\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\rX\u0084.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011\u00a8\u0006)"}, d2 = {"Lcom/example/docexpiry/BaseActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "permissionCallback", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "permission", "", "granted", "", "permissionManager", "Lcom/example/docexpiry/permissions/PermissionManager;", "getPermissionManager", "()Lcom/example/docexpiry/permissions/PermissionManager;", "setPermissionManager", "(Lcom/example/docexpiry/permissions/PermissionManager;)V", "areAllPermissionsGranted", "isNotificationPermissionGranted", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onRequestPermissionsResult", "requestCode", "", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "requestPermission", "callback", "Lkotlin/Function1;", "requestPermissions", "allGranted", "([Ljava/lang/String;ILkotlin/jvm/functions/Function2;)V", "app_debug"})
public class BaseActivity extends androidx.appcompat.app.AppCompatActivity {
    protected com.example.docexpiry.permissions.PermissionManager permissionManager;
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.Boolean, kotlin.Unit> permissionCallback;
    
    public BaseActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    protected final com.example.docexpiry.permissions.PermissionManager getPermissionManager() {
        return null;
    }
    
    protected final void setPermissionManager(@org.jetbrains.annotations.NotNull
    com.example.docexpiry.permissions.PermissionManager p0) {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    /**
     * Inflate a common menu for activities. By default we show the logout menu item.
     * If the current activity is `RegistrationActivity`, we inflate a different menu
     * that offers a registration shortcut instead of logout.
     */
    @java.lang.Override
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.Nullable
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull
    android.view.MenuItem item) {
        return false;
    }
    
    /**
     * Request a single permission with callback
     */
    protected final void requestPermission(@org.jetbrains.annotations.NotNull
    java.lang.String permission, int requestCode, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> callback) {
    }
    
    /**
     * Request multiple permissions with callback
     */
    protected final void requestPermissions(@org.jetbrains.annotations.NotNull
    java.lang.String[] permissions, int requestCode, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function2<? super java.lang.Boolean, ? super int[], kotlin.Unit> callback) {
    }
    
    /**
     * Override in subclasses to handle permission results
     * Example:
     * override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
     *    when (requestCode) {
     *        PermissionManager.REQUEST_CODE_CAMERA -> handleCameraPermission(grantResults[0])
     *    }
     * }
     */
    @java.lang.Override
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull
    int[] grantResults) {
    }
    
    /**
     * Check if all required permissions are granted
     */
    protected final boolean areAllPermissionsGranted() {
        return false;
    }
    
    /**
     * Check if notification permission is granted (Android 13+)
     */
    protected final boolean isNotificationPermissionGranted() {
        return false;
    }
}