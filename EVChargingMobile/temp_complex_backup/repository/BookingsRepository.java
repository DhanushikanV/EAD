package com.evcharging.mobile.repository;

import android.content.Context;

import com.evcharging.mobile.db.database.AppDatabase;
import com.evcharging.mobile.db.dao.BookingDao;
import com.evcharging.mobile.db.entities.BookingLocal;
import com.evcharging.mobile.network.api.BookingService;
import com.evcharging.mobile.network.models.Booking;
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * BookingsRepository
 * 
 * This repository handles data operations for user bookings.
 * It manages both local database and remote API operations.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class BookingsRepository {

    private final BookingDao bookingDao;
    private final BookingService apiService;
    private final SharedPreferencesManager preferencesManager;
    private final Context context;

    /**
     * Constructor with context injection
     * 
     * @param context Application context
     */
    public BookingsRepository(Context context) {
        this.context = context;
        
        // Initialize database DAO
        AppDatabase database = AppDatabase.getInstance(context);
        this.bookingDao = database.bookingDao();
        
        // Initialize API service
        this.apiService = ApiClient.getRetrofitInstance(context).create(BookingService.class);
        
        // Initialize preferences manager
        this.preferencesManager = new SharedPreferencesManager(context);
    }

    /**
     * Get all bookings for current user
     * 
     * @return List of user bookings
     */
    public List<Booking> getUserBookings() {
        String userNIC = preferencesManager.getUserNIC();
        if (userNIC == null) {
            return new ArrayList<>();
        }
        
        try {
            Response<List<Booking>> response = apiService.getUserBookings(userNIC).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                List<Booking> bookings = response.body();
                
                // Cache bookings locally
                cacheBookings(bookings);
                
                return bookings;
            } else {
                // Return cached data if API fails
                return getCachedBookings(userNIC);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return cached data on error
            return getCachedBookings(userNIC);
        }
    }

    /**
     * Get upcoming bookings for current user
     * 
     * @return List of upcoming bookings
     */
    public List<Booking> getUpcomingBookings() {
        List<Booking> allBookings = getUserBookings();
        List<Booking> upcomingBookings = new ArrayList<>();
        
        long currentTime = System.currentTimeMillis();
        
        for (Booking booking : allBookings) {
            try {
                // Parse booking datetime and check if it's in the future
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                    "MMM dd, yyyy h:mm:ss a", java.util.Locale.getDefault());
                java.util.Date bookingDate = format.parse(booking.getReservationDateTime());
                
                if (bookingDate != null && bookingDate.getTime() > currentTime) {
                    upcomingBookings.add(booking);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return upcomingBookings;
    }

    /**
     * Get past bookings for current user
     * 
     * @return List of past bookings
     */
    public List<Booking> getPastBookings() {
        List<Booking> allBookings = getUserBookings();
        List<Booking> pastBookings = new ArrayList<>();
        
        long currentTime = System.currentTimeMillis();
        
        for (Booking booking : allBookings) {
            try {
                // Parse booking datetime and check if it's in the past
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                    "MMM dd, yyyy h:mm:ss a", java.util.Locale.getDefault());
                java.util.Date bookingDate = format.parse(booking.getReservationDateTime());
                
                if (bookingDate != null && bookingDate.getTime() <= currentTime) {
                    pastBookings.add(booking);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return pastBookings;
    }

    /**
     * Create new booking
     * 
     * @param booking Booking data
     * @return Created booking or null if failed
     */
    public Booking createBooking(Booking booking) {
        try {
            Response<Booking> response = apiService.createBooking(booking).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                Booking createdBooking = response.body();
                
                // Cache the new booking
                cacheBooking(createdBooking);
                
                return createdBooking;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update existing booking
     * 
     * @param bookingId Booking ID
     * @param booking Updated booking data
     * @return Updated booking or null if failed
     */
    public Booking updateBooking(String bookingId, Booking booking) {
        try {
            Response<Void> response = apiService.updateBooking(bookingId, booking).execute();
            
            if (response.isSuccessful()) {
                // Update local cache
                cacheBooking(booking);
                return booking;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cancel booking
     * 
     * @param bookingId Booking ID
     * @return true if successful, false otherwise
     */
    public boolean cancelBooking(String bookingId) {
        try {
            Response<Void> response = apiService.deleteBooking(bookingId).execute();
            
            if (response.isSuccessful()) {
                // Remove from local cache
                removeCachedBooking(bookingId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get booking by ID
     * 
     * @param bookingId Booking ID
     * @return Booking or null if not found
     */
    public Booking getBookingById(String bookingId) {
        try {
            Response<Booking> response = apiService.getBookingById(bookingId).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Return cached booking if API fails
        return getCachedBookingById(bookingId);
    }

    /**
     * Cache booking locally
     * 
     * @param booking Booking to cache
     */
    private void cacheBooking(Booking booking) {
        new Thread(() -> {
            try {
                BookingLocal bookingLocal = convertToLocalEntity(booking);
                bookingDao.insertOrUpdate(bookingLocal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Cache multiple bookings locally
     * 
     * @param bookings List of bookings to cache
     */
    private void cacheBookings(List<Booking> bookings) {
        new Thread(() -> {
            try {
                for (Booking booking : bookings) {
                    BookingLocal bookingLocal = convertToLocalEntity(booking);
                    bookingDao.insertOrUpdate(bookingLocal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Get cached bookings for user
     * 
     * @param userNIC User NIC
     * @return List of cached bookings
     */
    private List<Booking> getCachedBookings(String userNIC) {
        try {
            List<BookingLocal> cachedBookings = bookingDao.getBookingsByNIC(userNIC);
            List<Booking> bookings = new ArrayList<>();
            
            for (BookingLocal cached : cachedBookings) {
                bookings.add(convertToApiModel(cached));
            }
            
            return bookings;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get cached booking by ID
     * 
     * @param bookingId Booking ID
     * @return Cached booking or null
     */
    private Booking getCachedBookingById(String bookingId) {
        try {
            BookingLocal cached = bookingDao.getBookingById(bookingId);
            return cached != null ? convertToApiModel(cached) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Remove cached booking
     * 
     * @param bookingId Booking ID to remove
     */
    private void removeCachedBooking(String bookingId) {
        new Thread(() -> {
            try {
                bookingDao.deleteBooking(bookingId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Convert API model to local entity
     * 
     * @param apiBooking API booking model
     * @return Local booking entity
     */
    private BookingLocal convertToLocalEntity(Booking apiBooking) {
        BookingLocal local = new BookingLocal();
        local.bookingId = apiBooking.getId();
        local.nic = apiBooking.getEVOwnerNIC();
        local.stationId = apiBooking.getStationId();
        local.reservationDateTime = apiBooking.getReservationDateTime();
        local.status = apiBooking.getStatus();
        local.createdAt = apiBooking.getCreatedAt();
        local.lastSyncAt = System.currentTimeMillis();
        return local;
    }

    /**
     * Convert local entity to API model
     * 
     * @param localBooking Local booking entity
     * @return API booking model
     */
    private Booking convertToApiModel(BookingLocal localBooking) {
        Booking api = new Booking();
        api.setId(localBooking.bookingId);
        api.setEVOwnerNIC(localBooking.nic);
        api.setStationId(localBooking.stationId);
        api.setReservationDateTime(localBooking.reservationDateTime);
        api.setStatus(localBooking.status);
        api.setCreatedAt(localBooking.createdAt);
        return api;
    }

    /**
     * Check if booking can be modified
     * 
     * @param bookingDateTime Booking date/time
     * @return true if booking can be modified
     */
    public boolean canModifyBooking(long bookingDateTime) {
        long currentTime = System.currentTimeMillis();
        long twelveHoursInMillis = 12 * 60 * 60 * 1000L;
        
        return (bookingDateTime - currentTime) >= twelveHoursInMillis;
    }

    /**
     * Check if booking can be cancelled
     * 
     * @param bookingDateTime Booking date/time
     * @return true if booking can be cancelled
     */
    public boolean canCancelBooking(long bookingDateTime) {
        return canModifyBooking(bookingDateTime);
    }

    /**
     * Refresh bookings data from API
     */
    public void refreshBookings() {
        String userNIC = preferencesManager.getUserNIC();
        if (userNIC != null) {
            getUserBookings(); // This will refresh the cache
        }
    }
}