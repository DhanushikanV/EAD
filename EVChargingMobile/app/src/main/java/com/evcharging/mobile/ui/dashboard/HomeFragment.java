package com.evcharging.mobile.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.CreateBookingActivity;
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.network.api.BookingService;
import com.evcharging.mobile.network.models.Booking;
import com.evcharging.mobile.utils.SharedPreferencesManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Home Fragment
 * 
 * Main dashboard fragment showing pending reservations, approved reservations,
 * and nearby charging stations.
 */
public class HomeFragment extends Fragment {

    private TextView tvPendingReservations;
    private TextView tvApprovedReservations;
    private LinearLayout recentActivityContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initializeViews(view);
        setupClickListeners(view);
        loadDashboardData();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh dashboard data when fragment becomes visible
        Log.d("HomeFragment", "Fragment resumed, refreshing dashboard data");
        loadDashboardData();
    }

    private void initializeViews(View view) {
        tvPendingReservations = view.findViewById(R.id.tv_pending_count);
        tvApprovedReservations = view.findViewById(R.id.tv_approved_count);
        recentActivityContainer = view.findViewById(R.id.recent_activity_container);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btn_create_booking).setOnClickListener(v -> {
            // Navigate to create booking activity
            Intent intent = new Intent(getActivity(), CreateBookingActivity.class);
            startActivity(intent);
        });
        
        view.findViewById(R.id.btn_view_stations).setOnClickListener(v -> {
            // Navigate to stations tab
            try {
                androidx.navigation.NavController navController = androidx.navigation.Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(com.evcharging.mobile.R.id.nav_stations);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to open stations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDashboardData() {
        Context context = getContext();
        if (context == null) return;

        // Get user NIC from SharedPreferences
        SharedPreferencesManager prefsManager = new SharedPreferencesManager(context);
        String userNIC = prefsManager.getUserNIC();
        
        if (userNIC == null || userNIC.isEmpty()) {
            Log.w("HomeFragment", "No user NIC found, using default data");
            tvPendingReservations.setText("0");
            tvApprovedReservations.setText("0");
            return;
        }

        Log.d("HomeFragment", "Loading dashboard data for NIC: " + userNIC);
        
        // Load real booking data from API
        BookingService bookingService = ApiClient.getRetrofitInstance(context).create(BookingService.class);
        Call<List<Booking>> call = bookingService.getUserBookings(userNIC);
        
        call.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();
                    Log.d("HomeFragment", "Successfully loaded " + bookings.size() + " bookings");
                    updateDashboardData(bookings);
                } else {
                    Log.e("HomeFragment", "Failed to load bookings: " + response.code());
                    setDefaultData();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Log.e("HomeFragment", "Network error loading bookings", t);
                setDefaultData();
            }
        });
    }
    
    private void updateDashboardData(List<Booking> bookings) {
        int pendingCount = 0;
        int approvedCount = 0;
        List<Booking> recentBookings = new ArrayList<>();
        
        // Count bookings by status and get recent ones
        for (Booking booking : bookings) {
            if ("Pending".equalsIgnoreCase(booking.getStatus())) {
                pendingCount++;
            } else if ("Confirmed".equalsIgnoreCase(booking.getStatus()) || "Approved".equalsIgnoreCase(booking.getStatus())) {
                approvedCount++;
            }
            recentBookings.add(booking);
        }
        
        // Update counts
        tvPendingReservations.setText(String.valueOf(pendingCount));
        tvApprovedReservations.setText(String.valueOf(approvedCount));
        
        // Update recent activity (show last 2 bookings)
        updateRecentActivity(recentBookings);
    }
    
    private void updateRecentActivity(List<Booking> bookings) {
        if (recentActivityContainer == null) return;
        
        // Clear existing mock data
        recentActivityContainer.removeAllViews();
        
        // Show last 2 bookings
        int count = Math.min(2, bookings.size());
        for (int i = bookings.size() - count; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            addRecentBookingItem(booking);
        }
        
        if (bookings.isEmpty()) {
            addEmptyState();
        }
    }
    
    private void addRecentBookingItem(Booking booking) {
        // Create the booking item layout programmatically
        LinearLayout itemLayout = new LinearLayout(getContext());
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(0, 0, 0, 32); // 8dp margin bottom
        
        // Blue dot indicator
        View dot = new View(getContext());
        dot.setBackgroundResource(R.drawable.circle_background);
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(32, 32); // 8dp
        dotParams.rightMargin = 48; // 12dp
        dotParams.gravity = android.view.Gravity.CENTER_VERTICAL;
        itemLayout.addView(dot, dotParams);
        
        // Booking details container
        LinearLayout detailsLayout = new LinearLayout(getContext());
        detailsLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        itemLayout.addView(detailsLayout, detailsParams);
        
        // Station name
        TextView stationName = new TextView(getContext());
        stationName.setText(getStationName(booking.getStationId()));
        stationName.setTextSize(14);
        stationName.setTextColor(getResources().getColor(R.color.text_primary));
        detailsLayout.addView(stationName);
        
        // Date and time
        TextView dateTime = new TextView(getContext());
        dateTime.setText(formatDateTime(booking.getReservationDateTime()));
        dateTime.setTextSize(12);
        dateTime.setTextColor(getResources().getColor(R.color.text_secondary));
        detailsLayout.addView(dateTime);
        
        // Status badge
        TextView statusBadge = new TextView(getContext());
        String status = getBookingStatus(booking.getStatus());
        statusBadge.setText(status);
        statusBadge.setTextSize(12);
        statusBadge.setPadding(16, 16, 16, 16); // 4dp padding
        statusBadge.setBackgroundResource(R.drawable.availability_background_available);
        
        // Always use white text for better visibility on green background
        statusBadge.setTextColor(android.graphics.Color.WHITE);
        
        itemLayout.addView(statusBadge);
        
        recentActivityContainer.addView(itemLayout);
    }
    
    private void addEmptyState() {
        TextView emptyText = new TextView(getContext());
        emptyText.setText("No recent bookings");
        emptyText.setTextSize(14);
        emptyText.setTextColor(getResources().getColor(R.color.text_secondary));
        emptyText.setPadding(0, 32, 0, 0); // 8dp margin top
        recentActivityContainer.addView(emptyText);
    }
    
    private String getStationName(String stationId) {
        // Use the same station mapping as BookingsFragment
        switch (stationId) {
            case "1":
            case "68e29a9e17275692c39f6848":
                return "Colombo City Centre Fast Charge";
            case "68e29a9f17275692c39f6849":
                return "Orion Tech Park AC Hub";
            case "68e29a9f17275692c39f684a":
                return "Galle Fort Harbor Station";
            case "68e29a9f17275692c39f684b":
                return "BIA Airport QuickCharge";
            case "68e29a9f17275692c39f684c":
                return "University of Peradeniya Green Lot";
            case "68e29a9f17275692c39f684d":
                return "Galle Face Promenade Chargers";
            case "68e29a9f17275692c39f684e":
                return "Kandy City Centre Multi-Storey";
            case "68e29a9f17275692c39f684f":
                return "Jaffna Library Plaza Chargers";
            default:
                return "Charging Station " + stationId;
        }
    }
    
    private String formatDateTime(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(dateTime.substring(0, Math.min(dateTime.length(), 19)));
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateTime.replace("T", " ").substring(0, Math.min(dateTime.length(), 16));
        }
    }
    
    private String getBookingStatus(String status) {
        switch (status.toLowerCase()) {
            case "pending": return "Pending";
            case "confirmed": 
            case "approved": return "Confirmed";
            case "completed": return "Completed";
            case "cancelled": return "Cancelled";
            default: return status;
        }
    }
    
    private void setDefaultData() {
        tvPendingReservations.setText("0");
        tvApprovedReservations.setText("0");
        if (recentActivityContainer != null) {
            recentActivityContainer.removeAllViews();
            addEmptyState();
        }
    }
}