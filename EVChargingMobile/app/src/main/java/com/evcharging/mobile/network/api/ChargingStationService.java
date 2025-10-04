package com.evcharging.mobile.network.api;

import com.evcharging.mobile.network.models.ChargingStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * ChargingStationService Interface
 * 
 * This service interface defines charging station management API endpoints.
 * It handles station listing, details, and availability operations.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public interface ChargingStationService {

    /**
     * Get all charging stations
     * 
     * @return Call<List<ChargingStation>> containing all stations
     */
    @GET("chargingstation")
    Call<List<ChargingStation>> getAllStations();

    /**
     * Get stations near a location
     * 
     * @param latitude Location latitude
     * @param longitude Location longitude
     * @param radius Search radius in kilometers (optional)
     * @return Call<List<ChargingStation>> containing nearby stations
     */
    @GET("chargingstation")
    Call<List<ChargingStation>> getNearbyStations(
            @Query("lat") double latitude,
            @Query("lng") double longitude,
            @Query("radius") Double radius
    );

    /**
     * Get station by ID
     * 
     * @param id Station ID
     * @return Call<ChargingStation> containing station details
     */
    @GET("chargingstation/{id}")
    Call<ChargingStation> getStationById(@Path("id") String id);

    /**
     * Search stations by name
     * 
     * @param query Search query
     * @return Call<List<ChargingStation>> containing matching stations
     */
    @GET("chargingstation/search")
    Call<List<ChargingStation>> searchStations(@Query("q") String query);

    /**
     * Get stations by type
     * 
     * @param type Station type (AC/DC)
     * @return Call<List<ChargingStation>> containing stations of specified type
     */
    @GET("chargingstation")
    Call<List<ChargingStation>> getStationsByType(@Query("type") String type);

    /**
     * Get station availability for a specific date
     * 
     * @param stationId Station ID
     * @param date Date in YYYY-MM-DD format
     * @return Call<ChargingStation> with updated availability
     */
    @GET("chargingstation/{id}/availability")
    Call<ChargingStation> getStationAvailability(
            @Path("id") String stationId,
            @Query("date") String date
    );
}

