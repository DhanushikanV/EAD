package com.evcharging.mobile.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * StationSlotsCache Entity
 * 
 * This entity represents cached slot availability data for charging stations.
 * It stores time-based slot availability information for offline access.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
@Entity(tableName = "station_slots_cache")
public class StationSlotsCache {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String stationId;
    private String date;
    private String timeSlotStart;
    private String timeSlotEnd;
    private int availableCount;
    private long lastUpdated;

    /**
     * Default constructor
     */
    public StationSlotsCache() {
    }

    /**
     * Constructor with all parameters
     * 
     * @param stationId Station unique identifier
     * @param date Date for the slot (YYYY-MM-DD format)
     * @param timeSlotStart Start time of the slot (HH:mm format)
     * @param timeSlotEnd End time of the slot (HH:mm format)
     * @param availableCount Number of available slots
     * @param lastUpdated Last update timestamp
     */
    public StationSlotsCache(String stationId, String date, String timeSlotStart, 
                           String timeSlotEnd, int availableCount, long lastUpdated) {
        this.stationId = stationId;
        this.date = date;
        this.timeSlotStart = timeSlotStart;
        this.timeSlotEnd = timeSlotEnd;
        this.availableCount = availableCount;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlotStart() {
        return timeSlotStart;
    }

    public void setTimeSlotStart(String timeSlotStart) {
        this.timeSlotStart = timeSlotStart;
    }

    public String getTimeSlotEnd() {
        return timeSlotEnd;
    }

    public void setTimeSlotEnd(String timeSlotEnd) {
        this.timeSlotEnd = timeSlotEnd;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

