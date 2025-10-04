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
import com.evcharging.mobile.db.entities.StationSlotsCache;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StationSlotsDao_Impl implements StationSlotsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StationSlotsCache> __insertionAdapterOfStationSlotsCache;

  private final EntityDeletionOrUpdateAdapter<StationSlotsCache> __deletionAdapterOfStationSlotsCache;

  private final EntityDeletionOrUpdateAdapter<StationSlotsCache> __updateAdapterOfStationSlotsCache;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSlotsForStation;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSlots;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSlotAvailability;

  public StationSlotsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStationSlotsCache = new EntityInsertionAdapter<StationSlotsCache>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `station_slots_cache` (`id`,`stationId`,`date`,`timeSlotStart`,`timeSlotEnd`,`availableCount`,`lastUpdated`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StationSlotsCache entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getStationId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getStationId());
        }
        if (entity.getDate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDate());
        }
        if (entity.getTimeSlotStart() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTimeSlotStart());
        }
        if (entity.getTimeSlotEnd() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTimeSlotEnd());
        }
        statement.bindLong(6, entity.getAvailableCount());
        statement.bindLong(7, entity.getLastUpdated());
      }
    };
    this.__deletionAdapterOfStationSlotsCache = new EntityDeletionOrUpdateAdapter<StationSlotsCache>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `station_slots_cache` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StationSlotsCache entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfStationSlotsCache = new EntityDeletionOrUpdateAdapter<StationSlotsCache>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `station_slots_cache` SET `id` = ?,`stationId` = ?,`date` = ?,`timeSlotStart` = ?,`timeSlotEnd` = ?,`availableCount` = ?,`lastUpdated` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StationSlotsCache entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getStationId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getStationId());
        }
        if (entity.getDate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDate());
        }
        if (entity.getTimeSlotStart() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTimeSlotStart());
        }
        if (entity.getTimeSlotEnd() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTimeSlotEnd());
        }
        statement.bindLong(6, entity.getAvailableCount());
        statement.bindLong(7, entity.getLastUpdated());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSlotsForStation = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM station_slots_cache WHERE stationId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllSlots = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM station_slots_cache";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSlotAvailability = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE station_slots_cache SET availableCount = ?, lastUpdated = ? WHERE stationId = ? AND date = ? AND timeSlotStart = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertSlot(final StationSlotsCache slot) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStationSlotsCache.insert(slot);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertSlots(final List<StationSlotsCache> slots) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStationSlotsCache.insert(slots);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteSlot(final StationSlotsCache slot) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfStationSlotsCache.handle(slot);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateSlot(final StationSlotsCache slot) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfStationSlotsCache.handle(slot);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteSlotsForStation(final String stationId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSlotsForStation.acquire();
    int _argIndex = 1;
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
      __preparedStmtOfDeleteSlotsForStation.release(_stmt);
    }
  }

  @Override
  public void deleteAllSlots() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSlots.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllSlots.release(_stmt);
    }
  }

  @Override
  public void updateSlotAvailability(final String stationId, final String date,
      final String timeSlotStart, final int availableCount, final long lastUpdated) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSlotAvailability.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, availableCount);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, lastUpdated);
    _argIndex = 3;
    if (stationId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, stationId);
    }
    _argIndex = 4;
    if (date == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, date);
    }
    _argIndex = 5;
    if (timeSlotStart == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, timeSlotStart);
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
      __preparedStmtOfUpdateSlotAvailability.release(_stmt);
    }
  }

  @Override
  public List<StationSlotsCache> getSlotsForStationAndDate(final String stationId,
      final String date) {
    final String _sql = "SELECT * FROM station_slots_cache WHERE stationId = ? AND date = ? ORDER BY timeSlotStart ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (stationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stationId);
    }
    _argIndex = 2;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTimeSlotStart = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlotStart");
      final int _cursorIndexOfTimeSlotEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlotEnd");
      final int _cursorIndexOfAvailableCount = CursorUtil.getColumnIndexOrThrow(_cursor, "availableCount");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final List<StationSlotsCache> _result = new ArrayList<StationSlotsCache>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StationSlotsCache _item;
        _item = new StationSlotsCache();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _item.setDate(_tmpDate);
        final String _tmpTimeSlotStart;
        if (_cursor.isNull(_cursorIndexOfTimeSlotStart)) {
          _tmpTimeSlotStart = null;
        } else {
          _tmpTimeSlotStart = _cursor.getString(_cursorIndexOfTimeSlotStart);
        }
        _item.setTimeSlotStart(_tmpTimeSlotStart);
        final String _tmpTimeSlotEnd;
        if (_cursor.isNull(_cursorIndexOfTimeSlotEnd)) {
          _tmpTimeSlotEnd = null;
        } else {
          _tmpTimeSlotEnd = _cursor.getString(_cursorIndexOfTimeSlotEnd);
        }
        _item.setTimeSlotEnd(_tmpTimeSlotEnd);
        final int _tmpAvailableCount;
        _tmpAvailableCount = _cursor.getInt(_cursorIndexOfAvailableCount);
        _item.setAvailableCount(_tmpAvailableCount);
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
  public List<StationSlotsCache> getAvailableSlotsForStationAndDate(final String stationId,
      final String date) {
    final String _sql = "SELECT * FROM station_slots_cache WHERE stationId = ? AND date = ? AND availableCount > 0 ORDER BY timeSlotStart ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (stationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stationId);
    }
    _argIndex = 2;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTimeSlotStart = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlotStart");
      final int _cursorIndexOfTimeSlotEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlotEnd");
      final int _cursorIndexOfAvailableCount = CursorUtil.getColumnIndexOrThrow(_cursor, "availableCount");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final List<StationSlotsCache> _result = new ArrayList<StationSlotsCache>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StationSlotsCache _item;
        _item = new StationSlotsCache();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _item.setDate(_tmpDate);
        final String _tmpTimeSlotStart;
        if (_cursor.isNull(_cursorIndexOfTimeSlotStart)) {
          _tmpTimeSlotStart = null;
        } else {
          _tmpTimeSlotStart = _cursor.getString(_cursorIndexOfTimeSlotStart);
        }
        _item.setTimeSlotStart(_tmpTimeSlotStart);
        final String _tmpTimeSlotEnd;
        if (_cursor.isNull(_cursorIndexOfTimeSlotEnd)) {
          _tmpTimeSlotEnd = null;
        } else {
          _tmpTimeSlotEnd = _cursor.getString(_cursorIndexOfTimeSlotEnd);
        }
        _item.setTimeSlotEnd(_tmpTimeSlotEnd);
        final int _tmpAvailableCount;
        _tmpAvailableCount = _cursor.getInt(_cursorIndexOfAvailableCount);
        _item.setAvailableCount(_tmpAvailableCount);
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
  public List<StationSlotsCache> getSlotsInTimeRange(final String stationId, final String date,
      final String startTime, final String endTime) {
    final String _sql = "SELECT * FROM station_slots_cache WHERE stationId = ? AND date = ? AND timeSlotStart >= ? AND timeSlotEnd <= ? ORDER BY timeSlotStart ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (stationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stationId);
    }
    _argIndex = 2;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    _argIndex = 3;
    if (startTime == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, startTime);
    }
    _argIndex = 4;
    if (endTime == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, endTime);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTimeSlotStart = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlotStart");
      final int _cursorIndexOfTimeSlotEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSlotEnd");
      final int _cursorIndexOfAvailableCount = CursorUtil.getColumnIndexOrThrow(_cursor, "availableCount");
      final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
      final List<StationSlotsCache> _result = new ArrayList<StationSlotsCache>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StationSlotsCache _item;
        _item = new StationSlotsCache();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _item.setDate(_tmpDate);
        final String _tmpTimeSlotStart;
        if (_cursor.isNull(_cursorIndexOfTimeSlotStart)) {
          _tmpTimeSlotStart = null;
        } else {
          _tmpTimeSlotStart = _cursor.getString(_cursorIndexOfTimeSlotStart);
        }
        _item.setTimeSlotStart(_tmpTimeSlotStart);
        final String _tmpTimeSlotEnd;
        if (_cursor.isNull(_cursorIndexOfTimeSlotEnd)) {
          _tmpTimeSlotEnd = null;
        } else {
          _tmpTimeSlotEnd = _cursor.getString(_cursorIndexOfTimeSlotEnd);
        }
        _item.setTimeSlotEnd(_tmpTimeSlotEnd);
        final int _tmpAvailableCount;
        _tmpAvailableCount = _cursor.getInt(_cursorIndexOfAvailableCount);
        _item.setAvailableCount(_tmpAvailableCount);
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
