using EV_2.Models;
using EV_2.Services;
using EV_2.Settings;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;

var builder = WebApplication.CreateBuilder(args);
//test  
// -------------------- Load .env and map to configuration --------------------
// Simple .env loader (KEY=VALUE, ignores lines starting with '#')
var envFilePath = Path.Combine(AppContext.BaseDirectory, ".env");
var projectDirEnvPath = Path.Combine(Directory.GetCurrentDirectory(), ".env");
if (!File.Exists(envFilePath) && File.Exists(projectDirEnvPath))
{
    envFilePath = projectDirEnvPath;
}
if (File.Exists(envFilePath))
{
    foreach (var rawLine in File.ReadAllLines(envFilePath))
    {
        var line = rawLine.Trim();
        if (string.IsNullOrWhiteSpace(line) || line.StartsWith("#")) continue;
        var equalsIndex = line.IndexOf('=');
        if (equalsIndex <= 0) continue;
        var key = line.Substring(0, equalsIndex).Trim();
        var value = line.Substring(equalsIndex + 1).Trim();
        Environment.SetEnvironmentVariable(key, value);
    }

    // Map flat env vars to strongly-typed configuration keys used by the app
    void SetIfPresent(string configKey, string envKey)
    {
        var value = Environment.GetEnvironmentVariable(envKey);
        if (!string.IsNullOrEmpty(value))
        {
            builder.Configuration[configKey] = value;
        }
    }

    // MongoDB
    SetIfPresent("MongoDBSettings:ConnectionString", "MONGODB_CONNECTION_STRING");
    SetIfPresent("MongoDBSettings:DatabaseName", "MONGODB_DATABASE_NAME");
    SetIfPresent("MongoDBSettings:BookingCollectionName", "MONGODB_BOOKING_COLLECTION");
    SetIfPresent("MongoDBSettings:ChargingStationCollectionName", "MONGODB_CHARGING_STATION_COLLECTION");
    SetIfPresent("MongoDBSettings:EVOwnerCollectionName", "MONGODB_EV_OWNER_COLLECTION");
    SetIfPresent("MongoDBSettings:UserCollectionName", "MONGODB_USER_COLLECTION");
    SetIfPresent("MongoDBSettings:StationSlotsCollectionName", "MONGODB_STATION_SLOTS_COLLECTION");

    // JWT
    SetIfPresent("JwtSettings:Secret", "JWT_SECRET");
    SetIfPresent("JwtSettings:Issuer", "JWT_ISSUER");
    SetIfPresent("JwtSettings:Audience", "JWT_AUDIENCE");
    SetIfPresent("JwtSettings:ExpiryMinutes", "JWT_EXPIRY_MINUTES");

    // CORS
    SetIfPresent("CORS_ALLOWED_ORIGINS", "CORS_ALLOWED_ORIGINS");
}

// -------------------- MongoDB settings --------------------
builder.Services.Configure<DatabaseSettings>(
    builder.Configuration.GetSection("MongoDBSettings")
);

builder.Services.AddSingleton<IMongoClient>(s =>
{
    var settings = s.GetRequiredService<IOptions<DatabaseSettings>>().Value;
    return new MongoClient(settings.ConnectionString);
});

// -------------------- Register Services --------------------
builder.Services.AddScoped<BookingService>();
builder.Services.AddScoped<ChargingStationService>();
builder.Services.AddScoped<EVOwnerService>();
builder.Services.AddScoped<UserService>();
builder.Services.AddScoped<StationSlotService>();
builder.Services.AddSingleton<JwtService>();

// -------------------- Add Controllers, Swagger, CORS --------------------
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowFrontend", policy =>
    {
        var allowedOrigins = builder.Configuration["CORS_ALLOWED_ORIGINS"]?.Split(',') 
                           ?? new[] { "http://localhost:3000", "http://localhost:5173" };
        policy.WithOrigins(allowedOrigins)
              .AllowAnyHeader()
              .AllowAnyMethod();
    });
});

// -------------------- JWT Authentication --------------------
var jwtSettings = builder.Configuration.GetSection("JwtSettings");

builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = true,
        ValidateAudience = true,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ValidIssuer = jwtSettings["Issuer"],
        ValidAudience = jwtSettings["Audience"],
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwtSettings["Secret"]))
    };
});

var app = builder.Build();

// -------------------- Middleware --------------------
app.UseSwagger();
app.UseSwaggerUI();

app.UseRouting();
app.UseCors("AllowFrontend");

app.UseAuthentication(); // JWT
app.UseAuthorization();

app.MapControllers();

app.Run();
