@echo off
REM Helper to ensure a physical device (preferred) or emulator is available, build, install, and launch the app.
SETLOCAL ENABLEDELAYEDEXPANSION

:: Adjust SDK path if different. This is taken from your local.properties.
set "SDK=C:\Users\Shara\AppData\Local\Android\Sdk"

echo Using SDK: %SDK%

:: Check SDK tools exist
if not exist "%SDK%\platform-tools\adb.exe" (
  echo ERROR: adb not found at "%SDK%\platform-tools\adb.exe".
  echo Please ensure Android SDK platform-tools are installed and SDK path in this script is correct.
  pause
  exit /b 1
)

:: Optional: check emulator exists
if not exist "%SDK%\emulator\emulator.exe" (
  echo NOTE: Emulator binary not found at "%SDK%\emulator\emulator.exe". Emulator start will not be available.
)

:: Ensure adb server restarts
"%SDK%\platform-tools\adb.exe" kill-server 2>nul
"%SDK%\platform-tools\adb.exe" start-server 2>nul

echo Checking for connected devices...
"%SDK%\platform-tools\adb.exe" devices -l

:: Detect unauthorized or offline devices first
for /f "tokens=*" %%L in ('"%SDK%\platform-tools\adb.exe" devices -l 2^>nul') do (
  echo %%L | findstr /I "unauthorized" >nul && set "HAS_UNAUTHORIZED=1"
  echo %%L | findstr /I "offline" >nul && set "HAS_OFFLINE=1"
)
if defined HAS_UNAUTHORIZED (
  echo WARNING: One or more devices are "unauthorized". Unlock your device and accept the RSA debugging prompt, then re-run this script.
)
if defined HAS_OFFLINE (
  echo WARNING: One or more devices are "offline". Reconnect device or try a different USB port/cable.
)

:: Try to capture the first connected physical device (state 'device')
set "DEVICE="
for /f "tokens=1,2*" %%A in ('"%SDK%\platform-tools\adb.exe" devices -l ^| findstr /R /C:" device$"') do (
  if not defined DEVICE set "DEVICE=%%A"
)

if defined DEVICE (
  echo Found device: %DEVICE% (will install to this device)
) else (
  echo No physical devices found. Attempting to start an emulator AVD (if available)...
  set "AVD="
  if exist "%SDK%\emulator\emulator.exe" (
    for /f "delims=" %%V in ('"%SDK%\emulator\emulator.exe" -list-avds 2^>nul') do (
      if not defined AVD set "AVD=%%V"
    )
  )
  if not defined AVD (
    echo No AVDs found or emulator unavailable. Connect a physical device with USB debugging enabled or create an AVD in Android Studio.
    pause
    exit /b 1
  )
  echo Starting AVD: %AVD%
  start "emulator" "%SDK%\emulator\emulator.exe" -avd "%AVD%" -no-snapshot-load -netdelay none -netspeed full

  echo Waiting for emulator device to appear (up to 180s)...
  set /a COUNT=0
  :WAIT_EMU
  "%SDK%\platform-tools\adb.exe" devices -l | findstr /R /C:"emulator-[0-9]* device" >nul
  if %errorlevel%==0 (
    echo Emulator device detected.
  ) else (
    timeout /t 2 >nul
    set /a COUNT+=2
    if %COUNT% GEQ 180 (
      echo Emulator did not appear within 180 seconds.
      exit /b 1
    )
    goto WAIT_EMU
  )

  echo Waiting for emulator to fully boot (sys.boot_completed)... (up to 300s)
  set /a COUNT=0
  :WAIT_BOOT
  "%SDK%\platform-tools\adb.exe" shell getprop sys.boot_completed 2>nul | findstr "1" >nul
  if %errorlevel%==0 (
    echo Boot completed.
  ) else (
    timeout /t 2 >nul
    set /a COUNT+=2
    if %COUNT% GEQ 300 (
      echo Emulator did not finish booting in 300 seconds.
      exit /b 1
    )
    goto WAIT_BOOT
  )

  for /f "tokens=1" %%D in ('"%SDK%\platform-tools\adb.exe" devices ^| findstr /R /C:"emulator-[0-9]* device"') do set "DEVICE=%%D"
  echo Using device: %DEVICE%
)

:: Ensure we have a device id before proceeding
if not defined DEVICE (
  echo ERROR: No device available for installation. Connect a phone or fix the emulator.
  pause
  exit /b 1
)

echo Building APK (assembleDebug)...
cd /d "%~dp0"
gradlew.bat assembleDebug --stacktrace
if errorlevel 1 (
  echo Build failed. See the gradle output above.
  pause
  exit /b 1
)

echo Installing APK to %DEVICE%...
"%SDK%\platform-tools\adb.exe" -s %DEVICE% install -r "app\build\outputs\apk\debug\app-debug.apk"
if errorlevel 1 (
  echo adb install failed. Try uninstalling previous app and retrying:
  echo "%SDK%\platform-tools\adb.exe" uninstall com.example.docexpiry
  pause
  exit /b 1
)

echo Launching app on %DEVICE%...
"%SDK%\platform-tools\adb.exe" -s %DEVICE% shell am start -n com.example.docexpiry/.DocumentChooserActivity -a android.intent.action.MAIN -c android.intent.category.LAUNCHER

echo Done.
ENDLOCAL
pause
