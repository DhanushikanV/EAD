# EV Charging Backend API

## Setup Instructions

### 1. Environment Configuration

1. Copy the environment template:
   ```bash
   cp environment.example .env
   ```

2. Edit `.env` file with your actual credentials:
   ```bash
   # MongoDB Configuration
   MONGODB_CONNECTION_STRING=mongodb+srv://username:password@cluster.mongodb.net/...
   MONGODB_DATABASE_NAME=EVChargingDB
   
   # JWT Configuration
   JWT_SECRET=your_super_secure_secret_key_at_least_32_chars
   
   # Google Maps API Key
   REACT_APP_GOOGLE_MAPS_API_KEY=your_google_maps_api_key
   ```

### 2. Running the Application

```bash
# Install dependencies
dotnet restore

# Run the application
dotnet run --urls=http://0.0.0.0:5263
```

### 3. Security Notes

- **NEVER commit `.env` files to version control**
- The `.env` file contains sensitive database credentials and API keys
- Use environment variables for all sensitive configuration
- Change default JWT secret in production

### 4. API Endpoints

- Health Check: `GET /api/health/mongo`
- User Management: `POST /api/user`
- Authentication: `POST /api/user/login`

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `MONGODB_CONNECTION_STRING` | MongoDB Atlas connection string | Yes |
| `MONGODB_DATABASE_NAME` | Database name | Yes |
| `JWT_SECRET` | Secret key for JWT token generation | Yes |
| `JWT_ISSUER` | JWT issuer claim | Yes |
| `JWT_AUDIENCE` | JWT audience claim | Yes |
| `JWT_EXPIRY_MINUTES` | JWT token expiry time | Yes |
| `CORS_ALLOWED_ORIGINS` | CORS allowed origins | Yes |
| `REACT_APP_GOOGLE_MAPS_API_KEY` | Google Maps API key | Yes |
