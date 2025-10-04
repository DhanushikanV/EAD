package com.evcharging.mobile.ui.bookings;

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
import com.evcharging.mobile.ui.bookings.adapter.BookingsAdapter;
import com.evcharging.mobile.ui.bookings.model.Booking;
import java.util.ArrayList;
import java.util.List;

/**
 * Bookings Fragment
 * 
 * Displays user's booking history and current reservations.
 */
public class BookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        loadBookings();
        
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_bookings);
    }

    private void setupRecyclerView() {
        adapter = new BookingsAdapter(new ArrayList<>(), booking -> {
            // Handle booking click
            Toast.makeText(getContext(), "Booking: " + booking.getStationName(), Toast.LENGTH_SHORT).show();
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadBookings() {
        List<Booking> bookings = getMockBookings();
        adapter.updateBookings(bookings);
    }

    private List<Booking> getMockBookings() {
        List<Booking> bookings = new ArrayList<>();
        
        bookings.add(new Booking("1", "Colombo Fort Station", "2024-01-15 14:00", "Approved", "Active"));
        bookings.add(new Booking("2", "Kandy City Center", "2024-01-16 10:30", "Pending", "Scheduled"));
        bookings.add(new Booking("3", "Galle Fort Station", "2024-01-10 16:00", "Completed", "Finished"));
        bookings.add(new Booking("4", "Anuradhapura Station", "2024-01-12 09:15", "Cancelled", "Cancelled"));
        
        return bookings;
    }
}