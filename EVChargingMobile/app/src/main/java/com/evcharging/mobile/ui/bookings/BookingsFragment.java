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
import com.evcharging.mobile.network.api.ChargingStationService;
import com.evcharging.mobile.network.models.Booking;
import com.evcharging.mobile.network.models.ChargingStation;
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
    private java.util.Map<String, ChargingStation> stationCache = new java.util.HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        loadBookings();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh bookings when fragment becomes visible (e.g., returning from CreateBookingActivity)
        Log.d("BookingsFragment", "Fragment resumed, refreshing bookings");
        loadBookings();
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_bookings);
        
        // Set up NEW BOOKING button click handler
        com.google.android.material.button.MaterialButton btnNewBooking = view.findViewById(R.id.btn_new_booking);
        if (btnNewBooking != null) {
            btnNewBooking.setOnClickListener(v -> {
                Log.d("BookingsFragment", "NEW BOOKING button clicked");
                // Start CreateBookingActivity instead of fragment
                android.content.Intent intent = new android.content.Intent(getActivity(), com.evcharging.mobile.ui.CreateBookingActivity.class);
                startActivity(intent);
            });
        } else {
            Log.w("BookingsFragment", "NEW BOOKING button not found");
        }
        
        // Set up REFRESH button click handler
        android.widget.ImageView btnRefresh = view.findViewById(R.id.btn_refresh);
        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> {
                Log.d("BookingsFragment", "REFRESH button clicked");
                loadBookings();
            });
        } else {
            Log.w("BookingsFragment", "REFRESH button not found");
        }
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
                Log.d("BookingsFragment", "Response received: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("BookingsFragment", "Successfully loaded " + response.body().size() + " bookings");
                    Log.d("BookingsFragment", "Raw booking data: " + response.body().toString());
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
            // Get station information
            ChargingStation station = getStationFromId(apiBooking.getStationId());
            String stationName = (station != null && station.getName() != null) ? station.getName() : "Station " + apiBooking.getStationId();
            
            Log.d("BookingsFragment", "Converting booking: " + apiBooking.getId() + 
                  ", StationId: " + apiBooking.getStationId() + 
                  ", StationName: " + stationName + 
                  ", DateTime: " + apiBooking.getReservationDateTime() + 
                  ", Status: " + apiBooking.getStatus());
            
            com.evcharging.mobile.ui.bookings.model.Booking uiBooking = new com.evcharging.mobile.ui.bookings.model.Booking(
                apiBooking.getId() != null ? apiBooking.getId() : "Unknown",
                stationName,
                formatDateTime(apiBooking.getReservationDateTime()),
                apiBooking.getStatus() != null ? apiBooking.getStatus() : "Unknown",
                getBookingState(apiBooking.getStatus())
            );
            uiBookings.add(uiBooking);
        }
        
        return uiBookings;
    }

    private ChargingStation getStationFromId(String stationId) {
        // Check cache first
        if (stationCache.containsKey(stationId)) {
            return stationCache.get(stationId);
        }
        
        // Try to fetch from API (this is a simplified version - in production you'd handle async properly)
        try {
            Context context = getContext();
            if (context != null) {
                ChargingStationService stationService = ApiClient.getRetrofitInstance(context).create(ChargingStationService.class);
                
                // For now, we'll create a station based on common IDs
                ChargingStation station = createStationFromId(stationId);
                stationCache.put(stationId, station);
                return station;
            }
        } catch (Exception e) {
            Log.e("BookingsFragment", "Error fetching station: " + stationId, e);
        }
        
        // Fallback to a generic station
        ChargingStation fallbackStation = new ChargingStation();
        fallbackStation.setId(stationId);
        fallbackStation.setName("Charging Station " + stationId);
        fallbackStation.setType("AC/DC");
        fallbackStation.setLocation("Sri Lanka");
        
        stationCache.put(stationId, fallbackStation);
        return fallbackStation;
    }
    
    private ChargingStation createStationFromId(String stationId) {
        Log.d("BookingsFragment", "Creating station for ID: " + stationId);
        
        ChargingStation station = new ChargingStation();
        station.setId(stationId);
        
        // Map common station IDs to known stations (based on the backend data)
        switch (stationId) {
            case "1":
                station.setName("Colombo City Centre Fast Charge");
                station.setType("DC Fast Charging");
                station.setLocation("Colombo City Centre Mall");
                Log.d("BookingsFragment", "Mapped station 1 to: Colombo City Centre Fast Charge");
                break;
            case "68e29a9e17275692c39f6848":
                station.setName("Colombo City Centre Fast Charge");
                station.setType("DC Fast Charging");
                station.setLocation("Colombo City Centre Mall");
                break;
            case "68e29a9f17275692c39f6849":
                station.setName("Orion Tech Park AC Hub");
                station.setType("AC Charging");
                station.setLocation("Orion City IT Park");
                break;
            case "68e29a9f17275692c39f684a":
                station.setName("Galle Fort Harbor Station");
                station.setType("DC Fast Charging");
                station.setLocation("Galle Fort Parking");
                break;
            case "68e29a9f17275692c39f684b":
                station.setName("BIA Airport QuickCharge");
                station.setType("DC Fast Charging");
                station.setLocation("Bandaranaike Intl Airport");
                break;
            case "68e29a9f17275692c39f684c":
                station.setName("University of Peradeniya Green Lot");
                station.setType("AC Charging");
                station.setLocation("University of Peradeniya");
                break;
            case "68e29a9f17275692c39f684d":
                station.setName("Galle Face Promenade Chargers");
                station.setType("AC Charging");
                station.setLocation("Galle Face Green Car Park");
                break;
            case "68e29a9f17275692c39f684e":
                station.setName("Kandy City Centre Multi-Storey");
                station.setType("AC Charging");
                station.setLocation("KCC Car Park");
                break;
            case "68e29a9f17275692c39f684f":
                station.setName("Jaffna Library Plaza Chargers");
                station.setType("DC Fast Charging");
                station.setLocation("Jaffna Public Library");
                break;
            default:
                station.setName("Charging Station " + stationId);
                station.setType("AC/DC Charging");
                station.setLocation("Sri Lanka");
                Log.d("BookingsFragment", "Using default station name for ID: " + stationId);
                break;
        }
        
        Log.d("BookingsFragment", "Created station: " + station.getName());
        return station;
    }

    private String formatDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "Unknown";
        }
        
        try {
            // Parse ISO datetime string
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", java.util.Locale.getDefault());
            
            java.util.Date date = inputFormat.parse(dateTime.substring(0, Math.min(dateTime.length(), 19)));
            return outputFormat.format(date);
        } catch (Exception e) {
            Log.e("BookingsFragment", "Error parsing date: " + dateTime, e);
            // Fallback to simple formatting
            return dateTime.replace("T", " ").substring(0, Math.min(dateTime.length(), 16));
        }
    }

    private String getBookingState(String status) {
        if (status == null) return "Unknown";
        
        switch (status.toLowerCase()) {
            case "pending":
                return "Pending Approval";
            case "confirmed":
            case "approved":
                return "Confirmed";
            case "completed":
                return "Completed";
            case "cancelled":
                return "Cancelled";
            default:
                return status; // Return the actual status if we don't have a mapping
        }
    }

    private List<com.evcharging.mobile.ui.bookings.model.Booking> getMockBookings() {
        List<com.evcharging.mobile.ui.bookings.model.Booking> bookings = new ArrayList<>();
        
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("1", "Colombo Fort Station", "Jan 15, 2024 at 14:00", "Approved", "Confirmed"));
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("2", "Kandy City Center", "Jan 16, 2024 at 10:30", "Pending", "Pending Approval"));
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("3", "Galle Fort Station", "Jan 10, 2024 at 16:00", "Completed", "Completed"));
        bookings.add(new com.evcharging.mobile.ui.bookings.model.Booking("4", "Anuradhapura Station", "Jan 12, 2024 at 09:15", "Cancelled", "Cancelled"));
        
        return bookings;
    }
}