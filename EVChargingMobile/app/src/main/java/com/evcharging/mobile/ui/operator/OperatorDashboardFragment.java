package com.evcharging.mobile.ui.operator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.bookings.model.Booking;
import com.evcharging.mobile.ui.operator.adapter.TodayBookingsAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Operator Dashboard Fragment
 * 
 * Dashboard for station operators to manage bookings and station operations.
 */
public class OperatorDashboardFragment extends Fragment {

    private TextView tvStationName;
    private TextView tvTotalSlots;
    private TextView tvAvailableSlots;
    private TextView tvOccupiedSlots;
    private RecyclerView recyclerViewBookings;
    private TodayBookingsAdapter bookingsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operator_dashboard, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        loadStationData();
        loadTodayBookings();
        
        return view;
    }

    private void initializeViews(View view) {
        tvStationName = view.findViewById(R.id.tv_station_name);
        tvTotalSlots = view.findViewById(R.id.tv_total_slots);
        tvAvailableSlots = view.findViewById(R.id.tv_available_slots);
        tvOccupiedSlots = view.findViewById(R.id.tv_occupied_slots);
        recyclerViewBookings = view.findViewById(R.id.recycler_view_today_bookings);
    }

    private void setupRecyclerView() {
        bookingsAdapter = new TodayBookingsAdapter(new ArrayList<>(), new TodayBookingsAdapter.OnBookingActionListener() {
            @Override
            public void onBookingClick(Booking booking) {
                showBookingDetails(booking);
            }

            @Override
            public void onApproveBooking(Booking booking) {
                // TODO: Approve booking
                Toast.makeText(getContext(), "Booking approved: " + booking.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelBooking(Booking booking) {
                // TODO: Cancel booking
                Toast.makeText(getContext(), "Booking cancelled: " + booking.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBookings.setAdapter(bookingsAdapter);
    }

    private void loadStationData() {
        // Mock station data
        tvStationName.setText("Colombo Fort Station");
        tvTotalSlots.setText("6");
        tvAvailableSlots.setText("3");
        tvOccupiedSlots.setText("3");
    }

    private void loadTodayBookings() {
        List<Booking> todayBookings = getTodayBookings();
        bookingsAdapter.updateBookings(todayBookings);
    }

    private List<Booking> getTodayBookings() {
        List<Booking> bookings = new ArrayList<>();
        
        bookings.add(new Booking("BK001", "John Doe", "2024-01-15 09:00", "Approved", "Active"));
        bookings.add(new Booking("BK002", "Jane Smith", "2024-01-15 10:30", "Approved", "Active"));
        bookings.add(new Booking("BK003", "Mike Johnson", "2024-01-15 14:00", "Pending", "Scheduled"));
        bookings.add(new Booking("BK004", "Sarah Wilson", "2024-01-15 16:30", "Approved", "Scheduled"));
        
        return bookings;
    }

    private void showBookingDetails(Booking booking) {
        // TODO: Show booking details dialog
        // For now, just show a toast
        Toast.makeText(getContext(), 
            "Booking: " + booking.getId() + " - " + booking.getStationName(), 
            Toast.LENGTH_SHORT).show();
    }
}