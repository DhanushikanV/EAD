package com.evcharging.mobile.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evcharging.mobile.db.entities.StationCache;

import java.util.List;

/**
 * StationDao Interface
 * 
 * This DAO provides methods to interact with the stations_cache table.
 * It defines database operations for charging station data management.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
@Dao
public interface StationDao {

    /**
     * Insert a new station or replace if exists
     * 
     * @param station Station entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStation(StationCache station);

    /**
     * Insert multiple stations
     * 
     * @param stations List of station entities to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStations(List<StationCache> stations);

    /**
     * Update existing station
     * 
     * @param station Station entity to update
     */
    @Update
    void updateStation(StationCache station);

    /**
     * Delete station
     * 
     * @param station Station entity to delete
     */
    @Delete
    void deleteStation(StationCache station);

    /**
     * Get all stations
     * 
     * @return List of all stations
     */
    @Query("SELECT * FROM stations_cache ORDER BY name ASC")
    List<StationCache> getAllStations();

    /**
     * Get station by ID
     * 
     * @param stationId Station ID
     * @return StationCache entity or null if not found
     */
    @Query("SELECT * FROM stations_cache WHERE stationId = :stationId")
    StationCache getStationById(String stationId);

    /**
     * Get active stations only
     * 
     * @return List of active stations
     */
    @Query("SELECT * FROM stations_cache WHERE isActive = 1 ORDER BY name ASC")
    List<StationCache> getActiveStations();

    /**
     * Get stations by type (AC/DC)
     * 
     * @param type Station type (AC or DC)
     * @return List of stations of specified type
     */
    @Query("SELECT * FROM stations_cache WHERE type = :type AND isActive = 1 ORDER BY name ASC")
    List<StationCache> getStationsByType(String type);

    /**
     * Search stations by name
     * 
     * @param searchQuery Search query
     * @return List of matching stations
     */
    @Query("SELECT * FROM stations_cache WHERE name LIKE '%' || :searchQuery || '%' AND isActive = 1 ORDER BY name ASC")
    List<StationCache> searchStationsByName(String searchQuery);

    /**
     * Delete all stations (for refresh)
     */
    @Query("DELETE FROM stations_cache")
    void deleteAllStations();

    /**
     * Update station availability
     * 
     * @param stationId Station ID
     * @param availableSlots Number of available slots
     * @param lastUpdated Last update timestamp
     */
    @Query("UPDATE stations_cache SET availableSlots = :availableSlots, lastUpdated = :lastUpdated WHERE stationId = :stationId")
    void updateStationAvailability(String stationId, int availableSlots, long lastUpdated);
}
