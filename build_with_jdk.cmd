@echo off
REM Temporary build script to use a specific JDK and run gradle wrapper
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
cd /d %~dp0
echo Using JAVA_HOME=%JAVA_HOME%
call gradlew.bat assembleDebug --no-daemon
pause

