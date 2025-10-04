package com.evcharging.mobile.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.evcharging.mobile.db.database.AppDbHelper;
import com.evcharging.mobile.db.entities.StationSlotsCache;
import java.util.ArrayList;
import java.util.List;

/**
 * StationSlotsDao - Raw SQLite Implementation
 * 
 * This DAO provides raw SQLite operations for the station_slots_cache table.
 * Uses SQLiteOpenHelper directly without Room ORM.
 */
public class StationSlotsDao {
    private SQLiteDatabase database;
    private AppDbHelper dbHelper;

    public StationSlotsDao(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Insert a new slot
     */
    public long insertSlot(StationSlotsCache slot) {
        ContentValues values = new ContentValues();
        values.put("stationId", slot.getStationId());
        values.put("date", slot.getDate());
        values.put("timeSlotStart", slot.getTimeSlotStart());
        values.put("timeSlotEnd", slot.getTimeSlotEnd());
        values.put("availableCount", slot.getAvailableCount());
        
        return database.insert("station_slots_cache", null, values);
    }

    /**
     * Get slots by station ID and date
     */
    public List<StationSlotsCache> getSlotsByStationIdAndDate(String stationId, String date) {
        List<StationSlotsCache> slots = new ArrayList<>();
        Cursor cursor = database.query("station_slots_cache", null, 
            "stationId = ? AND date = ?", new String[]{stationId, date}, 
            null, null, "timeSlotStart ASC");
        
        while (cursor.moveToNext()) {
            slots.add(cursorToSlot(cursor));
        }
        cursor.close();
        return slots;
    }

    /**
     * Update slot
     */
    public int updateSlot(StationSlotsCache slot) {
        ContentValues values = new ContentValues();
        values.put("stationId", slot.getStationId());
        values.put("date", slot.getDate());
        values.put("timeSlotStart", slot.getTimeSlotStart());
        values.put("timeSlotEnd", slot.getTimeSlotEnd());
        values.put("availableCount", slot.getAvailableCount());
        
        return database.update("station_slots_cache", values, "id = ?", 
            new String[]{String.valueOf(slot.getId())});
    }

    /**
     * Delete all slots
     */
    public void deleteAllSlots() {
        database.delete("station_slots_cache", null, null);
    }

    /**
     * Convert cursor to StationSlotsCache object
     */
    private StationSlotsCache cursorToSlot(Cursor cursor) {
        StationSlotsCache slot = new StationSlotsCache();
        slot.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        slot.setStationId(cursor.getString(cursor.getColumnIndexOrThrow("stationId")));
        slot.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        slot.setTimeSlotStart(cursor.getString(cursor.getColumnIndexOrThrow("timeSlotStart")));
        slot.setTimeSlotEnd(cursor.getString(cursor.getColumnIndexOrThrow("timeSlotEnd")));
        slot.setAvailableCount(cursor.getInt(cursor.getColumnIndexOrThrow("availableCount")));
        return slot;
    }
}
