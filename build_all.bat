@echo off
REM Build all artifacts using a known-good JDK and the Gradle wrapper
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d C:\Users\Shara\AndroidStudioProjects\DocExpiry
REM Use call so the wrapper returns control to this script
call gradlew.bat --daemon clean assemble -x lint --stacktrace --info --console=plain
exit /b %ERRORLEVEL%

