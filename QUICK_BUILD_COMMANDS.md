# DocExpiry Step-by-Step Build Commands

## Quick Start (Copy & Paste into Command Prompt)

### 1. Navigate to Project
```cmd
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
```

### 2. Clean Project
```cmd
gradlew.bat clean --console plain > clean.log 2>&1
echo Clean complete - check clean.log for any issues
```

### 3. Build Debug APK
```cmd
gradlew.bat assembleDebug --console plain > build_full.log 2>&1
```

### 4. Check Build Status
```cmd
REM Check if APK exists
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo BUILD SUCCESS!
    dir "app\build\outputs\apk\debug\app-debug.apk"
) else (
    echo BUILD FAILED
    echo Checking errors...
    type build_full.log | findstr /I "error"
)
```

### 5. Check Devices
```cmd
adb devices -l
```

### 6. Install APK
```cmd
gradlew.bat installDebug --console plain > install.log 2>&1
echo APK Installed
```

### 7. Launch App
```cmd
adb shell am start -n com.example.docexpiry/.MainActivity
```

### 8. Capture Logs
```cmd
adb logcat -d -t 300 > run_logcat.txt
type run_logcat.txt
```

---

## Individual Commands Reference

| Action | Command |
|--------|---------|
| **Clean** | `gradlew.bat clean` |
| **Build Debug** | `gradlew.bat assembleDebug` |
| **Build Release** | `gradlew.bat assembleRelease` |
| **Run Unit Tests** | `gradlew.bat test` |
| **Install Debug** | `gradlew.bat installDebug` |
| **Install Release** | `gradlew.bat installRelease` |
| **Check Devices** | `adb devices -l` |
| **Launch App** | `adb shell am start -n com.example.docexpiry/.MainActivity` |
| **Stop App** | `adb shell am force-stop com.example.docexpiry` |
| **Uninstall** | `adb uninstall com.example.docexpiry` |
| **View Logs** | `adb logcat` |
| **View App Logs** | `adb logcat \| findstr docexpiry` |
| **Clear Logs** | `adb logcat -c` |
| **Export Logs** | `adb logcat -d > logs.txt` |

---

## Troubleshooting

### Build Fails with "error: resource not found"
- All required strings are defined in `app/src/main/res/values/strings.xml`
- Try: `gradlew.bat clean` followed by `gradlew.bat assembleDebug`

### Build Fails with Kotlin Errors
- The `buildString` issue has been fixed (replaced with `StringBuilder().apply {}`)
- Try: `gradlew.bat clean assembleDebug --stacktrace`

### APK Install Fails
```cmd
REM Check if device is connected
adb devices

REM Uninstall previous version
adb uninstall com.example.docexpiry

REM Retry install
gradlew.bat installDebug
```

### App Won't Launch
```cmd
REM Check device permissions
adb shell pm dump com.example.docexpiry | findstr "permission"

REM Check if app is installed
adb shell pm list packages | findstr docexpiry

REM View error logs
adb logcat | findstr docexpiry
```

### View Build Output in Real-Time
```cmd
REM Clean and build with visible output
gradlew.bat assembleDebug --console plain
```

---

## Files Created After Build

- `app\build\outputs\apk\debug\app-debug.apk` - The compiled APK (installable)
- `clean.log` - Output from clean task
- `build_full.log` - Complete build output (check here for errors)
- `install.log` - APK installation output
- `run_logcat.txt` - Device log capture

---

## Important Notes

✓ **Fix Applied**: MainActivity.kt line 274-293 - replaced `buildString` with `StringBuilder().apply {}`
✓ **Strings Verified**: All required strings are present in strings.xml
✓ **Ready to Build**: The project should now build successfully

**Next Steps**:
1. Run the BUILD_AND_RUN.bat script, OR
2. Follow the Quick Start commands above, OR
3. Use Individual Commands Reference for specific tasks

---

**Project**: DocExpiry
**Build System**: Gradle 8.7
**Kotlin Version**: 1.9.22
**Android Target**: API 34 (Android 14)

