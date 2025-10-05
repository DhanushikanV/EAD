package com.evcharging.mobile.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.stations.model.Station;
import java.util.Calendar;

/**
 * Create Booking Activity
 * 
 * Standalone activity for creating new bookings.
 */
public class CreateBookingActivity extends AppCompatActivity {

    private TextView tvSelectedStation;
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;
    private Button btnSelectStation;
    private Button btnSelectDate;
    private Button btnSelectTime;
    private Button btnCreateBooking;
    
    private Station selectedStation;
    private Calendar selectedDateTime;
    private static final int STATION_SELECTION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);
        
        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create Booking");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initializeViews();
        setupClickListeners();
        selectedDateTime = Calendar.getInstance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeViews() {
        tvSelectedStation = findViewById(R.id.tv_selected_station);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvSelectedTime = findViewById(R.id.tv_selected_time);
        btnSelectStation = findViewById(R.id.btn_select_station);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnCreateBooking = findViewById(R.id.btn_create_booking);
    }

    private void setupClickListeners() {
        btnSelectStation.setOnClickListener(v -> showStationSelection());
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSelectTime.setOnClickListener(v -> showTimePicker());
        btnCreateBooking.setOnClickListener(v -> createBooking());
    }

    private void showStationSelection() {
        Intent intent = new Intent(this, StationSelectionActivity.class);
        startActivityForResult(intent, STATION_SELECTION_REQUEST_CODE);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
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
                this,
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
            Toast.makeText(this, "Please select a charging station", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (tvSelectedDate.getText().toString().equals("Select Date")) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (tvSelectedTime.getText().toString().equals("Select Time")) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create real booking via API
        createBookingViaAPI();
    }

    private void updateCreateButtonState() {
        boolean canCreate = selectedStation != null && 
                           !tvSelectedDate.getText().toString().equals("Select Date") &&
                           !tvSelectedTime.getText().toString().equals("Select Time");
        
        btnCreateBooking.setEnabled(canCreate);
        btnCreateBooking.setAlpha(canCreate ? 1.0f : 0.5f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == STATION_SELECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedStation = (Station) data.getSerializableExtra("selected_station");
            if (selectedStation != null) {
                tvSelectedStation.setText(selectedStation.getName() + " - " + 
                                        selectedStation.getAvailableSlots() + "/" + 
                                        selectedStation.getTotalSlots() + " slots available");
                updateCreateButtonState();
            }
        }
    }
    
    private void createBookingViaAPI() {
        // Get user NIC from SharedPreferences using the same manager as other fragments
        com.evcharging.mobile.utils.SharedPreferencesManager prefsManager = new com.evcharging.mobile.utils.SharedPreferencesManager(this);
        String userNIC = prefsManager.getUserNIC();
        
        if (userNIC == null || userNIC.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create booking request
        com.evcharging.mobile.network.models.Booking bookingRequest = new com.evcharging.mobile.network.models.Booking();
        bookingRequest.setEvOwnerNIC(userNIC);
        bookingRequest.setStationId(selectedStation.getId());
        bookingRequest.setStatus("Pending");
        
        // Parse date and time
        String dateStr = tvSelectedDate.getText().toString();
        String timeStr = tvSelectedTime.getText().toString();
        
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
            
            java.util.Date date = dateFormat.parse(dateStr);
            java.util.Date time = timeFormat.parse(timeStr);
            
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(date);
            
            java.util.Calendar timeCalendar = java.util.Calendar.getInstance();
            timeCalendar.setTime(time);
            calendar.set(java.util.Calendar.HOUR_OF_DAY, timeCalendar.get(java.util.Calendar.HOUR_OF_DAY));
            calendar.set(java.util.Calendar.MINUTE, timeCalendar.get(java.util.Calendar.MINUTE));
            
            java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault());
            bookingRequest.setReservationDateTime(isoFormat.format(calendar.getTime()));
            
        } catch (Exception e) {
            android.util.Log.e("CreateBookingActivity", "Error parsing date/time", e);
            // Use current time as fallback
            java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault());
            bookingRequest.setReservationDateTime(isoFormat.format(new java.util.Date()));
        }
        
        // Make API call
        com.evcharging.mobile.network.api.BookingService bookingService = com.evcharging.mobile.network.ApiClient.getRetrofitInstance(this).create(com.evcharging.mobile.network.api.BookingService.class);
        
        retrofit2.Call<com.evcharging.mobile.network.models.Booking> call = bookingService.createBooking(bookingRequest);
        
        call.enqueue(new retrofit2.Callback<com.evcharging.mobile.network.models.Booking>() {
            @Override
            public void onResponse(retrofit2.Call<com.evcharging.mobile.network.models.Booking> call, retrofit2.Response<com.evcharging.mobile.network.models.Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.evcharging.mobile.network.models.Booking createdBooking = response.body();
                    
                    Toast.makeText(CreateBookingActivity.this, 
                        "✅ Booking created successfully!\n" +
                        "Booking ID: " + createdBooking.getId() + "\n" +
                        "Station: " + selectedStation.getName() + "\n" +
                        "Status: " + createdBooking.getStatus(),
                        Toast.LENGTH_LONG).show();
                    
                    android.util.Log.d("CreateBookingActivity", "Booking created with ID: " + createdBooking.getId());
                    
                    // Finish activity after successful booking creation
                    new android.os.Handler().postDelayed(() -> {
                        finish();
                    }, 2000);
                    
                } else {
                    String errorMsg = "Failed to create booking: " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = "Failed to create booking: " + response.errorBody().string();
                        } catch (Exception e) {
                            android.util.Log.e("CreateBookingActivity", "Error reading error body", e);
                        }
                    }
                    Toast.makeText(CreateBookingActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    android.util.Log.e("CreateBookingActivity", "Create booking failed: " + response.code() + " - " + response.message());
                }
            }
            
            @Override
            public void onFailure(retrofit2.Call<com.evcharging.mobile.network.models.Booking> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Toast.makeText(CreateBookingActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                android.util.Log.e("CreateBookingActivity", "Create booking network error", t);
            }
        });
    }
}
