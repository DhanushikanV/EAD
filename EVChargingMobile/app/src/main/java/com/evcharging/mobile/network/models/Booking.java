package com.evcharging.mobile.network.models;

import com.squareup.moshi.Json;

/**
 * Booking Model
 * 
 * This model represents the Booking entity from the backend API.
 * It contains all booking information including reservation details and status.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class Booking {

    @Json(name = "id")
    private String id;

    @Json(name = "evOwnerNIC")
    private String evOwnerNIC;

    @Json(name = "stationId")
    private String stationId;

    @Json(name = "reservationDateTime")
    private String reservationDateTime;

    @Json(name = "status")
    private String status; // Pending, Approved, Cancelled, Completed

    @Json(name = "createdAt")
    private String createdAt;

    /**
     * Default constructor
     */
    public Booking() {
    }

    /**
     * Constructor with parameters
     * 
     * @param id Booking unique identifier
     * @param evOwnerNIC EV Owner NIC
     * @param stationId Station identifier
     * @param reservationDateTime Reservation date and time
     * @param status Booking status
     * @param createdAt Creation timestamp
     */
    public Booking(String id, String evOwnerNIC, String stationId, 
                   String reservationDateTime, String status, String createdAt) {
        this.id = id;
        this.evOwnerNIC = evOwnerNIC;
        this.stationId = stationId;
        this.reservationDateTime = reservationDateTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvOwnerNIC() {
        return evOwnerNIC;
    }

    public void setEvOwnerNIC(String evOwnerNIC) {
        this.evOwnerNIC = evOwnerNIC;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(String reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

