@echo off
REM Helper to set local JAVA_HOME and run gradle build in project root
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
echo Using JAVA_HOME=%JAVA_HOME%
"%JAVA_HOME%\bin\java" -version

REM Fast debug build: skips lint to speed up. Pass argument "daemon" to run with Gradle daemon.
set "GRADLEW=gradlew.bat"
if /I "%~1"=="daemon" (
    echo Running: %GRADLEW% --daemon clean assembleDebug -x lint --console=plain --stacktrace
    call "%GRADLEW%" --daemon clean assembleDebug -x lint --console=plain --stacktrace
) else (
    echo Running: %GRADLEW% clean assembleDebug -x lint --no-daemon --console=plain --stacktrace
    call "%GRADLEW%" clean assembleDebug -x lint --no-daemon --console=plain --stacktrace
)

set "EXITCODE=%ERRORLEVEL%"
echo Build finished with errorlevel %EXITCODE%
exit /b %EXITCODE%
