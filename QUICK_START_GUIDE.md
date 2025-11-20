# DocExpiry - Quick Start Reference

## 🎯 What's Included

### ✅ Notifications System
- **Daily expiry checks** for all documents
- **Automatic alerts** for documents expiring within 30 days
- **Persistent notifications** that survive device reboot
- **Smart scheduling** via WorkManager
- **Requires**: POST_NOTIFICATIONS permission (Android 13+)

### ✅ Permissions Handling
- **Runtime permissions** requested at appropriate times
- **CAMERA** - for taking document photos
- **READ_EXTERNAL_STORAGE** - for selecting from gallery
- **POST_NOTIFICATIONS** - for sending notifications
- **Graceful handling** of permission denials

### ✅ Photo & Gallery Features
- **Camera capture** - take photo directly from device camera
- **Gallery selection** - pick existing photos
- **Photo preview** - see selected photo before saving
- **Dashboard display** - photos shown on each document card
- **Glide caching** - fast loading with memory/disk cache
- **Persistent storage** - photos associated with documents

---

## 📱 How Permissions Work

### Camera Permission
```
User taps "📷 Camera" button
    ↓
App checks: Do we have CAMERA permission?
    ├─ YES → Open camera immediately
    └─ NO → Show "Allow camera access?" dialog
            ├─ Grant → Open camera
            └─ Deny → Show toast "Camera access needed"
```

**Enable Later**: Settings > Apps > DocExpiry > Permissions > Camera

### Gallery Permission
```
User taps "🖼️ Gallery" button
    ↓
App checks: Do we have READ_EXTERNAL_STORAGE permission?
    ├─ YES → Open gallery picker
    └─ NO → Show "Allow gallery access?" dialog
            ├─ Grant → Open gallery picker
            └─ Deny → Show toast "Gallery access needed"
```

**Enable Later**: Settings > Apps > DocExpiry > Permissions > Photos

### Notification Permission (Android 13+)
```
App launch
    ↓
If Android 13+:
    ├─ Check POST_NOTIFICATIONS permission
    └─ If not granted → Show "Allow notifications?" dialog
```

**Enable Later**: Settings > Apps > DocExpiry > Notifications

---

## 📸 Photo Features

### Adding Photo to Document

**Option 1: Camera**
1. Tap "+" FAB in dashboard
2. Tap "📷 Camera" button
3. Grant camera permission (if needed)
4. Take photo with camera
5. Photo displayed in preview
6. Fill rest of form
7. Tap "Save Document"

**Option 2: Gallery**
1. Tap "+" FAB in dashboard
2. Tap "🖼️ Gallery" button
3. Grant gallery permission (if needed)
4. Select photo from gallery
5. Photo displayed in preview
6. Fill rest of form
7. Tap "Save Document"

### Viewing Photos on Dashboard
- Each card shows document photo
- Photo displayed with rounded corners
- Placeholder shown if no photo selected
- Photos cached for fast loading

### Changing Document Photo
1. In dashboard, tap "✏️ Edit" on card
2. Photo currently shown in preview
3. Tap "📷 Camera" or "🖼️ Gallery" to change
4. Select/capture new photo
5. Tap "Save Document"

---

## 🔔 Notifications

### How They Work
1. **App launch** → Starts daily expiry checks
2. **Every day** → WorkManager checks all documents
3. **Found expiring?** → If expiring within 30 days
4. **Send notification** → Status bar notification appears
5. **User taps** → Opens document details

### What You'll See
```
📄 Document Expiring Soon
Aadhaar expires on 15 Dec 2025
```

### Disable Notifications
- Settings > Apps > DocExpiry > Notifications > Off

---

## 🐛 Troubleshooting

### Photos Not Showing
- Check if permission granted: Settings > Apps > DocExpiry > Permissions
- Clear app cache: Settings > Apps > DocExpiry > Storage > Clear Cache
- Take/select photo again

### Camera Not Opening
- Grant CAMERA permission in Settings
- Check device has working camera
- Restart app

### Gallery Not Opening
- Grant READ_EXTERNAL_STORAGE permission in Settings
- Make sure device has photos stored
- Restart app

### Notifications Not Appearing
- Grant POST_NOTIFICATIONS permission (Android 13+)
- Check Settings > Apps > DocExpiry > Notifications > ON
- Restart app
- Add document expiring within 30 days

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `NOTIFICATIONS_PERMISSIONS_GUIDE.md` | Detailed notifications & permissions guide |
| `GALLERY_PHOTO_FIXES.md` | Photo/gallery troubleshooting & fixes |
| `COMPLETE_PERMISSIONS_REFERENCE.md` | Complete reference with data flows |
| `PHOTO_AND_GALLERY_FEATURES.md` | Photo features overview |
| `LOGIN_AND_PHOTO_GUIDE.md` | Login & photo setup guide |

---

## 🎨 UI Components

### Camera/Gallery Buttons
- Location: Add/Edit Document screen
- **📷 Camera**: Tap to open camera app
- **🖼️ Gallery**: Tap to open photo gallery

### Photo Preview
- 160×160 dp square image
- Rounded corners
- Placeholder shown if empty
- Updates immediately after selection

### Dashboard Cards
- Photo displayed at top
- 200dp height
- Document info below
- Buttons: View, Edit, Share, Delete

---

## ⚙️ System Requirements

### Minimum
- Android 6.0 (API 23)
- Storage: 100MB free
- RAM: 2GB

### Recommended
- Android 10.0+ (API 29+)
- Storage: 500MB free
- RAM: 4GB+

### For Best Experience
- Android 13+ (API 33+)
- Modern device (2020+)
- Good internet for initial sync

---

## 🔒 Privacy & Security

- ✅ Photos stored locally on device
- ✅ No cloud upload (by default)
- ✅ Permissions scoped (only access what needed)
- ✅ Content URIs used (not full file paths)
- ✅ Deleted with document
- ✅ Survives app uninstall (if in gallery)

---

## 📞 Support

### Check These Files First
1. `NOTIFICATIONS_PERMISSIONS_GUIDE.md` - Detailed info
2. `GALLERY_PHOTO_FIXES.md` - Common issues
3. `COMPLETE_PERMISSIONS_REFERENCE.md` - Advanced info

### Common Issues
- **"Camera access needed"** → Grant CAMERA permission
- **"Gallery access needed"** → Grant READ_EXTERNAL_STORAGE permission
- **Photos not showing** → Select photo with camera or gallery
- **No notifications** → Grant POST_NOTIFICATIONS permission
- **Crashes on launch** → Check Android version compatibility

---

## 🚀 Quick Tips

1. **First Time?**
   - Grant all permissions when asked
   - Ensures all features work smoothly

2. **Taking Photos?**
   - Use good lighting for clear photos
   - Keep document flat and centered
   - Tap camera again if first attempt fails

3. **Selecting from Gallery?**
   - Make sure photos are .jpg or .png
   - Avoid very large files (>10MB)
   - Photos appear immediately after select

4. **Managing Photos?**
   - Clear cache monthly to free space
   - Edit to re-select if photo deleted
   - Use gallery photos for permanent storage

5. **Notifications?**
   - Check 1 day after adding document
   - Appears for documents expiring < 30 days
   - Tap notification to view document

---

**Last Updated**: November 2025  
**For Full Details**: See other documentation files  
**Status**: ✅ Ready to Use

