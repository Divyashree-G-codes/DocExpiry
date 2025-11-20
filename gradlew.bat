@echo off
:: -----------------------------------------------------------------------------
:: Gradle start up script for Windows
:: -----------------------------------------------------------------------------

setlocal

set DIRNAME=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%\

rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=-Xmx64m

rem Determine the Java command to use to start the JVM.
if defined JAVA_HOME (
  if exist "%JAVA_HOME%\bin\java.exe" (
    set "JAVACMD=%JAVA_HOME%\bin\java.exe"
  ) else (
    echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
    echo.
    echo Please set the JAVA_HOME variable in your environment to match the
    echo location of your Java installation.
    exit /b 1
  )
) else (
  set "JAVACMD=java"
)

rem Pass all args through to the wrapper main
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

rem Build the command line and execute
set CMD_LINE_ARGS=%DEFAULT_JVM_OPTS% %JVM_OPTS% %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
"%JAVACMD%" %CMD_LINE_ARGS%
endlocal

