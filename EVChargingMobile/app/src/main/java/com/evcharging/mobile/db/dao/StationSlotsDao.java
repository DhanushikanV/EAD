package com.evcharging.mobile.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evcharging.mobile.db.entities.StationSlotsCache;

import java.util.List;

/**
 * StationSlotsDao Interface
 * 
 * This DAO provides methods to interact with the station_slots_cache table.
 * It defines database operations for slot availability data management.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
@Dao
public interface StationSlotsDao {

    /**
     * Insert a new slot entry or replace if exists
     * 
     * @param slot Slot entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSlot(StationSlotsCache slot);

    /**
     * Insert multiple slot entries
     * 
     * @param slots List of slot entities to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSlots(List<StationSlotsCache> slots);

    /**
     * Update existing slot entry
     * 
     * @param slot Slot entity to update
     */
    @Update
    void updateSlot(StationSlotsCache slot);

    /**
     * Delete slot entry
     * 
     * @param slot Slot entity to delete
     */
    @Delete
    void deleteSlot(StationSlotsCache slot);

    /**
     * Get all slots for a station on a specific date
     * 
     * @param stationId Station ID
     * @param date Date in YYYY-MM-DD format
     * @return List of slots for the station on the date
     */
    @Query("SELECT * FROM station_slots_cache WHERE stationId = :stationId AND date = :date ORDER BY timeSlotStart ASC")
    List<StationSlotsCache> getSlotsForStationAndDate(String stationId, String date);

    /**
     * Get available slots for a station on a specific date
     * 
     * @param stationId Station ID
     * @param date Date in YYYY-MM-DD format
     * @return List of available slots (availableCount > 0)
     */
    @Query("SELECT * FROM station_slots_cache WHERE stationId = :stationId AND date = :date AND availableCount > 0 ORDER BY timeSlotStart ASC")
    List<StationSlotsCache> getAvailableSlotsForStationAndDate(String stationId, String date);

    /**
     * Get slots for a specific time range
     * 
     * @param stationId Station ID
     * @param date Date in YYYY-MM-DD format
     * @param startTime Start time in HH:mm format
     * @param endTime End time in HH:mm format
     * @return List of slots in the time range
     */
    @Query("SELECT * FROM station_slots_cache WHERE stationId = :stationId AND date = :date AND timeSlotStart >= :startTime AND timeSlotEnd <= :endTime ORDER BY timeSlotStart ASC")
    List<StationSlotsCache> getSlotsInTimeRange(String stationId, String date, String startTime, String endTime);

    /**
     * Delete all slots for a station
     * 
     * @param stationId Station ID
     */
    @Query("DELETE FROM station_slots_cache WHERE stationId = :stationId")
    void deleteSlotsForStation(String stationId);

    /**
     * Delete all slots (for refresh)
     */
    @Query("DELETE FROM station_slots_cache")
    void deleteAllSlots();

    /**
     * Update slot availability
     * 
     * @param stationId Station ID
     * @param date Date in YYYY-MM-DD format
     * @param timeSlotStart Start time in HH:mm format
     * @param availableCount New available count
     * @param lastUpdated Last update timestamp
     */
    @Query("UPDATE station_slots_cache SET availableCount = :availableCount, lastUpdated = :lastUpdated WHERE stationId = :stationId AND date = :date AND timeSlotStart = :timeSlotStart")
    void updateSlotAvailability(String stationId, String date, String timeSlotStart, int availableCount, long lastUpdated);
}

