package com.evcharging.mobile.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.evcharging.mobile.db.database.AppDbHelper;
import com.evcharging.mobile.db.entities.StationCache;
import java.util.ArrayList;
import java.util.List;

/**
 * StationDao - Raw SQLite Implementation
 * 
 * This DAO provides raw SQLite operations for the stations_cache table.
 * Uses SQLiteOpenHelper directly without Room ORM.
 */
public class StationDao {
    private SQLiteDatabase database;
    private AppDbHelper dbHelper;

    public StationDao(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Insert a new station into the cache
     */
    public long insertStation(StationCache station) {
        ContentValues values = new ContentValues();
        values.put("stationId", station.getStationId());
        values.put("name", station.getName());
        values.put("type", station.getType());
        values.put("latitude", station.getLatitude());
        values.put("longitude", station.getLongitude());
        values.put("address", station.getAddress());
        values.put("isActive", station.isActive() ? 1 : 0);
        
        return database.insert("stations_cache", null, values);
    }

    /**
     * Get station by ID
     */
    public StationCache getStationById(String stationId) {
        Cursor cursor = database.query("stations_cache", null, 
            "stationId = ?", new String[]{stationId}, null, null, null);
        
        if (cursor.moveToFirst()) {
            StationCache station = cursorToStation(cursor);
            cursor.close();
            return station;
        }
        cursor.close();
        return null;
    }

    /**
     * Get all stations
     */
    public List<StationCache> getAllStations() {
        List<StationCache> stations = new ArrayList<>();
        Cursor cursor = database.query("stations_cache", null, null, null, null, null, "name ASC");
        
        while (cursor.moveToNext()) {
            stations.add(cursorToStation(cursor));
        }
        cursor.close();
        return stations;
    }

    /**
     * Update station
     */
    public int updateStation(StationCache station) {
        ContentValues values = new ContentValues();
        values.put("name", station.getName());
        values.put("type", station.getType());
        values.put("latitude", station.getLatitude());
        values.put("longitude", station.getLongitude());
        values.put("address", station.getAddress());
        values.put("isActive", station.isActive() ? 1 : 0);
        
        return database.update("stations_cache", values, "stationId = ?", 
            new String[]{station.getStationId()});
    }

    /**
     * Delete all stations
     */
    public void deleteAllStations() {
        database.delete("stations_cache", null, null);
    }

    /**
     * Convert cursor to StationCache object
     */
    private StationCache cursorToStation(Cursor cursor) {
        StationCache station = new StationCache();
        station.setStationId(cursor.getString(cursor.getColumnIndexOrThrow("stationId")));
        station.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        station.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
        station.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
        station.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
        station.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        station.setActive(cursor.getInt(cursor.getColumnIndexOrThrow("isActive")) == 1);
        return station;
    }
}
