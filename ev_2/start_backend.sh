#!/bin/bash

# EV Charging Station Backend Startup Script
# This script sets up environment variables and starts the backend

echo "🚀 Starting EV Charging Station Backend..."

# Set environment variables
export MONGODB_CONNECTION_STRING="mongodb://localhost:27017"
export MONGODB_DATABASE_NAME="EVChargingDB"
export JWT_SECRET="YourSuperSecureSecretKeyThatIsAtLeast32CharactersLongForDevelopment!"
export JWT_ISSUER="EV_2API"
export JWT_AUDIENCE="EV_2Frontend"
export JWT_EXPIRY_MINUTES="60"

# Navigate to the project directory
cd "$(dirname "$0")/ev_2"

# Check if .NET is installed
if ! command -v dotnet &> /dev/null; then
    echo "❌ .NET is not installed. Please install .NET first:"
    echo "   brew install dotnet"
    exit 1
fi

# Start the backend
echo "📡 Starting backend on http://localhost:5263"
echo "📚 Swagger UI will be available at: http://localhost:5263/swagger"
echo "⏹️  Press Ctrl+C to stop the server"
echo ""

dotnet run --urls="http://localhost:5263"
