package com.evcharging.mobile.ui.qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.bookings.model.Booking;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * QR Generator Fragment
 * 
 * Generates and displays QR codes for approved bookings.
 */
public class QrGeneratorFragment extends Fragment {

    private ImageView qrImageView;
    private TextView tvBookingInfo;
    private Button btnShareQr;
    private Booking booking;
    private String argBookingId;
    private String argQrToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_generator, container, false);
        
        initializeViews(view);
        setupClickListeners(view);
        // read args from navigation
        Bundle args = getArguments();
        if (args != null) {
            argBookingId = args.getString("bookingId", null);
            argQrToken = args.getString("qrToken", null);
        }
        generateQrCode();
        
        return view;
    }

    private void initializeViews(View view) {
        qrImageView = view.findViewById(R.id.iv_qr_code);
        tvBookingInfo = view.findViewById(R.id.tv_booking_info);
        btnShareQr = view.findViewById(R.id.btn_share_qr);
    }

    private void setupClickListeners(View view) {
        btnShareQr.setOnClickListener(v -> {
            // TODO: Implement QR code sharing
            Toast.makeText(getContext(), "QR code sharing feature coming soon!", 
                          Toast.LENGTH_SHORT).show();
        });
    }

    private void generateQrCode() {
        if (argBookingId != null && argQrToken != null) {
            // minimal booking wrapper for display
            booking = new Booking(argBookingId, "", "", "Confirmed", "");
            booking.setQrToken(argQrToken);
            displayBookingInfo();
            generateQrBitmap();
        } else {
            Toast.makeText(getContext(), "QR not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayBookingInfo() {
        String info = "Booking ID: " + booking.getId() + "\n" +
                     "Station: " + booking.getStationName() + "\n" +
                     "Date: " + booking.getDateTime() + "\n" +
                     "Status: " + booking.getStatus();
        
        tvBookingInfo.setText(info);
    }

    private void generateQrBitmap() {
        try {
            // Create QR data JSON (backend expects bookingId + qrToken)
            JSONObject qrData = new JSONObject();
            qrData.put("bookingId", booking.getId());
            qrData.put("qrToken", booking.getQrToken());

            // Generate QR code
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrData.toString(), BarcodeFormat.QR_CODE, 512, 512);
            
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 
                        android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }
            
            qrImageView.setImageBitmap(bitmap);
            
        } catch (WriterException | JSONException e) {
            Toast.makeText(getContext(), "Error generating QR code: " + e.getMessage(), 
                          Toast.LENGTH_SHORT).show();
        }
    }

    // No mock booking in production
}
