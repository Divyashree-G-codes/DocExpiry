# build_on_laptop.ps1 - Build debug and release APKs using the Gradle wrapper
# Run with: powershell -NoProfile -ExecutionPolicy Bypass -File .\build_on_laptop.ps1

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Write-Host "Project dir: $root"
Set-Location $root

$gradlew = Join-Path $root 'gradlew.bat'
if (-not (Test-Path $gradlew)) {
    Write-Error "gradlew.bat not found in project root: $gradlew"
    exit 1
}

# Ensure JAVA_HOME is valid for this session. If not, attempt to auto-detect common JDK installs.
function Ensure-JavaHome {
    if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME 'bin\java.exe'))) {
        Write-Host "JAVA_HOME is already set and valid: $env:JAVA_HOME"
        return
    }

    $candidates = @(
        'C:\Program Files\Eclipse Adoptium',
        'C:\Program Files\AdoptOpenJDK',
        'C:\Program Files\Java',
        'C:\Program Files (x86)\Java'
    )

    foreach ($base in $candidates) {
        if (-not (Test-Path $base)) { continue }
        $jdkDirs = Get-ChildItem -Path $base -Directory -ErrorAction SilentlyContinue | Where-Object { $_.Name -match 'jdk|temurin|openjdk' } | Sort-Object Name -Descending
        if ($jdkDirs -and $jdkDirs.Count -gt 0) {
            $chosen = $jdkDirs[0].FullName
            if (Test-Path (Join-Path $chosen 'bin\java.exe')) {
                $env:JAVA_HOME = $chosen
                $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
                Write-Host "Auto-set JAVA_HOME to: $env:JAVA_HOME"
                return
            }
        }
    }

    # Fallback: try 'java' on PATH
    try {
        $ver = & java -version 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "Using 'java' found on PATH as fallback. (No JAVA_HOME set)"
            return
        }
    } catch {
        # ignore
    }

    Write-Warning "No valid JAVA_HOME found and no java on PATH. Please install a JDK or set JAVA_HOME."
}

Ensure-JavaHome

# Verify java is runnable
try {
    if ($env:JAVA_HOME) {
        & "${env:JAVA_HOME}\bin\java.exe" -version 2>&1 | ForEach-Object { Write-Host $_ }
    } else {
        & java -version 2>&1 | ForEach-Object { Write-Host $_ }
    }
} catch {
    Write-Warning "Failed to run java. Ensure JAVA_HOME is valid or java is on PATH."
}

# Replace complex Split-Args and previous Run-Gradle with a robust implementation
# that uses cmd.exe /c to run the wrapper in the project directory and redirect output.
function Run-Gradle {
    param(
        [string]$Args,
        [string]$LogPath
    )
    Write-Host "Running: $gradlew $Args"

    if (Test-Path $LogPath) { Remove-Item $LogPath -Force }

    # Build a cmd.exe command that changes to the project directory and runs gradlew with the provided args.
    $cmd = "cd /d `"$root`" && `"$gradlew`" $Args"

    # Run via cmd.exe so Windows quoting is handled consistently; capture output to the log.
    & cmd.exe /c $cmd 2>&1 | Tee-Object -FilePath $LogPath
    $code = $LASTEXITCODE

    if ($code -ne 0) {
        Write-Error "Gradle failed (exit code $code). See log: $LogPath"
        exit $code
    }
}

# Build debug
$debugLog = Join-Path $root 'gradle-debug.log'
Run-Gradle 'clean assembleDebug --warning-mode all --stacktrace' $debugLog

$debugApk = Join-Path $root 'app\build\outputs\apk\debug\app-debug.apk'
if (Test-Path $debugApk) {
    Write-Host "DEBUG APK created at: $debugApk"
} else {
    Write-Warning "DEBUG APK not found. Check $debugLog"
}

# Build release
$releaseLog = Join-Path $root 'gradle-release.log'
Run-Gradle 'assembleRelease --stacktrace' $releaseLog

# Check known release APK paths
$releasePaths = @(
    "$root\app\build\outputs\apk\release\app-release-unsigned.apk",
    "$root\app\build\outputs\apk\release\app-release.apk"
)
$foundRelease = $false
foreach ($p in $releasePaths) {
    if (Test-Path $p) {
        Write-Host "RELEASE APK found at: $p"
        $foundRelease = $true
    }
}
if (-not $foundRelease) {
    Write-Warning "No release APK found in expected locations. See $releaseLog"
}

Write-Host "Build script completed. Logs: $debugLog , $releaseLog"
exit 0
