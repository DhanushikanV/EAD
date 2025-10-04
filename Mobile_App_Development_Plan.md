# EV Charging Station Mobile App Development Plan

## 📱 Project Overview

**Project**: EV Charging Station Booking System - Android Mobile Application  
**Role**: Mobile App Developer  
**Technology**: Pure Android (Java), MVVM Architecture  
**Backend Integration**: REST API with existing .NET Core backend  

## ✅ Backend Alignment Verification

### Backend ↔ Mobile App Alignment

| **Backend Feature** | **Mobile App Integration** | **Status** |
|---------------------|----------------------------|------------|
| ✅ JWT Authentication | Token-based login/logout | **Aligned** |
| ✅ User Management | EV Owner signup/login | **Aligned** |
| ✅ EVOwner Model (NIC as PK) | NIC-based registration | **Aligned** |
| ✅ ChargingStation Model | Station listing with maps | **Aligned** |
| ✅ Booking Model | Reservation management | **Aligned** |
| ✅ REST API Endpoints | Retrofit integration | **Aligned** |
| ✅ MongoDB Collections | Room local caching | **Aligned** |

### Gap Analysis
- **Missing**: Station Operator authentication (needs backend extension)
- **Missing**: QR code validation endpoint (needs backend implementation)
- **Missing**: Slot availability checking (needs backend enhancement)

## 📋 Phase-by-Phase Execution Plan

### **Phase 1: Project Foundation & Setup** ⏱️ *2-3 hours*

**Objective**: Create Android project with proper MVVM architecture

**Tasks**:
- Create Android project with Pure Java
- Set up MVVM architecture (ui/, viewmodel/, repository/, network/, db/)
- Configure Gradle dependencies (Room, Retrofit, Moshi, ZXing, Google Maps)
- Set up project structure and naming conventions

**Dependencies to Add**:
```gradle
// Room Database
implementation "androidx.room:room-runtime:2.4.3"
annotationProcessor "androidx.room:room-compiler:2.4.3"

// Retrofit & Networking
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
implementation "com.squareup.moshi:moshi:1.14.0"

// ViewModel & LiveData
implementation "androidx.lifecycle:lifecycle-viewmodel:2.6.2"
implementation "androidx.lifecycle:lifecycle-livedata:2.6.2"

// Navigation
implementation "androidx.navigation:navigation-fragment:2.5.3"
implementation "androidx.navigation:navigation-ui:2.5.3"

// Google Maps
implementation "com.google.android.gms:play-services-maps:18.1.0"
implementation "com.google.android.gms:play-services-location:21.0.1"

// QR Code
implementation "com.journeyapps:zxing-android-embedded:4.3.0"
implementation "com.google.zxing:core:3.5.1"

// Material Design
implementation "com.google.android.material:material:1.9.0"
```

**Deliverables**:
- Project skeleton with proper folder structure
- `build.gradle` with all dependencies
- Basic navigation setup

---

### **Phase 2: Database Layer (Room)** ⏱️ *3-4 hours*

**Objective**: Implement local SQLite database with Room ORM

**Database Schema**:
```java
// Room Entities
@Entity(tableName = "user_local")
public class UserLocal {
    @PrimaryKey
    public String nic;
    public String name;
    public String email;
    public String phone;
    public String status;
    public String authToken;
    public long lastSyncAt;
    
    // Constructors, getters, setters
}

@Entity(tableName = "stations_cache")
public class StationCache {
    @PrimaryKey
    public String stationId;
    public String name;
    public String type; // AC/DC
    public double latitude;
    public double longitude;
    public String address;
    public boolean isActive;
    
    // Constructors, getters, setters
}

@Entity(tableName = "station_slots_cache")
public class StationSlotsCache {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String stationId;
    public String date;
    public String timeSlotStart;
    public String timeSlotEnd;
    public int availableCount;
    
    // Constructors, getters, setters
}

@Entity(tableName = "bookings_local")
public class BookingLocal {
    @PrimaryKey
    public String bookingId;
    public String nic;
    public String stationId;
    public long startTime;
    public String status;
    public String qrPayload;
    public long updatedAt;
    
    // Constructors, getters, setters
}
```

**Tasks**:
- Design Room entities for local caching
- Create DAO interfaces
- Set up database class with migrations
- Implement repository pattern for data access

**Deliverables**:
- Complete Room database setup
- Repository interfaces
- Data access layer

---

### **Phase 3: Networking Layer (Retrofit)** ⏱️ *2-3 hours*

**Objective**: Set up API client and services

**API Services**:
```java
// Service Interfaces
public interface AuthService {
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
    
    @POST("owners")
    Call<EVOwner> signup(@Body SignupRequest request);
}

public interface EVOwnerService {
    @GET("owners/{nic}")
    Call<EVOwner> getOwner(@Path("nic") String nic);
    
    @PUT("owners/{nic}")
    Call<Void> updateOwner(@Path("nic") String nic, @Body EVOwner owner);
    
    @POST("owners/{nic}/deactivate")
    Call<Void> deactivateOwner(@Path("nic") String nic);
}

public interface ChargingStationService {
    @GET("stations")
    Call<List<ChargingStation>> getStations(@Query("near") String location);
    
    @GET("stations/{id}")
    Call<ChargingStation> getStation(@Path("id") String id);
    
    @GET("stations/{id}/slots")
    Call<List<Slot>> getSlots(@Path("id") String id, @Query("date") String date);
}

public interface BookingService {
    @POST("bookings")
    Call<Booking> createBooking(@Body BookingRequest booking);
    
    @PUT("bookings/{id}")
    Call<Void> updateBooking(@Path("id") String id, @Body BookingUpdate booking);
    
    @DELETE("bookings/{id}")
    Call<Void> cancelBooking(@Path("id") String id);
    
    @GET("bookings")
    Call<List<Booking>> getBookings(@Query("nic") String nic);
}

public interface OperatorService {
    @POST("operator/scan")
    Call<Booking> scanQR(@Body QRScanRequest request);
    
    @POST("operator/bookings/{id}/start")
    Call<Void> startSession(@Path("id") String id);
    
    @POST("operator/bookings/{id}/finalize")
    Call<Void> finalizeSession(@Path("id") String id);
}
```

**Tasks**:
- Create API service interfaces matching backend endpoints
- Set up Retrofit with Moshi/Gson
- Implement authentication interceptor
- Create data models (DTOs) matching backend

**Deliverables**:
- Complete API client setup
- All service interfaces
- Data models and DTOs

---

### **Phase 4: Authentication System** ⏱️ *4-5 hours*

**Objective**: Implement user authentication flow

**Features**:
- EV Owner signup with NIC validation
- Login/logout functionality
- JWT token storage and management
- Auto-login functionality
- Role-based navigation

**Screens**:
- Splash Screen
- Login Screen
- Signup Screen
- Profile Management

**Tasks**:
- Implement EV Owner signup flow
- Create login/logout functionality
- Set up token storage and management
- Build authentication repository
- Create login/signup UI screens

**Validation Rules**:
- NIC format validation
- Email format validation
- Password strength requirements
- Duplicate account prevention

**Deliverables**:
- Complete authentication flow
- Login/Signup screens
- Token management system

---

### **Phase 5: Core UI & Navigation** ⏱️ *5-6 hours*

**Objective**: Set up navigation and base UI components

**Navigation Structure**:
```
EV Owner Flow:
├── Authentication (Login/Signup)
├── Dashboard
├── Stations
│   ├── Station List
│   └── Station Details
├── Bookings
│   ├── Create Booking
│   ├── Booking History
│   └── QR Code Display
└── Profile

Station Operator Flow:
├── Login
├── Dashboard
├── Today's Bookings
├── QR Scanner
└── Session Management
```

**Tasks**:
- Set up navigation component
- Create base UI components
- Implement Material Design theme
- Build navigation graphs for both roles
- Create splash screen and main activity

**Deliverables**:
- Complete navigation setup
- Base UI screens
- Material Design implementation

---

### **Phase 6: EV Owner Features** ⏱️ *8-10 hours*

**Objective**: Implement complete EV Owner functionality

**Dashboard Features**:
- Pending reservations count
- Approved reservations count
- Google Maps with nearby stations
- Station markers (AC/DC type, availability)

**Station Features**:
- Station listing with search/filter
- Station details view
- Slot availability checking
- Distance-based sorting

**Booking Features**:
- Create booking (≤7 days ahead)
- Edit/cancel booking (≥12 hours before)
- Booking history
- QR code generation for approved bookings

**Tasks**:
- Dashboard implementation with maps
- Station listing and search
- Station details screen
- Booking creation/editing
- Booking history
- Profile management
- QR code generation

**Validation Rules**:
- Booking within 7 days
- Edit/cancel ≥12 hours before
- Deactivated accounts cannot book
- Slot availability checking

**Deliverables**:
- Complete EV Owner functionality
- All required screens
- Google Maps integration
- QR code generation

---

### **Phase 7: Station Operator Features** ⏱️ *4-5 hours*

**Objective**: Implement Station Operator functionality

**Features**:
- Operator login (separate from EV Owner)
- QR code scanning
- Today's bookings view
- Session management (start/finalize)
- Booking confirmation

**Tasks**:
- Operator login implementation
- QR code scanning with ZXing
- Today's bookings dashboard
- Session start/finalize flow
- Booking status updates

**Deliverables**:
- Complete operator functionality
- QR scanning capability
- Session management screens

---

### **Phase 8: Advanced Features & Polish** ⏱️ *4-5 hours*

**Objective**: Polish application and add advanced features

**Features**:
- Offline caching implementation
- Comprehensive error handling
- Loading states and progress indicators
- Input validation and user feedback
- Performance optimization
- UI polish and animations

**Tasks**:
- Implement offline mode indicators
- Add comprehensive error handling
- Create loading states
- Implement form validation
- Optimize performance
- Polish UI/UX

**Deliverables**:
- Polished application
- Error handling system
- Offline capabilities

---

### **Phase 9: Testing & Documentation** ⏱️ *3-4 hours*

**Objective**: Final testing and documentation

**Tasks**:
- Take screenshots of all user flows
- Test all scenarios (EV Owner + Operator)
- Create comprehensive README
- Add code comments and documentation
- Prepare demo scenarios

**Screenshots Required**:
- Authentication flows
- Dashboard views
- Station listing and details
- Booking creation/management
- QR code generation/scanning
- Operator workflows

**Deliverables**:
- Complete documentation
- Screenshot collection
- README with setup instructions
- Demo-ready application

---

## 🎯 Project Timeline

| **Phase** | **Duration** | **Cumulative** |
|-----------|--------------|----------------|
| Phase 1: Project Setup | 2-3 hours | 3 hours |
| Phase 2: Database Layer | 3-4 hours | 7 hours |
| Phase 3: Networking | 2-3 hours | 10 hours |
| Phase 4: Authentication | 4-5 hours | 15 hours |
| Phase 5: Core UI | 5-6 hours | 21 hours |
| Phase 6: EV Owner Features | 8-10 hours | 31 hours |
| Phase 7: Operator Features | 4-5 hours | 36 hours |
| Phase 8: Polish & Features | 4-5 hours | 41 hours |
| Phase 9: Testing & Docs | 3-4 hours | 45 hours |

**Total Estimated Time: 35-45 hours**

## 🛠️ Technical Stack

### **Core Technologies**
- **Language**: Pure Java
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Framework**: Android Views (no frameworks)
- **Database**: Room (SQLite)
- **Networking**: Retrofit + Moshi
- **Maps**: Google Maps SDK
- **QR Code**: ZXing
- **Authentication**: JWT tokens

### **Key Libraries**
- Room Database
- Retrofit + Moshi
- ViewModel + LiveData
- Navigation Component
- Google Maps SDK
- ZXing (QR Code)
- Material Design Components

## 📱 App Features Summary

### **EV Owner Features**
- ✅ Account registration/login with NIC
- ✅ Profile management
- ✅ Dashboard with reservation counts
- ✅ Google Maps integration
- ✅ Station search and filtering
- ✅ Booking management (create/edit/cancel)
- ✅ QR code generation
- ✅ Booking history

### **Station Operator Features**
- ✅ Operator login
- ✅ QR code scanning
- ✅ Today's bookings view
- ✅ Session management
- ✅ Booking confirmation

### **Common Features**
- ✅ Offline caching
- ✅ Error handling
- ✅ Material Design UI
- ✅ Responsive design

## 🚀 Getting Started

1. **Clone/Create Android Project**
2. **Add Dependencies** (Phase 1)
3. **Set up Database** (Phase 2)
4. **Configure Networking** (Phase 3)
5. **Implement Authentication** (Phase 4)
6. **Build UI & Navigation** (Phase 5)
7. **Add Core Features** (Phases 6-7)
8. **Polish & Test** (Phases 8-9)

## 📋 Assignment Deliverables

### **Code Requirements**
- ✅ Comment header block on each .kt file
- ✅ Inline comments at beginning of each method
- ✅ Clear folder structure: ui/, viewmodel/, repository/, network/, db/
- ✅ Source code (not screenshots)

### **Documentation Requirements**
- ✅ Screenshots of all UIs
- ✅ Application high-level diagram
- ✅ Use case diagram
- ✅ DFD diagram
- ✅ Database design
- ✅ Source code
- ✅ References
- ✅ Individual contribution
- ✅ Challenges faced

### **Project Structure**
```
app/
├── src/main/java/com/evcharging/
│   ├── ui/
│   │   ├── auth/
│   │   ├── dashboard/
│   │   ├── stations/
│   │   ├── bookings/
│   │   ├── profile/
│   │   └── operator/
│   ├── viewmodel/
│   ├── repository/
│   ├── network/
│   │   ├── api/
│   │   └── models/
│   ├── db/
│   │   ├── entities/
│   │   ├── dao/
│   │   └── database/
│   └── utils/
```

---

**Ready to begin development! 🚀**
