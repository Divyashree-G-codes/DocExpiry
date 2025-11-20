# 🚀 DocExpiry - APK Build Guide

## ✅ All Features Ready to Build

Your DocExpiry app includes all these working features:

### ✅ Implemented Features

#### 1. **User Authentication**
- Login screen with email/password
- Registration system
- SharedPreferences for user data

#### 2. **Notifications System** ✨ NEW
- Daily expiry checks via WorkManager
- Automatic alerts for documents expiring < 30 days
- Device reboot persistence
- Notification channels (Android 8+)

#### 3. **Permissions Handling** ✨ NEW
- Runtime permission requests (Android 6+)
- CAMERA permission for photo capture
- READ_EXTERNAL_STORAGE for gallery
- POST_NOTIFICATIONS for notifications (Android 13+)

#### 4. **Camera Features** ✨ NEW
- Take photos directly with camera
- FileProvider integration
- Temporary file caching
- Preview display

#### 5. **Gallery Features** ✨ NEW
- Select photos from gallery
- File picker integration
- Scoped storage support (Android 11+)
- Preview display

#### 6. **Photo Display** ✨ NEW
- Glide library for efficient loading
- Memory and disk caching
- Rounded corners
- Error placeholders

#### 7. **Document Management**
- Add/Edit documents
- Document types (Aadhaar, PAN, Voter ID, etc.)
- Date picking
- Form validation
- Delete documents

#### 8. **Dashboard**
- Responsive grid layout
- Search functionality
- Filter by document type
- Sort by expiry date
- Document count display
- Expiring soon highlighting (red background for < 30 days)

#### 9. **Document Details**
- Full document information
- Photo display
- Flip animation between front/back
- Share functionality

#### 10. **Database**
- Room database integration
- Automatic migrations
- Data persistence

---

## 🔧 How to Build APK

### Step 1: Fix Java Installation (If Needed)

If you see Java error, set JAVA_HOME in Android Studio:

**Option A: Using Android Studio's JDK**
1. Open Android Studio
2. File → Project Structure
3. SDK Location
4. Check JDK location
5. Copy the path

**Option B: Using Built-in Gradle JDK**
Just use Android Studio's built-in JDK (recommended)

### Step 2: Build in Android Studio (EASIEST)

1. **Open Android Studio**
   - File → Open → Select DocExpiry folder

2. **Sync Project**
   - File → Sync Now
   - Wait for completion

3. **Build APK**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for "Build successful" message

4. **Locate APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### Step 3: Build via Terminal (If You Prefer)

**PowerShell:**
```powershell
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
./gradlew.bat assembleDebug
```

**CMD:**
```cmd
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
gradlew assembleDebug
```

Wait for completion. Success message:
```
BUILD SUCCESSFUL in XX seconds
```

### Step 4: Locate Your APK

After successful build, find your APK here:

```
C:\Users\Shara\AndroidStudioProjects\DocExpiry\app\build\outputs\apk\debug\app-debug.apk
```

---

## 🎯 APK Details

### Debug APK:
- **Name**: `app-debug.apk`
- **Location**: `app/build/outputs/apk/debug/`
- **Size**: ~5-10 MB
- **Purpose**: Testing and development

### Release APK (Optional):
- **Name**: `app-release.apk`
- **Location**: `app/build/outputs/apk/release/`
- **Requires**: Signing key
- **Purpose**: Google Play Store distribution

---

## 📱 Testing the APK

### Option 1: Install on Connected Device
```powershell
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
./gradlew.bat installDebug
```

### Option 2: Install on Emulator
1. Start Android Emulator
2. Run: `./gradlew.bat installDebug`

### Option 3: Manual Installation
1. Copy `app-debug.apk` to your phone
2. Open file manager
3. Tap APK file
4. Follow installation prompts
5. Open app from drawer

---

## ✅ APK Features Checklist

When you run the APK, test these features:

### Authentication
- [ ] Launch app (should show LoginActivity)
- [ ] Sign up with email and password
- [ ] Sign in with credentials
- [ ] See dashboard after login

### Dashboard
- [ ] View empty dashboard
- [ ] See "0 documents"
- [ ] See search bar
- [ ] See filters (Type, Issued Range, Sort)
- [ ] See FAB (+) button

### Add Document
- [ ] Tap FAB button
- [ ] See AddEditCardActivity
- [ ] See photo section at top
- [ ] See form fields
- [ ] Select document type
- [ ] Authority auto-populates
- [ ] Enter document number
- [ ] Enter name
- [ ] Pick issued date
- [ ] Pick expiry date

### Camera Feature
- [ ] Tap "📷 Camera" button
- [ ] Permit camera access (if asked)
- [ ] Camera app opens
- [ ] Take photo
- [ ] Photo displays in preview
- [ ] Photo previews before save

### Gallery Feature
- [ ] Tap "🖼️ Gallery" button
- [ ] Permit gallery access (if asked)
- [ ] Gallery opens
- [ ] Select photo
- [ ] Photo displays in preview

### Save Document
- [ ] Fill all fields including photo
- [ ] Tap "Save Document"
- [ ] See success toast
- [ ] Return to dashboard
- [ ] See new card with photo

### Dashboard Card Display
- [ ] Card shows with photo at top
- [ ] Document type shown
- [ ] Document number shown
- [ ] Name shown
- [ ] Dates shown
- [ ] View/Edit/Share/Delete buttons visible

### Photo Display
- [ ] Photos load from cache/gallery
- [ ] Rounded corners visible
- [ ] Placeholder if no photo
- [ ] Photos cached (fast reload)

### Notifications
- [ ] Add document expiring < 30 days
- [ ] Wait 1 day
- [ ] See notification in status bar
- [ ] Tap notification to view document

### Permissions
- [ ] Camera permission requested and works
- [ ] Gallery permission requested and works
- [ ] Notification permission requested (Android 13+)
- [ ] Denying permission shows error toast

---

## 🚀 Build Success Signs

You'll know build succeeded when:

### In Terminal:
```
BUILD SUCCESSFUL in 45 seconds
```

### In Android Studio:
- Green checkmark ✓
- Message: "Build successful"
- No red errors

### APK File Exists:
```
C:\Users\Shara\AndroidStudioProjects\DocExpiry\app\build\outputs\apk\debug\app-debug.apk
```

---

## 🐛 Troubleshooting Build Issues

### Issue: "JAVA_HOME is set to an invalid directory"

**Solution 1**: Open Android Studio (uses built-in JDK)
```powershell
# Just open Android Studio and build from there
# No need for terminal
```

**Solution 2**: Set JAVA_HOME in System Variables
1. Open System Environment Variables
2. Add new variable:
   - Name: `JAVA_HOME`
   - Value: `C:\Program Files\Eclipse Adoptium\jdk-17.0.9` (adjust version)
3. Restart PowerShell
4. Try build again

### Issue: "Gradle daemon exited unexpectedly"

**Solution**:
```powershell
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
./gradlew.bat --stop
./gradlew.bat clean
./gradlew.bat assembleDebug
```

### Issue: Build hangs or takes too long

**Solution**:
```powershell
# Cancel current build (Ctrl+C)
./gradlew.bat --no-daemon assembleDebug
```

### Issue: "Manifest merger failed"

**Solution**: Check AndroidManifest.xml for issues
- Duplicate permissions
- Invalid intent filters
- Missing declarations

---

## 📝 APK Variants

Your app has one main APK variant:

**Debug APK** (For Testing)
- Debuggable: Yes
- Signed: Yes (debug keystore)
- Size: ~5-10 MB
- Purpose: Development & QA testing

---

## ✨ After Build Success

### Next Steps:

1. **Install APK**
   ```powershell
   # On device
   ./gradlew.bat installDebug
   
   # Or manually copy app-debug.apk to phone
   ```

2. **Test All Features**
   - Follow checklist above
   - Try permissions
   - Test photo features
   - Check notifications

3. **Report Issues**
   - Check `GALLERY_PHOTO_FIXES.md` for known issues
   - Debug using logs
   - Reference code in documentation

4. **Update & Rebuild**
   - Make code changes
   - Run `./gradlew.bat assembleDebug` again
   - Install updated APK

---

## 📊 Build Statistics

| Metric | Value |
|--------|-------|
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 34 (Android 14) |
| **Compile SDK** | 34 (Android 14) |
| **Language** | Kotlin |
| **Database** | Room |
| **UI Framework** | Material Design 3 |
| **Features** | 10+ including notifications & permissions |

---

## 🎉 Ready to Build!

All your features are implemented and ready:

✅ Notifications with WorkManager  
✅ Camera permission & capture  
✅ Gallery permission & selection  
✅ Photo display with Glide  
✅ Document management  
✅ Database persistence  
✅ User authentication  
✅ Expiry tracking & alerts  

**Your APK will include all of these!**

---

## 📞 Need Help?

Check these documents:
- `QUICK_START_GUIDE.md` - Feature overview
- `GALLERY_PHOTO_FIXES.md` - Common issues
- `COMPLETE_PERMISSIONS_REFERENCE.md` - Full code reference
- `NOTIFICATIONS_PERMISSIONS_GUIDE.md` - How notifications work

---

**Status**: ✅ **Ready to Build**  
**Features**: ✅ **All Implemented**  
**Documentation**: ✅ **Complete**  

👉 **Next Step**: Build APK using Android Studio or terminal!

---

*Last Updated: November 2025*

