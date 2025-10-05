package com.evcharging.mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import com.evcharging.mobile.R;
import com.evcharging.mobile.utils.SharedPreferencesManager;
import com.evcharging.mobile.network.models.ChargingStation;
import java.util.ArrayList;
import java.util.List;

/**
 * Operator Activity
 * 
 * Main activity for station operators with operator-specific navigation and features.
 */
public class OperatorActivity extends AppCompatActivity {

    private SharedPreferencesManager prefsManager;
    private Spinner spinnerStationSelection;
    private TextView tvStationInfo;
    private TextView tvTodayBookingsList;
    private Button btnScan;
    private Button btnViewAllBookings;
    private List<ChargingStation> stations = new ArrayList<>();
    private ChargingStation selectedStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_simple);

        prefsManager = new SharedPreferencesManager(this);
        
        setupViews();
        setupActionBar();
        loadStations();
    }

    private void setupViews() {
        TextView tvWelcome = findViewById(R.id.tv_operator_welcome);
        spinnerStationSelection = findViewById(R.id.spinner_station_selection);
        tvStationInfo = findViewById(R.id.tv_station_info);
        tvTodayBookingsList = findViewById(R.id.tv_today_bookings_list);
        btnScan = findViewById(R.id.btn_scan_qr);
        btnViewAllBookings = findViewById(R.id.btn_view_all_bookings);
        
        String username = prefsManager.getUserName();
        if (username != null && !username.isEmpty()) {
            tvWelcome.setText("Welcome, " + username + "!");
        } else {
            tvWelcome.setText("Welcome, Operator!");
        }

        // Setup click listeners
        if (btnScan != null) {
            btnScan.setOnClickListener(v -> openScanner());
        }
        
        if (btnViewAllBookings != null) {
            btnViewAllBookings.setOnClickListener(v -> openAllBookings());
        }
    }

    private void loadStations() {
        com.evcharging.mobile.network.api.ChargingStationService stationService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(this)
                        .create(com.evcharging.mobile.network.api.ChargingStationService.class);

        stationService.getAllStations().enqueue(new retrofit2.Callback<List<ChargingStation>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ChargingStation>> call,
                                   retrofit2.Response<List<ChargingStation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stations = response.body();
                    setupStationSpinner();
                    // Select first station by default
                    if (!stations.isEmpty()) {
                        selectedStation = stations.get(0);
                        updateStationInfo();
                        loadTodayBookings();
                    }
                } else {
                    Toast.makeText(OperatorActivity.this, "Failed to load stations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<ChargingStation>> call, Throwable t) {
                Toast.makeText(OperatorActivity.this, "Failed to load stations: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupStationSpinner() {
        List<String> stationNames = new ArrayList<>();
        for (ChargingStation station : stations) {
            stationNames.add(station.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStationSelection.setAdapter(adapter);

        spinnerStationSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stations.get(position);
                updateStationInfo();
                loadTodayBookings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void updateStationInfo() {
        if (selectedStation != null) {
            String status = selectedStation.getStatus() != null ? selectedStation.getStatus() : "Unknown";
            int total = selectedStation.getTotalSlots();
            int available = selectedStation.getAvailableSlots();
            tvStationInfo.setText(selectedStation.getName() + "\nStatus: " + status + "\nAvailable Slots: " + available + "/" + total);
        }
    }

    private void loadOperatorStationInfo(TextView target) {
        // For now, derive station based on recent confirmed booking, or fetch first station
        com.evcharging.mobile.network.api.ChargingStationService stationService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(this)
                        .create(com.evcharging.mobile.network.api.ChargingStationService.class);

        stationService.getAllStations().enqueue(new retrofit2.Callback<java.util.List<com.evcharging.mobile.network.models.ChargingStation>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> call,
                                   retrofit2.Response<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    com.evcharging.mobile.network.models.ChargingStation s = response.body().get(0);
                    String status = s.getStatus() != null ? s.getStatus() : "Unknown";
                    int total = s.getTotalSlots();
                    int available = s.getAvailableSlots();
                    target.setText(s.getName() + "\nStatus: " + status + "\nAvailable Slots: " + available + "/" + total);
                } else {
                    target.setText("Station info unavailable");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> call, Throwable t) {
                target.setText("Station info unavailable");
            }
        });
    }

    private void loadTodayBookings() {
        if (selectedStation == null) {
            tvTodayBookingsList.setText("No station selected");
            return;
        }

        // Pull all bookings and show today's for selected station
        com.evcharging.mobile.network.api.BookingService bookingService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(this)
                        .create(com.evcharging.mobile.network.api.BookingService.class);

        bookingService.getAllBookings().enqueue(new retrofit2.Callback<java.util.List<com.evcharging.mobile.network.models.Booking>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.Booking>> call,
                                   retrofit2.Response<java.util.List<com.evcharging.mobile.network.models.Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    java.text.SimpleDateFormat input = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
                    java.text.SimpleDateFormat outTime = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
                    StringBuilder sb = new StringBuilder();
                    String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
                    
                    for (com.evcharging.mobile.network.models.Booking b : response.body()) {
                        // Filter by selected station
                        if (selectedStation.getId().equals(b.getStationId())) {
                            try {
                                String raw = b.getReservationDateTime();
                                java.util.Date d = input.parse(raw.substring(0, Math.min(raw.length(), 19)));
                                String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(d);
                                if (today.equals(dateStr)) {
                                    sb.append("• ").append(outTime.format(d)).append(" - ")
                                      .append(b.getEvOwnerNIC() != null ? b.getEvOwnerNIC() : "EV Owner")
                                      .append(" (").append(b.getStatus()).append(")\n");
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                    tvTodayBookingsList.setText(sb.length() > 0 ? sb.toString().trim() : "No bookings today");
                } else {
                    tvTodayBookingsList.setText("No bookings today");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.Booking>> call, Throwable t) {
                tvTodayBookingsList.setText("No bookings today");
            }
        });
    }

    private void openScanner() {
        // Show scanner fragment full screen
        com.evcharging.mobile.ui.qr.QrScannerFragment fragment = new com.evcharging.mobile.ui.qr.QrScannerFragment();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(android.R.id.content, fragment);
        tx.addToBackStack(null);
        tx.commit();
    }

    private void openAllBookings() {
        if (selectedStation == null) {
            Toast.makeText(this, "Please select a station first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an intent to open OperatorBookingsActivity with station ID
        Intent intent = new Intent(this, OperatorBookingsActivity.class);
        intent.putExtra("station_id", selectedStation.getId());
        intent.putExtra("station_name", selectedStation.getName());
        startActivity(intent);
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Station Operator");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.operator_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Handle logout
            prefsManager.clearUserData();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
