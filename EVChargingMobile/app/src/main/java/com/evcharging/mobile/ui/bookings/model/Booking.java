package com.evcharging.mobile.ui.bookings.model;

/**
 * Booking Model
 * 
 * Simple data model for booking information.
 */
public class Booking {
    private String id;
    private String stationName;
    private String dateTime;
    private String status;
    private String type;
    private String qrToken; // optional; present when Confirmed

    public Booking(String id, String stationName, String dateTime, String status, String type) {
        this.id = id;
        this.stationName = stationName;
        this.dateTime = dateTime;
        this.status = status;
        this.type = type;
    }

    // Getters
    public String getId() { return id; }
    public String getStationName() { return stationName; }
    public String getDateTime() { return dateTime; }
    public String getStatus() { return status; }
    public String getType() { return type; }
    public String getQrToken() { return qrToken; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setStationName(String stationName) { this.stationName = stationName; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setStatus(String status) { this.status = status; }
    public void setType(String type) { this.type = type; }
    public void setQrToken(String qrToken) { this.qrToken = qrToken; }
}

