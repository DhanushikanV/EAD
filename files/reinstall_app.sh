#!/bin/bash

# EV Charging Mobile App - Easy Reinstall Script
# This script uninstalls the old app and installs the new version

echo "🚀 EV Charging Mobile App - Reinstall Script"
echo "=============================================="

# Navigate to the mobile app directory
cd "$(dirname "$0")/../EVChargingMobile"

echo "📱 Uninstalling old app..."
adb uninstall com.evcharging.mobile

if [ $? -eq 0 ]; then
    echo "✅ App uninstalled successfully"
else
    echo "⚠️  App may not have been installed (this is okay)"
fi

echo ""
echo "🔨 Building new APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ APK built successfully"
else
    echo "❌ Build failed! Please check the errors above."
    exit 1
fi

echo ""
echo "📲 Installing new APK..."
adb install app/build/outputs/apk/debug/app-debug.apk

if [ $? -eq 0 ]; then
    echo "✅ App installed successfully!"
    echo ""
    echo "🎉 Ready to test!"
    echo "   - Launch the app on your emulator"
    echo "   - Login with: dha@gmail.com / 123456 (EV Owner)"
    echo "   - Or login with: operator@evcharging.com / 123456 (Operator)"
else
    echo "❌ Installation failed!"
    exit 1
fi
