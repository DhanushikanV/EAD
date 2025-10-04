package com.evcharging.mobile.network.models;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * ChargingStation Model
 * 
 * This model represents the Charging Station entity from the backend API.
 * It contains all charging station information including location and availability.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class ChargingStation {

    @Json(name = "Id")
    private String id;

    @Json(name = "Name")
    private String name;

    @Json(name = "Location")
    private String location;

    @Json(name = "Type")
    private String type; // AC/DC

    @Json(name = "TotalSlots")
    private int totalSlots;

    @Json(name = "AvailableSlots")
    private int availableSlots;

    @Json(name = "Status")
    private String status; // Active/Deactivated

    @Json(name = "Schedule")
    private List<String> schedule;

    @Json(name = "Latitude")
    private double latitude;

    @Json(name = "Longitude")
    private double longitude;

    /**
     * Default constructor
     */
    public ChargingStation() {
    }

    /**
     * Constructor with parameters
     * 
     * @param id Station unique identifier
     * @param name Station name
     * @param location Station location/address
     * @param type Charging type (AC/DC)
     * @param totalSlots Total number of slots
     * @param availableSlots Number of available slots
     * @param status Station status (Active/Deactivated)
     * @param schedule Station schedule
     * @param latitude Station latitude
     * @param longitude Station longitude
     */
    public ChargingStation(String id, String name, String location, String type, 
                          int totalSlots, int availableSlots, String status, 
                          List<String> schedule, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.status = status;
        this.schedule = schedule;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<String> schedule) {
        this.schedule = schedule;
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
}

