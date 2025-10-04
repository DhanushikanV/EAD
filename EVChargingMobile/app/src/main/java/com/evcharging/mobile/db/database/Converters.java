package com.evcharging.mobile.db.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Type Converters for Room Database
 * 
 * This class provides type converters for Room database to handle
 * custom data types that are not natively supported by SQLite.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class Converters {

    /**
     * Convert timestamp to Date object
     * 
     * @param timestamp Long timestamp
     * @return Date object or null
     */
    @TypeConverter
    public static Date fromTimestamp(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    /**
     * Convert Date object to timestamp
     * 
     * @param date Date object
     * @return Long timestamp or null
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

