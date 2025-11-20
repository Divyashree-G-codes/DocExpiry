# DocExpiry - Notifications & Permissions Guide

## 📱 Overview

This guide covers the **notifications system**, **permission handling**, and **photo/gallery features** in the DocExpiry app, including how they work together and troubleshooting common issues.

---

## 🔔 Notifications System

### 1. **Expiry Notifications**

The app automatically notifies users about documents expiring within **30 days**.

#### How It Works:

**Location**: `notifications/NotificationManager.kt`

```
App Launch (MainActivity)
    ↓
NotificationManager.scheduleExpiryNotifications()
    ├─ Checks all documents in database
    ├─ Calculates days until expiry
    ├─ Schedules work requests for each card
    └─ WorkManager runs checks daily
        ↓
    Document expiring < 30 days?
        ├─ YES → Send notification
        │   ├─ Title: "Document Expiring Soon"
        │   ├─ Body: "[Document Type] expires on [Date]"
        │   └─ Priority: HIGH
        └─ NO → Skip notification
```

#### Notification Features:

- ✅ **Automatic Scheduling**: Checks run daily via WorkManager
- ✅ **Smart Timing**: Only notifies about documents expiring within 30 days
- ✅ **Persistent**: Notifications survive device reboot
- ✅ **Non-intrusive**: Uses notification channel with appropriate importance level
- ✅ **Clickable**: Tap notification to open document details

#### Notification Triggers:

1. **App Launch**: Initial schedule created
2. **Daily**: WorkManager checks all documents
3. **Device Boot**: Receiver re-schedules if lost (`NotificationBootReceiver`)
4. **After 30 days**: Automatic removal (expiry passed)

### 2. **Notification Components**

#### `NotificationManager.kt`
- Manages notification scheduling
- Defines notification channels
- Creates WorkManager requests
- Handles notification creation

#### `NotificationBootReceiver.kt`
- Listens for `android.intent.action.BOOT_COMPLETED`
- Re-schedules notifications after device restart
- Ensures persistence across reboots

#### `ExpiryNotificationReceiver.kt`
- Receives scheduled notification broadcasts
- Triggers notification display
- Handles background work

#### `NotificationService.kt`
- Background service for notification operations
- Not blocking main thread
- Optimized for battery usage

### 3. **Permission Requirements for Notifications**

#### Android 13+ (API 33+):
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

#### How Permission is Requested:

**Location**: `permissions/PermissionManager.kt`

```kotlin
// In PermissionManager.kt
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requestPermissions(
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        NOTIFICATION_PERMISSION_REQUEST
    )
}
```

**User sees**: "Allow notifications?" dialog on first launch

#### What Happens If Permission Denied:
- ❌ Notifications won't be sent
- ✅ App still works normally
- ✅ User can enable notifications in Settings later
- 📍 **Settings Path**: Settings > Apps > DocExpiry > Notifications

---

## 📸 Permissions System

### 1. **Camera Permission**

Used for **taking photos** of documents directly.

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

#### Flow:
```
User taps "📷 Camera" button
    ↓
PermissionManager checks permission status
    ├─ GRANTED → Launch camera immediately
    └─ NOT GRANTED → Show permission dialog
        ↓
    User grants? 
        ├─ YES → Launch camera
        └─ NO → Show "Camera access needed" toast
```

#### Permission Request Code:
```kotlin
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
    == PackageManager.PERMISSION_GRANTED) {
    launchCamera()  // Proceed
} else {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST)
}
```

#### What App Does With Camera:
1. Opens device camera app
2. User takes photo
3. Saves to temporary cache file
4. Returns to app with photo URI
5. Displays in preview
6. Saves with document when saved

### 2. **Storage Permission (Gallery)**

Used for **browsing and selecting** photos from gallery.

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

#### Flow:
```
User taps "🖼️ Gallery" button
    ↓
PermissionManager checks permission status
    ├─ GRANTED → Open gallery picker
    └─ NOT GRANTED → Show permission dialog
        ↓
    User grants?
        ├─ YES → Open gallery/file picker
        └─ NO → Show "Gallery access needed" toast
```

#### Storage Strategy (Android 11+):
- **Scoped Storage**: App can only access photos user explicitly selects
- **No broad access** to all device storage
- **Safer** for user privacy
- **Better** for future Android versions

### 3. **Permission Management Class**

**Location**: `permissions/PermissionManager.kt`

```kotlin
class PermissionManager(private val activity: AppCompatActivity) {
    
    fun requestPermissions() {
        // Requests all necessary permissions
        // Handles Android version differences
        // Manages permission results
    }
    
    fun hasPermission(permission: String): Boolean {
        // Checks if permission is granted
    }
    
    fun shouldShowRationale(permission: String): Boolean {
        // Checks if should show "why we need this" dialog
    }
}
```

### 4. **Handling Permission Denial**

#### Camera Denied:
- User sees toast: "Camera access needed"
- Can tap "Camera" button again to re-request
- Can enable in Settings: `Settings > Apps > DocExpiry > Permissions`

#### Storage Denied:
- User sees toast: "Gallery access needed"
- Can tap "Gallery" button again to re-request
- Can enable in Settings: `Settings > Apps > DocExpiry > Permissions`

#### Notifications Denied (Android 13+):
- Notifications won't appear
- App functions normally
- User can enable in Settings

### 5. **AndroidManifest.xml Permissions**

```xml
<!-- Camera access for taking photos -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- Read external storage for gallery selection -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

<!-- Post notifications (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- Optional: Schedule exact alarms for notifications -->
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
```

---

## 📷 Gallery & Photo Features

### 1. **Photo Upload Workflow**

**Location**: `AddEditCardActivity.kt` + `activity_add_edit_card.xml`

```
Add/Edit Document Screen
    ↓
Photo Section (Top Card)
    ├─ Shows current photo or placeholder
    ├─ Camera button (📷)
    │   └─ Launches camera intent
    └─ Gallery button (🖼️)
        └─ Opens file picker for images
    ↓
Form Section
    ├─ Document type
    ├─ Authority
    ├─ Document number
    ├─ Name
    ├─ Issued date
    └─ Expiry date
    ↓
Save Button
    └─ Saves document WITH photo URI
```

### 2. **Camera Photo Capture**

#### How It Works:

1. **User taps "📷 Camera"**:
   - Permission check
   - If denied, show permission dialog

2. **Camera launches**:
   - Creates temporary file in cache: `photo_[timestamp].jpg`
   - FileProvider converts to content URI
   - Camera intent receives URI

3. **User takes photo**:
   - Photo saved to cache file
   - URI returned to app

4. **Preview updates**:
   - `imagePreview` ImageView shows photo
   - Photo URI stored in memory: `currentPhotoUri`
   - User can retake if unsatisfied

5. **On Save**:
   - Photo URI persisted to database
   - Associated with document

#### Code Flow:

```kotlin
// Launch camera button click
binding.btnCamera.setOnClickListener {
    if (hasPermission()) {
        launchCamera()  // Opens camera
    } else {
        requestPermission()  // Ask for permission
    }
}

// Create temp file and launch
private fun launchCamera() {
    val f = File(cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    f.createNewFile()
    val uri = FileProvider.getUriForFile(this, "${applicationId}.fileprovider", f)
    cameraUri = uri
    takePictureLauncher.launch(uri)  // Launch camera
}

// Handle camera result
takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
    if (success && cameraUri != null) {
        currentPhotoUri = cameraUri.toString()  // Save URI
        binding.imagePreview.setImageURI(cameraUri)  // Show preview
        Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show()
    }
}
```

### 3. **Gallery Photo Selection**

#### How It Works:

1. **User taps "🖼️ Gallery"**:
   - Permission check
   - If denied, show permission dialog

2. **File picker opens**:
   - Filters for image/* MIME type
   - Shows user's photo gallery
   - User selects photo

3. **Photo returned**:
   - Content URI provided by Android
   - Displayed in preview

4. **On Save**:
   - Photo URI persisted to database

#### Code Flow:

```kotlin
// Launch gallery button click
binding.btnPick.setOnClickListener {
    if (hasPermission()) {
        pickImageLauncher.launch("image/*")  // Open gallery
    } else {
        requestPermission()  // Ask for permission
    }
}

// Handle gallery selection
pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    uri?.let {
        currentPhotoUri = it.toString()  // Save URI
        binding.imagePreview.setImageURI(it)  // Show preview
        Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show()
    }
}
```

### 4. **Photo Display in Dashboard**

**Location**: `CardAdapter.kt` + `item_card.xml`

#### How Photos Are Displayed:

```
Dashboard (MainActivity)
    ↓
RecyclerView with CardAdapter
    ↓
For each card:
    ├─ Load photo with Glide library
    ├─ Apply rounded corners (12dp radius)
    ├─ Show placeholder if photo missing
    ├─ Cache for performance
    └─ Display in 200dp height section
        ↓
        Card-type specific styling:
        ├─ Aadhaar: Circular crop
        ├─ PAN: Rounded corners
        ├─ Others: Custom shapes
```

#### Photo Loading Code:

```kotlin
// Load photo with Glide
val glideRequest = Glide.with(binding.root)
    .load(card.photoUri)  // Load from URI
    .placeholder(R.drawable.placeholder_user)  // While loading
    .error(R.drawable.placeholder_user)  // If error
    .diskCacheStrategy(DiskCacheStrategy.ALL)  // Cache

// Apply card-type specific shape
if (template.isCircular) {
    glideRequest.apply(RequestOptions.circleCropTransform()).into(binding.ivPhoto)
} else {
    glideRequest.apply(RequestOptions.bitmapTransform(
        CenterCrop(),
        RoundedCorners(12)
    )).into(binding.ivPhoto)
}
```

#### Glide Features:
- ✅ **Disk Cache**: Photos cached for fast loading
- ✅ **Memory Cache**: Recent photos kept in RAM
- ✅ **Placeholder**: Shows placeholder while loading
- ✅ **Error Handling**: Shows placeholder if photo missing
- ✅ **Transformations**: Rounded corners, circular crop, centering

### 5. **Photo File Storage**

#### Where Photos Are Stored:

1. **Temporary (during capture)**:
   - Location: `App Cache Directory`
   - Path: `/data/data/com.example.docexpiry/cache/`
   - Files: `photo_[timestamp].jpg`
   - Auto-deleted: When app cleared or cache cleared

2. **Persistent (when saved)**:
   - **Stored as URI**, not copied
   - Can be:
     - **Gallery photo**: `/storage/emulated/0/DCIM/Camera/...` (user's gallery)
     - **Cache photo**: `/data/data/com.example.docexpiry/cache/...` (app cache)
   - URI saved in database
   - Accessible via content provider

#### FileProvider Configuration:

**Location**: `AndroidManifest.xml`

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

**Location**: `res/xml/file_paths.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- Cache directory for temporary camera photos -->
    <cache-path name="cache" path="/" />
    
    <!-- External files directory (optional) -->
    <external-files-path name="external_files" path="." />
</paths>
```

---

## 🐛 Troubleshooting

### Issue: Photos Not Showing on Cards

**Cause**: Photo URI is null or invalid

**Solution**:
```kotlin
// Check if photoUri is valid before loading
if (card.photoUri != null && card.photoUri.isNotEmpty()) {
    Glide.with(context)
        .load(card.photoUri)
        .into(imageView)
} else {
    // Show placeholder
    imageView.setImageResource(R.drawable.placeholder_user)
}
```

### Issue: "Camera access needed" Toast

**Cause**: CAMERA permission not granted

**Solution**:
1. Tap "Camera" button again to show permission dialog
2. Grant permission in dialog
3. Or manually enable: Settings > Apps > DocExpiry > Permissions

### Issue: "Gallery access needed" Toast

**Cause**: READ_EXTERNAL_STORAGE permission not granted

**Solution**:
1. Tap "Gallery" button again to show permission dialog
2. Grant permission in dialog
3. Or manually enable: Settings > Apps > DocExpiry > Permissions

### Issue: Notifications Not Appearing

**Causes**:
1. POST_NOTIFICATIONS permission denied (Android 13+)
2. Notification channel not created
3. WorkManager disabled in device settings

**Solutions**:

a) **Enable notification permission**:
   - Settings > Apps > DocExpiry > Notifications > Allow

b) **Check WorkManager settings**:
   - Settings > Apps > DocExpiry > Battery usage > Unrestricted

c) **Force reschedule**:
   - Clear app cache: Settings > Apps > DocExpiry > Storage > Clear Cache
   - Restart app

### Issue: Photo URI Invalid After Clearing Cache

**Cause**: Camera photos stored in cache; cache cleared

**Solution**:
- Re-take or re-select photo from gallery
- Consider saving photos to external storage instead

### Issue: Permission Dialog Not Showing

**Cause**: Permission already granted or denied

**Solution**:
1. If granted: No dialog needed (permission works)
2. If denied: Manual enable in Settings

---

## 🔒 Security & Privacy

### 1. **Permission Scoping**

- **No broad storage access**: App can only access photos user selects
- **Content URIs**: Photos identified by URI, not file path
- **Safer for users**: Less data exposed

### 2. **Photo Privacy**

- Photos stored locally on device
- Not uploaded to server (by default)
- Deleted when document deleted
- Survives app uninstall (if in gallery)

### 3. **Notification Privacy**

- Notifications show only document type and expiry date
- No sensitive information exposed
- Can be disabled by user

### 4. **Recommended Best Practices**

✅ **Do**:
- Always check permission before accessing camera/gallery
- Handle permission denial gracefully
- Use FileProvider for camera photos
- Validate photo URIs before loading

❌ **Don't**:
- Hardcode permission grants
- Assume permission always available
- Store sensitive info in notification text
- Access files without proper permissions

---

## 📝 Permission Request Flow

### First Time User:

```
App Launch
    ↓
Check if permission needed?
    ├─ YES, Android 13+ →
    │   ├─ Show: "Allow DocExpiry to post notifications?"
    │   └─ User grants or denies
    │
    └─ User adds document →
        ├─ Check CAMERA permission
        │   ├─ NOT GRANTED → "Allow camera access?" dialog
        │   └─ Check READ_EXTERNAL_STORAGE
        │       ├─ NOT GRANTED → "Allow gallery access?" dialog
        │       └─ All set!
```

### Returning User (After Denial):

```
User taps Camera button
    ↓
Check CAMERA permission
    ├─ GRANTED → Launch camera
    └─ NOT GRANTED → Show "Allow camera access?" dialog
        ├─ User grants → Launch camera
        └─ User denies → Show toast "Camera access needed"
```

---

## 📚 Code Files Reference

| File | Purpose |
|------|---------|
| `notifications/NotificationManager.kt` | Schedules and manages notifications |
| `notifications/NotificationBootReceiver.kt` | Reschedules after device reboot |
| `notifications/ExpiryNotificationReceiver.kt` | Receives & displays notifications |
| `notifications/NotificationService.kt` | Background notification service |
| `permissions/PermissionManager.kt` | Handles all permission requests |
| `AddEditCardActivity.kt` | Camera & gallery photo upload UI |
| `CardAdapter.kt` | Photo display in dashboard cards |
| `AndroidManifest.xml` | Declares permissions & components |
| `res/xml/file_paths.xml` | FileProvider paths configuration |

---

## 🚀 Quick Reference

### Request Permission:
```kotlin
PermissionManager(this).requestPermissions()
```

### Check Permission:
```kotlin
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
    == PackageManager.PERMISSION_GRANTED) {
    // Permission granted
}
```

### Take Photo:
```kotlin
binding.btnCamera.setOnClickListener {
    launchCamera()  // Or with permission check
}
```

### Select from Gallery:
```kotlin
binding.btnPick.setOnClickListener {
    pickImageLauncher.launch("image/*")
}
```

### Load Photo with Glide:
```kotlin
Glide.with(context)
    .load(card.photoUri)
    .placeholder(R.drawable.placeholder_user)
    .error(R.drawable.placeholder_user)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .into(imageView)
```

### Schedule Notifications:
```kotlin
val notificationManager = NotificationManager.getInstance(this)
notificationManager.scheduleExpiryNotifications()
```

---

**Last Updated**: November 2025  
**Status**: ✅ Complete & Functional

