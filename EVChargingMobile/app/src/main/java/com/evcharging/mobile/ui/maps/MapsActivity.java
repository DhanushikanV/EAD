package com.evcharging.mobile.ui.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.evcharging.mobile.R;
import com.evcharging.mobile.network.models.ChargingStation;
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
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST = 1002;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        stations = createMockStations();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        if (checkLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }

        addStationMarkers();
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, 
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST);
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(
                                    location.getLatitude(), 
                                    location.getLongitude()
                                );
                                
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    currentLocation, 12f));
                            }
                        }
                    });
        }
    }

    private void addStationMarkers() {
        for (Station station : stations) {
            LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
            
            float markerColor = getMarkerColor(station);
            
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .title(station.getName())
                    .snippet(station.getType() + " • " + 
                            station.getAvailableSlots() + "/" + 
                            station.getTotalSlots() + " available")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor));
            
            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(station);
        }
        
        googleMap.setOnMarkerClickListener(marker -> {
            Station station = (Station) marker.getTag();
            if (station != null) {
                Toast.makeText(this, 
                    "Station: " + station.getName() + "\n" +
                    "Type: " + station.getType() + "\n" +
                    "Available: " + station.getAvailableSlots() + "/" + station.getTotalSlots(),
                    Toast.LENGTH_LONG).show();
            }
            return true;
        });
    }

    private float getMarkerColor(Station station) {
        if (station.getAvailableSlots() == 0) {
            return BitmapDescriptorFactory.HUE_RED;
        } else if (station.getAvailableSlots() < station.getTotalSlots() / 2) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }

    private List<Station> createMockStations() {
        List<Station> stations = new ArrayList<>();
        
        stations.add(new Station("1", "Colombo Fort Station", "AC Fast Charging", 
                6.9271, 79.8612, "Colombo Fort, Sri Lanka", 4, 2, "Operational"));
        
        stations.add(new Station("2", "Kandy City Center", "DC Super Fast", 
                7.2906, 80.6337, "Kandy, Sri Lanka", 6, 4, "Operational"));
        
        stations.add(new Station("3", "Galle Fort Station", "AC Standard", 
                6.0329, 80.2169, "Galle, Sri Lanka", 3, 1, "Operational"));
        
        stations.add(new Station("4", "Anuradhapura Station", "AC Fast Charging", 
                8.3114, 80.4037, "Anuradhapura, Sri Lanka", 5, 0, "Maintenance"));
        
        stations.add(new Station("5", "Jaffna Station", "DC Fast", 
                9.6615, 80.0255, "Jaffna, Sri Lanka", 4, 3, "Operational"));
        
        return stations;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
            } else {
                Toast.makeText(this, 
                    "Location permission is required to show your location", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }
}
