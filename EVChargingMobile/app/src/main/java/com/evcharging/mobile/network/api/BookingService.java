package com.evcharging.mobile.network.api;

import com.evcharging.mobile.network.models.Booking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * BookingService Interface
 * 
 * This service interface defines booking management API endpoints.
 * It handles booking creation, updates, cancellation, and retrieval operations.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public interface BookingService {

    /**
     * Get all bookings for a user
     * 
     * @param nic User NIC
     * @return Call<List<Booking>> containing user's bookings
     */
    @GET("booking")
    Call<List<Booking>> getUserBookings(@Query("nic") String nic);

    /**
     * Get booking by ID
     * 
     * @param id Booking ID
     * @return Call<Booking> containing booking details
     */
    @GET("booking/{id}")
    Call<Booking> getBookingById(@Path("id") String id);

    /**
     * Create new booking
     * 
     * @param booking Booking data
     * @return Call<Booking> containing created booking
     */
    @POST("booking")
    Call<Booking> createBooking(@Body Booking booking);

    /**
     * Update existing booking
     * 
     * @param id Booking ID
     * @param booking Updated booking data
     * @return Call<Booking> containing updated booking
     */
    @PUT("booking/{id}")
    Call<Booking> updateBooking(@Path("id") String id, @Body Booking booking);

    /**
     * Cancel booking
     * 
     * @param id Booking ID
     * @return Call<Void> for cancellation confirmation
     */
    @DELETE("booking/{id}")
    Call<Void> cancelBooking(@Path("id") String id);

    /**
     * Get upcoming bookings for a user
     * 
     * @param nic User NIC
     * @return Call<List<Booking>> containing upcoming bookings
     */
    @GET("booking/upcoming")
    Call<List<Booking>> getUpcomingBookings(@Query("nic") String nic);

    /**
     * Get past bookings for a user
     * 
     * @param nic User NIC
     * @return Call<List<Booking>> containing past bookings
     */
    @GET("booking/history")
    Call<List<Booking>> getPastBookings(@Query("nic") String nic);

    /**
     * Get bookings by status
     * 
     * @param nic User NIC
     * @param status Booking status (Pending, Approved, Cancelled, Completed)
     * @return Call<List<Booking>> containing bookings with specified status
     */
    @GET("booking")
    Call<List<Booking>> getBookingsByStatus(
            @Query("nic") String nic,
            @Query("status") String status
    );
}

