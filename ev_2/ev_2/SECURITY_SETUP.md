# Security Setup Instructions

## ⚠️ CRITICAL: Sensitive Data Has Been Moved to Environment Variables

Your project previously had **hardcoded sensitive data** in configuration files, which is a major security risk. These have been moved to environment variables for security.

## Required Setup

### 1. Create Environment Variables File

Create a `.env` file in the project root with the following content:

```bash
# MongoDB Configuration
MONGODB_CONNECTION_STRING=mongodb+srv://jeno:jeno@evcharging.li43us0.mongodb.net/?retryWrites=true&w=majority&appName=EVCharging
MONGODB_DATABASE_NAME=EVChargingDB
MONGODB_BOOKING_COLLECTION=Bookings
MONGODB_CHARGING_STATION_COLLECTION=ChargingStations
MONGODB_EV_OWNER_COLLECTION=EVOwners
MONGODB_USER_COLLECTION=Users

# JWT Configuration
JWT_SECRET=ThisIsASecureSecretKeyThatIs32Chars!
JWT_ISSUER=EV_2API
JWT_AUDIENCE=EV_2Frontend
JWT_EXPIRY_MINUTES=60

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

### 2. Install Environment Variables Support

Add the following package to support `.env` files:

```bash
dotnet add package Microsoft.Extensions.Configuration.EnvironmentVariables
```

### 3. Update Program.cs (if needed)

If environment variables are not loading properly, you may need to add this to `Program.cs`:

```csharp
builder.Configuration.AddEnvironmentVariables();
```

## Security Recommendations

### 1. Change Default Credentials
- **IMMEDIATELY** change the MongoDB username and password from `jeno:jeno`
- Generate a new, strong JWT secret key (at least 32 characters)
- Use different credentials for different environments (dev, staging, production)

### 2. Environment-Specific Configuration
- Use different `.env` files for different environments
- Never commit `.env` files to version control
- Use secure secret management in production (Azure Key Vault, AWS Secrets Manager, etc.)

### 3. Database Security
- Enable MongoDB authentication
- Use connection string with specific database user (not admin)
- Restrict network access to your MongoDB cluster
- Enable SSL/TLS for database connections

### 4. JWT Security
- Generate a cryptographically secure secret key
- Consider using shorter token expiration times
- Implement token refresh mechanisms
- Validate token issuer and audience

## What Was Changed

1. **appsettings.json**: Removed hardcoded sensitive values, replaced with environment variable placeholders
2. **Program.cs**: Updated CORS configuration to use environment variables
3. **Added .gitignore**: Prevents accidental commit of `.env` files
4. **Created environment.example**: Template for setting up environment variables

## Verification

After setting up the `.env` file, verify that:
1. The application starts without errors
2. Database connections work
3. JWT authentication functions properly
4. CORS is configured correctly

## Production Deployment

For production deployment:
1. Use environment variables or secure secret management
2. Never use the example values
3. Rotate secrets regularly
4. Monitor for security vulnerabilities
5. Use HTTPS in production
