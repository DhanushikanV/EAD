package com.evcharging.mobile.ui.operator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evcharging.mobile.R;
import com.evcharging.mobile.network.models.Booking;
import com.evcharging.mobile.ui.operator.adapter.TodayBookingsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * OperatorDashboardFragment
 * 
 * This fragment displays the operator dashboard with today's bookings
 * and quick access to QR scanner and booking management.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class OperatorDashboardFragment extends Fragment {

    private TextView tvWelcomeOperator;
    private TextView tvTodayBookingsCount;
    private TextView tvActiveSessionsCount;
    private RecyclerView recyclerViewTodayBookings;
    private Button btnQrScanner;
    private Button btnViewAllBookings;
    private TodayBookingsAdapter todayBookingsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operator_dashboard, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadOperatorData();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        tvWelcomeOperator = view.findViewById(R.id.tv_welcome_operator);
        tvTodayBookingsCount = view.findViewById(R.id.tv_today_bookings_count);
        tvActiveSessionsCount = view.findViewById(R.id.tv_active_sessions_count);
        recyclerViewTodayBookings = view.findViewById(R.id.recycler_view_today_bookings);
        btnQrScanner = view.findViewById(R.id.btn_qr_scanner);
        btnViewAllBookings = view.findViewById(R.id.btn_view_all_bookings);
    }

    /**
     * Set up RecyclerView with adapter
     */
    private void setupRecyclerView() {
        todayBookingsAdapter = new TodayBookingsAdapter(booking -> {
            // Handle booking item click
            handleBookingClick(booking);
        });
        
        recyclerViewTodayBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewTodayBookings.setAdapter(todayBookingsAdapter);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        btnQrScanner.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.nav_qr_scanner);
        });

        btnViewAllBookings.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.nav_today_bookings);
        });
    }

    /**
     * Load operator data
     */
    private void loadOperatorData() {
        // Set welcome message
        tvWelcomeOperator.setText("Welcome, Station Operator!");
        
        // Load today's bookings
        loadTodayBookings();
        
        // Update statistics
        updateStatistics();
    }

    /**
     * Load today's bookings
     */
    private void loadTodayBookings() {
        // TODO: Load actual bookings from repository
        // For now, use mock data
        List<Booking> todayBookings = createMockTodayBookings();
        todayBookingsAdapter.updateBookings(todayBookings);
        
        // Update count
        tvTodayBookingsCount.setText(String.valueOf(todayBookings.size()));
    }

    /**
     * Update statistics
     */
    private void updateStatistics() {
        // TODO: Calculate actual statistics
        // For now, use mock data
        tvTodayBookingsCount.setText("5");
        tvActiveSessionsCount.setText("2");
    }

    /**
     * Handle booking item click
     * 
     * @param booking Selected booking
     */
    private void handleBookingClick(Booking booking) {
        // TODO: Navigate to booking details or show action dialog
        android.widget.Toast.makeText(requireContext(), 
            "Booking: " + booking.getId(), android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Create mock today's bookings (for development)
     * 
     * @return List of mock bookings
     */
    private List<Booking> createMockTodayBookings() {
        List<Booking> bookings = new ArrayList<>();
        
        long currentTime = System.currentTimeMillis();
        long oneHourInMillis = 60 * 60 * 1000L;
        
        // Morning booking
        Booking booking1 = new Booking();
        booking1.setId("1");
        booking1.setEVOwnerNIC("123456789V");
        booking1.setStationId("1");
        booking1.setReservationDateTime("Dec 15, 2024 9:00:00 AM");
        booking1.setStatus("Approved");
        booking1.setCreatedAt("Dec 14, 2024 8:00:00 PM");
        bookings.add(booking1);
        
        // Afternoon booking
        Booking booking2 = new Booking();
        booking2.setId("2");
        booking2.setEVOwnerNIC("987654321V");
        booking2.setStationId("2");
        booking2.setReservationDateTime("Dec 15, 2024 2:00:00 PM");
        booking2.setStatus("Approved");
        booking2.setCreatedAt("Dec 14, 2024 6:00:00 PM");
        bookings.add(booking2);
        
        // Evening booking
        Booking booking3 = new Booking();
        booking3.setId("3");
        booking3.setEVOwnerNIC("456789123V");
        booking3.setStationId("1");
        booking3.setReservationDateTime("Dec 15, 2024 6:00:00 PM");
        booking3.setStatus("Pending");
        booking3.setCreatedAt("Dec 15, 2024 10:00:00 AM");
        bookings.add(booking3);
        
        return bookings;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to dashboard
        loadOperatorData();
    }
}
