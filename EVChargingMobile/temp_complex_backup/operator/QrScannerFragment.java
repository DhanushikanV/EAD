package com.evcharging.mobile.ui.operator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

/**
 * QrScannerFragment
 * 
 * This fragment provides QR code scanning functionality for station operators.
 * It allows operators to scan QR codes from EV owner bookings.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class QrScannerFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST = 1001;
    
    private DecoratedBarcodeView barcodeView;
    private TextView tvScanInstructions;
    private Button btnToggleFlash;
    private Button btnManualEntry;
    private boolean isFlashOn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        
        initializeViews(view);
        setupClickListeners();
        checkCameraPermission();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        barcodeView = view.findViewById(R.id.barcode_scanner);
        tvScanInstructions = view.findViewById(R.id.tv_scan_instructions);
        btnToggleFlash = view.findViewById(R.id.btn_toggle_flash);
        btnManualEntry = view.findViewById(R.id.btn_manual_entry);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        btnToggleFlash.setOnClickListener(v -> toggleFlash());
        btnManualEntry.setOnClickListener(v -> showManualEntryDialog());
        
        // Set up barcode callback
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                handleBarcodeResult(result);
            }

            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
                // Handle possible result points if needed
            }
        });
    }

    /**
     * Check camera permission
     */
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
            != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(requireActivity(), 
                new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startScanning();
        }
    }

    /**
     * Start QR code scanning
     */
    private void startScanning() {
        barcodeView.resume();
        tvScanInstructions.setText("Point camera at QR code");
    }

    /**
     * Handle barcode scan result
     * 
     * @param result Barcode scan result
     */
    private void handleBarcodeResult(BarcodeResult result) {
        if (result != null && result.getText() != null) {
            String qrData = result.getText();
            
            // TODO: Process QR code data
            // 1. Validate QR code format
            // 2. Extract booking information
            // 3. Verify booking with server
            // 4. Show booking details
            
            processQRCode(qrData);
        }
    }

    /**
     * Process QR code data
     * 
     * @param qrData QR code content
     */
    private void processQRCode(String qrData) {
        // For now, show the scanned data
        Toast.makeText(requireContext(), "Scanned: " + qrData, Toast.LENGTH_SHORT).show();
        
        // TODO: Implement actual QR processing
        // 1. Parse booking ID from QR data
        // 2. Validate booking with server
        // 3. Show booking details dialog
        // 4. Allow operator to start/end session
    }

    /**
     * Toggle flashlight
     */
    private void toggleFlash() {
        if (barcodeView != null) {
            if (isFlashOn) {
                barcodeView.setTorchOff();
                btnToggleFlash.setText("Turn Flash On");
                isFlashOn = false;
            } else {
                barcodeView.setTorchOn();
                btnToggleFlash.setText("Turn Flash Off");
                isFlashOn = true;
            }
        }
    }

    /**
     * Show manual entry dialog
     */
    private void showManualEntryDialog() {
        // TODO: Implement manual QR code entry dialog
        Toast.makeText(requireContext(), "Manual entry coming soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(requireContext(), 
                    "Camera permission is required for QR scanning", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
            == PackageManager.PERMISSION_GRANTED) {
            barcodeView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isFlashOn) {
            barcodeView.setTorchOff();
        }
    }
}
