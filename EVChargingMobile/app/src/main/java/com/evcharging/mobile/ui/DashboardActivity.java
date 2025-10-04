package com.evcharging.mobile.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.evcharging.mobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Dashboard Activity
 * 
 * Main activity for authenticated users with bottom navigation.
 */
public class DashboardActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setupNavigation();
    }

    private void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }
}
