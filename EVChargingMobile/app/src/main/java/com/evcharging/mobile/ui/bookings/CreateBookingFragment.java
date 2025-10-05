package com.evcharging.mobile.ui.bookings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.network.api.BookingService;
import com.evcharging.mobile.network.models.Booking;
import com.evcharging.mobile.ui.stations.model.Station;
import com.evcharging.mobile.utils.SharedPreferencesManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        
        // Show loading state
        btnCreateBooking.setEnabled(false);
        btnCreateBooking.setText("Creating...");
        
        // Create booking request
        Booking bookingRequest = createBookingRequest();
        
        // Call API to create booking
        performCreateBooking(bookingRequest);
    }

    private Booking createBookingRequest() {
        Context context = getContext();
        SharedPreferencesManager prefsManager = new SharedPreferencesManager(context);
        
        // Get user info
        String userNIC = prefsManager.getUserNIC();
        String userEmail = prefsManager.getUserEmail();
        
        // Create date-time string
        String dateStr = tvSelectedDate.getText().toString();
        String timeStr = tvSelectedTime.getText().toString();
        
        // Parse date and time to create proper datetime
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        
        try {
            Date date = dateFormat.parse(dateStr);
            Date time = timeFormat.parse(timeStr);
            
            // Combine date and time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            
            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime(time);
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            
            // Create booking object
            Booking booking = new Booking();
            booking.setEvOwnerNIC(userNIC != null ? userNIC : "123456789V"); // Default NIC if not available
            booking.setStationId(selectedStation.getId());
            
            // Format date-time as ISO string
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            booking.setReservationDateTime(isoFormat.format(calendar.getTime()));
            booking.setStatus("Pending");
            
            return booking;
            
        } catch (Exception e) {
            Log.e("CreateBookingFragment", "Error parsing date/time", e);
            // Fallback to current time
            Booking booking = new Booking();
            booking.setEvOwnerNIC(userNIC != null ? userNIC : "123456789V");
            booking.setStationId(selectedStation.getId());
            
            // Format current time as ISO string
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            booking.setReservationDateTime(isoFormat.format(new Date()));
            booking.setStatus("Pending");
            return booking;
        }
    }

    private void performCreateBooking(Booking bookingRequest) {
        Context context = getContext();
        if (context == null) return;
        
        Log.d("CreateBookingFragment", "Creating booking for station: " + bookingRequest.getStationId());
        Log.d("CreateBookingFragment", "Booking request: " + bookingRequest.toString());
        Log.d("CreateBookingFragment", "User NIC: " + bookingRequest.getEvOwnerNIC());
        Log.d("CreateBookingFragment", "DateTime: " + bookingRequest.getReservationDateTime());
        
        // Create BookingService and make API call
        BookingService bookingService = ApiClient.getRetrofitInstance(context).create(BookingService.class);
        
        Call<Booking> call = bookingService.createBooking(bookingRequest);
        Log.d("CreateBookingFragment", "API call initiated");
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                btnCreateBooking.setEnabled(true);
                btnCreateBooking.setText("Create Booking");
                
                Log.d("CreateBookingFragment", "Response received: " + response.code());
                Log.d("CreateBookingFragment", "Response body: " + (response.body() != null ? response.body().toString() : "null"));
                Log.d("CreateBookingFragment", "Response message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    Booking createdBooking = response.body();
                    
                    Log.d("CreateBookingFragment", "Booking created successfully!");
                    Log.d("CreateBookingFragment", "Booking ID: " + createdBooking.getId());
                    Log.d("CreateBookingFragment", "Booking Status: " + createdBooking.getStatus());
                    
                    Toast.makeText(context, 
                        "✅ Booking created successfully!\n" +
                        "Booking ID: " + createdBooking.getId() + "\n" +
                        "Station: " + selectedStation.getName() + "\n" +
                        "Status: " + createdBooking.getStatus(),
                        Toast.LENGTH_LONG).show();
                    
                    Log.d("CreateBookingFragment", "Booking created with ID: " + createdBooking.getId());
                    
                    // Navigate back to bookings list
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                    
                } else {
                    String errorMsg = "Failed to create booking: " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            errorMsg = "Failed to create booking: " + errorBody;
                            Log.d("CreateBookingFragment", "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e("CreateBookingFragment", "Error reading error body", e);
                        }
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("CreateBookingFragment", "Create booking failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                btnCreateBooking.setEnabled(true);
                btnCreateBooking.setText("Create Booking");
                
                String errorMsg = "Network error: " + t.getMessage();
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                Log.e("CreateBookingFragment", "Create booking network error", t);
            }
        });
    }

    private void updateCreateButtonState() {
        boolean canCreate = selectedStation != null && 
                           !tvSelectedDate.getText().toString().equals("Select Date") &&
                           !tvSelectedTime.getText().toString().equals("Select Time");
        
        btnCreateBooking.setEnabled(canCreate);
        btnCreateBooking.setAlpha(canCreate ? 1.0f : 0.5f);
    }
}