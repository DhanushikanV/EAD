package com.evcharging.mobile.ui.stations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.stations.adapter.StationsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * StationsFragment
 * 
 * This fragment displays a list of charging stations with search and filter capabilities.
 * Users can view station details and create bookings from this screen.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class StationsFragment extends Fragment {

    private RecyclerView recyclerViewStations;
    private SearchView searchView;
    private FloatingActionButton fabRefresh;
    private StationsAdapter stationsAdapter;
    private StationsViewModel stationsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();
        observeViewModel();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        recyclerViewStations = view.findViewById(R.id.recycler_view_stations);
        searchView = view.findViewById(R.id.search_view);
        fabRefresh = view.findViewById(R.id.fab_refresh);
    }

    /**
     * Set up RecyclerView with adapter
     */
    private void setupRecyclerView() {
        stationsAdapter = new StationsAdapter(station -> {
            // Navigate to station details
            Bundle bundle = new Bundle();
            bundle.putString("station_id", station.getId());
            Navigation.findNavController(requireView()).navigate(R.id.nav_station_details, bundle);
        });
        
        recyclerViewStations.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewStations.setAdapter(stationsAdapter);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                stationsViewModel.searchStations(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                stationsViewModel.searchStations(newText);
                return true;
            }
        });

        // Refresh button
        fabRefresh.setOnClickListener(v -> stationsViewModel.refreshStations());
    }

    /**
     * Observe ViewModel data
     */
    private void observeViewModel() {
        stationsViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
        
        // Observe stations list
        stationsViewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
            stationsAdapter.updateStations(stations);
        });

        // Observe loading state
        stationsViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // TODO: Show/hide loading indicator
        });

        // Observe error messages
        stationsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // TODO: Show error message to user
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        stationsViewModel.refreshStations();
    }
}
