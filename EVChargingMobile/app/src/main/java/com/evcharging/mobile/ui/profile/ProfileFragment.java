package com.evcharging.mobile.ui.profile;

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

/**
 * Profile Fragment
 * 
 * Displays user profile information and provides logout functionality.
 */
public class ProfileFragment extends Fragment {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvUserPhone;
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
        tvUserPhone = view.findViewById(R.id.tv_user_phone_detail);
    }

    private void loadUserData() {
        String name = preferencesManager.getUserName();
        String email = preferencesManager.getUserEmail();
        String phone = preferencesManager.getUserPhone();

        tvUserName.setText(name != null ? name : "User Name");
        tvUserEmail.setText(email != null ? email : "user@example.com");
        tvUserPhone.setText(phone != null ? phone : "+94 77 123 4567");
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