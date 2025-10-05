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
import android.content.Context;
import com.evcharging.mobile.network.ApiClient;
import com.evcharging.mobile.network.api.OperatorService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        String qrToken = qrJson.optString("qrToken", "");
        if (qrToken.isEmpty()) {
            Toast.makeText(getContext(), "Invalid booking QR code", Toast.LENGTH_SHORT).show();
            return;
        }

        Context ctx = getContext();
        if (ctx == null) return;
        OperatorService service = ApiClient.getRetrofitInstance(ctx).create(OperatorService.class);
        service.scanQRCode(new OperatorService.ScanRequest(qrToken)).enqueue(new Callback<OperatorService.ScanResponse>() {
            @Override
            public void onResponse(Call<OperatorService.ScanResponse> call, Response<OperatorService.ScanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Valid booking: " + response.body().bookingId, Toast.LENGTH_SHORT).show();
                    // After successful validation, enable Start action in UI or auto-start
                    startSession(response.body().bookingId);
                } else {
                    Toast.makeText(getContext(), "Scan failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OperatorService.ScanResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Scan error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSession(String bookingId) {
        Context ctx = getContext();
        if (ctx == null) return;
        OperatorService service = ApiClient.getRetrofitInstance(ctx).create(OperatorService.class);
        service.startSession(bookingId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Session started", Toast.LENGTH_SHORT).show();
                    // For demo, finalize immediately or navigate to session screen
                    finalizeSession(bookingId);
                } else {
                    Toast.makeText(getContext(), "Start failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Start error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalizeSession(String bookingId) {
        Context ctx = getContext();
        if (ctx == null) return;
        OperatorService service = ApiClient.getRetrofitInstance(ctx).create(OperatorService.class);
        service.finalizeSession(bookingId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Session completed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Finalize failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Finalize error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

