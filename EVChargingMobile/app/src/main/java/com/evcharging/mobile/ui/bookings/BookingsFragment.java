package com.evcharging.mobile.ui.bookings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.network.api.BookingService;
import com.evcharging.mobile.network.models.Booking;
import com.evcharging.mobile.ui.bookings.adapter.BookingsAdapter;
import com.evcharging.mobile.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Bookings Fragment
 * 
 * Displays user's booking history and current reservations.
 */
public class BookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        loadBookings();
        
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_bookings);
    }

    private void setupRecyclerView() {
        adapter = new BookingsAdapter(new ArrayList<>(), booking -> {
            // Handle booking click
            Toast.makeText(getContext(), "Booking: " + booking.getStationName(), Toast.LENGTH_SHORT).show();
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadBookings() {
        Context context = getContext();
        if (context == null) return;

        SharedPreferencesManager prefsManager = new SharedPreferencesManager(context);
        String userNIC = prefsManager.getUserNIC();
        
        if (userNIC == null || userNIC.isEmpty()) {
            Log.w("BookingsFragment", "No user NIC found, using mock data");
            loadMockBookings();
            return;
        }

        Log.d("BookingsFragment", "Loading bookings for NIC: " + userNIC);
        
        BookingService bookingService = ApiClient.getRetrofitInstance(context).create(BookingService.class);
        Call<List<com.evcharging.mobile.network.models.Booking>> call = bookingService.getUserBookings(userNIC);
        
        call.enqueue(new Callback<List<com.evcharging.mobile.network.models.Booking>>() {
            @Override
            public void onResponse(Call<List<com.evcharging.mobile.network.models.Booking>> call, Response<List<com.evcharging.mobile.network.models.Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("BookingsFragment", "Successfully loaded " + response.body().size() + " bookings");
                    List<com.evcharging.mobile.ui.bookings.model.Booking> uiBookings = convertApiBookingsToUIBookings(response.body());
                    adapter.updateBookings(uiBookings);
                    
                    if (uiBookings.isEmpty()) {
                        Toast.makeText(context, "No bookings found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("BookingsFragment", "Failed to load bookings: " + response.code() + " - " + response.message());
                    Toast.makeText(context, "Failed to load bookings", Toast.LENGTH_SHORT).show();
                    loadMockBookings(); // Fallback to mock data
                }
            }

            @Override
            public void onFailure(Call<List<com.evcharging.mobile.network.models.Booking>> call, Throwable t) {
                Log.e("BookingsFragment", "Network error loading bookings", t);
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadMockBookings(); // Fallback to mock data
            }
        });
    }

    private void loadMockBookings() {
        List<com.evcharging.mobile.ui.bookings.model.Booking> bookings = getMockBookings();
        adapter.updateBookings(bookings);
    }

    private List<com.evcharging.mobile.ui.bookings.model.Booking> convertApiBookingsToUIBookings(List<com.evcharging.mobile.network.models.Booking> apiBookings) {
        List<com.evcharging.mobile.ui.bookings.model.Booking> uiBookings = new ArrayList<>();
        
        for (com.evcharging.mobile.network.models.Booking apiBooking : apiBookings) {
            com.evcharging.mobile.ui.bookings.model.Booking uiBooking = new com.evcharging.mobile.ui.bookings.model.Booking(
                apiBooking.getId() != null ? apiBooking.getId() : "Unknown",
                getStationNameFromId(apiBooking.getStationId()), // We'll need to map station IDs to names
                formatDateTime(apiBooking.getReservationDateTime()),
                apiBooking.getStatus() != null ? apiBooking.getStatus() : "Unknown",
                getBookingState(apiBooking.getStatus())
            );
            uiBookings.add(uiBooking);
        }
        
        return uiBookings;
    }

    private String getStationNameFromId(String stationId) {
        // For now, return a generic name. In a real app, you'd fetch station details
        return "Charging Station";
    }

    private String formatDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "Unknown";
        }
        // Simple formatting - in a real app you'd parse and format properly
        return dateTime.replace("T", " ").substring(0, Math.min(dateTime.length(), 16));
    }

    private String getBookingState(String status) {
        if (status == null) return "Unknown";
        
        switch (status.toLowerCase()) {
            case "pending":
                return "Scheduled";
            case "confirmed":
            case "approved":
                return "Active";
            case "completed":
                return "Finished";
            case "cancelled":
                return "Cancelled";
            default:
                return "Unknown";
        }
    }

    private List<com.evcharging.mobile.ui.bookings.model.Booking> getMockBookings() {
        List<com.evcharging.mobile.ui.bookings.model.Booking> bookings = new ArrayList<>();
        
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("1", "Colombo Fort Station", "2024-01-15 14:00", "Approved", "Active"));
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("2", "Kandy City Center", "2024-01-16 10:30", "Pending", "Scheduled"));
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("3", "Galle Fort Station", "2024-01-10 16:00", "Completed", "Finished"));
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("4", "Anuradhapura Station", "2024-01-12 09:15", "Cancelled", "Cancelled"));
        
        return bookings;
    }
}