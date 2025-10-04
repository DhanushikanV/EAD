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
        
        // Create mock booking
        String bookingId = "BK" + System.currentTimeMillis();
        String dateTime = tvSelectedDate.getText().toString() + " " + tvSelectedTime.getText().toString();
        
        Toast.makeText(this, 
            "Booking created successfully!\n" +
            "Booking ID: " + bookingId + "\n" +
            "Station: " + selectedStation.getName() + "\n" +
            "Date & Time: " + dateTime,
            Toast.LENGTH_LONG).show();
        
        // TODO: Save booking to database and sync with backend
        // For now, just show success message and finish activity
        
        // Delay before finishing to show the success message
        new android.os.Handler().postDelayed(() -> {
            finish();
        }, 2000);
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
}
