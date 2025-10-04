package com.evcharging.mobile.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.evcharging.mobile.db.database.AppDbHelper;
import com.evcharging.mobile.db.entities.UserLocal;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDao - Raw SQLite Implementation
 * 
 * This DAO provides raw SQLite operations for the user_local table.
 * Uses SQLiteOpenHelper directly without Room ORM.
 */
public class UserDao {
    private SQLiteDatabase database;
    private AppDbHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Insert a new user
     */
    public long insertUser(UserLocal user) {
        ContentValues values = new ContentValues();
        values.put("nic", user.getNic());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("status", user.getStatus());
        values.put("authToken", user.getAuthToken());
        values.put("lastSyncAt", user.getLastSyncAt());
        
        return database.insert("user_local", null, values);
    }

    /**
     * Get user by NIC
     */
    public UserLocal getUserByNic(String nic) {
        Cursor cursor = database.query("user_local", null, 
            "nic = ?", new String[]{nic}, null, null, null);
        
        if (cursor.moveToFirst()) {
            UserLocal user = cursorToUser(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    /**
     * Update user
     */
    public int updateUser(UserLocal user) {
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("status", user.getStatus());
        values.put("authToken", user.getAuthToken());
        values.put("lastSyncAt", user.getLastSyncAt());
        
        return database.update("user_local", values, "nic = ?", 
            new String[]{user.getNic()});
    }

    /**
     * Delete all users
     */
    public void deleteAllUsers() {
        database.delete("user_local", null, null);
    }

    /**
     * Convert cursor to UserLocal object
     */
    private UserLocal cursorToUser(Cursor cursor) {
        UserLocal user = new UserLocal();
        user.setNic(cursor.getString(cursor.getColumnIndexOrThrow("nic")));
        user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
        user.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        user.setAuthToken(cursor.getString(cursor.getColumnIndexOrThrow("authToken")));
        user.setLastSyncAt(cursor.getLong(cursor.getColumnIndexOrThrow("lastSyncAt")));
        return user;
    }
}
