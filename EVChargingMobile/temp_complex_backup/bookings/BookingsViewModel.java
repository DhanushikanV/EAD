package com.evcharging.mobile.ui.bookings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.evcharging.mobile.network.models.Booking;
import com.evcharging.mobile.repository.BookingsRepository;

import java.util.List;

/**
 * BookingsViewModel
 * 
 * This ViewModel manages the state and business logic for the bookings screen.
 * It handles loading, filtering, and management of user bookings.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class BookingsViewModel extends ViewModel {

    private final BookingsRepository bookingsRepository;
    private final MutableLiveData<List<Booking>> bookings = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BookingsViewModel() {
        bookingsRepository = new BookingsRepository();
    }

    /**
     * Get bookings LiveData
     * 
     * @return LiveData containing list of bookings
     */
    public LiveData<List<Booking>> getBookings() {
        return bookings;
    }

    /**
     * Get loading state LiveData
     * 
     * @return LiveData containing loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Get error message LiveData
     * 
     * @return LiveData containing error messages
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Load upcoming bookings
     */
    public void loadUpcomingBookings() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        // TODO: Implement actual repository call
        // For now, use mock data
        loadMockUpcomingBookings();
    }

    /**
     * Load past bookings
     */
    public void loadPastBookings() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        // TODO: Implement actual repository call
        // For now, use mock data
        loadMockPastBookings();
    }

    /**
     * Load mock upcoming bookings data (for development)
     */
    private void loadMockUpcomingBookings() {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            List<Booking> mockBookings = createMockUpcomingBookings();
            bookings.setValue(mockBookings);
            isLoading.setValue(false);
        }, 1000);
    }

    /**
     * Load mock past bookings data (for development)
     */
    private void loadMockPastBookings() {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            List<Booking> mockBookings = createMockPastBookings();
            bookings.setValue(mockBookings);
            isLoading.setValue(false);
        }, 1000);
    }

    /**
     * Create mock upcoming bookings data
     * 
     * @return List of mock upcoming bookings
     */
    private List<Booking> createMockUpcomingBookings() {
        long currentTime = System.currentTimeMillis();
        long oneDayInMillis = 24 * 60 * 60 * 1000L;
        
        return java.util.Arrays.asList(
            new Booking(
                "1",
                "123456789V",
                "1",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime + oneDayInMillis)),
                "Approved",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - oneDayInMillis))
            ),
            new Booking(
                "2",
                "123456789V",
                "2",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime + 2 * oneDayInMillis)),
                "Pending",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - 2 * oneDayInMillis))
            )
        );
    }

    /**
     * Create mock past bookings data
     * 
     * @return List of mock past bookings
     */
    private List<Booking> createMockPastBookings() {
        long currentTime = System.currentTimeMillis();
        long oneDayInMillis = 24 * 60 * 60 * 1000L;
        
        return java.util.Arrays.asList(
            new Booking(
                "3",
                "123456789V",
                "1",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - oneDayInMillis)),
                "Completed",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - 2 * oneDayInMillis))
            ),
            new Booking(
                "4",
                "123456789V",
                "3",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - 2 * oneDayInMillis)),
                "Completed",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - 3 * oneDayInMillis))
            ),
            new Booking(
                "5",
                "123456789V",
                "2",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - 3 * oneDayInMillis)),
                "Cancelled",
                java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date(currentTime - 4 * oneDayInMillis))
            )
        );
    }
}
