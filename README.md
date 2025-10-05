# 🚗 EV Charging Station Management System

A comprehensive EV charging station management system with backend API, web frontend, and mobile applications.

## 📁 Project Structure

```
EAD/
├── ev_2/                    # .NET Core Backend API
├── frontend/                # React.js Web Application
├── EVChargingMobile/        # Android Mobile Application
└── files/                   # Utility Scripts & Setup Tools
```

## 🛠️ Quick Setup & Management Scripts

The `files/` directory contains utility scripts to easily manage the entire system. All scripts support both macOS/Linux and Windows.

### 🚀 Backend Management

#### Start Backend
```bash
# macOS/Linux
./files/start_backend.sh

# Windows
files\start_backend.bat
```
- Starts the .NET Core backend on `http://localhost:5263`
- Sets up MongoDB connection and JWT configuration
- Swagger UI available at `http://localhost:5263/swagger`

#### Stop Backend
```bash
# macOS/Linux
./files/kill_backend.sh

# Windows  
files\kill_backend.bat
```
- Kills all running backend processes on port 5263
- Cleans up dotnet processes
- Ensures port is free for next startup

#### Restart Backend
```bash
# macOS/Linux
./files/restart_backend.sh
```
- Stops any running backend
- Builds and starts fresh backend instance
- Useful for applying code changes

### 🌐 Frontend Management

#### Stop Frontend
```bash
# macOS/Linux (default port 3000)
./files/kill_frontend.sh

# Custom port
FRONTEND_PORT=5173 ./files/kill_frontend.sh
```
- Kills React development server
- Supports custom port configuration
- Cleans up node processes

### 📱 Mobile App Management

#### Reinstall Mobile App
```bash
# macOS/Linux
./files/reinstall_app.sh

# Windows
files\reinstall_app.bat
```
- Uninstalls old app from emulator/device
- Builds new APK with latest changes
- Installs fresh app version
- **Test Credentials:**
  - EV Owner: `dha@gmail.com` / `123456`
  - Operator: `operator@evcharging.com` / `123456`

### 🔄 Complete System Reinstall
```bash
# macOS/Linux
./files/quick_reinstall.sh
```
- **Full system reset and setup**
- Stops any running backend
- Builds and starts backend
- Uninstalls old mobile app
- Builds and installs new mobile app
- Perfect for clean testing

### 🌱 Database Seeding

#### Seed Users
```bash
# macOS/Linux
./files/seed_users.sh

# Custom API endpoint
./files/seed_users.sh https://localhost:7179/api
```
Creates default users:
- **Backoffice Admin**: `admin@demo.lk` / `Admin@123`
- **Operator 1**: `operator1@demo.lk` / `Operator@123`
- **Operator 2**: `operator2@demo.lk` / `Operator@123`

#### Seed Charging Stations
```bash
# macOS/Linux
./files/seed_stations.sh

# Custom API endpoint
./files/seed_stations.sh https://localhost:7179/api
```
Creates 8 sample charging stations across Sri Lanka:
- Colombo City Centre Fast Charge
- Orion Tech Park AC Hub
- Galle Fort Harbor Station
- BIA Airport QuickCharge
- University of Peradeniya Green Lot
- Galle Face Promenade Chargers
- Kandy City Centre Multi-Storey
- Jaffna Library Plaza Chargers

## 🎯 Common Workflows

### Daily Development Setup
```bash
# 1. Start backend
./files/start_backend.sh

# 2. In another terminal, start frontend
cd frontend && npm start

# 3. For mobile testing
./files/reinstall_app.sh
```

### Clean Restart Everything
```bash
# Complete system reset
./files/quick_reinstall.sh
```

### Add Test Data
```bash
# Ensure backend is running first
./files/start_backend.sh

# In another terminal, seed data
./files/seed_users.sh
./files/seed_stations.sh
```

## 🔧 Prerequisites

### Required Software
- **.NET SDK 8.0+** - Backend development
- **Node.js 16+** - Frontend development  
- **Android Studio & ADB** - Mobile app development
- **MongoDB** - Database (local or cloud)
- **Git** - Version control

### macOS Installation
```bash
# Install .NET SDK
brew install dotnet

# Install Node.js
brew install node

# Install MongoDB
brew tap mongodb/brew
brew install mongodb-community@7.0
brew services start mongodb-community@7.0

# Android Studio (download from developer.android.com)
```

### Environment Setup
1. Copy environment example files:
   ```bash
   cp ev_2/ev_2/environment.example ev_2/ev_2/.env
   cp EVChargingMobile/environment.example EVChargingMobile/local.properties
   cp frontend/.env.example frontend/.env
   ```

2. Fill in your actual API keys and database credentials

## 🌐 Access Points

| Service | URL | Description |
|---------|-----|-------------|
| Backend API | `http://localhost:5263` | Main API endpoints |
| Swagger UI | `http://localhost:5263/swagger` | API documentation |
| Health Check | `http://localhost:5263/health` | Service status |
| Frontend | `http://localhost:3000` | Web application |
| Mobile App | Device/Emulator | Android application |

## 🔐 Security Notes

⚠️ **IMPORTANT**: Before pushing to git, ensure:
- No API keys are hardcoded in source files
- All sensitive data is in `.env` files (which are gitignored)
- Database credentials use environment variables
- JWT secrets are properly configured

## 📱 Mobile App Features

- **User Registration & Login**
- **Station Discovery & Mapping**
- **Booking Management**
- **QR Code Generation & Scanning**
- **Real-time Status Updates**
- **Operator Dashboard**

## 🌐 Web Frontend Features

- **Admin Dashboard**
- **User Management**
- **Station Management**
- **Booking Management**
- **Operator Interface**
- **Interactive Maps**

## 🔌 API Features

- **RESTful API Design**
- **JWT Authentication**
- **MongoDB Integration**
- **CORS Configuration**
- **Swagger Documentation**
- **Health Monitoring**

## 🆘 Troubleshooting

### Backend Issues
```bash
# Check if MongoDB is running
brew services list | grep mongodb

# Restart MongoDB
brew services restart mongodb-community@7.0

# Check backend logs
./files/start_backend.sh
```

### Frontend Issues
```bash
# Clear node modules and reinstall
cd frontend
rm -rf node_modules package-lock.json
npm install
npm start
```

### Mobile App Issues
```bash
# Check ADB connection
adb devices

# Restart ADB server
adb kill-server && adb start-server

# Clean rebuild
cd EVChargingMobile
./gradlew clean assembleDebug
```

### Port Conflicts
```bash
# Kill processes on specific ports
./files/kill_backend.sh    # Port 5263
./files/kill_frontend.sh   # Port 3000/5173
```

## 📞 Support

For issues or questions:
1. Check the troubleshooting section above
2. Review the individual README files in each project folder
3. Check the Swagger documentation at `http://localhost:5263/swagger`

---

**Happy Coding! 🚀**
