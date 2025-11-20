
@echo off
REM Simple DocExpiry Build & Run Script - No Java Setup Required
REM Uses Gradle wrapper which handles JDK auto-detection

setlocal

cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry

echo.
echo ======================================================
echo DocExpiry - Build and Run
echo ======================================================
echo.

REM Step 1: Clean
echo [1/4] Cleaning project...
call .\gradlew.bat clean
if errorlevel 1 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)
echo OK - Project cleaned

REM Step 2: Build APK
echo.
echo [2/4] Building Debug APK...
call .\gradlew.bat assembleDebug
if errorlevel 1 (
    echo ERROR: Build failed
    echo Check app/build/outputs/apk/debug/ for APK or logs
    pause
    exit /b 1
)
echo OK - APK built

REM Step 3: Install
echo.
echo [3/4] Installing APK to device...
adb devices
echo.
pause /prompt "Press ENTER when device is ready (ensure adb can see device)..."
call .\gradlew.bat installDebug
if errorlevel 1 (
    echo ERROR: Install failed
    echo Make sure device is connected and USB debugging is enabled
    pause
    exit /b 1
)
echo OK - APK installed

REM Step 4: Launch
echo.
echo [4/4] Launching app...
adb shell am start -n com.example.docexpiry/.MainActivity
if errorlevel 1 (
    echo ERROR: Failed to launch
    pause
    exit /b 1
)
echo OK - App launched!

echo.
echo ======================================================
echo SUCCESS! App is running.
echo Tap the floating + button to add a new document.
echo ======================================================
echo.

pause /prompt "Press ENTER to exit..."

