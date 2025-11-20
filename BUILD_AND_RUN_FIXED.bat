@echo off
REM DocExpiry Complete Build, Install, and Run Script (Windows Compatible)
REM Run this file directly from cmd.exe

setlocal enabledelayedexpansion

cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry

echo.
echo ======================================================
echo DocExpiry Build, Install ^& Run Script
echo ======================================================
echo.

REM Check if gradlew.bat exists
if not exist gradlew.bat (
    echo ERROR: gradlew.bat not found in current directory!
    echo Current directory: %cd%
    pause
    exit /b 1
)

echo [1/5] Cleaning project...
call gradlew.bat clean --console plain > clean.log 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Clean failed! Check clean.log
    echo.
    type clean.log
    pause
    exit /b 1
)
echo [OK] Clean complete

echo.
echo [2/5] Building Debug APK...
call gradlew.bat assembleDebug --console plain > build_full.log 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Build failed! Check build_full.log
    echo.
    echo === ERRORS FROM BUILD LOG ===
    type build_full.log | findstr /I "error"
    echo === END ERRORS ===
    echo.
    echo Full log saved to: build_full.log
    pause
    exit /b 1
)
echo [OK] Build complete

REM Check if APK was created
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ERROR: APK not found after build!
    echo Expected location: app\build\outputs\apk\debug\app-debug.apk
    echo Checking build log for errors...
    type build_full.log | findstr /I "error"
    pause
    exit /b 1
)
echo [OK] APK created successfully

echo.
echo [3/5] Checking connected devices...
adb devices -l
echo.
echo Make sure your device shows "device" above (not offline or unauthorized)
set /p device_prompt="Press Enter to continue (ensure device is connected):"

echo.
echo [4/5] Installing APK on device...
call gradlew.bat installDebug --console plain > install.log 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Install failed! Check install.log
    echo.
    type install.log | findstr /I "error"
    echo.
    echo Troubleshooting steps:
    echo 1. Uninstall previous version: adb uninstall com.example.docexpiry
    echo 2. Reconnect device
    echo 3. Try again
    pause
    exit /b 1
)
echo [OK] APK installed successfully

echo.
echo [5/5] Launching app...
adb shell am start -n com.example.docexpiry/.MainActivity
if %errorlevel% neq 0 (
    echo ERROR: Failed to launch app
    echo.
    echo Troubleshooting steps:
    echo 1. Check if app is installed: adb shell pm list packages ^| findstr docexpiry
    echo 2. Check device logs: adb logcat ^| findstr docexpiry
    pause
    exit /b 1
)
echo [OK] App launched!

echo.
echo ======================================================
echo Build Complete Successfully!
echo ======================================================
echo.
echo Capturing device logcat (last 300 lines)...
adb logcat -d -t 300 > run_logcat.txt
echo [OK] Logcat saved to: run_logcat.txt
echo.
echo Generated files:
echo   - clean.log: Clean output
echo   - build_full.log: Full build output (check for errors)
echo   - install.log: Installation output
echo   - run_logcat.txt: Device logs (check for runtime errors)
echo.
echo Next steps:
echo 1. Check that app is visible on device
echo 2. Test login functionality
echo 3. Try adding a document
echo 4. If any issues, check run_logcat.txt for errors
echo.
echo To view live logs: adb logcat ^| findstr docexpiry
echo To uninstall app: adb uninstall com.example.docexpiry
echo.
pause
endlocal

