package com.evcharging.mobile.ui.stations;

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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.stations.model.Station;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Map Fragment
 * 
 * Displays charging stations on Google Maps with user location.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Station> stations;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        stations = new ArrayList<>();
        fetchStationsFromApi();
        
        setupMapFragment();
        
        return view;
    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        
        // Enable user location
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }
        
        // Add station markers
        addStationMarkers();
        
        // Set map click listener
        googleMap.setOnMarkerClickListener(marker -> {
            Station station = (Station) marker.getTag();
            if (station != null) {
                Toast.makeText(getContext(), 
                    station.getName() + " - " + station.getAvailableSlots() + "/" + 
                    station.getTotalSlots() + " slots available", 
                    Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void addStationMarkers() {
        for (Station station : stations) {
            LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
            
            // Choose marker color based on availability
            float markerColor = station.getAvailableSlots() > 0 ? 
                BitmapDescriptorFactory.HUE_GREEN : BitmapDescriptorFactory.HUE_RED;
            
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(station.getName())
                    .snippet(station.getAddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
            
            marker.setTag(station);
        }
        
        // Move camera to show all stations
        if (!stations.isEmpty()) {
            LatLng center = new LatLng(stations.get(0).getLatitude(), stations.get(0).getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                }
            });
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchStationsFromApi() {
        com.evcharging.mobile.network.api.ChargingStationService stationService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(requireContext())
                        .create(com.evcharging.mobile.network.api.ChargingStationService.class);

        stationService.getAllStations().enqueue(new retrofit2.Callback<java.util.List<com.evcharging.mobile.network.models.ChargingStation>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> call,
                                   retrofit2.Response<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stations.clear();
                    for (com.evcharging.mobile.network.models.ChargingStation s : response.body()) {
                        stations.add(new Station(
                                s.getId(),
                                s.getName(),
                                s.getType(),
                                s.getLatitude(),
                                s.getLongitude(),
                                s.getLocation() != null ? s.getLocation() : "",
                                s.getTotalSlots(),
                                s.getAvailableSlots(),
                                s.getStatus() != null ? s.getStatus() : ""
                        ));
                    }
                    if (googleMap != null) {
                        addStationMarkers();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.evcharging.mobile.network.models.ChargingStation>> call, Throwable t) {
                // keep empty map
            }
        });
    }
}

