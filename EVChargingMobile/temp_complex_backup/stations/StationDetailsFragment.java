package com.evcharging.mobile.ui.stations;

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
import com.google.android.material.chip.Chip;

/**
 * StationDetailsFragment
 * 
 * This fragment displays detailed information about a specific charging station.
 * Users can view station details, check availability, and create bookings.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class StationDetailsFragment extends Fragment {

    private TextView tvStationName;
    private TextView tvStationLocation;
    private TextView tvStationAddress;
    private Chip chipStationType;
    private TextView tvTotalSlots;
    private TextView tvAvailableSlots;
    private TextView tvOperatingHours;
    private Button btnCreateBooking;
    private Button btnViewOnMap;
    private String stationId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get station ID from arguments
        if (getArguments() != null) {
            stationId = getArguments().getString("station_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_details, container, false);
        
        initializeViews(view);
        setupClickListeners();
        loadStationDetails();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        tvStationName = view.findViewById(R.id.tv_station_name);
        tvStationLocation = view.findViewById(R.id.tv_station_location);
        tvStationAddress = view.findViewById(R.id.tv_station_address);
        chipStationType = view.findViewById(R.id.chip_station_type);
        tvTotalSlots = view.findViewById(R.id.tv_total_slots);
        tvAvailableSlots = view.findViewById(R.id.tv_available_slots);
        tvOperatingHours = view.findViewById(R.id.tv_operating_hours);
        btnCreateBooking = view.findViewById(R.id.btn_create_booking);
        btnViewOnMap = view.findViewById(R.id.btn_view_on_map);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        btnCreateBooking.setOnClickListener(v -> {
            // Navigate to create booking screen
            Bundle bundle = new Bundle();
            bundle.putString("station_id", stationId);
            Navigation.findNavController(v).navigate(R.id.nav_create_booking, bundle);
        });

        btnViewOnMap.setOnClickListener(v -> {
            // TODO: Navigate to map view
            // For now, show a toast
            android.widget.Toast.makeText(requireContext(), 
                "Map view coming soon!", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Load and display station details
     */
    private void loadStationDetails() {
        if (stationId == null) {
            // Handle error - no station ID provided
            return;
        }

        // TODO: Load actual station data from repository
        // For now, display mock data
        displayMockStationData();
    }

    /**
     * Display mock station data (for development)
     */
    private void displayMockStationData() {
        tvStationName.setText("Colombo City Center");
        tvStationLocation.setText("Galle Face, Colombo 03");
        tvStationAddress.setText("Galle Face Green, Colombo 03, Sri Lanka");
        
        chipStationType.setText("AC");
        chipStationType.setChipBackgroundColorResource(R.color.ac_charging_color);
        
        tvTotalSlots.setText("4");
        tvAvailableSlots.setText("2");
        tvOperatingHours.setText("08:00 - 22:00");
        
        // Set availability indicator
        int available = Integer.parseInt(tvAvailableSlots.getText().toString());
        if (available > 0) {
            tvAvailableSlots.setTextColor(getResources().getColor(R.color.status_approved));
            btnCreateBooking.setEnabled(true);
        } else {
            tvAvailableSlots.setTextColor(getResources().getColor(R.color.status_cancelled));
            btnCreateBooking.setEnabled(false);
            btnCreateBooking.setText("No Slots Available");
        }
    }
}
