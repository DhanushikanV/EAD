package com.evcharging.mobile.db.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.evcharging.mobile.db.dao.BookingDao;
import com.evcharging.mobile.db.dao.BookingDao_Impl;
import com.evcharging.mobile.db.dao.StationDao;
import com.evcharging.mobile.db.dao.StationDao_Impl;
import com.evcharging.mobile.db.dao.StationSlotsDao;
import com.evcharging.mobile.db.dao.StationSlotsDao_Impl;
import com.evcharging.mobile.db.dao.UserDao;
import com.evcharging.mobile.db.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile StationDao _stationDao;

  private volatile StationSlotsDao _stationSlotsDao;

  private volatile BookingDao _bookingDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_local` (`nic` TEXT NOT NULL, `name` TEXT, `email` TEXT, `phone` TEXT, `status` TEXT, `authToken` TEXT, `lastSyncAt` INTEGER NOT NULL, PRIMARY KEY(`nic`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `stations_cache` (`stationId` TEXT NOT NULL, `name` TEXT, `type` TEXT, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `address` TEXT, `isActive` INTEGER NOT NULL, `totalSlots` INTEGER NOT NULL, `availableSlots` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`stationId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `station_slots_cache` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `stationId` TEXT, `date` TEXT, `timeSlotStart` TEXT, `timeSlotEnd` TEXT, `availableCount` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bookings_local` (`bookingId` TEXT NOT NULL, `nic` TEXT, `stationId` TEXT, `reservationDateTime` INTEGER NOT NULL, `status` TEXT, `qrPayload` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`bookingId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4cb00fb08861ebbaedfebd00b78a9bce')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `user_local`");
        db.execSQL("DROP TABLE IF EXISTS `stations_cache`");
        db.execSQL("DROP TABLE IF EXISTS `station_slots_cache`");
        db.execSQL("DROP TABLE IF EXISTS `bookings_local`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUserLocal = new HashMap<String, TableInfo.Column>(7);
        _columnsUserLocal.put("nic", new TableInfo.Column("nic", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserLocal.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserLocal.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserLocal.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserLocal.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserLocal.put("authToken", new TableInfo.Column("authToken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserLocal.put("lastSyncAt", new TableInfo.Column("lastSyncAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserLocal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserLocal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserLocal = new TableInfo("user_local", _columnsUserLocal, _foreignKeysUserLocal, _indicesUserLocal);
        final TableInfo _existingUserLocal = TableInfo.read(db, "user_local");
        if (!_infoUserLocal.equals(_existingUserLocal)) {
          return new RoomOpenHelper.ValidationResult(false, "user_local(com.evcharging.mobile.db.entities.UserLocal).\n"
                  + " Expected:\n" + _infoUserLocal + "\n"
                  + " Found:\n" + _existingUserLocal);
        }
        final HashMap<String, TableInfo.Column> _columnsStationsCache = new HashMap<String, TableInfo.Column>(10);
        _columnsStationsCache.put("stationId", new TableInfo.Column("stationId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("totalSlots", new TableInfo.Column("totalSlots", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("availableSlots", new TableInfo.Column("availableSlots", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationsCache.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStationsCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStationsCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStationsCache = new TableInfo("stations_cache", _columnsStationsCache, _foreignKeysStationsCache, _indicesStationsCache);
        final TableInfo _existingStationsCache = TableInfo.read(db, "stations_cache");
        if (!_infoStationsCache.equals(_existingStationsCache)) {
          return new RoomOpenHelper.ValidationResult(false, "stations_cache(com.evcharging.mobile.db.entities.StationCache).\n"
                  + " Expected:\n" + _infoStationsCache + "\n"
                  + " Found:\n" + _existingStationsCache);
        }
        final HashMap<String, TableInfo.Column> _columnsStationSlotsCache = new HashMap<String, TableInfo.Column>(7);
        _columnsStationSlotsCache.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationSlotsCache.put("stationId", new TableInfo.Column("stationId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationSlotsCache.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationSlotsCache.put("timeSlotStart", new TableInfo.Column("timeSlotStart", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationSlotsCache.put("timeSlotEnd", new TableInfo.Column("timeSlotEnd", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationSlotsCache.put("availableCount", new TableInfo.Column("availableCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStationSlotsCache.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStationSlotsCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStationSlotsCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStationSlotsCache = new TableInfo("station_slots_cache", _columnsStationSlotsCache, _foreignKeysStationSlotsCache, _indicesStationSlotsCache);
        final TableInfo _existingStationSlotsCache = TableInfo.read(db, "station_slots_cache");
        if (!_infoStationSlotsCache.equals(_existingStationSlotsCache)) {
          return new RoomOpenHelper.ValidationResult(false, "station_slots_cache(com.evcharging.mobile.db.entities.StationSlotsCache).\n"
                  + " Expected:\n" + _infoStationSlotsCache + "\n"
                  + " Found:\n" + _existingStationSlotsCache);
        }
        final HashMap<String, TableInfo.Column> _columnsBookingsLocal = new HashMap<String, TableInfo.Column>(8);
        _columnsBookingsLocal.put("bookingId", new TableInfo.Column("bookingId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookingsLocal.put("nic", new TableInfo.Column("nic", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookingsLocal.put("stationId", new TableInfo.Column("stationId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookingsLocal.put("reservationDateTime", new TableInfo.Column("reservationDateTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookingsLocal.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookingsLocal.put("qrPayload", new TableInfo.Column("qrPayload", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookingsLocal.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookingsLocal.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookingsLocal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBookingsLocal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBookingsLocal = new TableInfo("bookings_local", _columnsBookingsLocal, _foreignKeysBookingsLocal, _indicesBookingsLocal);
        final TableInfo _existingBookingsLocal = TableInfo.read(db, "bookings_local");
        if (!_infoBookingsLocal.equals(_existingBookingsLocal)) {
          return new RoomOpenHelper.ValidationResult(false, "bookings_local(com.evcharging.mobile.db.entities.BookingLocal).\n"
                  + " Expected:\n" + _infoBookingsLocal + "\n"
                  + " Found:\n" + _existingBookingsLocal);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4cb00fb08861ebbaedfebd00b78a9bce", "d67189bd4af5bc7ae5a41474f07d70a0");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "user_local","stations_cache","station_slots_cache","bookings_local");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `user_local`");
      _db.execSQL("DELETE FROM `stations_cache`");
      _db.execSQL("DELETE FROM `station_slots_cache`");
      _db.execSQL("DELETE FROM `bookings_local`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StationDao.class, StationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StationSlotsDao.class, StationSlotsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BookingDao.class, BookingDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public StationDao stationDao() {
    if (_stationDao != null) {
      return _stationDao;
    } else {
      synchronized(this) {
        if(_stationDao == null) {
          _stationDao = new StationDao_Impl(this);
        }
        return _stationDao;
      }
    }
  }

  @Override
  public StationSlotsDao stationSlotsDao() {
    if (_stationSlotsDao != null) {
      return _stationSlotsDao;
    } else {
      synchronized(this) {
        if(_stationSlotsDao == null) {
          _stationSlotsDao = new StationSlotsDao_Impl(this);
        }
        return _stationSlotsDao;
      }
    }
  }

  @Override
  public BookingDao bookingDao() {
    if (_bookingDao != null) {
      return _bookingDao;
    } else {
      synchronized(this) {
        if(_bookingDao == null) {
          _bookingDao = new BookingDao_Impl(this);
        }
        return _bookingDao;
      }
    }
  }
}
