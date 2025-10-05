package com.evcharging.mobile.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.SplashActivity;
import com.evcharging.mobile.utils.SharedPreferencesManager;
import java.util.List;

/**
 * Profile Fragment
 * 
 * Displays user profile information and provides logout functionality.
 */
public class ProfileFragment extends Fragment {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvUserPhone;
    private TextView tvUserNameDetail;
    private TextView tvUserEmailDetail;
    private TextView tvUserPhoneDetail;
    private TextView tvTotalBookings;
    private TextView tvCompletedBookings;
    private TextView tvCancelledBookings;
    private SharedPreferencesManager preferencesManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        preferencesManager = new SharedPreferencesManager(getContext());
        initializeViews(view);
        loadUserData();
        setupClickListeners(view);
        
        return view;
    }

    private void initializeViews(View view) {
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        tvUserNameDetail = view.findViewById(R.id.tv_user_name_detail);
        tvUserEmailDetail = view.findViewById(R.id.tv_user_email_detail);
        tvUserPhoneDetail = view.findViewById(R.id.tv_user_phone_detail);
        tvTotalBookings = view.findViewById(R.id.tv_total_bookings);
        tvCompletedBookings = view.findViewById(R.id.tv_completed_bookings);
        tvCancelledBookings = view.findViewById(R.id.tv_cancelled_bookings);
    }

    private void loadUserData() {
        // Load basic data from SharedPreferences first
        String name = preferencesManager.getUserName();
        String email = preferencesManager.getUserEmail();
        String phone = preferencesManager.getUserPhone();

        tvUserName.setText(name != null ? name : "User Name");
        tvUserEmail.setText(email != null ? email : "user@example.com");
        tvUserNameDetail.setText(name != null ? name : "User Name");
        tvUserEmailDetail.setText(email != null ? email : "user@example.com");
        tvUserPhoneDetail.setText(phone != null ? phone : "No phone number");

        // Fetch fresh data from API
        fetchUserDataFromAPI();
        fetchBookingStatistics();
    }

    private void fetchUserDataFromAPI() {
        Context context = getContext();
        if (context == null) return;

        String userNIC = preferencesManager.getUserNIC();
        if (userNIC == null || userNIC.isEmpty()) {
            android.util.Log.e("ProfileFragment", "User NIC not found in preferences");
            return;
        }

        com.evcharging.mobile.network.api.UserService userService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(context)
                        .create(com.evcharging.mobile.network.api.UserService.class);

        userService.getEVOwnerByNIC(userNIC).enqueue(new retrofit2.Callback<com.evcharging.mobile.network.models.EVOwner>() {
            @Override
            public void onResponse(retrofit2.Call<com.evcharging.mobile.network.models.EVOwner> call,
                                   retrofit2.Response<com.evcharging.mobile.network.models.EVOwner> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.evcharging.mobile.network.models.EVOwner evOwner = response.body();
                    android.util.Log.d("ProfileFragment", "User data fetched: " + evOwner.getName());
                    
                    // Update UI with real data
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            tvUserName.setText(evOwner.getName());
                            tvUserEmail.setText(evOwner.getEmail());
                            tvUserNameDetail.setText(evOwner.getName());
                            tvUserEmailDetail.setText(evOwner.getEmail());
                            tvUserPhoneDetail.setText(evOwner.getPhone() != null ? evOwner.getPhone() : "No phone number");
                        });
                    }
                } else {
                    android.util.Log.e("ProfileFragment", "Failed to fetch user data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.evcharging.mobile.network.models.EVOwner> call, Throwable t) {
                android.util.Log.e("ProfileFragment", "Network call failed for user data", t);
            }
        });
    }

    private void fetchBookingStatistics() {
        Context context = getContext();
        if (context == null) return;

        String userNIC = preferencesManager.getUserNIC();
        if (userNIC == null || userNIC.isEmpty()) {
            android.util.Log.e("ProfileFragment", "User NIC not found for booking stats");
            return;
        }

        com.evcharging.mobile.network.api.BookingService bookingService =
                com.evcharging.mobile.network.ApiClient.getRetrofitInstance(context)
                        .create(com.evcharging.mobile.network.api.BookingService.class);

        bookingService.getUserBookings(userNIC).enqueue(new retrofit2.Callback<List<com.evcharging.mobile.network.models.Booking>>() {
            @Override
            public void onResponse(retrofit2.Call<List<com.evcharging.mobile.network.models.Booking>> call,
                                   retrofit2.Response<List<com.evcharging.mobile.network.models.Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.evcharging.mobile.network.models.Booking> bookings = response.body();
                    android.util.Log.d("ProfileFragment", "Bookings fetched: " + bookings.size());
                    
                    // Calculate statistics
                    final int totalBookings = bookings.size();
                    int completedBookings = 0;
                    int cancelledBookings = 0;
                    
                    for (com.evcharging.mobile.network.models.Booking booking : bookings) {
                        if ("Completed".equals(booking.getStatus())) {
                            completedBookings++;
                        } else if ("Cancelled".equals(booking.getStatus())) {
                            cancelledBookings++;
                        }
                    }
                    
                    final int finalCompletedBookings = completedBookings;
                    final int finalCancelledBookings = cancelledBookings;
                    
                    // Update UI with real statistics
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            tvTotalBookings.setText(String.valueOf(totalBookings));
                            tvCompletedBookings.setText(String.valueOf(finalCompletedBookings));
                            tvCancelledBookings.setText(String.valueOf(finalCancelledBookings));
                        });
                    }
                } else {
                    android.util.Log.e("ProfileFragment", "Failed to fetch booking statistics. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<com.evcharging.mobile.network.models.Booking>> call, Throwable t) {
                android.util.Log.e("ProfileFragment", "Network call failed for booking stats", t);
            }
        });
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
            // Clear user data
            preferencesManager.clearUserData();
            
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            
            // Navigate to splash screen
            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }
}