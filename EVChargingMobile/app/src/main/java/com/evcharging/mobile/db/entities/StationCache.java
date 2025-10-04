package com.evcharging.mobile.db.entities;

/**
 * StationCache Entity
 * 
 * This entity represents cached charging station data stored locally in SQLite.
 * It contains station information for offline access and faster loading.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class StationCache {
    
    private String stationId;
    private String name;
    private String type; // AC/DC
    private double latitude;
    private double longitude;
    private String address;
    private boolean isActive;
    private int totalSlots;
    private int availableSlots;
    private long lastUpdated;

    /**
     * Default constructor
     */
    public StationCache() {
    }

    /**
     * Constructor with all parameters
     * 
     * @param stationId Station unique identifier
     * @param name Station name
     * @param type Charging type (AC/DC)
     * @param latitude Station latitude
     * @param longitude Station longitude
     * @param address Station address
     * @param isActive Station active status
     * @param totalSlots Total number of charging slots
     * @param availableSlots Number of available slots
     * @param lastUpdated Last update timestamp
     */
    public StationCache(String stationId, String name, String type, double latitude, 
                       double longitude, String address, boolean isActive, 
                       int totalSlots, int availableSlots, long lastUpdated) {
        this.stationId = stationId;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.isActive = isActive;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
