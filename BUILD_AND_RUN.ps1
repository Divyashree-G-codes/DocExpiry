#Requires -Version 5.0
<#
.SYNOPSIS
    Complete build, install, and run script for DocExpiry Android app
.DESCRIPTION
    This script performs the following:
    1. Clean the project
    2. Assemble the debug APK with verbose logging
    3. Install the APK on a connected device/emulator
    4. Launch the app
    5. Capture device logcat for debugging
.EXAMPLE
    .\BUILD_AND_RUN.ps1
#>

$ErrorActionPreference = "Continue"
$script:projectRoot = "C:\Users\Shara\AndroidStudioProjects\DocExpiry"

function Write-Header {
    param([string]$Text)
    Write-Host "`n" -ForegroundColor Black
    Write-Host "═══════════════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host $Text -ForegroundColor Yellow
    Write-Host "═══════════════════════════════════════════════════" -ForegroundColor Cyan
}

function Write-Info {
    param([string]$Text)
    Write-Host "[INFO] $Text" -ForegroundColor Green
}

function Write-Error-Custom {
    param([string]$Text)
    Write-Host "[ERROR] $Text" -ForegroundColor Red
}

# Step 1: Verify project root
Write-Header "STEP 1: Verifying Project Root"
if (-not (Test-Path "$projectRoot\gradlew.bat")) {
    Write-Error-Custom "gradlew.bat not found in $projectRoot"
    exit 1
}
Write-Info "Project root verified: $projectRoot"

# Step 2: Clean
Write-Header "STEP 2: Cleaning Project"
Push-Location $projectRoot
Write-Info "Running: .\gradlew.bat clean"
& .\gradlew.bat clean --console plain 2>&1 | Tee-Object -FilePath clean.log | Select-Object -Last 20
Write-Info "Clean completed. Log saved to: clean.log"

# Step 3: Build with verbose logging
Write-Header "STEP 3: Building Debug APK"
Write-Info "Running: .\gradlew.bat assembleDebug --info --stacktrace"
& .\gradlew.bat assembleDebug --info --stacktrace --console plain 2>&1 | Tee-Object -FilePath build_full.log | Select-Object -Last 50
Write-Info "Build completed. Full log saved to: build_full.log"

# Step 4: Check if build succeeded
Write-Header "STEP 4: Checking Build Status"
if (Test-Path "app\build\outputs\apk\debug\app-debug.apk") {
    Write-Info "✓ Build SUCCESSFUL! APK found at: app\build\outputs\apk\debug\app-debug.apk"
    $buildSuccessful = $true
} else {
    Write-Error-Custom "✗ Build FAILED! APK not found."
    Write-Error-Custom ""
    Write-Error-Custom "=== BUILD ERRORS ==="
    # Extract error lines from log
    Select-String -Path build_full.log -Pattern "error:|FAILURE|Exception" -Context 0,5 | Select-Object -First 50
    Write-Error-Custom "==== END ERRORS ===="
    $buildSuccessful = $false
}

if (-not $buildSuccessful) {
    Pop-Location
    exit 1
}

# Step 5: Install APK
Write-Header "STEP 5: Installing APK on Device"
Write-Info "Available devices:"
& adb devices -l

Write-Info "Running: .\gradlew.bat installDebug"
& .\gradlew.bat installDebug --console plain 2>&1 | Tee-Object -FilePath install.log | Select-Object -Last 20

if ($LASTEXITCODE -eq 0) {
    Write-Info "✓ APK installed successfully!"
} else {
    Write-Error-Custom "✗ APK installation failed!"
    Write-Error-Custom "Last 20 lines of install log:"
    Get-Content install.log -Tail 20
    Pop-Location
    exit 1
}

# Step 6: Launch app
Write-Header "STEP 6: Launching App"
Write-Info "Running: adb shell am start -n com.example.docexpiry/.MainActivity"
& adb shell am start -n com.example.docexpiry/.MainActivity

if ($LASTEXITCODE -eq 0) {
    Write-Info "✓ App launched successfully!"
    Start-Sleep -Seconds 2
} else {
    Write-Error-Custom "✗ Failed to launch app"
    Pop-Location
    exit 1
}

# Step 7: Capture logcat
Write-Header "STEP 7: Capturing Logcat"
Write-Info "Capturing last 300 lines of logcat..."
& adb logcat -d -t 300 | Tee-Object -FilePath run_logcat.txt | Select-Object -Last 50

Write-Info "Logcat saved to: run_logcat.txt"

# Step 8: Summary
Write-Header "BUILD COMPLETE ✓"
Write-Info "Summary:"
Write-Info "  - Clean log: clean.log"
Write-Info "  - Build log: build_full.log"
Write-Info "  - Install log: install.log"
Write-Info "  - Logcat: run_logcat.txt"
Write-Info ""
Write-Info "APK installed and running on device!"
Write-Info "Use 'adb logcat' to view real-time logs"

Pop-Location
exit 0

