@echo off
REM EV Charging Station Backend Startup Script for Windows
REM This script sets up environment variables and starts the backend

echo 🚀 Starting EV Charging Station Backend...

REM Set environment variables
set MONGODB_CONNECTION_STRING=mongodb://localhost:27017
set MONGODB_DATABASE_NAME=EVChargingDB
set JWT_SECRET=YourSuperSecureSecretKeyThatIsAtLeast32CharactersLongForDevelopment!
set JWT_ISSUER=EV_2API
set JWT_AUDIENCE=EV_2Frontend
set JWT_EXPIRY_MINUTES=60

REM Navigate to the project directory
cd /d "%~dp0..\ev_2"

REM Check if .NET is installed
dotnet --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ .NET is not installed. Please install .NET first from: https://dotnet.microsoft.com/download
    pause
    exit /b 1
)

REM Start the backend
echo 📡 Starting backend on http://localhost:5263
echo 📚 Swagger UI will be available at: http://localhost:5263/swagger
echo ⏹️  Press Ctrl+C to stop the server
echo.

dotnet run --urls="http://localhost:5263"
