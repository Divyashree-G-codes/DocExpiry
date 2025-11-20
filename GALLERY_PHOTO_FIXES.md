# DocExpiry - Gallery & Photo Issues - Fix Guide

## ⚠️ Common Gallery/Photo Problems & Solutions

### 1. **Photos Not Displaying on Dashboard Cards**

#### Symptom:
- Cards show placeholder image instead of actual photo
- ImageView shows gray/default placeholder
- No error in logs

#### Root Causes & Fixes:

**Cause A: photoUri is NULL**
```kotlin
// Problem: Photo not saved with card
if (card.photoUri == null || card.photoUri.isEmpty()) {
    // Shows placeholder
}

// Solution: Ensure photo selected before save
in AddEditCardActivity:
    if (currentPhotoUri.isNullOrEmpty()) {
        Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }
```

**Cause B: Photo URI Invalid/Deleted**
```kotlin
// Problem: URI points to file that no longer exists (cache cleared)
// Gallery photos: /storage/emulated/0/DCIM/...
// Cache photos: /data/data/com.example.docexpiry/cache/... (deleted)

// Solution: Validate URI before loading
Glide.with(context)
    .load(if (card.photoUri?.isNotEmpty() == true) card.photoUri else null)
    .placeholder(R.drawable.placeholder_user)
    .error(R.drawable.placeholder_user)
    .into(imageView)
```

**Cause C: Glide Caching Issue**
```kotlin
// Solution: Clear Glide cache on app launch
Glide.get(this).clearDiskCache()  // Clear disk cache
Glide.get(this).clearMemory()     // Clear memory cache

// Then reload cards
viewModel.cards.observe(this) { list ->
    adapter.submitList(list)
}
```

**Cause D: Permissions Issue**
```kotlin
// Problem: App can't read photo due to permission denial
// Solution: Request READ_EXTERNAL_STORAGE permission

PermissionManager(this).requestPermissions()

// Then reload Glide
Glide.with(this).load(card.photoUri).into(imageView)
```

#### Debug Steps:
1. Check logcat for Glide errors:
   ```
   android.util.Log.d("Glide", "Loading photo from: " + card.photoUri)
   ```

2. Add debug toast:
   ```kotlin
   Toast.makeText(this, "Photo URI: " + card.photoUri, Toast.LENGTH_LONG).show()
   ```

3. Verify photo exists:
   ```kotlin
   val file = File(card.photoUri ?: "")
   if (file.exists()) {
       // Photo exists
   } else {
       // Photo deleted
   }
   ```

---

### 2. **Camera Button: "Camera access needed" Toast**

#### Symptom:
- Tapping "📷 Camera" button shows toast
- Camera doesn't launch
- Dialog might appear first time

#### Root Causes & Fixes:

**Cause A: CAMERA Permission Not Granted**
```kotlin
// Problem: User denied permission
// Solution: Request again

// Code in AddEditCardActivity.kt:
binding.btnCamera.setOnClickListener {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
        == PackageManager.PERMISSION_GRANTED) {
        launchCamera()
    } else {
        ActivityCompat.requestPermissions(
            this, 
            arrayOf(Manifest.permission.CAMERA), 
            CAMERA_REQUEST
        )
    }
}
```

**Cause B: Permission Denied Permanently**
- User tapped "Don't ask again"
- Solution: Manual enable in device settings

**Steps to Enable**:
1. Open device Settings
2. Navigate to: **Settings > Apps > DocExpiry**
3. Tap: **Permissions** or **App Permissions**
4. Find: **Camera**
5. Change: **Deny** → **Allow**
6. Return to app and try again

#### Quick Fix:
```kotlin
// In PermissionManager.kt - Request with explanation
if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
    // Show dialog explaining why camera needed
    AlertDialog.Builder(activity)
        .setTitle("Camera Access")
        .setMessage("DocExpiry needs camera access to take photos of documents")
        .setPositiveButton("Grant") { _, _ ->
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
        }
        .setNegativeButton("Cancel", null)
        .show()
} else {
    // Request directly
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
}
```

---

### 3. **Gallery Button: "Gallery access needed" Toast**

#### Symptom:
- Tapping "🖼️ Gallery" button shows toast
- File picker doesn't open
- Can't select photos

#### Root Causes & Fixes:

**Cause A: READ_EXTERNAL_STORAGE Not Granted**
```kotlin
// Solution: Request storage permission

binding.btnPick.setOnClickListener {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
        == PackageManager.PERMISSION_GRANTED) {
        pickImageLauncher.launch("image/*")
    } else {
        ActivityCompat.requestPermissions(
            this, 
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 
            STORAGE_REQUEST
        )
    }
}
```

**Cause B: Android 11+ Scoped Storage**
```kotlin
// Android 11+ requires different approach
// Solution: Use GetContent contract (already implemented)

pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    // Returns URI from user's gallery selection
    // No broad storage access needed
}
```

**Steps to Enable**:
1. Open device Settings
2. Navigate to: **Settings > Apps > DocExpiry**
3. Tap: **Permissions** or **App Permissions**
4. Find: **Storage** or **Photos**
5. Change: **Deny** → **Allow** (or "Allow only while using the app")
6. Return to app and try again

#### Debug Check:
```kotlin
// Log permission status
val hasPerm = ContextCompat.checkSelfPermission(
    this, 
    Manifest.permission.READ_EXTERNAL_STORAGE
) == PackageManager.PERMISSION_GRANTED

android.util.Log.d("Permission", "Has storage: $hasPerm")
Toast.makeText(this, "Storage permission: $hasPerm", Toast.LENGTH_SHORT).show()
```

---

### 4. **Camera Opens But Photo Not Saved**

#### Symptom:
- Camera launches and works
- Taking photo succeeds
- But preview doesn't update
- Photo not saved with document

#### Root Causes & Fixes:

**Cause A: ActivityResult Not Registered**
```kotlin
// Check in AddEditCardActivity.onCreate()
takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
    if (success && cameraUri != null) {
        currentPhotoUri = cameraUri.toString()
        binding.imagePreview.setImageURI(cameraUri)
        Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show()
    }
}

// Ensure this runs BEFORE button click listener!
```

**Cause B: cameraUri Not Created Properly**
```kotlin
// Problem: File or FileProvider issue
private fun launchCamera() {
    try {
        val f = File(cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        if (!f.createNewFile()) {
            Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show()
            return
        }
        
        val uri = FileProvider.getUriForFile(
            this, 
            "${applicationContext.packageName}.fileprovider", 
            f
        )
        cameraUri = uri
        takePictureLauncher.launch(uri)
    } catch (e: Exception) {
        android.util.Log.e("Camera", "Error: ${e.message}", e)
        Toast.makeText(this, "Camera error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
```

**Cause C: FileProvider Not Configured**
```xml
<!-- Check AndroidManifest.xml -->
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>

<!-- Check res/xml/file_paths.xml -->
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <cache-path name="cache" path="/" />
</paths>
```

#### Debug Steps:
1. Add logging:
   ```kotlin
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       android.util.Log.d("Camera", "Result: $requestCode, $resultCode")
   }
   ```

2. Check file creation:
   ```kotlin
   val f = File(cacheDir, "test.jpg")
   val created = f.createNewFile()
   android.util.Log.d("File", "Created: $created")
   ```

---

### 5. **Gallery Selection Returns Wrong Photo**

#### Symptom:
- User selects photo from gallery
- Wrong/different photo displayed
- Or photo doesn't display

#### Root Causes & Fixes:

**Cause A: Older Device - Content URI Issue**
```kotlin
// Problem: Content URI not properly handled on older Android
// Solution: Copy file to app cache for consistency

pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    uri?.let {
        try {
            // Validate URI
            val size = contentResolver.getType(it)
            if (size == null || !size.startsWith("image")) {
                Toast.makeText(this, "Invalid image selected", Toast.LENGTH_SHORT).show()
                return@let
            }
            
            currentPhotoUri = it.toString()
            binding.imagePreview.setImageURI(it)
            Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            android.util.Log.e("Gallery", "Error: ${e.message}", e)
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
        }
    }
}
```

**Cause B: ImageView Caching**
```kotlin
// Problem: ImageView shows old cached image
// Solution: Clear before loading new

binding.imagePreview.setImageBitmap(null)  // Clear
binding.imagePreview.setImageURI(uri)      // Load new
```

---

### 6. **Shared Photos Not Working**

#### Symptom:
- Tap "Share" button
- Share dialog appears but photo not included
- Only text shared

#### Root Causes & Fixes:

**Cause A: Photo URI Not Valid for Sharing**
```kotlin
// Problem: Sharing cache photo - may be deleted
// Solution: Use FileProvider for sharing

// In MainActivity.kt - shareCard()
private fun shareCard(card: Card) {
    try {
        val shareText = "Document Type: ${card.type}\n" +
                       "Number: ${card.number}\n" +
                       "Expiry: ${Date(card.expiryDate)}"
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            
            // Include photo if available
            if (card.photoUri?.isNotEmpty() == true) {
                try {
                    val photoUri = Uri.parse(card.photoUri)
                    putExtra(Intent.EXTRA_STREAM, photoUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } catch (e: Exception) {
                    android.util.Log.w("Share", "Photo not available: ${e.message}")
                }
            }
        }
        
        startActivity(Intent.createChooser(intent, "Share Document"))
    } catch (e: Exception) {
        android.util.Log.e("Share", "Error: ${e.message}", e)
        Toast.makeText(this, "Error sharing document", Toast.LENGTH_SHORT).show()
    }
}
```

---

### 7. **App Crashes When Opening Camera/Gallery**

#### Symptom:
- App force closes when tapping Camera/Gallery
- "Unfortunately, DocExpiry has stopped" dialog
- Crash in logcat

#### Root Causes & Fixes:

**Cause A: Missing Try-Catch**
```kotlin
// Problem: No error handling
binding.btnCamera.setOnClickListener {
    launchCamera()  // May crash if file creation fails
}

// Solution: Add error handling
binding.btnCamera.setOnClickListener {
    try {
        launchCamera()
    } catch (e: Exception) {
        android.util.Log.e("Camera", "Error: ${e.message}", e)
        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
```

**Cause B: Missing Permissions in Manifest**
```xml
<!-- Add to AndroidManifest.xml if missing -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

**Cause C: ActivityResultLauncher Not Registered**
```kotlin
// Problem: Launcher registered after onCreate ends
// Solution: Register in onCreate() BEFORE setting listeners

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddEditCardBinding.inflate(layoutInflater)
    
    // Register FIRST
    pickImageLauncher = registerForActivityResult(...) { ... }
    takePictureLauncher = registerForActivityResult(...) { ... }
    
    // Then set listeners
    binding.btnPick.setOnClickListener { ... }
    binding.btnCamera.setOnClickListener { ... }
}
```

#### Check Crash Log:
```bash
# In Android Studio Logcat
adb logcat | grep FATAL
# or filter by tag "AndroidRuntime"
```

---

### 8. **"Unable to Load Photo" After Clearing Cache**

#### Symptom:
- Was working fine
- User cleared app cache in Settings
- Photos no longer display

#### Root Causes & Fixes:

**Cause: Photos Stored in Cache Directory**
```
When user takes photo:
    └─ Saved to: /data/data/com.example.docexpiry/cache/photo_[timestamp].jpg

When user clears cache:
    └─ File deleted!
    └─ Database still has URI reference
    └─ But file doesn't exist = Glide shows placeholder
```

**Solution A: Tell Users Not to Clear Cache**
```
Inform users in FAQ:
"Clearing app cache will remove saved photos. 
Instead, select photos from your gallery."
```

**Solution B: Save to External Storage**
```kotlin
// Alternative approach (not current implementation):
val externalDir = File(getExternalFilesDir(null), "docexpiry_photos")
externalDir.mkdirs()
val photoFile = File(externalDir, "photo_${System.currentTimeMillis()}.jpg")
// Files in getExternalFilesDir() survive cache clear
```

**Solution C: Save Gallery Photos (Recommended)**
```kotlin
// Encourage users to select from gallery instead of taking photos
Toast.makeText(this, 
    "Tip: Selecting from gallery is more reliable than camera", 
    Toast.LENGTH_LONG).show()
```

#### Workaround for Users:
1. Tap Edit button on card with missing photo
2. Re-select photo from Gallery
3. Save (updates photo URI)

---

## 🔍 Diagnostic Checklist

Before reporting bug, check:

- [ ] CAMERA permission granted in Settings
- [ ] READ_EXTERNAL_STORAGE permission granted in Settings
- [ ] Photos selected (not null) in form
- [ ] FileProvider configured in AndroidManifest.xml
- [ ] file_paths.xml exists and configured
- [ ] Glide dependency in build.gradle (current version: 4.16.0)
- [ ] No app crashes in Logcat
- [ ] Device storage has space (> 100MB)
- [ ] No cache cleared recently (clears camera photos)
- [ ] Device time/date correct (for file timestamps)

---

## 📱 Test Scenarios

### Test 1: Add Document with Camera Photo
```
1. Launch app
2. Tap "+" FAB → AddEditCardActivity opens
3. Tap "📷 Camera" button
   ✓ Permission dialog shown (if not granted)
   ✓ Camera app launches
4. Take photo
   ✓ Photo preview shows in app
5. Fill form
6. Tap "Save Document"
   ✓ Toast: "Document saved successfully"
7. Check dashboard
   ✓ New card appears with photo
```

### Test 2: Add Document with Gallery Photo
```
1. Launch app
2. Tap "+" FAB → AddEditCardActivity opens
3. Tap "🖼️ Gallery" button
   ✓ Permission dialog shown (if not granted)
   ✓ Gallery/file picker opens
4. Select photo
   ✓ Photo preview shows in app
5. Fill form
6. Tap "Save Document"
   ✓ Toast: "Document saved successfully"
7. Check dashboard
   ✓ New card appears with photo
```

### Test 3: Edit Document Photo
```
1. Dashboard shows card with photo
2. Tap "✏️ Edit" button
3. Photo already displayed
4. Tap "📷 Camera" or "🖼️ Gallery" to change
   ✓ New photo loads
5. Tap "Save Document"
   ✓ Card photo updated in dashboard
```

### Test 4: Clear Cache
```
1. Dashboard shows cards with photos
2. Device Settings > Apps > DocExpiry > Storage > Clear Cache
3. Return to app
4. Cards show in dashboard
   ⚠️ Camera photos: May show placeholder
   ✓ Gallery photos: Should still display
```

---

## 📚 Reference Files

| File | Contains |
|------|----------|
| `AddEditCardActivity.kt` | Photo upload UI & logic |
| `activity_add_edit_card.xml` | Layout with camera/gallery buttons |
| `CardAdapter.kt` | Photo display in dashboard |
| `item_card.xml` | Card layout with ImageView |
| `AndroidManifest.xml` | Permissions & FileProvider |
| `res/xml/file_paths.xml` | FileProvider paths |
| `PermissionManager.kt` | Permission request logic |

---

**Last Updated**: November 2025  
**Fixes**: Photo display, camera/gallery access, sharing

