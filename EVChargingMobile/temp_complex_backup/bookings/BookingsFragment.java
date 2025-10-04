package com.evcharging.mobile.ui.bookings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.bookings.adapter.BookingsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/**
 * BookingsFragment
 * 
 * This fragment displays the user's booking history and upcoming reservations.
 * Users can view, edit, and cancel their bookings from this screen.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class BookingsFragment extends Fragment {

    private TabLayout tabLayoutBookings;
    private RecyclerView recyclerViewBookings;
    private FloatingActionButton fabCreateBooking;
    private BookingsAdapter bookingsAdapter;
    private BookingsViewModel bookingsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingsViewModel = new ViewModelProvider(this).get(BookingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();
        observeViewModel();
        setupTabs();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        tabLayoutBookings = view.findViewById(R.id.tab_layout_bookings);
        recyclerViewBookings = view.findViewById(R.id.recycler_view_bookings);
        fabCreateBooking = view.findViewById(R.id.fab_create_booking);
    }

    /**
     * Set up RecyclerView with adapter
     */
    private void setupRecyclerView() {
        bookingsAdapter = new BookingsAdapter(booking -> {
            // Navigate to booking details
            Bundle bundle = new Bundle();
            bundle.putString("booking_id", booking.getId());
            Navigation.findNavController(requireView()).navigate(R.id.nav_booking_details, bundle);
        });
        
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewBookings.setAdapter(bookingsAdapter);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        fabCreateBooking.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.nav_create_booking);
        });
    }

    /**
     * Set up tab layout
     */
    private void setupTabs() {
        tabLayoutBookings.addTab(tabLayoutBookings.newTab().setText("Upcoming"));
        tabLayoutBookings.addTab(tabLayoutBookings.newTab().setText("History"));
        
        tabLayoutBookings.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        bookingsViewModel.loadUpcomingBookings();
                        break;
                    case 1:
                        bookingsViewModel.loadPastBookings();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * Observe ViewModel data
     */
    private void observeViewModel() {
        // Observe bookings list
        bookingsViewModel.getBookings().observe(getViewLifecycleOwner(), bookings -> {
            bookingsAdapter.updateBookings(bookings);
        });

        // Observe loading state
        bookingsViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // TODO: Show/hide loading indicator
        });

        // Observe error messages
        bookingsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // TODO: Show error message to user
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load upcoming bookings by default
        bookingsViewModel.loadUpcomingBookings();
    }
}
