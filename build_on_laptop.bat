@echo off
REM build_on_laptop.bat - Build debug and release APKs for the DocExpiry project
REM Usage: double-click or run from project root. Adjust JAVA_HOME if needed.
setlocal

echo Project dir: %~dp0
cd /d "%~dp0"

echo === Building DEBUG APK ===
gradlew.bat clean assembleDebug --warning-mode all --stacktrace
if errorlevel 1 (
  echo.
  echo BUILD FAILED for DEBUG. See Gradle output above.
  pause
  exit /b 1
)
echo Debug APK created at:
echo "%~dp0app\build\outputs\apk\debug\app-debug.apk"

echo.
echo === Building RELEASE APK (unsigned) ===
gradlew.bat assembleRelease --warning-mode all --stacktrace
if errorlevel 1 (
  echo.
  echo BUILD FAILED for RELEASE. See Gradle output above.
  pause
  exit /b 1
)
echo Release APK (unsigned) path:
echo "%~dp0app\build\outputs\apk\release\app-release-unsigned.apk"
echo.
echo To produce a signed release APK, add a signingConfig to app/build.gradle.kts or use apksigner from the Android SDK build-tools.
echo Example apksigner usage:
echo "C:\Users\Shara\AppData\Local\Android\Sdk\build-tools\<version>\apksigner.bat" sign --ks C:\path\to\keystore.jks --out app-release-signed.apk app-release-unsigned.apk
echo.
echo Done.
endlocal
pause
