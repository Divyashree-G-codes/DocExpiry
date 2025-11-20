@echo off
REM BUILD_SIMPLE.bat - Most reliable build script for DocExpiry
REM This script clears JAVA_HOME and lets Gradle auto-detect Java

setlocal enabledelayedexpansion

REM Navigate to project root
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry

REM CRITICAL: Clear invalid JAVA_HOME - Gradle will auto-detect Java
set "JAVA_HOME="

REM Show current directory
echo Project directory: %cd%
echo.

REM Check if gradlew.bat exists
if not exist gradlew.bat (
    echo ERROR: gradlew.bat not found in %cd%
    pause
    exit /b 1
)

REM Start build
echo ======================================================
echo Building DocExpiry APK
echo ======================================================
echo.

echo Cleaning...
call .\gradlew.bat clean
if %errorlevel% neq 0 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)

echo.
echo Building Debug APK...
call .\gradlew.bat assembleDebug
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo.
echo ======================================================
echo BUILD SUCCESSFUL!
echo ======================================================
echo.
echo APK created at:
echo   app\build\outputs\apk\debug\app-debug.apk
echo.
echo Next: Drag this APK to your Android device, or use:
echo   adb install -r app\build\outputs\apk\debug\app-debug.apk
echo.
pause

