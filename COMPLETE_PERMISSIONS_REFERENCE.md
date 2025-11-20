# DocExpiry - Complete App Permissions & Features Reference

## 📋 Overview

This is a **complete reference guide** for all app features involving **permissions**, **notifications**, **photos/gallery**, and their interactions.

---

## 🔐 Permission Model

### Flow Chart

```
App Launch
    ↓
├─ Is user registered? (SharedPreferences check)
│  ├─ NO → RegistrationActivity
│  └─ YES → Proceed to MainActivity
│
├─ Request Permissions (if needed)
│  ├─ POST_NOTIFICATIONS (Android 13+)
│  ├─ CAMERA (for photo capture)
│  └─ READ_EXTERNAL_STORAGE (for gallery)
│
├─ Initialize Notification Manager
│  ├─ Create notification channels
│  └─ Schedule expiry notification checks
│
└─ Show Dashboard (MainActivity)
   ├─ Display all documents
   ├─ Show expiring alerts
   └─ Enable + button to add documents
```

### Permissions Summary

| Permission | Android | Purpose | When Requested |
|-----------|---------|---------|-----------------|
| `CAMERA` | 6+ | Take document photos | When user taps "Camera" button |
| `READ_EXTERNAL_STORAGE` | 6+ | Select from gallery | When user taps "Gallery" button |
| `POST_NOTIFICATIONS` | 13+ | Send expiry reminders | App startup (if Android 13+) |
| `SCHEDULE_EXACT_ALARM` | 12+ | Precise notification timing | Optional, for better timing |

### Permission Request Logic

```kotlin
// Location: permissions/PermissionManager.kt

class PermissionManager(private val activity: AppCompatActivity) {
    
    fun requestRequiredPermissions() {
        // CAMERA & READ_EXTERNAL_STORAGE: Runtime (Android 6+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestStoragePermission()   // READ_EXTERNAL_STORAGE
            requestCameraPermission()     // CAMERA
        }
        
        // POST_NOTIFICATIONS: Runtime (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()  // POST_NOTIFICATIONS
        }
    }
    
    private fun requestCameraPermission() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA
            )
        }
    }
    
    private fun requestStoragePermission() {
        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE
            )
        }
    }
    
    private fun requestNotificationPermission() {
        if (!hasPermission(Manifest.permission.POST_NOTIFICATIONS)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_NOTIFICATION
            )
        }
    }
    
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
```

---

## 📸 Photo/Gallery Feature Details

### Complete Workflow

```
┌─────────────────────────────────────────────────────────────┐
│                   ADD/EDIT DOCUMENT FLOW                    │
└─────────────────────────────────────────────────────────────┘

MainActivity (Dashboard)
         ↓ User taps "+" FAB
AddEditCardActivity (Form)
         ↓
  ┌──────────────────────────────────┐
  │   Step 1: Photo Section           │
  │                                  │
  │  📸 Photo Preview                │
  │  [160x160 dp image display]      │
  │                                  │
  │  [📷 Camera] [🖼️ Gallery]        │
  │                                  │
  └──────────────────────────────────┘
         ↓
  ┌──────────────────────────────────┐
  │   Step 2: Form Fields             │
  │                                  │
  │  • Document Type (Dropdown)      │
  │  • Authority (Auto-filled)       │
  │  • Document Number               │
  │  • Owner Name                    │
  │  • Issued Date                   │
  │  • Expiry Date                   │
  │  [Save Document Button]          │
  │                                  │
  └──────────────────────────────────┘
         ↓
    ┌─────────────────┐
    │  Photo Selected?│
    └────────┬────────┘
      YES ↙  ↖ NO
       ↓      ↓
    [OK]   [?]  ← Shows placeholder
             
After Save:
    ↓
Database stores:
  • Document data (type, number, etc.)
  • Photo URI (reference to photo)
    ↓
MainActivity updated
  • Card shows with photo
  • Uses Glide to load & cache
```

### Camera Photo Capture Details

**Sequence**:
1. User taps "📷 Camera"
2. Check CAMERA permission
   - If denied: Request permission
   - If granted: Continue
3. Create temp cache file: `/cache/photo_[timestamp].jpg`
4. Create content URI via FileProvider
5. Launch camera intent
6. User takes photo
7. Photo saved to cache file
8. Return to app with URI
9. Display in preview
10. Save document with photo URI

**Code Implementation**:
```kotlin
private fun launchCamera() {
    // Step 1: Create temp file
    val tempFile = File(cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    tempFile.createNewFile()
    
    // Step 2: Get content URI from FileProvider
    val photoUri = FileProvider.getUriForFile(
        this,
        "${packageName}.fileprovider",
        tempFile
    )
    
    // Step 3: Store for result
    cameraUri = photoUri
    
    // Step 4: Launch camera
    takePictureLauncher.launch(photoUri)
}

// Step 5: Handle result
takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
    if (success && cameraUri != null) {
        // Step 6: Save URI
        currentPhotoUri = cameraUri.toString()
        
        // Step 7: Show preview
        binding.imagePreview.setImageURI(cameraUri)
        
        // Step 8: Feedback
        Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show()
    }
}
```

### Gallery Photo Selection Details

**Sequence**:
1. User taps "🖼️ Gallery"
2. Check READ_EXTERNAL_STORAGE permission
   - If denied: Request permission
   - If granted: Continue
3. Launch file picker (GetContent contract)
4. Filter for image/* MIME type
5. User selects photo from gallery
6. Return with content URI
7. Display in preview
8. Save document with photo URI

**Code Implementation**:
```kotlin
pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    uri?.let {
        try {
            // Step 1: Validate MIME type
            val mimeType = contentResolver.getType(it)
            if (mimeType == null || !mimeType.startsWith("image")) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@let
            }
            
            // Step 2: Save URI
            currentPhotoUri = it.toString()
            
            // Step 3: Show preview
            binding.imagePreview.setImageURI(it)
            
            // Step 4: Feedback
            Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

// Launch from button
binding.btnPick.setOnClickListener {
    pickImageLauncher.launch("image/*")
}
```

### Photo Display on Dashboard

**How Photos Are Loaded**:
```kotlin
// In CardAdapter.kt - bind() function
fun bind(card: Card) {
    // Load photo with Glide
    Glide.with(context)
        .load(card.photoUri)              // Load from URI
        .placeholder(R.drawable.placeholder_user)  // While loading
        .error(R.drawable.placeholder_user)        // If error
        .diskCacheStrategy(DiskCacheStrategy.ALL)  // Cache to disk
        .apply(
            RequestOptions.bitmapTransform(
                CenterCrop(),              // Crop to fit
                RoundedCorners(12)         // Round corners
            )
        )
        .into(binding.ivPhoto)             // Display
}
```

**Glide Caching Strategy**:
- **Memory Cache**: Recent photos in RAM for instant display
- **Disk Cache**: Photos cached on device for app relaunch
- **Network**: Not applicable (local photos only)

**Photo Display Features**:
- ✅ Rounded corners (12dp radius)
- ✅ Circular for certain doc types
- ✅ Placeholder while loading
- ✅ Error placeholder if photo missing
- ✅ 200dp height on dashboard
- ✅ 160dp height in editor

---

## 🔔 Notifications System Details

### Architecture

```
┌───────────────────────────────────────────────────────┐
│              NOTIFICATIONS SYSTEM                      │
└───────────────────────────────────────────────────────┘

User's Device
  └─ Every Day:
     └─ WorkManager runs scheduled job
        └─ NotificationManager checks all documents
           ├─ Get all cards from database
           ├─ For each card:
           │  └─ Calculate daysToExpire
           │     ├─ If 0-30 days → Create notification
           │     └─ If > 30 days → Skip
           └─ Display notifications
              └─ User sees in status bar
                 └─ Tap to open CardDetailActivity
```

### Notification Channels

**Android 8.0+** (API 26+) requires notification channels:

```kotlin
// In NotificationManager.kt
fun createNotificationChannels() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "expiry_channel",
            "Document Expiry Alerts",
            NotificationManager.IMPORTANCE_HIGH  // Causes sound/vibration
        ).apply {
            description = "Notifications for documents expiring soon"
            enableVibration(true)
            setShowBadge(true)
        }
        
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
```

### Scheduling Notifications

**How WorkManager Schedules**:
```kotlin
// In NotificationManager.kt
fun scheduleExpiryNotifications() {
    val dailyCheckRequest = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(
        1,                          // Repeat every
        TimeUnit.DAYS               // TimeUnit
    )
        .setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(true)  // Not low battery
                .setRequiresDeviceIdle(true)     // Only when idle
                .build()
        )
        .build()
    
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "expiry_check",
        ExistingPeriodicWorkPolicy.KEEP,
        dailyCheckRequest
    )
}
```

### Notification Persistence

**Problem**: Notifications lost on device reboot

**Solution**: Boot Receiver

```xml
<!-- In AndroidManifest.xml -->
<receiver
    android:name=".notifications.NotificationBootReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
```

```kotlin
// In NotificationBootReceiver.kt
class NotificationBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Device restarted, reschedule notifications
            NotificationManager.getInstance(context!!).scheduleExpiryNotifications()
        }
    }
}
```

### Notification Example

When document expires in 15 days:

```
┌─────────────────────────────┐
│         NOTIFICATION        │
├─────────────────────────────┤
│ 📄 Document Expiring Soon   │  ← Title
│                             │
│ Aadhaar expires on          │  ← Body
│ 15 Dec 2025                 │
│                             │
│ 🔔                          │  ← Icon
└─────────────────────────────┘
           ↓ User taps
    CardDetailActivity opens
    └─ Shows full document details
```

---

## 🔒 Permission Handling Scenarios

### Scenario 1: First Time Launch

```
1. User launches app for first time
   ↓
2. LoginActivity shown
   ↓
3. User registers
   ↓
4. MainActivity launched
   ↓
5. PermissionManager.requestRequiredPermissions() called
   ↓
6. Shows permission dialogs:
   ✓ "Allow DocExpiry to use your camera?"
   ✓ "Allow DocExpiry to access your photos?"
   ✓ "Allow DocExpiry to send you notifications?" (Android 13+)
   ↓
7. User grants or denies each
   ↓
8. Dashboard displays
   └─ Can now use all features (if granted)
```

### Scenario 2: User Denies Permission

```
1. User taps "📷 Camera"
   ↓
2. Check: Does app have CAMERA permission?
   ├─ YES → Launch camera (skip to 6)
   └─ NO → Continue to 3
   ↓
3. Show permission dialog:
   "Allow DocExpiry to use your camera?"
   ├─ Grant
   ├─ Deny
   └─ Don't ask again
   ↓
4. User taps "Deny"
   ↓
5. Show toast: "Camera access needed"
   └─ Camera doesn't launch
   ↓
6. User still wants to take photo?
   ├─ Option A: Tap camera again → Shows permission dialog again
   ├─ Option B: Manual enable in Settings
   │  Settings > Apps > DocExpiry > Permissions > Camera > Allow
   └─ Option C: Take photo differently (use gallery instead)
```

### Scenario 3: User Denied "Don't Ask Again"

```
User previously denied with "Don't ask again"
   ↓
User taps camera button
   ↓
Permission dialog NOT shown again
   ↓
Show toast: "Camera access needed"
   ↓
User must manually enable in Settings:
  1. Device Settings
  2. Apps > DocExpiry
  3. Permissions > Camera
  4. Change Deny → Allow
  5. Return to app
```

### Scenario 4: Permission Revoked Manually

```
User in Settings:
  1. Apps > DocExpiry
  2. Permissions > Camera
  3. Taps toggle OFF
   ↓
Returns to app
   ↓
User taps camera button
   ↓
Permission check fails
   ↓
Show permission dialog or toast
```

---

## 🧪 Testing Permissions & Features

### Test 1: Verify All Permissions Requested

```kotlin
// In MainActivity.onCreate()
PermissionManager(this).requestRequiredPermissions()

// Check logcat for:
// - CAMERA permission request
// - READ_EXTERNAL_STORAGE permission request
// - POST_NOTIFICATIONS permission request (Android 13+)
```

### Test 2: Test Camera Permission Denial

```
1. Launch app
2. When asked: "Allow camera?" → Deny
3. Tap "+" FAB
4. In AddEditCardActivity, tap "📷 Camera"
5. Expected: Show permission dialog again
   (unless "Don't ask again" was tapped before)
```

### Test 3: Test Gallery Permission Denial

```
1. Launch app
2. When asked: "Allow photos?" → Deny
3. Tap "+" FAB
4. In AddEditCardActivity, tap "🖼️ Gallery"
5. Expected: Show permission dialog again
```

### Test 4: Test Notification Permission (Android 13+)

```
1. Launch app
2. When asked: "Allow notifications?" → Deny
3. Add document with expiry < 30 days
4. Wait 1 day
5. Expected: No notification appears
6. Enable permission in Settings
7. Expected: Notification appears next check
```

### Test 5: Test Photo Persistence

```
1. Add document with camera photo
2. Go to dashboard
   ✓ Photo displays
3. Tap edit
   ✓ Photo still displayed in preview
4. Close without saving
5. Go back to dashboard
   ✓ Photo still displays
6. Kill app (swipe from recent)
7. Reopen app
   ✓ Photo still displays
8. Device restart
   ✓ Photo still displays (if from gallery)
   ? Photo may be gone (if from camera cache)
```

### Test 6: Test Notifications Schedule

```
1. Add 3 documents:
   - Expiring in 15 days
   - Expiring in 40 days
   - Expiring in 3 days
2. Check notification status
3. Expected: Only 15-day and 3-day documents notify
4. Wait 1 day (or advance device clock)
5. Expected: Notifications updated
```

---

## 📊 Data Flow Diagram

### Complete App Data Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    USER INTERACTIONS                            │
├─────────────────────────────────────────────────────────────────┤

1. App Launch
   ↓
2. LoginActivity
   ├─ Sign In
   ├─ Sign Up → RegistrationActivity
   ↓
3. MainActivity (Dashboard)
   ├─ Shows all documents
   ├─ Shows expiry alerts
   │
   ├─ Tap FAB "+"
   │  ↓
   │  AddEditCardActivity
   │  ├─ Tap "📷 Camera"
   │  │  └─ Check CAMERA permission → Request if needed
   │  │     └─ Launch camera
   │  │        └─ Take photo
   │  │           └─ Save to cache
   │  │              └─ Display preview
   │  │
   │  ├─ Tap "🖼️ Gallery"
   │  │  └─ Check READ_EXTERNAL_STORAGE permission → Request if needed
   │  │     └─ Launch gallery picker
   │  │        └─ Select photo
   │  │           └─ Display preview
   │  │
   │  ├─ Fill form fields
   │  │  ├─ Document type
   │  │  ├─ Authority
   │  │  ├─ Number
   │  │  ├─ Name
   │  │  ├─ Issued date
   │  │  └─ Expiry date
   │  │
   │  └─ Tap Save
   │     └─ Validate data
   │        └─ Save to database
   │           ├─ Document data
   │           ├─ Photo URI
   │           └─ Return to MainActivity
   │
   ├─ Dashboard card click
   │  ├─ View → CardDetailActivity (full details)
   │  ├─ Edit → AddEditCardActivity (edit form)
   │  ├─ Share
   │  │  └─ Check photoUri valid
   │  │     └─ Share via intent (include photo)
   │  │        └─ User's messaging app
   │  │
   │  └─ Delete → Remove from database
   │
   └─ Daily background task
      ├─ WorkManager runs
      │  └─ Check all documents
      │     ├─ Calculate expiry date
      │     │  └─ < 30 days remaining?
      │     │     ├─ YES → Create notification
      │     │     │   └─ Check POST_NOTIFICATIONS permission (Android 13+)
      │     │     │      └─ Send notification
      │     │     │         └─ User sees in status bar
      │     │     │
      │     │     └─ NO → Skip
```

---

## 🚨 Error Handling

### Common Errors & Recovery

| Error | Cause | Fix |
|-------|-------|-----|
| `FileProvider: ... not declared` | Provider not in manifest | Add provider to AndroidManifest.xml |
| `Permission denied: ... CAMERA` | Permission not granted | Request permission via PermissionManager |
| `Could not decompress image` | Glide can't load photo | Set error placeholder |
| `Unable to open camera` | Permission/hardware issue | Check device has camera + permission |
| `Gallery app not available` | No file picker app | Handle gracefully with toast |
| `WorkManager not scheduling` | Device restricted | Check battery saver settings |
| `Notifications not showing` | POST_NOTIFICATIONS denied | Show rationale dialog + request again |

---

## 📚 File Directory Structure

```
app/
├── src/
│  └── main/
│     ├── AndroidManifest.xml         ← Permissions & components
│     │
│     ├── java/com/example/docexpiry/
│     │  ├── LoginActivity.kt         ← Login screen
│     │  ├── RegistrationActivity.kt  ← User registration
│     │  ├── MainActivity.kt          ← Dashboard
│     │  ├── AddEditCardActivity.kt   ← Photo upload + form
│     │  ├── CardDetailActivity.kt    ← Document details
│     │  │
│     │  ├── CardAdapter.kt           ← Photo display in list
│     │  ├── CardListViewModel.kt     ← Data management
│     │  │
│     │  ├── permissions/
│     │  │  └── PermissionManager.kt  ← Permission requests
│     │  │
│     │  ├── notifications/
│     │  │  ├── NotificationManager.kt          ← Schedule notifications
│     │  │  ├── NotificationBootReceiver.kt    ← Persist after reboot
│     │  │  ├── ExpiryNotificationReceiver.kt  ← Receive broadcasts
│     │  │  └── NotificationService.kt         ← Background service
│     │  │
│     │  └── data/
│     │     ├── AppDatabase.kt        ← Room database
│     │     ├── CardDao.kt            ← Database queries
│     │     └── Card.kt               ← Data model
│     │
│     └── res/
│        ├── layout/
│        │  ├── activity_login.xml
│        │  ├── activity_registration.xml
│        │  ├── activity_main.xml
│        │  ├── activity_add_edit_card.xml  ← Photo upload UI
│        │  ├── activity_card_detail.xml
│        │  └── item_card.xml              ← Dashboard card layout
│        │
│        ├── xml/
│        │  └── file_paths.xml        ← FileProvider configuration
│        │
│        ├── drawable/
│        │  └── placeholder_user.png  ← Default photo
│        │
│        └── values/
│           └── colors.xml
│
└── build.gradle.kts                 ← Dependencies (Glide, etc.)
```

---

**Last Updated**: November 2025  
**Version**: 1.2 Complete  
**Status**: ✅ All Features Functional

