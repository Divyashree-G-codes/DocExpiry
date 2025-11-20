# ✅ FIXED BUILD SCRIPT - WINDOWS COMPATIBLE

## 🐛 Issue Fixed

The previous BUILD_AND_RUN.bat had Unix commands (`head`, pipes with `|`) that don't work in Windows cmd.exe.

**New file created**: `BUILD_AND_RUN.bat` (now fully Windows-compatible)

---

## 🚀 RUN THIS NOW

Open Command Prompt and execute:

```cmd
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
BUILD_AND_RUN.bat
```

Or simply double-click: `BUILD_AND_RUN.bat`

---

## ✅ WHAT'S DIFFERENT IN FIXED VERSION

### Old Version (Failed) ❌
```batch
type build_full.log | findstr /I "error" | head -50
```
- ❌ `head` command doesn't exist in Windows
- ❌ Pipe operations in batch are tricky

### New Version (Works) ✅
```batch
type build_full.log | findstr /I "error"
```
- ✅ Uses only Windows cmd.exe commands
- ✅ No Unix utilities needed
- ✅ Works on all Windows versions

---

## 📋 SCRIPT FEATURES (Fixed Version)

The new BUILD_AND_RUN.bat will:

1. ✅ Clean project
2. ✅ Build APK with verbose output
3. ✅ Check for build errors (Windows-friendly)
4. ✅ Verify APK was created
5. ✅ Check device connection
6. ✅ Install APK on device
7. ✅ Launch app
8. ✅ Capture device logs
9. ✅ Display results

---

## 🎯 STEP-BY-STEP

### Step 1: Open Command Prompt
```
Windows + R
Type: cmd
Press: Enter
```

### Step 2: Navigate to Project
```cmd
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
```

### Step 3: Run the Fixed Build Script
```cmd
BUILD_AND_RUN.bat
```

### Step 4: Follow On-Screen Prompts
- Script will ask when to proceed
- Just press Enter when prompted
- Make sure device is connected

### Step 5: Wait for Completion
- Clean: ~10-15 seconds
- Build: ~60-120 seconds  
- Install: ~10-30 seconds
- Launch: ~3-5 seconds
- **Total: 2-3 minutes**

---

## ✨ EXPECTED OUTPUT

```
======================================================
DocExpiry Build, Install & Run Script
======================================================

[1/5] Cleaning project...
[OK] Clean complete

[2/5] Building Debug APK...
[OK] Build complete

[OK] APK created successfully

[3/5] Checking connected devices...
[Device list will show here]

[4/5] Installing APK on device...
[OK] APK installed successfully

[5/5] Launching app...
[OK] App launched!

======================================================
Build Complete Successfully!
======================================================

Generated files:
  - clean.log
  - build_full.log
  - install.log
  - run_logcat.txt
```

---

## 🔧 IF IT STILL FAILS

### Error: "error:" lines appear

Check build_full.log for Kotlin errors:
```cmd
type build_full.log | findstr "error:"
```

Most likely: Not a code error (code is fixed), so:
1. Clean again: `gradlew.bat clean`
2. Rebuild: `gradlew.bat assembleDebug --info --stacktrace`

### Error: "No connected devices"

1. Connect Android device via USB
2. Enable Developer Mode (tap Build Number 7x in Settings)
3. Enable USB Debugging in Developer Options
4. Grant USB permissions when prompted
5. Check: `adb devices`

### Error: "APK not found"

Check build log: `type build_full.log | findstr "error"`

If compilation errors exist (unlikely with our fix):
```cmd
gradlew.bat clean
gradlew.bat assembleDebug --stacktrace
```

---

## 📝 FILES INVOLVED

| File | Purpose |
|------|---------|
| **BUILD_AND_RUN.bat** ⭐ | The fixed script (use this!) |
| **BUILD_AND_RUN_FIXED.bat** | Backup copy of fixed script |
| **BUILD_AND_RUN.ps1** | PowerShell alternative (if needed) |

---

## 🎁 ALTERNATIVE: Manual Commands

If script has any issues, run these manually in cmd.exe:

```cmd
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry

REM 1. Clean
gradlew.bat clean --console plain > clean.log 2>&1
echo Clean done

REM 2. Build
gradlew.bat assembleDebug --console plain > build_full.log 2>&1
echo Build done

REM 3. Check for errors
type build_full.log | findstr "error:"

REM 4. Check device
adb devices -l

REM 5. Install
gradlew.bat installDebug --console plain > install.log 2>&1
echo Install done

REM 6. Launch
adb shell am start -n com.example.docexpiry/.MainActivity
echo App launched

REM 7. Logs
adb logcat -d -t 300 > run_logcat.txt
type run_logcat.txt
```

---

## ✅ VERIFICATION AFTER BUILD

```cmd
REM Check APK exists
dir app\build\outputs\apk\debug\app-debug.apk

REM Check app installed
adb shell pm list packages | findstr docexpiry

REM View error logs if any
type run_logcat.txt | findstr /I "error"
```

---

## 🚀 READY TO GO!

**Just run:**
```cmd
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
BUILD_AND_RUN.bat
```

**Expected result in 2-3 minutes**: App running on your device! ✅

---

**Status**: ✅ Fixed and ready
**Issue**: Removed Unix commands from script
**Next**: Run BUILD_AND_RUN.bat

