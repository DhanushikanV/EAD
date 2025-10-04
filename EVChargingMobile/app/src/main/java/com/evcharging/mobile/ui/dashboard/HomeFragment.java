package com.evcharging.mobile.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.CreateBookingActivity;

/**
 * Home Fragment
 * 
 * Main dashboard fragment showing pending reservations, approved reservations,
 * and nearby charging stations.
 */
public class HomeFragment extends Fragment {

    private TextView tvPendingReservations;
    private TextView tvApprovedReservations;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initializeViews(view);
        setupClickListeners(view);
        loadDashboardData();
        
        return view;
    }

    private void initializeViews(View view) {
        tvPendingReservations = view.findViewById(R.id.tv_pending_count);
        tvApprovedReservations = view.findViewById(R.id.tv_approved_count);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btn_create_booking).setOnClickListener(v -> {
            // Navigate to create booking activity
            Intent intent = new Intent(getActivity(), CreateBookingActivity.class);
            startActivity(intent);
        });
        
        view.findViewById(R.id.btn_view_stations).setOnClickListener(v -> {
            // TODO: Navigate to stations (will implement later)
            Toast.makeText(getContext(), "Stations feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadDashboardData() {
        // Mock data for now
        tvPendingReservations.setText("3");
        tvApprovedReservations.setText("5");
    }
}