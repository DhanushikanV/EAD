package com.evcharging.mobile.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.bookings.model.Booking;
import com.evcharging.mobile.ui.operator.adapter.TodayBookingsAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Operator Bookings Activity
 * 
 * Shows all bookings for a selected station
 */
public class OperatorBookingsActivity extends AppCompatActivity {

    private TextView tvStationName;
    private RecyclerView recyclerViewBookings;
    private TodayBookingsAdapter bookingsAdapter;
    private String stationId;
    private String stationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_bookings);

        // Get station info from intent
        stationId = getIntent().getStringExtra("station_id");
        stationName = getIntent().getStringExtra("station_name");

        if (stationId == null || stationName == null) {
            Toast.makeText(this, "Station information not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupViews();
        setupActionBar();
        loadBookingsForStation();
    }

    private void setupViews() {
        tvStationName = findViewById(R.id.tv_station_name);
        recyclerViewBookings = findViewById(R.id.recycler_view_bookings);

        tvStationName.setText("Bookings for " + stationName);

        // Setup RecyclerView
        bookingsAdapter = new TodayBookingsAdapter(new ArrayList<>(), new TodayBookingsAdapter.OnBookingActionListener() {
            @Override
            public void onBookingClick(Booking booking) {
                showBookingDetails(booking);
            }

            @Override
            public void onApproveBooking(Booking booking) {
                approveBooking(booking);
            }

            @Override
            public void onCancelBooking(Booking booking) {
                cancelBooking(booking);
            }
        });

        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(bookingsAdapter);
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Station Bookings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadBookingsForStation() {
        com.evcharging.mobile.network.api.BookingService bookingService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(this)
                        .create(com.evcharging.mobile.network.api.BookingService.class);

        bookingService.getAllBookings().enqueue(new retrofit2.Callback<List<com.evcharging.mobile.network.models.Booking>>() {
            @Override
            public void onResponse(retrofit2.Call<List<com.evcharging.mobile.network.models.Booking>> call,
                                   retrofit2.Response<List<com.evcharging.mobile.network.models.Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> uiBookings = new ArrayList<>();
                    
                    for (com.evcharging.mobile.network.models.Booking b : response.body()) {
                        // Filter by selected station
                        if (stationId.equals(b.getStationId())) {
                            uiBookings.add(convertToUIBooking(b));
                        }
                    }
                    
                    bookingsAdapter.updateBookings(uiBookings);
                } else {
                    Toast.makeText(OperatorBookingsActivity.this, "Failed to load bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<com.evcharging.mobile.network.models.Booking>> call, Throwable t) {
                Toast.makeText(OperatorBookingsActivity.this, "Failed to load bookings: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Booking convertToUIBooking(com.evcharging.mobile.network.models.Booking apiBooking) {
        // Convert API booking to UI booking model
        String dateTime = formatDateTime(apiBooking.getReservationDateTime());
        String status = getBookingStatus(apiBooking.getStatus());
        
        return new Booking(
            apiBooking.getId(),
            apiBooking.getEvOwnerNIC(),
            dateTime,
            status,
            "Scheduled"
        );
    }

    private String formatDateTime(String dateTimeString) {
        try {
            java.text.SimpleDateFormat input = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
            java.text.SimpleDateFormat output = new java.text.SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", java.util.Locale.getDefault());
            java.util.Date date = input.parse(dateTimeString.substring(0, Math.min(dateTimeString.length(), 19)));
            return output.format(date);
        } catch (Exception e) {
            return dateTimeString;
        }
    }

    private String getBookingStatus(String status) {
        if (status == null) return "Unknown";
        switch (status.toLowerCase()) {
            case "pending": return "Pending Approval";
            case "confirmed": return "Confirmed";
            case "cancelled": return "Cancelled";
            case "completed": return "Completed";
            case "inprogress": return "In Progress";
            default: return status;
        }
    }

    private void showBookingDetails(Booking booking) {
        Toast.makeText(this, "Booking: " + booking.getId() + " - " + booking.getStationName(), Toast.LENGTH_SHORT).show();
    }

    private void approveBooking(Booking booking) {
        // TODO: Implement booking approval
        Toast.makeText(this, "Booking approved: " + booking.getId(), Toast.LENGTH_SHORT).show();
    }

    private void cancelBooking(Booking booking) {
        // TODO: Implement booking cancellation
        Toast.makeText(this, "Booking cancelled: " + booking.getId(), Toast.LENGTH_SHORT).show();
    }
}


