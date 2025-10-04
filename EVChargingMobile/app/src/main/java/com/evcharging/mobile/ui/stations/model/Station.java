package com.evcharging.mobile.ui.stations.model;

import java.io.Serializable;

/**
 * Station Model
 * 
 * Simple data model for charging station information.
 */
public class Station implements Serializable {
    private String id;
    private String name;
    private String type;
    private double latitude;
    private double longitude;
    private String address;
    private int totalSlots;
    private int availableSlots;
    private String status;

    public Station(String id, String name, String type, double latitude, double longitude, 
                   String address, int totalSlots, int availableSlots, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getAddress() { return address; }
    public int getTotalSlots() { return totalSlots; }
    public int getAvailableSlots() { return availableSlots; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setAddress(String address) { this.address = address; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }
    public void setStatus(String status) { this.status = status; }
}
