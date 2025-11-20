# 🎯 Step-by-Step Build Guide (Visual)

## 🏁 FASTEST WAY: Double-Click to Build

### Step 1: Open File Explorer
```
Press: Windows + E (or open File Explorer)
Navigate to: C:\Users\Shara\AndroidStudioProjects\DocExpiry
```

### Step 2: Find the Build Script
```
Look for file: BUILD_AND_RUN.bat
Icon: Command prompt icon (cmd)
```

### Step 3: Run It
```
Double-click: BUILD_AND_RUN.bat
Wait: 2-3 minutes
```

### Step 4: Watch the Progress
```
You should see:
1. "Cleaning project..." → ✓
2. "Building Debug APK..." → ✓
3. "Checking devices..." → Select your device
4. "Installing APK..." → ✓
5. "Launching app..." → ✓ App appears on device!
6. "Capturing logcat..." → Done!
```

---

## 📍 ALTERNATIVE: Command Prompt Method

### Step 1: Open Command Prompt
```
Press: Windows + R
Type: cmd
Press: Enter
```

### Step 2: Navigate to Project
```cmd
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
```

### Step 3: Run the Build Script
```cmd
BUILD_AND_RUN.bat
```

### Step 4: Watch Output
```
[Clean] → [Build] → [Device Check] → [Install] → [Launch] → Done!
```

---

## 🔧 MANUAL METHOD: Step-by-Step Commands

### Step 1: Open Command Prompt
```
Windows + R → cmd → Enter
```

### Step 2: Navigate to Project
```cmd
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
```

### Step 3: Clean Project
```cmd
gradlew.bat clean --console plain > clean.log 2>&1
```
**What it does**: Removes previous build files
**Time**: ~10-15 seconds
**Output**: clean.log

### Step 4: Build APK
```cmd
gradlew.bat assembleDebug --console plain > build_full.log 2>&1
```
**What it does**: Compiles code and creates APK
**Time**: ~60-120 seconds
**Output**: build_full.log
**Expected**: `app\build\outputs\apk\debug\app-debug.apk`

### Step 5: Verify Build Success
```cmd
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo BUILD SUCCESSFUL!
) else (
    echo BUILD FAILED
    type build_full.log | findstr "error:"
)
```

### Step 6: Check Devices Connected
```cmd
adb devices -l
```
**What you should see**: Your device listed as "device"

### Step 7: Install APK
```cmd
gradlew.bat installDebug --console plain > install.log 2>&1
```
**What it does**: Installs APK on connected device
**Time**: ~10-30 seconds
**Output**: install.log

### Step 8: Launch App
```cmd
adb shell am start -n com.example.docexpiry/.MainActivity
```
**What it does**: Launches app on device
**Expected**: App appears on device screen in ~3-5 seconds

### Step 9: Capture Device Logs
```cmd
adb logcat -d -t 300 > run_logcat.txt
type run_logcat.txt
```
**What it does**: Captures device logs for debugging
**Output**: run_logcat.txt

---

## ✅ VERIFICATION CHECKLIST

After build completes, check these:

### ☑️ 1. APK Exists
```cmd
dir app\build\outputs\apk\debug\app-debug.apk
```
✓ File should be 5-10 MB (not 0 bytes)

### ☑️ 2. App Installed
```cmd
adb shell pm list packages | findstr docexpiry
```
✓ Should output: `package:com.example.docexpiry`

### ☑️ 3. App Launched
```
✓ App window visible on device
✓ No crash/force close
```

### ☑️ 4. Check Device Logs
```cmd
type run_logcat.txt | findstr "error\|crash"
```
✓ Should show minimal errors (normal startup messages OK)

---

## 🎮 TESTING THE APP

Once app is running:

### Feature 1: Login
1. Open app
2. Enter credentials
3. ✓ Should show dashboard

### Feature 2: Dashboard
1. ✓ Document list visible
2. ✓ Can scroll/search
3. ✓ Stats show correct

### Feature 3: Add Document
1. Tap "Add" button
2. Select document type
3. Fill in fields
4. ✓ Should save without crash

### Feature 4: Dates Display
1. Add document with dates
2. ✓ Should show: `DD/MM/YYYY` format
3. ✗ Should NOT show: `$(sdf.format(...))`

### Feature 5: Share Document
1. Open document details
2. Tap "Share" button
3. ✓ Should open share options
4. ✓ Text should be formatted nicely

---

## 🐛 TROUBLESHOOTING: If Something Goes Wrong

### Issue: "BUILD FAILED"
```
Check: build_full.log for error messages
View: type build_full.log | findstr "error:"
Solution: See "BUILD_STATUS_READY.md" troubleshooting section
```

### Issue: "No connected devices"
```
Check: adb devices -l
Fix: 
  1. Connect Android device via USB
  2. Enable "Developer Mode" on device
  3. Enable "USB Debugging" in Developer Options
  4. Grant USB permissions when prompted
Solution: Retry install after reconnecting
```

### Issue: "APK not found after build"
```
Check: build_full.log for compilation errors
View: type build_full.log | findstr "error:"
Solution: Run clean + rebuild:
  gradlew.bat clean
  gradlew.bat assembleDebug --stacktrace
```

### Issue: "Install failed"
```
Fix:
  1. Uninstall previous version: adb uninstall com.example.docexpiry
  2. Retry install: gradlew.bat installDebug
Solution: Check install.log for specific error
```

### Issue: "App crashes on launch"
```
Check: run_logcat.txt or adb logcat | findstr docexpiry
Solution: Check for permission errors, missing resources
View: Full error in logcat output
```

---

## ⏱️ TIMING REFERENCE

| Task | Expected Time |
|------|---|
| Clean | 10-15 sec |
| Build | 60-120 sec |
| Install | 10-30 sec |
| Launch | 3-5 sec |
| Log Capture | 5 sec |
| **TOTAL** | **2-3 min** |

---

## 📊 SUCCESS INDICATORS

### ✅ Build Succeeded If You See:
```
✓ "BUILD SUCCESSFUL" in console
✓ APK file exists at: app\build\outputs\apk\debug\app-debug.apk
✓ build_full.log shows no "error:" lines
```

### ✅ Install Succeeded If You See:
```
✓ "Installed successfully" message
✓ adb shell pm list packages | findstr docexpiry returns result
```

### ✅ App Running If You See:
```
✓ App window opens on device/emulator
✓ Login screen visible
✓ No immediate crash
```

---

## 🎁 LOG FILES FOR REFERENCE

After successful build:

| File | Check For |
|------|-----------|
| `clean.log` | Any clean errors (should be empty usually) |
| `build_full.log` | Compilation errors/warnings |
| `install.log` | Installation errors |
| `run_logcat.txt` | Runtime errors/crashes |

---

## 🚀 SUMMARY: FROM START TO RUNNING APP

**Option A - Fastest (5 minutes total):**
```
1. Double-click: BUILD_AND_RUN.bat
2. Wait for completion
3. App appears on device ✓
```

**Option B - Manual Control (10 minutes total):**
```
1. gradlew.bat clean
2. gradlew.bat assembleDebug
3. adb devices (verify device connected)
4. gradlew.bat installDebug
5. adb shell am start -n com.example.docexpiry/.MainActivity
```

**Option C - Android Studio:**
```
1. Open Android Studio
2. File → Open → Select project root
3. Build → Build Bundle(s)/APK(s) → Build APK(s)
4. Run → Run 'app'
5. Select device
```

---

## ✨ NEXT STEPS

### NOW:
- [ ] Choose build method above (A, B, or C)
- [ ] Run the build

### AFTER BUILD:
- [ ] Verify app launches (no crash)
- [ ] Check features work (login, add doc, share)
- [ ] Review run_logcat.txt for any errors

### IF ISSUES:
- [ ] Check relevant log file
- [ ] Refer to troubleshooting section above
- [ ] Run: `gradlew.bat clean assembleDebug --stacktrace`
- [ ] Review build_full.log for compilation errors

---

**Status**: ✅ READY
**Next Action**: Choose Option A, B, or C and start building!
**Expected Result**: App running on device in 2-10 minutes

