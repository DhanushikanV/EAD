# 🚀 Quick Start Guide - EV Charging Station Backend

## Easy Commands to Run the Backend

### Option 1: Using the Startup Script (Recommended)

**On macOS/Linux:**
```bash
./start_backend.sh
```

**On Windows:**
```cmd
start_backend.bat
```

### Option 2: Single Command (if you prefer)

**Navigate to the project directory:**
```bash
cd ev_2
```

**Run the backend:**
```bash
MONGODB_CONNECTION_STRING="mongodb://localhost:27017" MONGODB_DATABASE_NAME="EVChargingDB" JWT_SECRET="YourSuperSecureSecretKeyThatIsAtLeast32CharactersLongForDevelopment!" JWT_ISSUER="EV_2API" JWT_AUDIENCE="EV_2Frontend" JWT_EXPIRY_MINUTES="60" dotnet run --urls="http://localhost:5263"
```

## 📋 Prerequisites

1. **MongoDB** - Make sure MongoDB is running locally
   ```bash
   # Check if MongoDB is running
   brew services list | grep mongodb
   ```

2. **.NET SDK** - Install if not already installed
   ```bash
   brew install dotnet
   ```

## 🌐 Access Points

Once running, your backend will be available at:
- **API Base URL**: `http://localhost:5263`
- **Swagger Documentation**: `http://localhost:5263/swagger`
- **Health Check**: `http://localhost:5263/health`

## 🛑 Stopping the Backend

Press `Ctrl+C` in the terminal where the backend is running.

## 📊 Available Endpoints

- `GET /api/user` - Get all users
- `GET /api/chargingstation` - Get all charging stations
- `GET /api/booking` - Get all bookings
- `POST /api/user/register` - Register new user
- `POST /api/user/login` - User login
- And many more... (see Swagger UI for complete list)

## 🔧 Troubleshooting

If you get permission errors on macOS/Linux:
```bash
chmod +x start_backend.sh
```

If MongoDB connection fails, make sure MongoDB is running:
```bash
brew services start mongodb-community@7.0
```
