package com.evcharging.mobile.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.evcharging.mobile.R;
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.network.api.ChargingStationService;
import com.evcharging.mobile.network.models.ChargingStation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * APITestActivity
 * 
 * Simple test activity to verify API connectivity and debug connection issues.
 * This helps troubleshoot why the app shows fallback data instead of real API data.
 */
public class APITestActivity extends AppCompatActivity {

    private static final String TAG = "APITestActivity";
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);
        
        tvResult = findViewById(R.id.tv_result);
        
        // Test API connectivity
        testAPIConnectivity();
    }

    /**
     * Test API connectivity and display results
     */
    private void testAPIConnectivity() {
        tvResult.setText("Testing API connectivity...\n");
        
        ChargingStationService service = ApiClient.getRetrofitInstance(this)
                .create(ChargingStationService.class);
        
        Call<List<ChargingStation>> call = service.getAllStations();
        
        call.enqueue(new Callback<List<ChargingStation>>() {
            @Override
            public void onResponse(Call<List<ChargingStation>> call, Response<List<ChargingStation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChargingStation> stations = response.body();
                    String result = "✅ API Connection SUCCESS!\n\n";
                    result += "Received " + stations.size() + " stations:\n\n";
                    
                    for (ChargingStation station : stations) {
                        result += "• " + station.getName() + " (" + station.getType() + ")\n";
                        result += "  Location: " + station.getLocation() + "\n";
                        result += "  Slots: " + station.getAvailableSlots() + "/" + station.getTotalSlots() + "\n";
                        result += "  Status: " + station.getStatus() + "\n\n";
                    }
                    
                    tvResult.setText(result);
                    Toast.makeText(APITestActivity.this, "API connection successful!", Toast.LENGTH_LONG).show();
                    
                    Log.d(TAG, "API Response: " + stations.size() + " stations received");
                } else {
                    String error = "❌ API Error: " + response.code() + " - " + response.message();
                    tvResult.setText(error);
                    Toast.makeText(APITestActivity.this, "API error: " + response.code(), Toast.LENGTH_LONG).show();
                    
                    Log.e(TAG, "API Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ChargingStation>> call, Throwable t) {
                String error = "❌ Network Error: " + t.getMessage();
                tvResult.setText(error);
                Toast.makeText(APITestActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                
                Log.e(TAG, "Network Error", t);
            }
        });
    }

    /**
     * Handle test again button click
     */
    public void onTestAgainClick(android.view.View view) {
        testAPIConnectivity();
    }
}
