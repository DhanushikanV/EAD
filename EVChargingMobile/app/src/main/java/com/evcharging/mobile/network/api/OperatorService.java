package com.evcharging.mobile.network.api;

import com.evcharging.mobile.network.models.Booking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * OperatorService Interface
 * 
 * This service interface defines station operator API endpoints.
 * It handles QR code scanning, session management, and operator-specific operations.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public interface OperatorService {

    /**
     * Scan and validate QR code
     * 
     * @param qrData QR code payload data
     * @return Call<Booking> containing booking details from QR code
     */
    @POST("operator/scan")
    Call<Booking> scanQRCode(@Body QRScanRequest qrData);

    /**
     * Get today's bookings for a station
     * 
     * @param stationId Station ID
     * @param date Date in YYYY-MM-DD format
     * @return Call<List<Booking>> containing today's bookings
     */
    @GET("operator/bookings/today")
    Call<List<Booking>> getTodayBookings(
            @Query("stationId") String stationId,
            @Query("date") String date
    );

    /**
     * Start charging session
     * 
     * @param bookingId Booking ID
     * @return Call<Void> for session start confirmation
     */
    @POST("operator/booking/{id}/start")
    Call<Void> startSession(@Path("id") String bookingId);

    /**
     * End charging session
     * 
     * @param bookingId Booking ID
     * @return Call<Void> for session end confirmation
     */
    @POST("operator/booking/{id}/end")
    Call<Void> endSession(@Path("id") String bookingId);

    /**
     * Update station slot availability
     * 
     * @param stationId Station ID
     * @param availableSlots Number of available slots
     * @return Call<Void> for update confirmation
     */
    @POST("operator/station/{id}/slots")
    Call<Void> updateStationSlots(
            @Path("id") String stationId,
            @Body SlotUpdateRequest request
    );

    /**
     * QR Scan Request Model
     */
    class QRScanRequest {
        private String qrPayload;

        public QRScanRequest(String qrPayload) {
            this.qrPayload = qrPayload;
        }

        public String getQrPayload() {
            return qrPayload;
        }

        public void setQrPayload(String qrPayload) {
            this.qrPayload = qrPayload;
        }
    }

    /**
     * Slot Update Request Model
     */
    class SlotUpdateRequest {
        private int availableSlots;

        public SlotUpdateRequest(int availableSlots) {
            this.availableSlots = availableSlots;
        }

        public int getAvailableSlots() {
            return availableSlots;
        }

        public void setAvailableSlots(int availableSlots) {
            this.availableSlots = availableSlots;
        }
    }
}
