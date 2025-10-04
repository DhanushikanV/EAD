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
import com.evcharging.mobile.db.entities.BookingLocal;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BookingDao_Impl implements BookingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BookingLocal> __insertionAdapterOfBookingLocal;

  private final EntityDeletionOrUpdateAdapter<BookingLocal> __deletionAdapterOfBookingLocal;

  private final EntityDeletionOrUpdateAdapter<BookingLocal> __updateAdapterOfBookingLocal;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBookingsByNic;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllBookings;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBookingStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBookingQRPayload;

  public BookingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookingLocal = new EntityInsertionAdapter<BookingLocal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bookings_local` (`bookingId`,`nic`,`stationId`,`reservationDateTime`,`status`,`qrPayload`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final BookingLocal entity) {
        if (entity.getBookingId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBookingId());
        }
        if (entity.getNic() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNic());
        }
        if (entity.getStationId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getStationId());
        }
        statement.bindLong(4, entity.getReservationDateTime());
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        if (entity.getQrPayload() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getQrPayload());
        }
        statement.bindLong(7, entity.getCreatedAt());
        statement.bindLong(8, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfBookingLocal = new EntityDeletionOrUpdateAdapter<BookingLocal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `bookings_local` WHERE `bookingId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final BookingLocal entity) {
        if (entity.getBookingId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBookingId());
        }
      }
    };
    this.__updateAdapterOfBookingLocal = new EntityDeletionOrUpdateAdapter<BookingLocal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `bookings_local` SET `bookingId` = ?,`nic` = ?,`stationId` = ?,`reservationDateTime` = ?,`status` = ?,`qrPayload` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `bookingId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final BookingLocal entity) {
        if (entity.getBookingId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBookingId());
        }
        if (entity.getNic() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNic());
        }
        if (entity.getStationId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getStationId());
        }
        statement.bindLong(4, entity.getReservationDateTime());
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        if (entity.getQrPayload() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getQrPayload());
        }
        statement.bindLong(7, entity.getCreatedAt());
        statement.bindLong(8, entity.getUpdatedAt());
        if (entity.getBookingId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getBookingId());
        }
      }
    };
    this.__preparedStmtOfDeleteBookingsByNic = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bookings_local WHERE nic = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllBookings = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bookings_local";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBookingStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE bookings_local SET status = ?, updatedAt = ? WHERE bookingId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBookingQRPayload = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE bookings_local SET qrPayload = ?, updatedAt = ? WHERE bookingId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertBooking(final BookingLocal booking) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBookingLocal.insert(booking);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertBookings(final List<BookingLocal> bookings) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBookingLocal.insert(bookings);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteBooking(final BookingLocal booking) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfBookingLocal.handle(booking);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBooking(final BookingLocal booking) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfBookingLocal.handle(booking);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteBookingsByNic(final String nic) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBookingsByNic.acquire();
    int _argIndex = 1;
    if (nic == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, nic);
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
      __preparedStmtOfDeleteBookingsByNic.release(_stmt);
    }
  }

  @Override
  public void deleteAllBookings() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllBookings.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllBookings.release(_stmt);
    }
  }

  @Override
  public void updateBookingStatus(final String bookingId, final String status,
      final long updatedAt) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBookingStatus.acquire();
    int _argIndex = 1;
    if (status == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, status);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, updatedAt);
    _argIndex = 3;
    if (bookingId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, bookingId);
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
      __preparedStmtOfUpdateBookingStatus.release(_stmt);
    }
  }

  @Override
  public void updateBookingQRPayload(final String bookingId, final String qrPayload,
      final long updatedAt) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBookingQRPayload.acquire();
    int _argIndex = 1;
    if (qrPayload == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, qrPayload);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, updatedAt);
    _argIndex = 3;
    if (bookingId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, bookingId);
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
      __preparedStmtOfUpdateBookingQRPayload.release(_stmt);
    }
  }

  @Override
  public List<BookingLocal> getBookingsByNic(final String nic) {
    final String _sql = "SELECT * FROM bookings_local WHERE nic = ? ORDER BY reservationDateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nic == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nic);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBookingId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingId");
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfReservationDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDateTime");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQrPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "qrPayload");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<BookingLocal> _result = new ArrayList<BookingLocal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookingLocal _item;
        _item = new BookingLocal();
        final String _tmpBookingId;
        if (_cursor.isNull(_cursorIndexOfBookingId)) {
          _tmpBookingId = null;
        } else {
          _tmpBookingId = _cursor.getString(_cursorIndexOfBookingId);
        }
        _item.setBookingId(_tmpBookingId);
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _item.setNic(_tmpNic);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final long _tmpReservationDateTime;
        _tmpReservationDateTime = _cursor.getLong(_cursorIndexOfReservationDateTime);
        _item.setReservationDateTime(_tmpReservationDateTime);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQrPayload;
        if (_cursor.isNull(_cursorIndexOfQrPayload)) {
          _tmpQrPayload = null;
        } else {
          _tmpQrPayload = _cursor.getString(_cursorIndexOfQrPayload);
        }
        _item.setQrPayload(_tmpQrPayload);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public BookingLocal getBookingById(final String bookingId) {
    final String _sql = "SELECT * FROM bookings_local WHERE bookingId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (bookingId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, bookingId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBookingId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingId");
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfReservationDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDateTime");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQrPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "qrPayload");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final BookingLocal _result;
      if (_cursor.moveToFirst()) {
        _result = new BookingLocal();
        final String _tmpBookingId;
        if (_cursor.isNull(_cursorIndexOfBookingId)) {
          _tmpBookingId = null;
        } else {
          _tmpBookingId = _cursor.getString(_cursorIndexOfBookingId);
        }
        _result.setBookingId(_tmpBookingId);
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _result.setNic(_tmpNic);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _result.setStationId(_tmpStationId);
        final long _tmpReservationDateTime;
        _tmpReservationDateTime = _cursor.getLong(_cursorIndexOfReservationDateTime);
        _result.setReservationDateTime(_tmpReservationDateTime);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpQrPayload;
        if (_cursor.isNull(_cursorIndexOfQrPayload)) {
          _tmpQrPayload = null;
        } else {
          _tmpQrPayload = _cursor.getString(_cursorIndexOfQrPayload);
        }
        _result.setQrPayload(_tmpQrPayload);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _result.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _result.setUpdatedAt(_tmpUpdatedAt);
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
  public List<BookingLocal> getUpcomingBookings(final String nic, final long currentTime) {
    final String _sql = "SELECT * FROM bookings_local WHERE nic = ? AND reservationDateTime > ? ORDER BY reservationDateTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (nic == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nic);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, currentTime);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBookingId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingId");
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfReservationDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDateTime");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQrPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "qrPayload");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<BookingLocal> _result = new ArrayList<BookingLocal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookingLocal _item;
        _item = new BookingLocal();
        final String _tmpBookingId;
        if (_cursor.isNull(_cursorIndexOfBookingId)) {
          _tmpBookingId = null;
        } else {
          _tmpBookingId = _cursor.getString(_cursorIndexOfBookingId);
        }
        _item.setBookingId(_tmpBookingId);
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _item.setNic(_tmpNic);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final long _tmpReservationDateTime;
        _tmpReservationDateTime = _cursor.getLong(_cursorIndexOfReservationDateTime);
        _item.setReservationDateTime(_tmpReservationDateTime);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQrPayload;
        if (_cursor.isNull(_cursorIndexOfQrPayload)) {
          _tmpQrPayload = null;
        } else {
          _tmpQrPayload = _cursor.getString(_cursorIndexOfQrPayload);
        }
        _item.setQrPayload(_tmpQrPayload);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BookingLocal> getPastBookings(final String nic, final long currentTime) {
    final String _sql = "SELECT * FROM bookings_local WHERE nic = ? AND reservationDateTime < ? ORDER BY reservationDateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (nic == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nic);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, currentTime);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBookingId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingId");
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfReservationDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDateTime");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQrPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "qrPayload");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<BookingLocal> _result = new ArrayList<BookingLocal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookingLocal _item;
        _item = new BookingLocal();
        final String _tmpBookingId;
        if (_cursor.isNull(_cursorIndexOfBookingId)) {
          _tmpBookingId = null;
        } else {
          _tmpBookingId = _cursor.getString(_cursorIndexOfBookingId);
        }
        _item.setBookingId(_tmpBookingId);
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _item.setNic(_tmpNic);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final long _tmpReservationDateTime;
        _tmpReservationDateTime = _cursor.getLong(_cursorIndexOfReservationDateTime);
        _item.setReservationDateTime(_tmpReservationDateTime);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQrPayload;
        if (_cursor.isNull(_cursorIndexOfQrPayload)) {
          _tmpQrPayload = null;
        } else {
          _tmpQrPayload = _cursor.getString(_cursorIndexOfQrPayload);
        }
        _item.setQrPayload(_tmpQrPayload);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BookingLocal> getBookingsByStatus(final String nic, final String status) {
    final String _sql = "SELECT * FROM bookings_local WHERE nic = ? AND status = ? ORDER BY reservationDateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (nic == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nic);
    }
    _argIndex = 2;
    if (status == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, status);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBookingId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingId");
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfReservationDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDateTime");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQrPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "qrPayload");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<BookingLocal> _result = new ArrayList<BookingLocal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookingLocal _item;
        _item = new BookingLocal();
        final String _tmpBookingId;
        if (_cursor.isNull(_cursorIndexOfBookingId)) {
          _tmpBookingId = null;
        } else {
          _tmpBookingId = _cursor.getString(_cursorIndexOfBookingId);
        }
        _item.setBookingId(_tmpBookingId);
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _item.setNic(_tmpNic);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final long _tmpReservationDateTime;
        _tmpReservationDateTime = _cursor.getLong(_cursorIndexOfReservationDateTime);
        _item.setReservationDateTime(_tmpReservationDateTime);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQrPayload;
        if (_cursor.isNull(_cursorIndexOfQrPayload)) {
          _tmpQrPayload = null;
        } else {
          _tmpQrPayload = _cursor.getString(_cursorIndexOfQrPayload);
        }
        _item.setQrPayload(_tmpQrPayload);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BookingLocal> getBookingsForStationAndDate(final String stationId, final long date,
      final long nextDay) {
    final String _sql = "SELECT * FROM bookings_local WHERE stationId = ? AND reservationDateTime >= ? AND reservationDateTime < ? ORDER BY reservationDateTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (stationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stationId);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, date);
    _argIndex = 3;
    _statement.bindLong(_argIndex, nextDay);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBookingId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingId");
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfReservationDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDateTime");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQrPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "qrPayload");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<BookingLocal> _result = new ArrayList<BookingLocal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookingLocal _item;
        _item = new BookingLocal();
        final String _tmpBookingId;
        if (_cursor.isNull(_cursorIndexOfBookingId)) {
          _tmpBookingId = null;
        } else {
          _tmpBookingId = _cursor.getString(_cursorIndexOfBookingId);
        }
        _item.setBookingId(_tmpBookingId);
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _item.setNic(_tmpNic);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final long _tmpReservationDateTime;
        _tmpReservationDateTime = _cursor.getLong(_cursorIndexOfReservationDateTime);
        _item.setReservationDateTime(_tmpReservationDateTime);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQrPayload;
        if (_cursor.isNull(_cursorIndexOfQrPayload)) {
          _tmpQrPayload = null;
        } else {
          _tmpQrPayload = _cursor.getString(_cursorIndexOfQrPayload);
        }
        _item.setQrPayload(_tmpQrPayload);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BookingLocal> getTodayBookings(final String stationId, final long startOfDay,
      final long endOfDay) {
    final String _sql = "SELECT * FROM bookings_local WHERE stationId = ? AND reservationDateTime >= ? AND reservationDateTime <= ? ORDER BY reservationDateTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (stationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stationId);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endOfDay);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBookingId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingId");
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfStationId = CursorUtil.getColumnIndexOrThrow(_cursor, "stationId");
      final int _cursorIndexOfReservationDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDateTime");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQrPayload = CursorUtil.getColumnIndexOrThrow(_cursor, "qrPayload");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<BookingLocal> _result = new ArrayList<BookingLocal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookingLocal _item;
        _item = new BookingLocal();
        final String _tmpBookingId;
        if (_cursor.isNull(_cursorIndexOfBookingId)) {
          _tmpBookingId = null;
        } else {
          _tmpBookingId = _cursor.getString(_cursorIndexOfBookingId);
        }
        _item.setBookingId(_tmpBookingId);
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _item.setNic(_tmpNic);
        final String _tmpStationId;
        if (_cursor.isNull(_cursorIndexOfStationId)) {
          _tmpStationId = null;
        } else {
          _tmpStationId = _cursor.getString(_cursorIndexOfStationId);
        }
        _item.setStationId(_tmpStationId);
        final long _tmpReservationDateTime;
        _tmpReservationDateTime = _cursor.getLong(_cursorIndexOfReservationDateTime);
        _item.setReservationDateTime(_tmpReservationDateTime);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQrPayload;
        if (_cursor.isNull(_cursorIndexOfQrPayload)) {
          _tmpQrPayload = null;
        } else {
          _tmpQrPayload = _cursor.getString(_cursorIndexOfQrPayload);
        }
        _item.setQrPayload(_tmpQrPayload);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
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
