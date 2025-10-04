package com.evcharging.mobile.ui.qr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.evcharging.mobile.R;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import org.json.JSONObject;

/**
 * QR Scanner Fragment
 * 
 * Handles QR code scanning for station access and booking validation.
 */
public class QrScannerFragment extends Fragment {

    private DecoratedBarcodeView barcodeView;
    private boolean isScanning = false;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        
        initializeViews(view);
        setupScanner();
        
        return view;
    }

    private void initializeViews(View view) {
        barcodeView = view.findViewById(R.id.barcode_scanner);
    }

    private void setupScanner() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        } else {
            requestCameraPermission();
        }
    }

    private void startScanning() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (!isScanning) return;
                
                isScanning = false;
                barcodeView.pause();
                
                handleQrResult(result.getText());
            }

            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
                // Optional: Handle possible result points
            }
        });
        
        isScanning = true;
        barcodeView.resume();
    }

    private void handleQrResult(String qrData) {
        try {
            JSONObject qrJson = new JSONObject(qrData);
            String type = qrJson.optString("type", "");
            
            switch (type) {
                case "booking":
                    handleBookingQr(qrJson);
                    break;
                case "station":
                    handleStationQr(qrJson);
                    break;
                default:
                    Toast.makeText(getContext(), "Invalid QR code format", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error reading QR code: " + e.getMessage(), 
                          Toast.LENGTH_SHORT).show();
        }
        
        // Resume scanning after 2 seconds
        new android.os.Handler().postDelayed(() -> {
            if (getActivity() != null) {
                isScanning = true;
                barcodeView.resume();
            }
        }, 2000);
    }

    private void handleBookingQr(JSONObject qrJson) {
        String bookingId = qrJson.optString("bookingId", "");
        String stationId = qrJson.optString("stationId", "");
        
        if (!bookingId.isEmpty() && !stationId.isEmpty()) {
            Toast.makeText(getContext(), 
                "Booking validated: " + bookingId + " at Station " + stationId, 
                Toast.LENGTH_LONG).show();
            
            // TODO: Validate booking with backend
            // TODO: Update booking status
            // TODO: Navigate to appropriate screen
        } else {
            Toast.makeText(getContext(), "Invalid booking QR code", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleStationQr(JSONObject qrJson) {
        String stationId = qrJson.optString("stationId", "");
        String action = qrJson.optString("action", "");
        
        if (!stationId.isEmpty()) {
            switch (action) {
                case "info":
                    Toast.makeText(getContext(), 
                        "Station Info: " + stationId, Toast.LENGTH_SHORT).show();
                    // TODO: Show station details
                    break;
                case "book":
                    Toast.makeText(getContext(), 
                        "Quick booking for Station: " + stationId, Toast.LENGTH_SHORT).show();
                    // TODO: Navigate to booking creation
                    break;
                default:
                    Toast.makeText(getContext(), 
                        "Station QR: " + stationId, Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Invalid station QR code", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(getContext(), "Camera permission required for QR scanning", 
                              Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeView.resume();
            isScanning = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
        isScanning = false;
    }
}

