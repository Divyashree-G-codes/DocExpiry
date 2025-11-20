# 📱 HOW TO ACCESS DOCEXPIRY APP

## 🚀 INSTALLATION & ACCESS GUIDE

### **Step 1: Get the APK File**

The app is built and ready to install. The APK file is located at:
```
C:\Users\Shara\AndroidStudioProjects\DocExpiry\app\build\outputs\apk\debug\app-debug.apk
```

### **Step 2: Transfer APK to Your Device**

**Option A - USB Cable:**
1. Connect your Android device to computer via USB
2. Copy the `app-debug.apk` file
3. Paste it into your device's Downloads folder
4. Tap the file on your device to install

**Option B - Email:**
1. Email the APK file to yourself
2. Open the email on your Android device
3. Tap the attachment
4. Install when prompted

**Option C - Android Studio Emulator:**
1. Open Android Studio
2. Run an emulator
3. In terminal, run:
   ```bash
   cd C:\Users\Shara\AndroidStudioProjects\DocExpiry
   ./gradlew installDebug
   ```

### **Step 3: Enable Installation from Unknown Sources**

1. On your Android device, go to **Settings**
2. Navigate to **Security** or **Apps**
3. Find **Unknown Sources** or **Install from Unknown Sources**
4. Toggle it **ON**
5. Confirm the warning

### **Step 4: Install the App**

1. Locate the `app-debug.apk` file on your device
2. Tap to open it
3. Tap **Install**
4. Wait for installation to complete
5. Tap **Open** when done

### **Step 5: Grant Permissions**

The app will request:
- **Camera** - For taking document photos
- **Storage** - For accessing gallery photos

Tap **Allow** for each permission.

---

## 📲 ACCESSING THE APP

### **From Your Device:**

1. **Find the App:**
   - Look for "DocExpiry" icon on home screen
   - Or find it in app drawer

2. **Tap the Icon:**
   - Opens LoginActivity screen
   - Shows beautiful blue gradient header

3. **First Time Users:**
   - Email: Use any valid email (user@example.com)
   - Password: Any 6+ character password
   - Tap "Sign Up"
   - Register with name, DOB, address
   - Access dashboard

4. **Returning Users:**
   - App auto-recognizes you
   - Skips registration
   - Takes you to dashboard

---

## 🎯 QUICK ACCESS PATHS

### **From Login Screen:**
```
Login Screen
├─ Enter Email
├─ Enter Password
├─ Tap "Sign In" → Registration
└─ Tap "Sign Up" → Registration
    ↓
    Registration Screen (if new)
    ├─ Enter Name
    ├─ Pick DOB
    ├─ Enter Address
    ├─ Tap "Complete"
    ↓
    Dashboard (Main Screen)
```

### **From Dashboard:**
```
Dashboard
├─ Search Bar → Find documents
├─ Filter Spinners → Narrow results
├─ Document Cards → View/Edit/Share/Delete
├─ + FAB Button → Add new document
└─ User Profile (Top) → Your info & doc count
```

### **From Document Card:**
```
Document Card
├─ Tap Card → View Details (Flip Animation)
├─ 👁 View → Open detail screen
├─ ✏️ Edit → Modify document
├─ 📤 Share → Share with others
└─ 🗑️ Delete → Remove document
```

---

## 🖥️ DESKTOP ACCESS (Development)

### **Using Android Studio:**

1. **Open Project:**
   ```
   File → Open → C:\Users\Shara\AndroidStudioProjects\DocExpiry
   ```

2. **Start Emulator:**
   - Tools → AVD Manager
   - Select device
   - Click Play button

3. **Run App:**
   - Run → Run 'app'
   - Or press Shift+F10

4. **View in Emulator:**
   - App opens automatically
   - Shows login screen
   - Fully functional

### **Using Command Line:**

```bash
# Navigate to project
cd C:\Users\Shara\AndroidStudioProjects\DocExpiry

# Set Java Home
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot

# Build APK
gradlew.bat assembleDebug

# Install on connected device
gradlew.bat installDebug

# Install on emulator
gradlew.bat installDebug
```

---

## 📂 PROJECT FILE STRUCTURE

### **Access Source Code:**

```
C:\Users\Shara\AndroidStudioProjects\DocExpiry\
├── app/
│   ├── src/main/
│   │   ├── java/com/example/docexpiry/
│   │   │   ├── LoginActivity.kt
│   │   │   ├── RegistrationActivity.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── AddEditCardActivity.kt
│   │   │   ├── CardDetailActivity.kt
│   │   │   ├── CardAdapter.kt
│   │   │   └── ... (other files)
│   │   ├── res/
│   │   │   ├── layout/ (XML layouts)
│   │   │   ├── drawable/ (Images & drawables)
│   │   │   └── values/ (Colors, strings, etc)
│   │   └── AndroidManifest.xml
│   ├── build/
│   │   └── outputs/apk/debug/
│   │       └── app-debug.apk ← **APK FILE HERE**
│   └── build.gradle.kts
├── gradle/
├── gradlew.bat
└── settings.gradle.kts
```

---

## 🔍 ACCESSING SPECIFIC FEATURES

### **Add Document:**
```
Dashboard
  ↓
Tap "+" FAB (Floating Action Button)
  ↓
AddEditCardActivity Opens
  ↓
1. Tap "📷 Camera" or "🖼️ Gallery"
2. Select/Capture photo
3. Fill form fields
4. Tap "Save Document"
  ↓
Document appears on Dashboard ✓
```

### **View Document:**
```
Dashboard
  ↓
Tap Document Card
  ↓
Detail Screen Opens with Flip Animation
  ↓
See Image + Text Details
  ↓
Tap to Flip between Front/Back
```

### **Search Documents:**
```
Dashboard
  ↓
Tap Search Bar
  ↓
Type search term
  ↓
Results auto-filter in real-time
```

### **Share Document:**
```
Document Card or Detail Screen
  ↓
Tap "📤 Share" Button
  ↓
Share Menu Appears:
├─ Email
├─ Messaging
├─ WhatsApp
├─ Drive
└─ ... (other apps)
  ↓
Select app
  ↓
Document shared (image + text)
```

---

## 📋 DEVICE REQUIREMENTS

### **Minimum:**
- Android 7.0 (API 24)
- 100MB free storage
- Camera or gallery access

### **Recommended:**
- Android 10+ (API 29+)
- 500MB+ free storage
- Good internet for future features

### **Permissions:**
- Camera access (optional - for photos)
- Storage access (optional - for gallery)

---

## 🎮 TEST ACCOUNTS

### **Demo Login:**
```
Email: user@example.com
Password: password123
(Any valid email + 6+ char password works)
```

### **Document Types Available:**
- Aadhaar
- PAN
- Voter ID
- Driving License
- Passport
- Birth Certificate
- Ration Card
- Academic Certificate

---

## 📞 TROUBLESHOOTING ACCESS

### **Issue: Can't install APK**
**Solution:**
1. Enable "Unknown Sources" in Settings
2. Check storage space (min 100MB)
3. Try USB cable instead of email
4. Reinstall app

### **Issue: App won't open**
**Solution:**
1. Force close: Settings → Apps → DocExpiry → Force Stop
2. Clear cache: Settings → Apps → DocExpiry → Storage → Clear Cache
3. Reinstall app
4. Restart device

### **Issue: Camera not working**
**Solution:**
1. Allow camera permission
2. Settings → Apps → DocExpiry → Permissions → Camera → Allow
3. Restart app

### **Issue: Photos not loading**
**Solution:**
1. Allow storage permission
2. Check internet connection
3. Clear app cache
4. Reinstall app

---

## 🎬 QUICK START WALKTHROUGH

### **First Time (2 minutes):**
```
1. Install app (1 min)
2. Open app (instantly)
3. Tap "Sign Up" (10 sec)
4. Enter email: user@example.com (20 sec)
5. Enter password: MyPassword123 (20 sec)
6. Fill registration (30 sec)
7. See dashboard (instantly)
✓ Ready to use!
```

### **Add First Document (3 minutes):**
```
1. Tap "+" FAB (5 sec)
2. Tap "📷 Camera" (5 sec)
3. Take photo of Aadhaar (30 sec)
4. Select type: Aadhaar (10 sec)
5. Enter number: 123456789012 (20 sec)
6. Enter name: John Doe (20 sec)
7. Pick issued date (20 sec)
8. Pick expiry date (20 sec)
9. Tap "Save Document" (5 sec)
10. See card on dashboard (instantly)
✓ Document added!
```

---

## 🔗 IMPORTANT LINKS

### **Project Files:**
- **Source Code**: `C:\Users\Shara\AndroidStudioProjects\DocExpiry\app\src`
- **APK**: `C:\Users\Shara\AndroidStudioProjects\DocExpiry\app\build\outputs\apk\debug\app-debug.apk`
- **Documentation**: `C:\Users\Shara\AndroidStudioProjects\DocExpiry\` (*.md files)

### **Documentation Files:**
- **PROJECT_COMPLETE.md** - Full project overview
- **COMPLETION_CHECKLIST.md** - Feature checklist
- **VISUAL_FLOW_GUIDE.md** - App flows (current file)
- **QUICK_REFERENCE.md** - Quick guide
- **LOGIN_AND_PHOTO_GUIDE.md** - Login & photo features

---

## ✅ VERIFICATION CHECKLIST

Before using the app:
- [ ] APK file exists in `app/build/outputs/apk/debug/`
- [ ] Device has Android 7.0+
- [ ] 100MB+ free storage
- [ ] "Unknown Sources" enabled
- [ ] Camera & storage permissions allowed
- [ ] App installs successfully
- [ ] Login screen appears on first launch
- [ ] All features accessible

---

**Ready to Access DocExpiry!** 🚀

Follow the steps above and you'll have the app running in minutes.

**Need help?** Check the troubleshooting section or refer to the documentation files.

