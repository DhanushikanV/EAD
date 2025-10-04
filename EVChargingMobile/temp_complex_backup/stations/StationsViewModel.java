package com.evcharging.mobile.ui.stations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.evcharging.mobile.network.models.ChargingStation;
import com.evcharging.mobile.repository.StationsRepository;

import java.util.List;

/**
 * StationsViewModel
 * 
 * This ViewModel manages the state and business logic for the stations screen.
 * It handles data loading, search, and filtering operations.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class StationsViewModel extends ViewModel {

    private final StationsRepository stationsRepository;
    private final MutableLiveData<List<ChargingStation>> stations = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public StationsViewModel() {
        stationsRepository = new StationsRepository();
        loadStations();
    }

    /**
     * Get stations LiveData
     * 
     * @return LiveData containing list of stations
     */
    public LiveData<List<ChargingStation>> getStations() {
        return stations;
    }

    /**
     * Get loading state LiveData
     * 
     * @return LiveData containing loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Get error message LiveData
     * 
     * @return LiveData containing error messages
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Load all stations
     */
    public void loadStations() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        // TODO: Implement actual repository call
        // For now, use mock data
        loadMockStations();
    }

    /**
     * Refresh stations data
     */
    public void refreshStations() {
        loadStations();
    }

    /**
     * Search stations by query
     * 
     * @param query Search query
     */
    public void searchStations(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadStations();
            return;
        }

        isLoading.setValue(true);
        
        // TODO: Implement actual search
        // For now, filter mock data
        filterMockStations(query.trim());
    }

    /**
     * Filter stations by type
     * 
     * @param type Station type (AC/DC)
     */
    public void filterStationsByType(String type) {
        isLoading.setValue(true);
        
        // TODO: Implement actual filtering
        // For now, filter mock data
        filterMockStationsByType(type);
    }

    /**
     * Load mock stations data (for development)
     */
    private void loadMockStations() {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            List<ChargingStation> mockStations = createMockStations();
            stations.setValue(mockStations);
            isLoading.setValue(false);
        }, 1000);
    }

    /**
     * Filter mock stations by search query
     * 
     * @param query Search query
     */
    private void filterMockStations(String query) {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            List<ChargingStation> mockStations = createMockStations();
            List<ChargingStation> filteredStations = mockStations.stream()
                    .filter(station -> station.getName().toLowerCase().contains(query.toLowerCase()) ||
                                     station.getLocation().toLowerCase().contains(query.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
            
            stations.setValue(filteredStations);
            isLoading.setValue(false);
        }, 500);
    }

    /**
     * Filter mock stations by type
     * 
     * @param type Station type
     */
    private void filterMockStationsByType(String type) {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            List<ChargingStation> mockStations = createMockStations();
            List<ChargingStation> filteredStations = mockStations.stream()
                    .filter(station -> station.getType().equals(type))
                    .collect(java.util.stream.Collectors.toList());
            
            stations.setValue(filteredStations);
            isLoading.setValue(false);
        }, 500);
    }

    /**
     * Create mock stations data
     * 
     * @return List of mock charging stations
     */
    private List<ChargingStation> createMockStations() {
        return java.util.Arrays.asList(
            new ChargingStation(
                "1", 
                "Colombo City Center", 
                "Galle Face, Colombo 03", 
                "AC", 
                4, 
                2, 
                "Active", 
                java.util.Arrays.asList("08:00-22:00"), 
                6.9271, 
                79.8612
            ),
            new ChargingStation(
                "2", 
                "One Galle Face", 
                "Galle Road, Colombo 03", 
                "DC", 
                6, 
                4, 
                "Active", 
                java.util.Arrays.asList("24/7"), 
                6.9207, 
                79.8581
            ),
            new ChargingStation(
                "3", 
                "Liberty Plaza", 
                "Liberty Plaza, Colombo 03", 
                "AC", 
                8, 
                6, 
                "Active", 
                java.util.Arrays.asList("06:00-23:00"), 
                6.9147, 
                79.8568
            ),
            new ChargingStation(
                "4", 
                "Battaramulla Junction", 
                "Battaramulla, Colombo", 
                "DC", 
                4, 
                1, 
                "Active", 
                java.util.Arrays.asList("24/7"), 
                6.9027, 
                79.9168
            ),
            new ChargingStation(
                "5", 
                "Kandy City Center", 
                "Kandy City Center, Kandy", 
                "AC", 
                6, 
                3, 
                "Active", 
                java.util.Arrays.asList("08:00-22:00"), 
                7.2906, 
                80.6337
            )
        );
    }
}
