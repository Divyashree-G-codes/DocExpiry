package com.example.docexpiry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 $2\u00020\u0001:\u0001$B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\bH\u0002J\u0010\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\bH\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J\u0012\u0010\u001b\u001a\u00020\u001a2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0014J\u001c\u0010\u001e\u001a\u00020\u001a2\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u001a0 H\u0002J\u0010\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020#H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/example/docexpiry/AddEditCardActivity;", "Lcom/example/docexpiry/BaseActivity;", "()V", "binding", "Lcom/example/docexpiry/databinding/ActivityAddEditCardBinding;", "cameraUri", "Landroid/net/Uri;", "currentPhotoUri", "", "dao", "Lcom/example/docexpiry/data/CardDao;", "getDao", "()Lcom/example/docexpiry/data/CardDao;", "dao$delegate", "Lkotlin/Lazy;", "editingId", "", "pickImageLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "takePictureLauncher", "isValidAadhaar", "", "aadhaar", "isValidPan", "pan", "launchCamera", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "pickDate", "onPicked", "Lkotlin/Function1;", "populateForEdit", "card", "Lcom/example/docexpiry/data/Card;", "Companion", "app_debug"})
public final class AddEditCardActivity extends com.example.docexpiry.BaseActivity {
    private com.example.docexpiry.databinding.ActivityAddEditCardBinding binding;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy dao$delegate = null;
    @org.jetbrains.annotations.Nullable
    private java.lang.String currentPhotoUri;
    private long editingId = 0L;
    private androidx.activity.result.ActivityResultLauncher<java.lang.String> pickImageLauncher;
    private androidx.activity.result.ActivityResultLauncher<android.net.Uri> takePictureLauncher;
    @org.jetbrains.annotations.Nullable
    private android.net.Uri cameraUri;
    @org.jetbrains.annotations.NotNull
    public static final com.example.docexpiry.AddEditCardActivity.Companion Companion = null;
    
    public AddEditCardActivity() {
        super();
    }
    
    private final com.example.docexpiry.data.CardDao getDao() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void populateForEdit(com.example.docexpiry.data.Card card) {
    }
    
    private final void pickDate(kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onPicked) {
    }
    
    private final void launchCamera() {
    }
    
    private final boolean isValidPan(java.lang.String pan) {
        return false;
    }
    
    private final boolean isValidAadhaar(java.lang.String aadhaar) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u00a8\u0006\t"}, d2 = {"Lcom/example/docexpiry/AddEditCardActivity$Companion;", "", "()V", "newIntent", "Landroid/content/Intent;", "ctx", "Landroid/content/Context;", "id", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.content.Intent newIntent(@org.jetbrains.annotations.NotNull
        android.content.Context ctx, long id) {
            return null;
        }
    }
}