package com.evcharging.mobile.db.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * AppDbHelper - Raw SQLite Database Helper
 * 
 * This class extends SQLiteOpenHelper to manage the local SQLite database.
 * It creates and manages tables for user data, station cache, and booking cache.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class AppDbHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "ev_app.db";
    private static final int DATABASE_VERSION = 1;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user_local table
        db.execSQL("CREATE TABLE user_local(" +
            "nic TEXT PRIMARY KEY, " +
            "name TEXT NOT NULL, " +
            "email TEXT NOT NULL, " +
            "phone TEXT, " +
            "status TEXT, " +
            "authToken TEXT, " +
            "lastSyncAt INTEGER" +
        ")");

        // Create stations_cache table
        db.execSQL("CREATE TABLE stations_cache(" +
            "stationId TEXT PRIMARY KEY, " +
            "name TEXT NOT NULL, " +
            "type TEXT, " +
            "latitude REAL, " +
            "longitude REAL, " +
            "address TEXT, " +
            "isActive INTEGER" +
        ")");

        // Create bookings_local table
        db.execSQL("CREATE TABLE bookings_local(" +
            "bookingId TEXT PRIMARY KEY, " +
            "nic TEXT NOT NULL, " +
            "stationId TEXT NOT NULL, " +
            "startTs INTEGER, " +
            "status TEXT, " +
            "qrPayload TEXT, " +
            "updatedAt INTEGER" +
        ")");

        // Create station_slots_cache table
        db.execSQL("CREATE TABLE station_slots_cache(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "stationId TEXT NOT NULL, " +
            "date TEXT NOT NULL, " +
            "timeSlotStart TEXT, " +
            "timeSlotEnd TEXT, " +
            "availableCount INTEGER" +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS user_local");
        db.execSQL("DROP TABLE IF EXISTS stations_cache");
        db.execSQL("DROP TABLE IF EXISTS bookings_local");
        db.execSQL("DROP TABLE IF EXISTS station_slots_cache");
        
        // Recreate tables
        onCreate(db);
    }
}
