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
import com.evcharging.mobile.db.entities.UserLocal;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserLocal> __insertionAdapterOfUserLocal;

  private final EntityDeletionOrUpdateAdapter<UserLocal> __deletionAdapterOfUserLocal;

  private final EntityDeletionOrUpdateAdapter<UserLocal> __updateAdapterOfUserLocal;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllUsers;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAuthToken;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastSync;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserLocal = new EntityInsertionAdapter<UserLocal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_local` (`nic`,`name`,`email`,`phone`,`status`,`authToken`,`lastSyncAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final UserLocal entity) {
        if (entity.getNic() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getNic());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhone());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        if (entity.getAuthToken() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAuthToken());
        }
        statement.bindLong(7, entity.getLastSyncAt());
      }
    };
    this.__deletionAdapterOfUserLocal = new EntityDeletionOrUpdateAdapter<UserLocal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `user_local` WHERE `nic` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final UserLocal entity) {
        if (entity.getNic() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getNic());
        }
      }
    };
    this.__updateAdapterOfUserLocal = new EntityDeletionOrUpdateAdapter<UserLocal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user_local` SET `nic` = ?,`name` = ?,`email` = ?,`phone` = ?,`status` = ?,`authToken` = ?,`lastSyncAt` = ? WHERE `nic` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final UserLocal entity) {
        if (entity.getNic() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getNic());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhone());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        if (entity.getAuthToken() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAuthToken());
        }
        statement.bindLong(7, entity.getLastSyncAt());
        if (entity.getNic() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNic());
        }
      }
    };
    this.__preparedStmtOfDeleteAllUsers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_local";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAuthToken = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_local SET authToken = ?, lastSyncAt = ? WHERE nic = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastSync = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_local SET lastSyncAt = ? WHERE nic = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertUser(final UserLocal user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserLocal.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteUser(final UserLocal user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfUserLocal.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateUser(final UserLocal user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUserLocal.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllUsers() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllUsers.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllUsers.release(_stmt);
    }
  }

  @Override
  public void updateAuthToken(final String nic, final String authToken, final long lastSyncAt) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAuthToken.acquire();
    int _argIndex = 1;
    if (authToken == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, authToken);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, lastSyncAt);
    _argIndex = 3;
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
      __preparedStmtOfUpdateAuthToken.release(_stmt);
    }
  }

  @Override
  public void updateLastSync(final String nic, final long lastSyncAt) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastSync.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, lastSyncAt);
    _argIndex = 2;
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
      __preparedStmtOfUpdateLastSync.release(_stmt);
    }
  }

  @Override
  public UserLocal getUserByNic(final String nic) {
    final String _sql = "SELECT * FROM user_local WHERE nic = ?";
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
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfAuthToken = CursorUtil.getColumnIndexOrThrow(_cursor, "authToken");
      final int _cursorIndexOfLastSyncAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncAt");
      final UserLocal _result;
      if (_cursor.moveToFirst()) {
        _result = new UserLocal();
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _result.setNic(_tmpNic);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        _result.setEmail(_tmpEmail);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpAuthToken;
        if (_cursor.isNull(_cursorIndexOfAuthToken)) {
          _tmpAuthToken = null;
        } else {
          _tmpAuthToken = _cursor.getString(_cursorIndexOfAuthToken);
        }
        _result.setAuthToken(_tmpAuthToken);
        final long _tmpLastSyncAt;
        _tmpLastSyncAt = _cursor.getLong(_cursorIndexOfLastSyncAt);
        _result.setLastSyncAt(_tmpLastSyncAt);
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
  public UserLocal getUserByEmail(final String email) {
    final String _sql = "SELECT * FROM user_local WHERE email = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNic = CursorUtil.getColumnIndexOrThrow(_cursor, "nic");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfAuthToken = CursorUtil.getColumnIndexOrThrow(_cursor, "authToken");
      final int _cursorIndexOfLastSyncAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncAt");
      final UserLocal _result;
      if (_cursor.moveToFirst()) {
        _result = new UserLocal();
        final String _tmpNic;
        if (_cursor.isNull(_cursorIndexOfNic)) {
          _tmpNic = null;
        } else {
          _tmpNic = _cursor.getString(_cursorIndexOfNic);
        }
        _result.setNic(_tmpNic);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        _result.setEmail(_tmpEmail);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpAuthToken;
        if (_cursor.isNull(_cursorIndexOfAuthToken)) {
          _tmpAuthToken = null;
        } else {
          _tmpAuthToken = _cursor.getString(_cursorIndexOfAuthToken);
        }
        _result.setAuthToken(_tmpAuthToken);
        final long _tmpLastSyncAt;
        _tmpLastSyncAt = _cursor.getLong(_cursorIndexOfLastSyncAt);
        _result.setLastSyncAt(_tmpLastSyncAt);
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
  public boolean userExists(final String nic) {
    final String _sql = "SELECT COUNT(*) > 0 FROM user_local WHERE nic = ?";
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
      final boolean _result;
      if (_cursor.moveToFirst()) {
        final int _tmp;
        _tmp = _cursor.getInt(0);
        _result = _tmp != 0;
      } else {
        _result = false;
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
