@echo off
REM Temporary build script to set JAVA_HOME and run Gradle wrapper
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d %~dp0
gradlew.bat clean assembleDebug --no-daemon
