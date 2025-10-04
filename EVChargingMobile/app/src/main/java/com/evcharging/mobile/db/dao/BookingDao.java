package com.evcharging.mobile.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.evcharging.mobile.db.entities.BookingLocal;

import java.util.List;

/**
 * BookingDao Interface
 * 
 * This DAO provides methods to interact with the bookings_local table.
 * It defines database operations for booking data management.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
@Dao
public interface BookingDao {

    /**
     * Insert a new booking or replace if exists
     * 
     * @param booking Booking entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooking(BookingLocal booking);

    /**
     * Insert multiple bookings
     * 
     * @param bookings List of booking entities to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookings(List<BookingLocal> bookings);

    /**
     * Update existing booking
     * 
     * @param booking Booking entity to update
     */
    @Update
    void updateBooking(BookingLocal booking);

    /**
     * Delete booking
     * 
     * @param booking Booking entity to delete
     */
    @Delete
    void deleteBooking(BookingLocal booking);

    /**
     * Get all bookings for a user (by NIC)
     * 
     * @param nic User NIC
     * @return List of bookings for the user
     */
    @Query("SELECT * FROM bookings_local WHERE nic = :nic ORDER BY reservationDateTime DESC")
    List<BookingLocal> getBookingsByNic(String nic);

    /**
     * Get booking by ID
     * 
     * @param bookingId Booking ID
     * @return BookingLocal entity or null if not found
     */
    @Query("SELECT * FROM bookings_local WHERE bookingId = :bookingId")
    BookingLocal getBookingById(String bookingId);

    /**
     * Get upcoming bookings for a user
     * 
     * @param nic User NIC
     * @param currentTime Current timestamp
     * @return List of upcoming bookings
     */
    @Query("SELECT * FROM bookings_local WHERE nic = :nic AND reservationDateTime > :currentTime ORDER BY reservationDateTime ASC")
    List<BookingLocal> getUpcomingBookings(String nic, long currentTime);

    /**
     * Get past bookings for a user
     * 
     * @param nic User NIC
     * @param currentTime Current timestamp
     * @return List of past bookings
     */
    @Query("SELECT * FROM bookings_local WHERE nic = :nic AND reservationDateTime < :currentTime ORDER BY reservationDateTime DESC")
    List<BookingLocal> getPastBookings(String nic, long currentTime);

    /**
     * Get bookings by status
     * 
     * @param nic User NIC
     * @param status Booking status
     * @return List of bookings with specified status
     */
    @Query("SELECT * FROM bookings_local WHERE nic = :nic AND status = :status ORDER BY reservationDateTime DESC")
    List<BookingLocal> getBookingsByStatus(String nic, String status);

    /**
     * Get bookings for a station on a specific date
     * 
     * @param stationId Station ID
     * @param date Date timestamp (start of day)
     * @param nextDay Next day timestamp (start of next day)
     * @return List of bookings for the station on the date
     */
    @Query("SELECT * FROM bookings_local WHERE stationId = :stationId AND reservationDateTime >= :date AND reservationDateTime < :nextDay ORDER BY reservationDateTime ASC")
    List<BookingLocal> getBookingsForStationAndDate(String stationId, long date, long nextDay);

    /**
     * Get bookings for today (for operators)
     * 
     * @param stationId Station ID
     * @param startOfDay Start of current day timestamp
     * @param endOfDay End of current day timestamp
     * @return List of today's bookings for the station
     */
    @Query("SELECT * FROM bookings_local WHERE stationId = :stationId AND reservationDateTime >= :startOfDay AND reservationDateTime <= :endOfDay ORDER BY reservationDateTime ASC")
    List<BookingLocal> getTodayBookings(String stationId, long startOfDay, long endOfDay);

    /**
     * Delete all bookings for a user (for logout)
     * 
     * @param nic User NIC
     */
    @Query("DELETE FROM bookings_local WHERE nic = :nic")
    void deleteBookingsByNic(String nic);

    /**
     * Delete all bookings (for refresh)
     */
    @Query("DELETE FROM bookings_local")
    void deleteAllBookings();

    /**
     * Update booking status
     * 
     * @param bookingId Booking ID
     * @param status New status
     * @param updatedAt Last update timestamp
     */
    @Query("UPDATE bookings_local SET status = :status, updatedAt = :updatedAt WHERE bookingId = :bookingId")
    void updateBookingStatus(String bookingId, String status, long updatedAt);

    /**
     * Update booking QR payload
     * 
     * @param bookingId Booking ID
     * @param qrPayload QR payload data
     * @param updatedAt Last update timestamp
     */
    @Query("UPDATE bookings_local SET qrPayload = :qrPayload, updatedAt = :updatedAt WHERE bookingId = :bookingId")
    void updateBookingQRPayload(String bookingId, String qrPayload, long updatedAt);
}
