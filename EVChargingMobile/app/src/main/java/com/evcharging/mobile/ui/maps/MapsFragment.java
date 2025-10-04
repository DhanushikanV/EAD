package com.evcharging.mobile.ui.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.evcharging.mobile.R;
import com.evcharging.mobile.network.models.ChargingStation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * MapsFragment
 * 
 * This fragment displays charging stations on Google Maps.
 * Users can view nearby stations and get directions.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST = 1002;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private List<ChargingStation> chargingStations = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        
        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        
        // Configure map settings
        configureMapSettings();
        
        // Check location permission
        if (checkLocationPermission()) {
            enableMyLocation();
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }
        
        // Load charging stations
        loadChargingStations();
    }

    /**
     * Configure map settings
     */
    private void configureMapSettings() {
        if (googleMap != null) {
            // Enable zoom controls
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            
            // Set map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            
            // Set click listener for markers
            googleMap.setOnMarkerClickListener(marker -> {
                // Show station details when marker is clicked
                showStationDetails(marker);
                return true;
            });
        }
    }

    /**
     * Check location permission
     * 
     * @return true if permission is granted
     */
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), 
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request location permission
     */
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST);
    }

    /**
     * Enable my location on map
     */
    private void enableMyLocation() {
        if (googleMap != null && checkLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Get current location
     */
    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(
                                    location.getLatitude(), 
                                    location.getLongitude()
                                );
                                
                                // Move camera to current location
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    currentLocation, 15f));
                            }
                        }
                    });
        }
    }

    /**
     * Load charging stations on map
     */
    private void loadChargingStations() {
        // TODO: Load actual stations from repository
        // For now, add mock stations
        addMockStations();
    }

    /**
     * Add mock charging stations (for development)
     */
    private void addMockStations() {
        // Mock stations around Colombo, Sri Lanka
        List<ChargingStation> mockStations = createMockStations();
        
        for (ChargingStation station : mockStations) {
            addStationMarker(station);
        }
    }

    /**
     * Create mock charging stations
     * 
     * @return List of mock stations
     */
    private List<ChargingStation> createMockStations() {
        List<ChargingStation> stations = new ArrayList<>();
        
        // Colombo City Center
        ChargingStation station1 = new ChargingStation();
        station1.setId("1");
        station1.setName("Colombo City Center");
        station1.setType("AC");
        station1.setLatitude(6.9271);
        station1.setLongitude(79.8612);
        station1.setAvailableSlots(2);
        station1.setTotalSlots(4);
        stations.add(station1);
        
        // Galle Face Green
        ChargingStation station2 = new ChargingStation();
        station2.setId("2");
        station2.setName("Galle Face Green");
        station2.setType("DC");
        station2.setLatitude(6.9169);
        station2.setLongitude(79.8512);
        station2.setAvailableSlots(1);
        station2.setTotalSlots(2);
        stations.add(station2);
        
        // Independence Square
        ChargingStation station3 = new ChargingStation();
        station3.setId("3");
        station3.setName("Independence Square");
        station3.setType("AC");
        station3.setLatitude(6.9067);
        station3.setLongitude(79.8607);
        station3.setAvailableSlots(3);
        station3.setTotalSlots(4);
        stations.add(station3);
        
        return stations;
    }

    /**
     * Add station marker to map
     * 
     * @param station Charging station
     */
    private void addStationMarker(ChargingStation station) {
        if (googleMap != null) {
            LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
            
            // Choose marker color based on availability
            float markerColor = getMarkerColor(station);
            
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .title(station.getName())
                    .snippet(station.getType() + " • " + 
                            station.getAvailableSlots() + "/" + 
                            station.getTotalSlots() + " available")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor));
            
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(station); // Store station data in marker
        }
    }

    /**
     * Get marker color based on station availability
     * 
     * @param station Charging station
     * @return Hue value for marker color
     */
    private float getMarkerColor(ChargingStation station) {
        if (station.getAvailableSlots() == 0) {
            return BitmapDescriptorFactory.HUE_RED; // No availability
        } else if (station.getAvailableSlots() < station.getTotalSlots() / 2) {
            return BitmapDescriptorFactory.HUE_ORANGE; // Limited availability
        } else {
            return BitmapDescriptorFactory.HUE_GREEN; // Good availability
        }
    }

    /**
     * Show station details when marker is clicked
     * 
     * @param marker Clicked marker
     */
    private void showStationDetails(Marker marker) {
        ChargingStation station = (ChargingStation) marker.getTag();
        if (station != null) {
            // TODO: Navigate to station details or show info window
            Toast.makeText(requireContext(), 
                "Station: " + station.getName() + "\n" +
                "Type: " + station.getType() + "\n" +
                "Available: " + station.getAvailableSlots() + "/" + station.getTotalSlots(),
                Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                getCurrentLocation();
            } else {
                Toast.makeText(requireContext(), 
                    "Location permission is required to show your location", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }
}
