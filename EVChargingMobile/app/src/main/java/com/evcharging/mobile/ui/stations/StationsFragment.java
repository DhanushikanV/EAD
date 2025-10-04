package com.evcharging.mobile.ui.stations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.db.dao.StationDao;
import com.evcharging.mobile.db.entities.StationCache;
import com.evcharging.mobile.ui.stations.adapter.StationsAdapter;
import com.evcharging.mobile.ui.stations.model.Station;
import java.util.ArrayList;
import java.util.List;

/**
 * Stations Fragment
 * 
 * Displays a list of charging stations with basic information.
 * Updated to use raw SQLite operations instead of Room ORM.
 */
public class StationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private StationsAdapter adapter;
    private StationDao stationDao;
    private com.google.android.material.button.MaterialButton btnMapView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        
        initializeViews(view);
        setupClickListeners(view);
        setupRecyclerView();
        loadStations();
        
        return view;
    }

    /**
     * Initialize UI components
     */
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_stations);
        btnMapView = view.findViewById(R.id.btn_map_view);
        
        // Initialize DAO
        stationDao = new StationDao(getContext());
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners(View view) {
        btnMapView.setOnClickListener(v -> {
            // Launch Maps Activity
            Toast.makeText(getContext(), "Map view functionality", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Set up RecyclerView
     */
    private void setupRecyclerView() {
        adapter = new StationsAdapter(new ArrayList<>(), station -> {
            // Handle station click
            Toast.makeText(getContext(), "Selected: " + station.getName(), Toast.LENGTH_SHORT).show();
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Load stations from local SQLite database
     */
    private void loadStations() {
        try {
            stationDao.open();
            List<StationCache> cachedStations = stationDao.getAllStations();
            stationDao.close();
            
            if (!cachedStations.isEmpty()) {
                // Convert cached stations to display model
                List<Station> stations = convertCachedStationsToDisplayStations(cachedStations);
                adapter.updateStations(stations);
            } else {
                // Load mock data if no cached data
                loadMockStations();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error loading stations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            loadMockStations();
        }
    }

    /**
     * Convert cached stations to display stations
     */
    private List<Station> convertCachedStationsToDisplayStations(List<StationCache> cachedStations) {
        List<Station> stations = new ArrayList<>();
        
        for (StationCache cachedStation : cachedStations) {
            Station station = new Station(
                cachedStation.getStationId(),
                cachedStation.getName(),
                cachedStation.getType(),
                cachedStation.getLatitude(),
                cachedStation.getLongitude(),
                cachedStation.getAddress(),
                0, // Total slots - would need separate query
                0, // Available slots - would need separate query
                cachedStation.isActive() ? "Operational" : "Inactive"
            );
            stations.add(station);
        }
        
        return stations;
    }

    /**
     * Load mock stations as fallback
     */
    private void loadMockStations() {
        List<Station> stations = new ArrayList<>();
        
        stations.add(new Station("1", "Colombo Fort Station", "AC Fast Charging", 
                6.9271, 79.8612, "Colombo Fort, Sri Lanka", 4, 2, "Operational"));
        
        stations.add(new Station("2", "Kandy City Center", "DC Super Fast", 
                7.2906, 80.6337, "Kandy, Sri Lanka", 6, 4, "Operational"));
        
        stations.add(new Station("3", "Galle Fort Station", "AC Standard", 
                6.0329, 80.2169, "Galle, Sri Lanka", 3, 1, "Operational"));
        
        stations.add(new Station("4", "Anuradhapura Station", "AC Fast Charging", 
                8.3114, 80.4037, "Anuradhapura, Sri Lanka", 5, 3, "Maintenance"));
        
        adapter.updateStations(stations);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Ensure DAO is closed
        if (stationDao != null) {
            stationDao.close();
        }
    }
}