@echo off
REM Temporary build script to set JAVA_HOME and PATH then run gradle wrapper
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
call .\gradlew.bat assembleDebug --no-daemon --stacktrace
echo BUILD_EXIT_CODE=%ERRORLEVEL%
if exist app\build\outputs\apk\debug\app-debug.apk (
  echo APK_FOUND
  dir /T:W app\build\outputs\apk\debug\app-debug.apk
  certutil -hashfile app\build\outputs\apk\debug\app-debug.apk SHA256
) else (
  echo APK_MISSING
)

