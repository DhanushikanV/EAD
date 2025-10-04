package com.evcharging.mobile.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.evcharging.mobile.R;
import com.evcharging.mobile.utils.SharedPreferencesManager;

/**
 * HomeFragment
 * 
 * This fragment displays the main dashboard for EV owners. It shows
 * reservation counts, nearby stations, and quick actions.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class HomeFragment extends Fragment {

    private TextView tvWelcomeMessage;
    private TextView tvPendingCount;
    private TextView tvApprovedCount;
    private Button btnViewStations;
    private Button btnCreateBooking;
    private Button btnViewBookings;
    private SharedPreferencesManager preferencesManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        preferencesManager = new SharedPreferencesManager(requireContext());
        
        initializeViews(view);
        setupClickListeners();
        loadDashboardData();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        tvWelcomeMessage = view.findViewById(R.id.tv_welcome_message);
        tvPendingCount = view.findViewById(R.id.tv_pending_count);
        tvApprovedCount = view.findViewById(R.id.tv_approved_count);
        btnViewStations = view.findViewById(R.id.btn_view_stations);
        btnCreateBooking = view.findViewById(R.id.btn_create_booking);
        btnViewBookings = view.findViewById(R.id.btn_view_bookings);
    }

    /**
     * Set up click listeners for navigation buttons
     */
    private void setupClickListeners() {
        btnViewStations.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.nav_stations);
        });

        btnCreateBooking.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.nav_create_booking);
        });

        btnViewBookings.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.nav_bookings);
        });
    }

    /**
     * Load and display dashboard data
     */
    private void loadDashboardData() {
        // Set welcome message
        String userName = preferencesManager.getUserName();
        if (userName != null && !userName.isEmpty()) {
            tvWelcomeMessage.setText(getString(R.string.welcome_user, userName));
        } else {
            tvWelcomeMessage.setText(getString(R.string.welcome_message));
        }

        // TODO: Load actual data from repository
        // For now, display placeholder data
        tvPendingCount.setText("0");
        tvApprovedCount.setText("0");
    }

    /**
     * Refresh dashboard data
     */
    public void refreshData() {
        loadDashboardData();
    }
}
