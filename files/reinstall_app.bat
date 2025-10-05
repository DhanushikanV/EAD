@echo off
REM EV Charging Mobile App - Easy Reinstall Script (Windows)
REM This script uninstalls the old app and installs the new version

echo 🚀 EV Charging Mobile App - Reinstall Script
echo ==============================================

REM Navigate to the mobile app directory
cd /d "%~dp0..\EVChargingMobile"

echo 📱 Uninstalling old app...
adb uninstall com.evcharging.mobile

if %errorlevel% == 0 (
    echo ✅ App uninstalled successfully
) else (
    echo ⚠️  App may not have been installed (this is okay)
)

echo.
echo 🔨 Building new APK...
call gradlew.bat assembleDebug

if %errorlevel% == 0 (
    echo ✅ APK built successfully
) else (
    echo ❌ Build failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo 📲 Installing new APK...
adb install app\build\outputs\apk\debug\app-debug.apk

if %errorlevel% == 0 (
    echo ✅ App installed successfully!
    echo.
    echo 🎉 Ready to test!
    echo    - Launch the app on your emulator
    echo    - Login with: dha@gmail.com / 123456 (EV Owner)
    echo    - Or login with: operator@evcharging.com / 123456 (Operator)
) else (
    echo ❌ Installation failed!
    pause
    exit /b 1
)

pause
