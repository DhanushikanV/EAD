package com.evcharging.mobile.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.auth.AuthActivity;
import com.evcharging.mobile.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

/**
 * DashboardActivity
 * 
 * This is the main dashboard activity that provides navigation between different
 * sections of the app. It includes bottom navigation for EV owners and
 * drawer navigation for station operators.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        preferencesManager = new SharedPreferencesManager(this);
        
        initializeViews();
        setupNavigation();
        setupUserInfo();
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    /**
     * Set up navigation based on user role
     */
    private void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        
        String userRole = preferencesManager.getUserRole();
        
        if ("StationOperator".equals(userRole)) {
            setupOperatorNavigation();
        } else {
            setupEVOwnerNavigation();
        }
    }

    /**
     * Set up navigation for EV owners (bottom navigation)
     */
    private void setupEVOwnerNavigation() {
        // Hide drawer navigation, show bottom navigation
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        bottomNavigationView.setVisibility(View.VISIBLE);
        
        // Set up bottom navigation
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        
        // Set up app bar
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_stations,
                R.id.nav_bookings,
                R.id.nav_profile
        ).build();
        
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    /**
     * Set up navigation for station operators (drawer navigation)
     */
    private void setupOperatorNavigation() {
        // Hide bottom navigation, show drawer navigation
        bottomNavigationView.setVisibility(View.GONE);
        navigationView.setVisibility(View.VISIBLE);
        
        // Set up drawer navigation
        NavigationUI.setupWithNavController(navigationView, navController);
        
        // Set up app bar with drawer
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_operator_dashboard,
                R.id.nav_today_bookings,
                R.id.nav_qr_scanner,
                R.id.nav_operator_profile
        ).setDrawerLayout(drawerLayout)
         .build();
        
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    /**
     * Set up user information in navigation header
     */
    private void setupUserInfo() {
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            TextView tvUserName = headerView.findViewById(R.id.tv_user_name);
            TextView tvUserEmail = headerView.findViewById(R.id.tv_user_email);
            
            if (tvUserName != null) {
                tvUserName.setText(preferencesManager.getUserName());
            }
            
            if (tvUserEmail != null) {
                tvUserEmail.setText(preferencesManager.getUserEmail());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            handleLogout();
            return true;
        }
        
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Handle user logout
     */
    private void handleLogout() {
        // Clear user data
        preferencesManager.clearUserData();
        
        // Navigate to auth activity
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
