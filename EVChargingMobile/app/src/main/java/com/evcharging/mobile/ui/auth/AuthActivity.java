package com.evcharging.mobile.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.DashboardActivity;

/**
 * AuthActivity
 * 
 * This activity handles user authentication flow including login and signup.
 * It provides navigation between login and signup fragments.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class AuthActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignup;
    private TextView tvSwitchAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initializeViews();
        setupClickListeners();
        
        // Start with login fragment by default
        showLoginFragment();
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        // UI elements will be handled by fragments
    }

    /**
     * Set up click listeners for navigation buttons
     */
    private void setupClickListeners() {
        // Click listeners will be handled by fragments
    }

    /**
     * Show login fragment
     */
    private void showLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    /**
     * Show signup fragment
     */
    private void showSignupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SignupFragment())
                .commit();
    }

    /**
     * Navigate to dashboard after successful authentication
     */
    public void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Show error message
     * 
     * @param message Error message to display
     */
    public void showErrorMessage(String message) {
        // TODO: Implement error display mechanism
        // This will be enhanced in later phases with proper error handling
    }
}
