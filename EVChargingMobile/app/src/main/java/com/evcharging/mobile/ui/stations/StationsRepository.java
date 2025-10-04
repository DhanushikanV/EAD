package com.evcharging.mobile.ui.stations;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
 * Repository for managing charging station data from both local database and remote API.
 */
public class StationsRepository {

    private ChargingStationService apiService;
    private MutableLiveData<List<Station>> stationsLiveData;
    private MutableLiveData<String> errorLiveData;

    public StationsRepository(Context context) {
        this.apiService = ApiClient.getRetrofitInstance(context).create(ChargingStationService.class);
        this.stationsLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    /**
     * Get all charging stations from the backend API
     */
    public LiveData<List<Station>> getAllStations() {
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
     * Load stations from backend API
     */
    private void loadStationsFromAPI() {
        Call<List<ChargingStation>> call = apiService.getAllStations();
        call.enqueue(new Callback<List<ChargingStation>>() {
            @Override
            public void onResponse(Call<List<ChargingStation>> call, Response<List<ChargingStation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Station> stations = convertApiStationsToLocalStations(response.body());
                    stationsLiveData.setValue(stations);
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
     * Fallback mock stations when API is not available
     */
    private void loadMockStations() {
        List<Station> stations = new ArrayList<>();
        
        // Colombo Fort Station
        stations.add(new Station("1", "Colombo Fort Station", "AC Fast Charging", 
                6.9271, 79.8612, "Colombo Fort, Sri Lanka", 4, 2, "Operational"));
        
        // Kandy City Center
        stations.add(new Station("2", "Kandy City Center", "DC Super Fast", 
                7.2906, 80.6337, "Kandy, Sri Lanka", 6, 4, "Operational"));
        
        // Galle Fort Station
        stations.add(new Station("3", "Galle Fort Station", "AC Standard", 
                6.0329, 80.2169, "Galle, Sri Lanka", 3, 1, "Operational"));
        
        // Anuradhapura Station
        stations.add(new Station("4", "Anuradhapura Station", "AC Fast Charging", 
                8.3114, 80.4037, "Anuradhapura, Sri Lanka", 5, 0, "Maintenance"));
        
        // Jaffna Station
        stations.add(new Station("5", "Jaffna Station", "DC Fast", 
                9.6615, 80.0255, "Jaffna, Sri Lanka", 4, 3, "Operational"));
        
        // Negombo Station
        stations.add(new Station("6", "Negombo Station", "AC Standard", 
                7.2094, 79.8356, "Negombo, Sri Lanka", 3, 2, "Operational"));
        
        // Kurunegala Station
        stations.add(new Station("7", "Kurunegala Station", "AC Fast Charging", 
                7.4863, 80.3637, "Kurunegala, Sri Lanka", 4, 1, "Operational"));
        
        // Batticaloa Station
        stations.add(new Station("8", "Batticaloa Station", "DC Standard", 
                7.7102, 81.6924, "Batticaloa, Sri Lanka", 3, 3, "Operational"));
        
        stationsLiveData.setValue(stations);
    }

    /**
     * Refresh stations data
     */
    public void refreshStations() {
        loadStationsFromAPI();
    }
}

