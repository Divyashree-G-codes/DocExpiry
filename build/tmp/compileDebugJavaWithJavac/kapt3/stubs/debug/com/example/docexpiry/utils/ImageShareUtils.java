package com.example.docexpiry.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ \u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\b\u00a8\u0006\r"}, d2 = {"Lcom/example/docexpiry/utils/ImageShareUtils;", "", "()V", "getShareableImageUri", "Landroid/net/Uri;", "context", "Landroid/content/Context;", "input", "", "shareText", "", "text", "imageUri", "app_debug"})
public final class ImageShareUtils {
    @org.jetbrains.annotations.NotNull
    public static final com.example.docexpiry.utils.ImageShareUtils INSTANCE = null;
    
    private ImageShareUtils() {
        super();
    }
    
    /**
     * Returns a content:// Uri suitable for sharing via FileProvider when possible.
     * - Accepts content://, file://, and absolute file paths.
     * - If input is an http/https URL, returns null (not handled here).
     */
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri getShareableImageUri(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String input) {
        return null;
    }
    
    /**
     * Shares text (and optional image) using Android's share intent.
     * @param context Context to use for starting the activity
     * @param text Text to share
     * @param imageUri Optional image Uri to share
     */
    public final void shareText(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String text, @org.jetbrains.annotations.Nullable
    java.lang.String imageUri) {
    }
}