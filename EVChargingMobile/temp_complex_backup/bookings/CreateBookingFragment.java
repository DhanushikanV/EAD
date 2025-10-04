package com.evcharging.mobile.ui.bookings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.evcharging.mobile.R;
import com.evcharging.mobile.utils.ValidationUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * CreateBookingFragment
 * 
 * This fragment allows users to create new charging station bookings.
 * It provides date/time selection and booking confirmation.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class CreateBookingFragment extends Fragment {

    private TextView tvStationName;
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;
    private Button btnSelectDate;
    private Button btnSelectTime;
    private Button btnCreateBooking;
    private String stationId;
    private Calendar selectedDateTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get station ID from arguments
        if (getArguments() != null) {
            stationId = getArguments().getString("station_id");
        }
        
        selectedDateTime = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_booking, container, false);
        
        initializeViews(view);
        setupClickListeners();
        loadStationInfo();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        tvStationName = view.findViewById(R.id.tv_station_name);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        tvSelectedTime = view.findViewById(R.id.tv_selected_time);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnSelectTime = view.findViewById(R.id.btn_select_time);
        btnCreateBooking = view.findViewById(R.id.btn_create_booking);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSelectTime.setOnClickListener(v -> showTimePicker());
        btnCreateBooking.setOnClickListener(v -> createBooking());
    }

    /**
     * Load station information
     */
    private void loadStationInfo() {
        // TODO: Load actual station data
        // For now, display mock data
        tvStationName.setText("Colombo City Center");
    }

    /**
     * Show date picker dialog
     */
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            (view, year, month, dayOfMonth) -> {
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, month);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateDisplay();
            },
            selectedDateTime.get(Calendar.YEAR),
            selectedDateTime.get(Calendar.MONTH),
            selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        
        // Set maximum date to 7 days from now
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 7);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        
        datePickerDialog.show();
    }

    /**
     * Show time picker dialog
     */
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            requireContext(),
            (view, hourOfDay, minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);
                updateTimeDisplay();
            },
            selectedDateTime.get(Calendar.HOUR_OF_DAY),
            selectedDateTime.get(Calendar.MINUTE),
            false
        );
        
        timePickerDialog.show();
    }

    /**
     * Update date display
     */
    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        tvSelectedDate.setText(dateFormat.format(selectedDateTime.getTime()));
    }

    /**
     * Update time display
     */
    private void updateTimeDisplay() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        tvSelectedTime.setText(timeFormat.format(selectedDateTime.getTime()));
    }

    /**
     * Create booking
     */
    private void createBooking() {
        if (validateBooking()) {
            // TODO: Implement actual booking creation
            // For now, show success message
            android.widget.Toast.makeText(requireContext(), 
                "Booking created successfully!", android.widget.Toast.LENGTH_SHORT).show();
            
            // Navigate back to bookings list
            Navigation.findNavController(requireView()).navigateUp();
        }
    }

    /**
     * Validate booking data
     * 
     * @return true if valid, false otherwise
     */
    private boolean validateBooking() {
        long bookingTime = selectedDateTime.getTimeInMillis();
        
        // Check if booking is within 7 days
        if (!ValidationUtils.isValidBookingDate(bookingTime)) {
            android.widget.Toast.makeText(requireContext(), 
                "Booking must be within 7 days", android.widget.Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Check if booking is not in the past
        if (bookingTime <= System.currentTimeMillis()) {
            android.widget.Toast.makeText(requireContext(), 
                "Cannot book in the past", android.widget.Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
}
