@echo off
REM sign_release.bat - Sign an unsigned release APK using a local keystore
REM Usage: sign_release.bat [keystorePath] [alias]

nsetlocal
set SDK=C:\Users\Shara\AppData\Local\Android\Sdk
nset KEYSTORE=%~1
nset KEYALIAS=%~2
n
nif "%KEYSTORE%"=="" (
  echo Please provide the keystore path as first argument, or create one with keytool.
  echo Example: sign_release.bat C:\Users\Shara\keystore\docexpiry-release.jks docexpirykey
  exit /b 1
)
if "%KEYALIAS%"=="" (
  echo Please provide the key alias as second argument.
  exit /b 1
)

nset UNSIGNED=app\build\outputs\apk\release\app-release-unsigned.apk
nif not exist "%UNSIGNED%" (
  echo Unsigned release APK not found at %UNSIGNED%
  echo Run: gradlew.bat assembleRelease
  exit /b 1
)

n:: Find the highest build-tools version
nfor /f "delims=" %%v in ('dir /b "%SDK%\build-tools" ^| sort /r') do (
  set BT=%%v
n  goto :foundbt
)
necho Build-tools not found under %SDK%\build-tools
nexit /b 1
:foundbt
necho Using build-tools: %BT%
set ZIPALIGN=%SDK%\build-tools\%BT%\zipalign.exe
nset APKSIGNER=%SDK%\build-tools\%BT%\apksigner.bat
n
nset ALIGNED=app\build\outputs\apk\release\app-release-unsigned-aligned.apk
nset SIGNED=app\build\outputs\apk\release\app-release-signed.apk

n"%ZIPALIGN%" -v -p 4 "%UNSIGNED%" "%ALIGNED%"
necho Signing...
"%APKSIGNER%" sign --ks "%KEYSTORE%" --ks-key-alias %KEYALIAS% --out "%SIGNED%" "%ALIGNED%"
necho Signed APK: %SIGNED%
"%APKSIGNER%" verify --print-certs "%SIGNED%"
necho Done.
endlocal

