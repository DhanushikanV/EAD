package com.evcharging.mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.evcharging.mobile.R;
import com.evcharging.mobile.utils.SharedPreferencesManager;

/**
 * Splash Screen Activity
 * 
 * Displays the app logo and checks for existing authentication token.
 * Navigates to Dashboard if user is logged in, otherwise to Auth screen.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check authentication status after splash delay
        new Handler().postDelayed(this::checkAuthStatus, SPLASH_DELAY);
    }

    private void checkAuthStatus() {
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(this);
        String authToken = preferencesManager.getAuthToken();

        Intent intent;
        if (authToken != null && !authToken.isEmpty()) {
            // User is logged in, go to dashboard
            intent = new Intent(this, DashboardActivity.class);
        } else {
            // User is not logged in, go to authentication
            intent = new Intent(this, AuthActivity.class);
        }

        startActivity(intent);
        finish();
    }
}