package com.evcharging.mobile.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.evcharging.mobile.R;
import com.evcharging.mobile.utils.SharedPreferencesManager;

/**
 * Operator Activity
 * 
 * Main activity for station operators with operator-specific navigation and features.
 */
public class OperatorActivity extends AppCompatActivity {

    private SharedPreferencesManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_simple);

        prefsManager = new SharedPreferencesManager(this);
        
        setupViews();
        setupActionBar();
    }

    private void setupViews() {
        TextView tvWelcome = findViewById(R.id.tv_operator_welcome);
        TextView tvStationInfo = findViewById(R.id.tv_station_info);
        
        String username = prefsManager.getUserName();
        if (username != null && !username.isEmpty()) {
            tvWelcome.setText("Welcome, " + username + "!");
        } else {
            tvWelcome.setText("Welcome, Operator!");
        }
        
        tvStationInfo.setText("Colombo Fort Charging Station\nStatus: Active\nAvailable Slots: 3/6");
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Station Operator");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.operator_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Handle logout
            prefsManager.clearUserData();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
