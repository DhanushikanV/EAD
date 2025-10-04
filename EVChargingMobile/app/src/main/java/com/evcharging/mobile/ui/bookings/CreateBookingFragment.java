package com.evcharging.mobile.ui.bookings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.stations.model.Station;
import java.util.Calendar;
import java.util.List;

/**
 * Create Booking Fragment
 * 
 * Allows users to create new bookings for charging stations.
 */
public class CreateBookingFragment extends Fragment {

    private TextView tvSelectedStation;
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;
    private Button btnSelectStation;
    private Button btnSelectDate;
    private Button btnSelectTime;
    private Button btnCreateBooking;
    
    private Station selectedStation;
    private Calendar selectedDateTime;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_booking, container, false);
        
        initializeViews(view);
        setupClickListeners();
        selectedDateTime = Calendar.getInstance();
        
        return view;
    }

    private void initializeViews(View view) {
        tvSelectedStation = view.findViewById(R.id.tv_selected_station);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        tvSelectedTime = view.findViewById(R.id.tv_selected_time);
        btnSelectStation = view.findViewById(R.id.btn_select_station);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnSelectTime = view.findViewById(R.id.btn_select_time);
        btnCreateBooking = view.findViewById(R.id.btn_create_booking);
    }

    private void setupClickListeners() {
        btnSelectStation.setOnClickListener(v -> showStationSelection());
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSelectTime.setOnClickListener(v -> showTimePicker());
        btnCreateBooking.setOnClickListener(v -> createBooking());
    }

    private void showStationSelection() {
        // For now, show a simple station selection
        Toast.makeText(getContext(), "Station selection feature - selecting Colombo Fort Station", 
                      Toast.LENGTH_SHORT).show();
        
        // Mock station selection
        selectedStation = new Station("1", "Colombo Fort Station", "AC Fast Charging", 
                6.9271, 79.8612, "Colombo Fort, Sri Lanka", 4, 2, "Operational");
        
        tvSelectedStation.setText(selectedStation.getName() + " - " + 
                                selectedStation.getAvailableSlots() + "/" + 
                                selectedStation.getTotalSlots() + " slots available");
        
        updateCreateButtonState();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    
                    String dateStr = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    tvSelectedDate.setText(dateStr);
                    updateCreateButtonState();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    
                    String timeStr = String.format("%02d:%02d", hourOfDay, minute);
                    tvSelectedTime.setText(timeStr);
                    updateCreateButtonState();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true // 24-hour format
        );
        timePickerDialog.show();
    }

    private void createBooking() {
        if (selectedStation == null) {
            Toast.makeText(getContext(), "Please select a charging station", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (tvSelectedDate.getText().toString().equals("Select Date")) {
            Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (tvSelectedTime.getText().toString().equals("Select Time")) {
            Toast.makeText(getContext(), "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create mock booking
        String bookingId = "BK" + System.currentTimeMillis();
        String dateTime = tvSelectedDate.getText().toString() + " " + tvSelectedTime.getText().toString();
        
        Toast.makeText(getContext(), 
            "Booking created successfully!\n" +
            "Booking ID: " + bookingId + "\n" +
            "Station: " + selectedStation.getName() + "\n" +
            "Date & Time: " + dateTime,
            Toast.LENGTH_LONG).show();
        
        // TODO: Save booking to database and sync with backend
        // For now, just show success message
        
        // Navigate back to bookings list
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    private void updateCreateButtonState() {
        boolean canCreate = selectedStation != null && 
                           !tvSelectedDate.getText().toString().equals("Select Date") &&
                           !tvSelectedTime.getText().toString().equals("Select Time");
        
        btnCreateBooking.setEnabled(canCreate);
        btnCreateBooking.setAlpha(canCreate ? 1.0f : 0.5f);
    }
}