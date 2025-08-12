@echo off
REM Wrapper forwarder for CI: runs the Gradle wrapper inside android_frontend
setlocal
cd /d "%~dp0android_frontend"
call gradlew.bat %*
endlocal
