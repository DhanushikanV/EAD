# 🚀 Easy App Reinstall Scripts

This directory contains scripts to easily uninstall and reinstall the EV Charging mobile app on your emulator.

## 📱 Quick Mobile App Reinstall

### For macOS/Linux:
```bash
./reinstall_app.sh
```

### For Windows:
```batch
reinstall_app.bat
```

## 🔧 Complete System Reinstall (Backend + Mobile App)

### For macOS/Linux:
```bash
./quick_reinstall.sh
```

## 📋 What These Scripts Do

### `reinstall_app.sh` / `reinstall_app.bat`
- ✅ Uninstalls old app from emulator
- ✅ Builds new APK
- ✅ Installs new APK on emulator
- ✅ Shows test credentials

### `quick_reinstall.sh` (Complete System)
- ✅ Stops any running backend
- ✅ Starts backend with proper configuration
- ✅ Uninstalls old mobile app
- ✅ Builds new mobile app
- ✅ Installs new mobile app
- ✅ Verifies everything is working
- ✅ Shows all test credentials

## 🧪 Test Credentials

**EV Owner (Regular User):**
- Email: `dha@gmail.com`
- Password: `123456`

**Operator User:**
- Email: `operator@evcharging.com`
- Password: `123456`

## 📱 Usage

1. **Make sure your emulator is running**
2. **Run the appropriate script:**
   - For just the mobile app: `./reinstall_app.sh`
   - For complete system: `./quick_reinstall.sh`
3. **Wait for completion**
4. **Launch the app on your emulator**
5. **Test with the provided credentials**

## ⚠️ Prerequisites

- Android emulator running
- ADB (Android Debug Bridge) installed
- .NET SDK installed (for complete system script)
- Gradle installed
- MongoDB running (for complete system script)

## 🐛 Troubleshooting

If you encounter issues:

1. **Check emulator is running:**
   ```bash
   adb devices
   ```

2. **Check backend is running (for complete system):**
   ```bash
   curl http://localhost:5263/api/booking
   ```

3. **Manual backend start:**
   ```bash
   cd ev_2/ev_2
   ./start_backend.sh
   ```

4. **Manual app reinstall:**
   ```bash
   cd EVChargingMobile
   adb uninstall com.evcharging.mobile
   ./gradlew assembleDebug
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## 📞 Support

If you continue to have issues, check the logs in the terminal output for specific error messages.
