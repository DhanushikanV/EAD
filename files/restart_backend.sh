#!/bin/bash

# EV Charging Backend - Restart Script
# This script stops any running backend and starts a fresh one

echo "🔄 EV Charging Backend - Restart Script"
echo "======================================="

# Navigate to backend directory
cd "$(dirname "$0")/../ev_2/ev_2"

echo "🛑 Stopping any running backend processes..."
pkill -f "dotnet run" 2>/dev/null || true
sleep 2

echo "🔧 Building and starting backend..."

# Build first to catch any errors
echo "   Building backend..."
dotnet build > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ Backend build failed! Please check for compilation errors."
    echo "   Run 'dotnet build' to see detailed errors."
    exit 1
fi

echo "✅ Build successful"

# Start backend with environment variables
MONGODB_CONNECTION_STRING="mongodb://localhost:27017" \
MONGODB_DATABASE_NAME="EVChargingDB" \
JWT_SECRET="YourSuperSecureSecretKeyThatIsAtLeast32CharactersLongForDevelopment!" \
JWT_ISSUER="EV_2API" \
JWT_AUDIENCE="EV_2Frontend" \
JWT_EXPIRY_MINUTES="60" \
dotnet run --urls="http://0.0.0.0:5263"

echo ""
echo "🛑 Backend stopped"
