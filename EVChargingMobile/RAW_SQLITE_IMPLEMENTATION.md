# EV Charging Mobile App - Raw SQLite Implementation

## 🔧 Database Migration: Room ORM → Raw SQLite

This document outlines the complete migration from Room ORM to raw SQLite implementation as per assignment requirements.

## 📋 Changes Made

### ✅ **1. Removed Room Dependencies**
- **File**: `app/build.gradle`
- **Change**: Removed Room ORM dependencies
- **Before**: 
  ```gradle
  implementation 'androidx.room:room-runtime:2.6.1'
  annotationProcessor 'androidx.room:room-compiler:2.6.1'
  ```
- **After**: 
  ```gradle
  // Raw SQLite Database (no Room ORM)
  // Using SQLiteOpenHelper directly for raw SQLite operations
  ```

### ✅ **2. Created SQLiteOpenHelper**
- **File**: `app/src/main/java/com/evcharging/mobile/db/database/AppDbHelper.java`
- **Purpose**: Direct SQLite database management
- **Features**:
  - Raw SQL table creation
  - Database version management
  - Migration handling

### ✅ **3. Replaced Room DAOs with Raw SQLite DAOs**

#### **UserDao.java**
- **Purpose**: EV Owner data management
- **Operations**: CRUD operations using raw SQL queries
- **Key Methods**:
  - `insertUser(UserLocal user)`
  - `getUserByNic(String nic)`
  - `updateUser(UserLocal user)`
  - `deleteUser(String nic)`

#### **BookingDao.java**
- **Purpose**: Booking data management
- **Operations**: CRUD operations with booking-specific queries
- **Key Methods**:
  - `insertBooking(BookingLocal booking)`
  - `getBookingsByNic(String nic)`
  - `updateBookingStatus(String bookingId, String status, long updatedAt)`
  - `updateBookingQRPayload(String bookingId, String qrPayload, long updatedAt)`

#### **StationDao.java**
- **Purpose**: Station data management
- **Operations**: CRUD operations with station-specific queries
- **Key Methods**:
  - `insertStation(StationCache station)`
  - `getAllStations()`
  - `getActiveStations()`
  - `searchStationsByName(String name)`

#### **StationSlotsDao.java**
- **Purpose**: Station slot availability management
- **Operations**: CRUD operations for slot data
- **Key Methods**:
  - `insertSlot(StationSlotsCache slot)`
  - `getSlotsByStationId(String stationId)`
  - `getAvailableSlots(String stationId)`
  - `updateSlotAvailability(int slotId, int availableCount)`

### ✅ **4. Updated Repository Classes**
- **File**: `app/src/main/java/com/evcharging/mobile/ui/stations/StationsRepository.java`
- **Changes**:
  - Integrated raw SQLite DAO operations
  - Added local cache management
  - Implemented offline-first strategy
  - Added cache update methods

### ✅ **5. Created Database Manager**
- **File**: `app/src/main/java/com/evcharging/mobile/db/database/DatabaseManager.java`
- **Purpose**: Centralized database management
- **Features**:
  - Singleton pattern for database access
  - DAO instance management
  - Database lifecycle management

### ✅ **6. Updated Fragment Implementation**
- **File**: `app/src/main/java/com/evcharging/mobile/ui/stations/StationsFragment.java`
- **Changes**:
  - Direct SQLite DAO usage
  - Proper database connection management
  - Error handling for database operations

## 🗄️ Database Schema (Raw SQLite)

### **User Local Table**
```sql
CREATE TABLE user_local(
    nic TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone TEXT,
    status TEXT,
    authToken TEXT,
    lastSyncAt INTEGER
);
```

### **Stations Cache Table**
```sql
CREATE TABLE stations_cache(
    stationId TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    type TEXT,
    latitude REAL,
    longitude REAL,
    address TEXT,
    isActive INTEGER
);
```

### **Bookings Local Table**
```sql
CREATE TABLE bookings_local(
    bookingId TEXT PRIMARY KEY,
    nic TEXT NOT NULL,
    stationId TEXT NOT NULL,
    startTs INTEGER,
    status TEXT,
    qrPayload TEXT,
    updatedAt INTEGER
);
```

### **Station Slots Cache Table**
```sql
CREATE TABLE station_slots_cache(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    stationId TEXT NOT NULL,
    date TEXT,
    timeSlotStart TEXT,
    timeSlotEnd TEXT,
    availableCount INTEGER
);
```

## 🔄 Usage Examples

### **Basic Database Operations**

```java
// Initialize DAO
StationDao stationDao = new StationDao(context);

// Open database connection
stationDao.open();

// Insert station
StationCache station = new StationCache("1", "Colombo Station", "AC", 6.9271, 79.8612, "Colombo", true);
long result = stationDao.insertStation(station);

// Query stations
List<StationCache> stations = stationDao.getAllStations();

// Close database connection
stationDao.close();
```

### **Repository Pattern Usage**

```java
// Initialize repository
StationsRepository repository = new StationsRepository(context);

// Get stations with caching
LiveData<List<Station>> stationsLiveData = repository.getAllStations();

// Refresh data
repository.refreshStations();

// Clear cache
repository.clearCache();
```

### **Database Manager Usage**

```java
// Get singleton instance
DatabaseManager dbManager = DatabaseManager.getInstance(context);

// Get DAO instances
UserDao userDao = dbManager.getUserDao();
StationDao stationDao = dbManager.getStationDao();

// Open database
dbManager.openDatabase();

// Perform operations
// ... database operations ...

// Close database
dbManager.closeDatabase();
```

## 🎯 Key Benefits

### **1. Assignment Compliance**
- ✅ **Pure Android**: No Room ORM framework
- ✅ **Raw SQLite**: Direct SQLiteOpenHelper usage
- ✅ **Local User Management**: EV Owner data persisted locally
- ✅ **Web Service Only**: No direct MongoDB connection

### **2. Performance Benefits**
- **Direct SQL Queries**: No ORM overhead
- **Manual Optimization**: Custom query optimization
- **Memory Efficiency**: Reduced memory footprint
- **Faster Operations**: Direct database access

### **3. Control Benefits**
- **Full Control**: Complete control over SQL queries
- **Custom Logic**: Business logic in application layer
- **Debugging**: Easier to debug raw SQL
- **Flexibility**: Easy to modify database schema

## 🚀 Migration Checklist

- ✅ Removed Room dependencies from build.gradle
- ✅ Created SQLiteOpenHelper (AppDbHelper)
- ✅ Implemented raw SQLite DAOs
- ✅ Updated repository classes
- ✅ Created database manager
- ✅ Updated fragment implementations
- ✅ Removed old Room database files
- ✅ Added comprehensive documentation

## 📝 Notes

### **Database Connection Management**
- Always call `dao.open()` before operations
- Always call `dao.close()` after operations
- Use try-catch blocks for error handling
- Consider using DatabaseManager for centralized management

### **Error Handling**
- Wrap database operations in try-catch blocks
- Provide fallback data when database operations fail
- Log errors for debugging purposes
- Show user-friendly error messages

### **Performance Considerations**
- Use background threads for database operations
- Implement proper connection pooling
- Consider database indexing for large datasets
- Monitor memory usage with large result sets

## 🔧 Future Enhancements

1. **Connection Pooling**: Implement database connection pooling
2. **Background Threading**: Add AsyncTask or ThreadPool for database operations
3. **Database Indexing**: Add indexes for frequently queried columns
4. **Migration Support**: Enhanced database migration handling
5. **Performance Monitoring**: Add database performance monitoring

---

**Implementation Status**: ✅ **COMPLETE**  
**Assignment Compliance**: ✅ **FULLY COMPLIANT**  
**Testing Status**: ✅ **READY FOR TESTING**
