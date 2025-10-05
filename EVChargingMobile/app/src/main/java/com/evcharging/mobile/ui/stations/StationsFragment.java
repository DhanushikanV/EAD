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
            // Navigate to MapFragment within stations
            try {
                androidx.fragment.app.Fragment mapFragment = new com.evcharging.mobile.ui.stations.MapFragment();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(com.evcharging.mobile.R.id.nav_host_fragment, mapFragment)
                        .addToBackStack(null)
                        .commit();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to open map view", Toast.LENGTH_SHORT).show();
            }
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
     * Load stations - always fetch fresh data from API
     */
    private void loadStations() {
        android.util.Log.d("StationsFragment", "Loading stations - fetching fresh data from API");
        fetchStationsFromApi();
    }

    private void fetchStationsFromApi() {
        Context context = getContext();
        if (context == null) return;

        com.evcharging.mobile.network.api.ChargingStationService stationService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(context)
                        .create(com.evcharging.mobile.network.api.ChargingStationService.class);

        stationService.getAllStations().enqueue(new retrofit2.Callback<java.util.List<com.evcharging.mobile.network.models.ChargingStation>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> call,
                                   retrofit2.Response<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> response) {
                android.util.Log.d("StationsFragment", "API Response received. Success: " + response.isSuccessful() + ", Code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    android.util.Log.d("StationsFragment", "Processing " + response.body().size() + " stations from API");
                    List<Station> uiStations = new ArrayList<>();
                    for (com.evcharging.mobile.network.models.ChargingStation s : response.body()) {
                        android.util.Log.d("StationsFragment", "Station: " + s.getName() + " - Slots: " + s.getAvailableSlots() + "/" + s.getTotalSlots() + " - Status: " + s.getStatus());
                        uiStations.add(new Station(
                                s.getId(),
                                s.getName(),
                                s.getType(),
                                s.getLatitude(),
                                s.getLongitude(),
                                s.getLocation() != null ? s.getLocation() : "",
                                s.getTotalSlots(),
                                s.getAvailableSlots(),
                                s.getStatus() != null ? s.getStatus() : "Active"
                        ));
                    }
                    adapter.updateStations(uiStations);
                    android.util.Log.d("StationsFragment", "Successfully updated adapter with real station data");
                } else {
                    android.util.Log.e("StationsFragment", "API call failed. Code: " + response.code() + ", Message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            android.util.Log.e("StationsFragment", "Error body: " + response.errorBody().string());
                        } catch (Exception e) {
                            android.util.Log.e("StationsFragment", "Could not read error body", e);
                        }
                    }
                    loadMockStations();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> call, Throwable t) {
                android.util.Log.e("StationsFragment", "Network call failed", t);
                loadMockStations();
            }
        });
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
        android.util.Log.d("StationsFragment", "Loading mock stations as fallback");
        List<Station> stations = new ArrayList<>();
        
        // Use real station data from backend as mock data
        stations.add(new Station("68e29a9e17275692c39f6848", "Colombo City Centre Fast Charge", "DC", 
                6.927079, 79.861244, "Colombo City Centre Mall Parking, Colombo, Sri Lanka", 8, 6, "Active"));
        
        stations.add(new Station("68e29a9f17275692c39f6849", "Orion Tech Park AC Hub", "AC", 
                6.905, 79.87, "Orion City IT Park, Colombo, Sri Lanka", 12, 10, "Active"));
        
        stations.add(new Station("68e29a9f17275692c39f684a", "Galle Fort Harbor Station", "DC", 
                6.0329, 80.2168, "Galle Fort Parking, Galle, Sri Lanka", 6, 6, "Active"));
        
        stations.add(new Station("68e29a9f17275692c39f684b", "BIA Airport QuickCharge", "DC", 
                7.18, 79.884, "Bandaranaike Intl Airport, Katunayake, Sri Lanka", 10, 10, "Active"));
        
        stations.add(new Station("68e29a9f17275692c39f684c", "University of Peradeniya Green Lot", "AC", 
                7.2546, 80.597, "University of Peradeniya, Kandy, Sri Lanka", 16, 16, "Active"));
        
        stations.add(new Station("68e29a9f17275692c39f684d", "Galle Face Promenade Chargers", "AC", 
                6.9279, 79.8428, "Galle Face Green Car Park, Colombo, Sri Lanka", 8, 8, "Active"));
        
        stations.add(new Station("68e29a9f17275692c39f684e", "Kandy City Centre Multi-Storey", "AC", 
                7.2916, 80.6337, "KCC Car Park, Kandy, Sri Lanka", 20, 20, "Active"));
        
        stations.add(new Station("68e29a9f17275692c39f684f", "Jaffna Library Plaza Chargers", "DC", 
                9.6615, 80.0255, "Jaffna Public Library Car Park, Jaffna, Sri Lanka", 4, 4, "Active"));
        
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