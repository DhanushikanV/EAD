package com.evcharging.mobile.ui.stations;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.evcharging.mobile.db.dao.StationDao;
import com.evcharging.mobile.db.entities.StationCache;
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.network.api.ChargingStationService;
import com.evcharging.mobile.network.models.ChargingStation;
import com.evcharging.mobile.ui.stations.model.Station;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Stations Repository
 * 
 * Repository for managing charging station data from both local SQLite database and remote API.
 * Uses raw SQLite operations instead of Room ORM.
 */
public class StationsRepository {

    private ChargingStationService apiService;
    private StationDao stationDao;
    private MutableLiveData<List<Station>> stationsLiveData;
    private MutableLiveData<String> errorLiveData;

    public StationsRepository(Context context) {
        this.apiService = ApiClient.getRetrofitInstance(context).create(ChargingStationService.class);
        this.stationDao = new StationDao(context);
        this.stationsLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    /**
     * Get all charging stations from the backend API with local caching
     */
    public LiveData<List<Station>> getAllStations() {
        // First try to load from local cache
        loadStationsFromLocalCache();
        
        // Then try to load from API and update cache
        loadStationsFromAPI();
        return stationsLiveData;
    }

    /**
     * Get error messages
     */
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Load stations from local SQLite cache
     */
    private void loadStationsFromLocalCache() {
        try {
            stationDao.open();
            List<StationCache> cachedStations = stationDao.getAllStations();
            stationDao.close();
            
            if (!cachedStations.isEmpty()) {
                List<Station> stations = convertCachedStationsToLocalStations(cachedStations);
                stationsLiveData.setValue(stations);
            }
        } catch (Exception e) {
            errorLiveData.setValue("Error loading cached stations: " + e.getMessage());
        }
    }

    /**
     * Load stations from backend API and update local cache
     */
    private void loadStationsFromAPI() {
        Call<List<ChargingStation>> call = apiService.getAllStations();
        call.enqueue(new Callback<List<ChargingStation>>() {
            @Override
            public void onResponse(Call<List<ChargingStation>> call, Response<List<ChargingStation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Station> stations = convertApiStationsToLocalStations(response.body());
                    stationsLiveData.setValue(stations);
                    
                    // Update local cache
                    updateLocalCache(response.body());
                } else {
                    // If API fails, fallback to mock data
                    loadMockStations();
                    errorLiveData.setValue("Failed to load stations from server. Showing offline data.");
                }
            }

            @Override
            public void onFailure(Call<List<ChargingStation>> call, Throwable t) {
                // If network fails, fallback to mock data
                loadMockStations();
                errorLiveData.setValue("Network error. Showing offline data: " + t.getMessage());
            }
        });
    }

    /**
     * Convert API ChargingStation objects to local Station objects
     */
    private List<Station> convertApiStationsToLocalStations(List<ChargingStation> apiStations) {
        List<Station> stations = new ArrayList<>();
        
        for (ChargingStation apiStation : apiStations) {
            Station localStation = new Station(
                apiStation.getId(),
                apiStation.getName(),
                apiStation.getType(),
                apiStation.getLatitude(),
                apiStation.getLongitude(),
                apiStation.getLocation(),
                apiStation.getTotalSlots(),
                apiStation.getAvailableSlots(),
                apiStation.getStatus()
            );
            stations.add(localStation);
        }
        
        return stations;
    }

    /**
     * Convert cached StationCache objects to local Station objects
     */
    private List<Station> convertCachedStationsToLocalStations(List<StationCache> cachedStations) {
        List<Station> stations = new ArrayList<>();
        
        for (StationCache cachedStation : cachedStations) {
            Station localStation = new Station(
                cachedStation.getStationId(),
                cachedStation.getName(),
                cachedStation.getType(),
                cachedStation.getLatitude(),
                cachedStation.getLongitude(),
                cachedStation.getAddress(),
                cachedStation.getTotalSlots(), // Use actual cached total slots
                cachedStation.getAvailableSlots(), // Use actual cached available slots
                cachedStation.isActive() ? "Active" : "Deactivated"
            );
            stations.add(localStation);
        }
        
        return stations;
    }

    /**
     * Update local cache with API data
     */
    private void updateLocalCache(List<ChargingStation> apiStations) {
        try {
            stationDao.open();
            
            // Clear existing cache
            stationDao.deleteAllStations();
            
            // Insert new data
            for (ChargingStation apiStation : apiStations) {
                StationCache stationCache = new StationCache(
                    apiStation.getId(),
                    apiStation.getName(),
                    apiStation.getType(),
                    apiStation.getLatitude(),
                    apiStation.getLongitude(),
                    apiStation.getLocation(),
                    "Operational".equals(apiStation.getStatus()),
                    apiStation.getTotalSlots(),
                    apiStation.getAvailableSlots(),
                    System.currentTimeMillis()
                );
                stationDao.insertStation(stationCache);
            }
            
            stationDao.close();
        } catch (Exception e) {
            errorLiveData.setValue("Error updating local cache: " + e.getMessage());
        }
    }

    /**
     * Fallback mock stations when API is not available
     */
    private void loadMockStations() {
        List<Station> stations = new ArrayList<>();
        
        // Colombo Fort Station
        stations.add(new Station("1", "Colombo Fort Station", "AC Fast Charging", 
                6.9271, 79.8612, "Colombo Fort, Sri Lanka", 4, 2, "Active"));
        
        // Kandy City Center
        stations.add(new Station("2", "Kandy City Center", "DC Super Fast", 
                7.2906, 80.6337, "Kandy, Sri Lanka", 6, 4, "Active"));
        
        // Galle Fort Station
        stations.add(new Station("3", "Galle Fort Station", "AC Standard", 
                6.0329, 80.2169, "Galle, Sri Lanka", 3, 1, "Active"));
        
        // Anuradhapura Station
        stations.add(new Station("4", "Anuradhapura Station", "AC Fast Charging", 
                8.3114, 80.4037, "Anuradhapura, Sri Lanka", 5, 0, "Deactivated"));
        
        // Jaffna Station
        stations.add(new Station("5", "Jaffna Station", "DC Fast", 
                9.6615, 80.0255, "Jaffna, Sri Lanka", 4, 3, "Active"));
        
        // Negombo Station
        stations.add(new Station("6", "Negombo Station", "AC Standard", 
                7.2094, 79.8356, "Negombo, Sri Lanka", 3, 2, "Active"));
        
        // Kurunegala Station
        stations.add(new Station("7", "Kurunegala Station", "AC Fast Charging", 
                7.4863, 80.3637, "Kurunegala, Sri Lanka", 4, 1, "Active"));
        
        // Batticaloa Station
        stations.add(new Station("8", "Batticaloa Station", "DC Standard", 
                7.7102, 81.6924, "Batticaloa, Sri Lanka", 3, 3, "Active"));
        
        stationsLiveData.setValue(stations);
    }

    /**
     * Refresh stations data from API and update cache
     */
    public void refreshStations() {
        loadStationsFromAPI();
    }

    /**
     * Clear local cache
     */
    public void clearCache() {
        try {
            stationDao.open();
            stationDao.deleteAllStations();
            stationDao.close();
        } catch (Exception e) {
            errorLiveData.setValue("Error clearing cache: " + e.getMessage());
        }
    }
}

