# 🚀 Quick APK Build Commands

## ⚡ Fastest Way - Use Android Studio (Recommended)

### Step 1: Open Android Studio
```
Start Menu → Android Studio
```

### Step 2: Open Project
```
File → Open → Select: C:\Users\Shara\AndroidStudioProjects\DocExpiry
```

### Step 3: Sync Project
```
File → Sync Now
(Wait for it to complete)
```

### Step 4: Build APK
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

### Step 5: Wait for Success
```
You should see: "Build successful" message
(Green checkmark in bottom right)
```

### Step 6: Find Your APK
```
App will show: "locate"
Click it, or navigate to:
C:\Users\Shara\AndroidStudioProjects\DocExpiry\app\build\outputs\apk\debug\app-debug.apk
```

---

## 💻 Alternative - PowerShell Commands

### Clean Build (Recommended for first time)
```powershell
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
./gradlew.bat clean
./gradlew.bat assembleDebug
```

### Quick Rebuild (After small changes)
```powershell
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
./gradlew.bat assembleDebug
```

### Install to Connected Device
```powershell
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
./gradlew.bat installDebug
```

### Install to Emulator
```powershell
# Make sure emulator is running first
cd "C:\Users\Shara\AndroidStudioProjects\DocExpiry"
./gradlew.bat installDebug
```

---

## 📱 After Build - Testing on Device

### Method 1: Direct Installation (Easiest)
1. Connect Android phone with USB
2. Enable Developer Mode (tap Build Number 7 times)
3. Allow USB debugging
4. Run: `./gradlew.bat installDebug`
5. App auto-installs
6. Open from app drawer

### Method 2: Emulator
1. Start Android Emulator
2. Run: `./gradlew.bat installDebug`
3. Emulator auto-installs app
4. Open from app drawer

### Method 3: Manual APK Install
1. Navigate to: `C:\Users\Shara\AndroidStudioProjects\DocExpiry\app\build\outputs\apk\debug\`
2. Copy: `app-debug.apk`
3. Email or transfer to phone
4. Open file manager on phone
5. Tap APK file
6. Follow installation prompts

---

## 🎯 What to Test

### Feature Testing Checklist:

**Login & Setup**
- [ ] App launches to login
- [ ] Can register new user
- [ ] Can sign in

**Dashboard**
- [ ] Shows 0 documents initially
- [ ] Search bar works
- [ ] Filters work
- [ ] Sort works

**Add Document with Camera**
- [ ] Tap + button
- [ ] Tap "📷 Camera"
- [ ] Grant camera permission
- [ ] Take photo
- [ ] Fill form
- [ ] Save document

**Add Document with Gallery**
- [ ] Tap + button
- [ ] Tap "🖼️ Gallery"
- [ ] Grant gallery permission
- [ ] Select photo
- [ ] Fill form
- [ ] Save document

**Dashboard Display**
- [ ] Cards show with photos
- [ ] Document info visible
- [ ] Buttons work (View/Edit/Share/Delete)
- [ ] Expiring soon highlighted (< 30 days)

**Document Details**
- [ ] View button shows full details
- [ ] Edit button opens form
- [ ] Share button works
- [ ] Delete button removes document

**Notifications**
- [ ] Add document expiring < 30 days
- [ ] Wait 1 day (or advance phone clock)
- [ ] Notification appears
- [ ] Tap notification opens document

---

## ✅ Build Success Indicators

### You'll Know It Worked When:

**Terminal Output**:
```
BUILD SUCCESSFUL in XX seconds
```

**Android Studio**:
- Green checkmark ✓
- Status bar shows "Build successful"
- No red error messages

**File Exists**:
```
app\build\outputs\apk\debug\app-debug.apk
(File size: 5-10 MB)
```

---

## 🐛 Common Issues & Quick Fixes

### "BUILD FAILED"
→ Open Android Studio instead of terminal
→ Android Studio handles Java automatically

### "JAVA_HOME invalid"
→ Just use Android Studio (it has built-in Java)

### "Build hangs"
→ Wait longer (first build takes 5-10 min)
→ If still hanging: Ctrl+C, restart, try again

### APK won't install
→ Uninstall old version first
→ Clear app data: Settings > Apps > DocExpiry > Storage > Clear Data
→ Reinstall APK

### App crashes on launch
→ Check: Are all permissions granted?
→ Check: Did you register as user first?
→ Check: Logcat for error messages

---

## 📊 Expected Build Time

**First Build**: 5-10 minutes (downloading dependencies)  
**Subsequent Builds**: 1-2 minutes (incremental)  

---

## 📦 What's in Your APK

✅ Login & Registration  
✅ Document Management (Add/Edit/Delete)  
✅ Camera Photo Capture  
✅ Gallery Photo Selection  
✅ Photo Display with Glide  
✅ Notifications System  
✅ Permissions Handling  
✅ Database (Room)  
✅ Search & Filter  
✅ Expiry Tracking  

---

## 🎉 Ready to Build!

**Easiest Method**:
1. Open Android Studio
2. File → Open → Select DocExpiry folder
3. Wait for sync
4. Build → Build APK(s)
5. Done! 🎊

---

**Questions?** Check APK_BUILD_GUIDE.md for detailed instructions

