package com.evcharging.mobile.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * BookingLocal Entity
 * 
 * This entity represents local booking data stored in SQLite database.
 * It contains booking information for offline access and synchronization.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
@Entity(tableName = "bookings_local")
public class BookingLocal {
    
    @PrimaryKey
    @NonNull
    private String bookingId;
    private String nic; // EV Owner NIC
    private String stationId;
    private long reservationDateTime;
    private String status; // Pending, Approved, Cancelled, Completed
    private String qrPayload; // QR code data for approved bookings
    private long createdAt;
    private long updatedAt;

    /**
     * Default constructor
     */
    public BookingLocal() {
    }

    /**
     * Constructor with all parameters
     * 
     * @param bookingId Booking unique identifier
     * @param nic EV Owner NIC
     * @param stationId Station identifier
     * @param reservationDateTime Reservation date and time (timestamp)
     * @param status Booking status
     * @param qrPayload QR code payload data
     * @param createdAt Creation timestamp
     * @param updatedAt Last update timestamp
     */
    public BookingLocal(String bookingId, String nic, String stationId, 
                       long reservationDateTime, String status, String qrPayload, 
                       long createdAt, long updatedAt) {
        this.bookingId = bookingId;
        this.nic = nic;
        this.stationId = stationId;
        this.reservationDateTime = reservationDateTime;
        this.status = status;
        this.qrPayload = qrPayload;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public long getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(long reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrPayload() {
        return qrPayload;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
