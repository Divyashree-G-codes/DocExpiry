@echo off
REM Simple Build Script - No adb required
REM Just builds the APK, you can install manually

setlocal enabledelayedexpansion

cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry

REM Clear invalid JAVA_HOME to allow Gradle to auto-detect
if not exist "%JAVA_HOME%\bin\java.exe" (
    set "JAVA_HOME="
)

echo.
echo ======================================================
echo DocExpiry - Build APK Only
echo ======================================================
echo.

REM Check if gradlew.bat exists
if not exist gradlew.bat (
    echo ERROR: gradlew.bat not found!
    echo Current directory: %cd%
    pause
    exit /b 1
)

echo [1/3] Cleaning project...
call .\gradlew.bat clean --console plain > clean.log 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Clean failed!
    type clean.log | findstr /I "error"
    pause
    exit /b 1
)
echo [OK] Clean complete

echo.
echo [2/3] Building Debug APK...
call .\gradlew.bat assembleDebug --console plain > build_full.log 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Build failed!
    type build_full.log | findstr /I "error"
    pause
    exit /b 1
)
echo [OK] Build complete

REM Check if APK was created
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ERROR: APK not found!
    echo Expected: app\build\outputs\apk\debug\app-debug.apk
    pause
    exit /b 1
)

echo [OK] APK created successfully
for %%F in ("app\build\outputs\apk\debug\app-debug.apk") do (
    echo     Location: app\build\outputs\apk\debug\app-debug.apk
    echo     Size: %%~zF bytes
)

echo.
echo [3/3] APK Ready!
echo.
echo ======================================================
echo BUILD SUCCESSFUL
echo ======================================================
echo.
echo APK location:
echo   app\build\outputs\apk\debug\app-debug.apk
echo.
echo Next steps:
echo   Option 1: Drag & drop APK to connected Android device
echo   Option 2: Use Android Studio to install (Run menu)
echo   Option 3: From cmd.exe, run:
echo     adb install -r app\build\outputs\apk\debug\app-debug.apk
echo.
pause

