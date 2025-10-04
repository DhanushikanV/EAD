package com.evcharging.mobile.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.evcharging.mobile.db.entities.StationCache;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StationDao_Impl implements StationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StationCache> __insertionAdapterOfStationCache;

  private final EntityDeletionOrUpdateAdapter<StationCache> __deletionAdapterOfStationCache;

  private final EntityDeletionOrUpdateAdapter<StationCache> __updateAdapterOfStationCache;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllStations;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStationAvailability;

  public StationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStationCache = new EntityInsertionAdapter<StationCache>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `stations_cache` (`stationId`,`name`,`type`,`latitude`,`longitude`,`address`,`isActive`,`totalSlots`,`availableSlots`,`lastUpdated`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StationCache entity) {
        if (entity.getStationId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getStationId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        statement.bindDouble(4, entity.getLatitude());
        statement.bindDouble(5, entity.getLongitude());
        if (entity.getAddress() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAddress());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getTotalSlots());
        statement.bindLong(9, entity.getAvailableSlots());
        statement.bindLong(10, entity.getLastUpdated());
      }
    };
    this.__deletionAdapterOfStationCache = new EntityDeletionOrUpdateAdapter<StationCache>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `stations_cache` WHERE `stationId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StationCache entity) {
        if (entity.getStationId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getStationId());
        }
      }
    };
    this.__updateAdapterOfStationCache = new EntityDeletionOrUpdateAdapter<StationCache>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `stations_cache` SET `stationId` = ?,`name` = ?,`type` = ?,`latitude` = ?,`longitude` = ?,`address` = ?,`isActive` = ?,`totalSlots` = ?,`availableSlots` = ?,`lastUpdated` = ? WHERE `stationId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StationCache entity) {
        if (entity.getStationId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getStationId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        statement.bindDouble(4, entity.getLatitude());
        statement.bindDouble(5, entity.getLongitude());
        if (entity.getAddress() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAddress());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getTotalSlots());
        statement.bindLong(9, entity.getAvailableSlots());
        statement.bindLong(10, entity.getLastUpdated());
        if (entity.getStationId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getStationId());
        }
      }
    };
    this.__preparedStmtOfDeleteAllStations = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM stations_cache";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStationAvailability = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE stations_cache SET availableSlots = ?, lastUpdated = ? WHERE stationId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertStation(final StationCache station) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStationCache.insert(station);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertStations(final List<StationCache> stations) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStationCache.insert(stations);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteStation(final StationCache station) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfStationCache.handle(station);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateStation(final StationCache station) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfStationCache.handle(station);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllStations() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllStations.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllStations.release(_stmt);
    }
  }

  @Override
  public void updateStationAvailability(final String stationId, final int availableSlots,
      final long lastUpdated) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStationAvailability.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, availableSlots);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, lastUpdated);
    _argIndex = 3;
    if (stationId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, stationId);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateStationAvailability.release(_stmt);
    }
  }

  @Override
  public List<StationCache> getAllStations() {
    final String _sql = "SELECT * FROM stations_cache ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTotalSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSlots");
      final int _cursorIndexOfAvailableSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "availableSlots");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final List<StationCache> _result = new ArrayList<StationCache>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StationCache _item;
        _item = new StationCache();
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final int _tmpTotalSlots;
        _tmpTotalSlots = _cursor.getInt(_cursorIndexOfTotalSlots);
        _item.setTotalSlots(_tmpTotalSlots);
        final int _tmpAvailableSlots;
        _tmpAvailableSlots = _cursor.getInt(_cursorIndexOfAvailableSlots);
        _item.setAvailableSlots(_tmpAvailableSlots);
        final long _tmpLastUpdated;
        _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
        _item.setLastUpdated(_tmpLastUpdated);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public StationCache getStationById(final String stationId) {
    final String _sql = "SELECT * FROM stations_cache WHERE stationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (stationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stationId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTotalSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSlots");
      final int _cursorIndexOfAvailableSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "availableSlots");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final StationCache _result;
      if (_cursor.moveToFirst()) {
        _result = new StationCache();
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _result.setStationId(_tmpStationId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _result.setType(_tmpType);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _result.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _result.setLongitude(_tmpLongitude);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _result.setAddress(_tmpAddress);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _result.setActive(_tmpIsActive);
        final int _tmpTotalSlots;
        _tmpTotalSlots = _cursor.getInt(_cursorIndexOfTotalSlots);
        _result.setTotalSlots(_tmpTotalSlots);
        final int _tmpAvailableSlots;
        _tmpAvailableSlots = _cursor.getInt(_cursorIndexOfAvailableSlots);
        _result.setAvailableSlots(_tmpAvailableSlots);
        final long _tmpLastUpdated;
        _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
        _result.setLastUpdated(_tmpLastUpdated);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<StationCache> getActiveStations() {
    final String _sql = "SELECT * FROM stations_cache WHERE isActive = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTotalSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSlots");
      final int _cursorIndexOfAvailableSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "availableSlots");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final List<StationCache> _result = new ArrayList<StationCache>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StationCache _item;
        _item = new StationCache();
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final int _tmpTotalSlots;
        _tmpTotalSlots = _cursor.getInt(_cursorIndexOfTotalSlots);
        _item.setTotalSlots(_tmpTotalSlots);
        final int _tmpAvailableSlots;
        _tmpAvailableSlots = _cursor.getInt(_cursorIndexOfAvailableSlots);
        _item.setAvailableSlots(_tmpAvailableSlots);
        final long _tmpLastUpdated;
        _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
        _item.setLastUpdated(_tmpLastUpdated);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<StationCache> getStationsByType(final String type) {
    final String _sql = "SELECT * FROM stations_cache WHERE type = ? AND isActive = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTotalSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSlots");
      final int _cursorIndexOfAvailableSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "availableSlots");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final List<StationCache> _result = new ArrayList<StationCache>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StationCache _item;
        _item = new StationCache();
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final int _tmpTotalSlots;
        _tmpTotalSlots = _cursor.getInt(_cursorIndexOfTotalSlots);
        _item.setTotalSlots(_tmpTotalSlots);
        final int _tmpAvailableSlots;
        _tmpAvailableSlots = _cursor.getInt(_cursorIndexOfAvailableSlots);
        _item.setAvailableSlots(_tmpAvailableSlots);
        final long _tmpLastUpdated;
        _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
        _item.setLastUpdated(_tmpLastUpdated);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<StationCache> searchStationsByName(final String searchQuery) {
    final String _sql = "SELECT * FROM stations_cache WHERE name LIKE '%' || ? || '%' AND isActive = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTotalSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSlots");
      final int _cursorIndexOfAvailableSlots = CursorUtil.getColumnIndexOrThrow(_cursor, "availableSlots");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final List<StationCache> _result = new ArrayList<StationCache>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StationCache _item;
        _item = new StationCache();
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final double _tmpLatitude;
        _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item.setLatitude(_tmpLatitude);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final int _tmpTotalSlots;
        _tmpTotalSlots = _cursor.getInt(_cursorIndexOfTotalSlots);
        _item.setTotalSlots(_tmpTotalSlots);
        final int _tmpAvailableSlots;
        _tmpAvailableSlots = _cursor.getInt(_cursorIndexOfAvailableSlots);
        _item.setAvailableSlots(_tmpAvailableSlots);
        final long _tmpLastUpdated;
        _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
        _item.setLastUpdated(_tmpLastUpdated);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
