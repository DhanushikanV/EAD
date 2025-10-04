package com.evcharging.mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.stations.StationsRepository;
import com.evcharging.mobile.ui.stations.adapter.StationSelectionAdapter;
import com.evcharging.mobile.ui.stations.model.Station;
import java.util.ArrayList;
import java.util.List;

/**
 * Station Selection Activity
 * 
 * Allows users to select from available charging stations.
 */
public class StationSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StationSelectionAdapter adapter;
    private Station selectedStation;
    private StationsRepository stationsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_selection);
        
        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select Charging Station");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        initializeViews();
        setupRecyclerView();
        initializeRepository();
        loadStations();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view_stations);
    }

    private void setupRecyclerView() {
        adapter = new StationSelectionAdapter(new ArrayList<>(), station -> {
            selectedStation = station;
            returnSelectedStation();
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initializeRepository() {
        stationsRepository = new StationsRepository(this);
    }

    private void loadStations() {
        // Load stations from repository (tries API first, falls back to mock data)
        stationsRepository.getAllStations().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(List<Station> stations) {
                if (stations != null) {
                    adapter.updateStations(stations);
                }
            }
        });

        // Observe error messages
        stationsRepository.getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Toast.makeText(StationSelectionActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void returnSelectedStation() {
        if (selectedStation != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_station", selectedStation);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
