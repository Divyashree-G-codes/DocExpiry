@echo off
REM DocExpiry Complete Build, Install, and Run Script
REM Run this file directly from cmd.exe

setlocal enabledelayedexpansion

cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry

REM Auto-detect JAVA_HOME from Android Studio or Gradle bundled JDK
if not defined JAVA_HOME (
    REM Try to find Java from common locations
    if exist "C:\Program Files\Android\Android Studio\jre" (
        set "JAVA_HOME=C:\Program Files\Android\Android Studio\jre"
    ) else if exist "C:\Program Files\Android\Android Studio\plugins\android\lib\studio.jdk" (
        set "JAVA_HOME=C:\Program Files\Android\Android Studio\plugins\android\lib\studio.jdk"
    )
)

REM Clear invalid JAVA_HOME to allow Gradle to auto-detect
if not exist "%JAVA_HOME%\bin\java.exe" (
    set "JAVA_HOME="
)

echo.
echo ======================================================
echo DocExpiry Build, Install & Run Script
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
    type clean.log | findstr /I "error failed"
    pause
    exit /b 1
)
echo [✓] Clean complete

echo.
echo [2/5] Building Debug APK...
call gradlew.bat assembleDebug --console plain > build_full.log 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Build failed! Check build_full.log
    echo.
    echo === ERRORS ===
    setlocal enabledelayedexpansion
    set count=0
    for /f "delims=" %%A in ('type build_full.log ^| findstr /I "error"') do (
        set /a count+=1
        if !count! leq 50 echo %%A
    )
    endlocal
    echo === END ERRORS ===
    echo.
    pause
    exit /b 1
)
echo [✓] Build complete

REM Check if APK was created
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ERROR: APK not found after build!
    echo Expected location: app\build\outputs\apk\debug\app-debug.apk
    echo Checking build log for errors...
    type build_full.log | findstr /I "error\|failed"
    pause
    exit /b 1
)
echo [✓] APK created successfully
for %%F in ("app\build\outputs\apk\debug\app-debug.apk") do echo     Size: %%~zF bytes

echo.
echo [3/5] Checking connected devices...
adb devices -l
echo.
set /p device_prompt="Enter to continue (ensure device is connected):"

echo.
echo [4/5] Installing APK on device...
call gradlew.bat installDebug --console plain > install.log 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Install failed! Check install.log
    type install.log | findstr /I "error\|failed"
    pause
    exit /b 1
)
echo [✓] APK installed successfully

echo.
echo [5/5] Launching app...
adb shell am start -n com.example.docexpiry/.MainActivity
if %errorlevel% neq 0 (
    echo ERROR: Failed to launch app
    echo Check if app is installed: adb shell pm list packages | findstr docexpiry
    pause
    exit /b 1
)
echo [✓] App launched!

echo.
echo ======================================================
echo Build Complete!
echo ======================================================
echo.
echo Capturing device logcat (last 300 lines)...
adb logcat -d -t 300 > run_logcat.txt
echo Logcat saved to: run_logcat.txt
echo.
echo Generated files:
echo   - clean.log: Clean output
echo   - build_full.log: Full build output
echo   - install.log: Installation output
echo   - run_logcat.txt: Device logs
echo.
echo To view live logs: adb logcat | findstr docexpiry
echo To uninstall app: adb uninstall com.example.docexpiry
echo.
pause
endlocal
