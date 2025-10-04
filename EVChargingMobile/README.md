# EV Charging Mobile App

## Setup Instructions

### 1. Environment Configuration

1. Copy the environment template:
   ```bash
   cp environment.example .env
   ```

2. Edit `.env` file with your actual credentials:
   ```bash
   # Google Maps API Key
   GOOGLE_MAPS_API_KEY=your_actual_google_maps_api_key
   
   # Backend API URL
   BACKEND_API_URL=http://your_backend_ip:port/api/
   ```

### 2. Build Configuration

The app uses Gradle build system. Make sure you have:
- Android SDK
- Gradle 8.x
- Java 21

### 3. Security Notes

- **NEVER commit `.env` files to version control**
- The `.env` file contains sensitive API keys and credentials
- Use environment variables for all sensitive configuration

### 4. Building the App

```bash
# Clean and build
gradle clean assembleDebug

# Install on device/emulator
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `GOOGLE_MAPS_API_KEY` | Google Maps API key for map functionality | Yes |
| `BACKEND_API_URL` | Backend API base URL | Yes |