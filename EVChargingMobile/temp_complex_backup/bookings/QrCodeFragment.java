package com.evcharging.mobile.ui.bookings;

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
import com.evcharging.mobile.network.models.Booking;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * QrCodeFragment
 * 
 * This fragment displays the QR code for approved bookings.
 * Users can show this QR code to station operators for verification.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class QrCodeFragment extends Fragment {

    private TextView tvStationName;
    private TextView tvBookingDateTime;
    private TextView tvBookingId;
    private ImageView ivQrCode;
    private Button btnShareQr;
    private Button btnClose;
    private Booking booking;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get booking data from arguments
        if (getArguments() != null) {
            String bookingId = getArguments().getString("booking_id");
            // TODO: Load booking data from repository
            booking = createMockBooking(bookingId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code, container, false);
        
        initializeViews(view);
        setupClickListeners();
        displayBookingInfo();
        generateQRCode();
        
        return view;
    }

    /**
     * Initialize UI components
     * 
     * @param view Root view of the fragment
     */
    private void initializeViews(View view) {
        tvStationName = view.findViewById(R.id.tv_station_name);
        tvBookingDateTime = view.findViewById(R.id.tv_booking_date_time);
        tvBookingId = view.findViewById(R.id.tv_booking_id);
        ivQrCode = view.findViewById(R.id.iv_qr_code);
        btnShareQr = view.findViewById(R.id.btn_share_qr);
        btnClose = view.findViewById(R.id.btn_close);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        btnShareQr.setOnClickListener(v -> shareQRCode());
        btnClose.setOnClickListener(v -> closeFragment());
    }

    /**
     * Display booking information
     */
    private void displayBookingInfo() {
        if (booking != null) {
            tvStationName.setText("Station " + booking.getStationId());
            tvBookingDateTime.setText(formatDateTime(booking.getReservationDateTime()));
            tvBookingId.setText("Booking ID: " + booking.getId());
        }
    }

    /**
     * Generate QR code for the booking
     */
    private void generateQRCode() {
        if (booking != null) {
            String qrData = createQRData(booking);
            Bitmap qrBitmap = generateQRCodeBitmap(qrData);
            
            if (qrBitmap != null) {
                ivQrCode.setImageBitmap(qrBitmap);
            } else {
                showError("Failed to generate QR code");
            }
        }
    }

    /**
     * Create QR code data string
     * 
     * @param booking Booking data
     * @return QR code data string
     */
    private String createQRData(Booking booking) {
        // Create a structured QR data string
        return String.format(
            "EV_CHARGING_BOOKING|%s|%s|%s|%s|%s",
            booking.getId(),
            booking.getEVOwnerNIC(),
            booking.getStationId(),
            booking.getReservationDateTime(),
            booking.getStatus()
        );
    }

    /**
     * Generate QR code bitmap
     * 
     * @param data QR code data
     * @return Generated bitmap or null if failed
     */
    private Bitmap generateQRCodeBitmap(String data) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 
                        getResources().getColor(R.color.black) : 
                        getResources().getColor(R.color.white));
                }
            }
            
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Share QR code
     */
    private void shareQRCode() {
        if (booking != null) {
            String shareText = createShareText(booking);
            
            android.content.Intent shareIntent = new android.content.Intent(
                android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
                "EV Charging Booking - " + booking.getId());
            
            startActivity(android.content.Intent.createChooser(shareIntent, 
                "Share Booking Details"));
        }
    }

    /**
     * Create share text for booking
     * 
     * @param booking Booking data
     * @return Share text
     */
    private String createShareText(Booking booking) {
        return String.format(
            "EV Charging Booking Details:\n\n" +
            "Booking ID: %s\n" +
            "Station: Station %s\n" +
            "Date & Time: %s\n" +
            "Status: %s\n\n" +
            "Show this QR code to the station operator.",
            booking.getId(),
            booking.getStationId(),
            formatDateTime(booking.getReservationDateTime()),
            booking.getStatus()
        );
    }

    /**
     * Close fragment
     */
    private void closeFragment() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    /**
     * Format date/time string
     * 
     * @param dateTimeString Date/time string
     * @return Formatted date/time
     */
    private String formatDateTime(String dateTimeString) {
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat(
                "MMM dd, yyyy h:mm:ss a", java.util.Locale.getDefault());
            java.util.Date date = inputFormat.parse(dateTimeString);
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat(
                "MMM dd, yyyy 'at' h:mm a", java.util.Locale.getDefault());
            return outputFormat.format(date);
        } catch (java.text.ParseException e) {
            return dateTimeString; // Return original if parsing fails
        }
    }

    /**
     * Show error message
     * 
     * @param message Error message
     */
    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Create mock booking for development
     * 
     * @param bookingId Booking ID
     * @return Mock booking
     */
    private Booking createMockBooking(String bookingId) {
        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId != null ? bookingId : "12345");
        mockBooking.setEVOwnerNIC("123456789V");
        mockBooking.setStationId("1");
        mockBooking.setReservationDateTime("Dec 15, 2024 2:30:00 PM");
        mockBooking.setStatus("Approved");
        mockBooking.setCreatedAt("Dec 14, 2024 10:00:00 AM");
        return mockBooking;
    }
}
