#!/bin/bash

# EV Charging System - Complete Reinstall Script
# This script handles both backend and mobile app

echo "🚀 EV Charging System - Complete Reinstall"
echo "==========================================="

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo "🔍 Checking prerequisites..."

if ! command_exists adb; then
    echo "❌ ADB not found. Please install Android SDK platform tools."
    exit 1
fi

if ! command_exists dotnet; then
    echo "❌ .NET SDK not found. Please install .NET SDK."
    exit 1
fi

echo "✅ Prerequisites check passed"
echo ""

# Navigate to project root
cd "$(dirname "$0")"

# Step 1: Stop any running backend
echo "🛑 Stopping any running backend processes..."
pkill -f "dotnet run" 2>/dev/null || true
sleep 2

# Step 2: Build and start backend
echo "🔧 Building and starting backend..."
cd ev_2/ev_2

# Build first to catch any errors
echo "   Building backend..."
dotnet build > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ Backend build failed! Please check for compilation errors."
    exit 1
fi

MONGODB_CONNECTION_STRING="mongodb://localhost:27017" \
MONGODB_DATABASE_NAME="EVChargingDB" \
JWT_SECRET="YourSuperSecureSecretKeyThatIsAtLeast32CharactersLongForDevelopment!" \
JWT_ISSUER="EV_2API" \
JWT_AUDIENCE="EV_2Frontend" \
JWT_EXPIRY_MINUTES="60" \
dotnet run --urls="http://0.0.0.0:5263" &

BACKEND_PID=$!
echo "✅ Backend started (PID: $BACKEND_PID)"

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Check if backend is running
if curl -s http://localhost:5263/api/booking >/dev/null 2>&1; then
    echo "✅ Backend is responding"
else
    echo "❌ Backend failed to start properly"
    kill $BACKEND_PID 2>/dev/null || true
    exit 1
fi

# Step 3: Uninstall old app
echo ""
echo "📱 Uninstalling old mobile app..."
cd ../../EVChargingMobile
adb uninstall com.evcharging.mobile 2>/dev/null || true
echo "✅ Old app uninstalled"

# Step 4: Build new app
echo ""
echo "🔨 Building new mobile app..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Mobile app built successfully"
else
    echo "❌ Mobile app build failed!"
    kill $BACKEND_PID 2>/dev/null || true
    exit 1
fi

# Step 5: Install new app
echo ""
echo "📲 Installing new mobile app..."
adb install app/build/outputs/apk/debug/app-debug.apk

if [ $? -eq 0 ]; then
    echo "✅ Mobile app installed successfully!"
else
    echo "❌ Mobile app installation failed!"
    kill $BACKEND_PID 2>/dev/null || true
    exit 1
fi

# Final status
echo ""
echo "🎉 COMPLETE SETUP SUCCESSFUL!"
echo "=============================="
echo "✅ Backend running on: http://192.168.8.113:5263"
echo "✅ Mobile app installed on emulator"
echo ""
echo "🧪 Test Credentials:"
echo "   EV Owner: dha@gmail.com / 123456"
echo "   Operator:  operator@evcharging.com / 123456"
echo ""
echo "📱 Launch the app on your emulator and test!"
echo ""
echo "⚠️  Backend is running in background (PID: $BACKEND_PID)"
echo "   To stop backend: kill $BACKEND_PID"
