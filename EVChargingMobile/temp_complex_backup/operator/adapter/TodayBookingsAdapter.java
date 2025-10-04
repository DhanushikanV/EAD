package com.evcharging.mobile.ui.operator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evcharging.mobile.R;
import com.evcharging.mobile.network.models.Booking;
import com.google.android.material.chip.Chip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TodayBookingsAdapter
 * 
 * This adapter displays today's bookings for station operators.
 * It shows booking details with action buttons for session management.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class TodayBookingsAdapter extends RecyclerView.Adapter<TodayBookingsAdapter.BookingViewHolder> {

    private List<Booking> bookings = new ArrayList<>();
    private OnBookingClickListener onBookingClickListener;

    /**
     * Interface for booking click events
     */
    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }

    /**
     * Constructor
     * 
     * @param onBookingClickListener Click listener for booking items
     */
    public TodayBookingsAdapter(OnBookingClickListener onBookingClickListener) {
        this.onBookingClickListener = onBookingClickListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_operator_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    /**
     * Update bookings list
     * 
     * @param bookings New list of bookings
     */
    public void updateBookings(List<Booking> bookings) {
        this.bookings = bookings != null ? bookings : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for booking items
     */
    class BookingViewHolder extends RecyclerView.ViewHolder {
        
        private TextView tvBookingId;
        private TextView tvCustomerNIC;
        private TextView tvBookingDateTime;
        private TextView tvStationId;
        private Chip chipBookingStatus;
        private ImageView ivBookingIcon;
        private View cardView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvBookingId = itemView.findViewById(R.id.tv_booking_id);
            tvCustomerNIC = itemView.findViewById(R.id.tv_customer_nic);
            tvBookingDateTime = itemView.findViewById(R.id.tv_booking_date_time);
            tvStationId = itemView.findViewById(R.id.tv_station_id);
            chipBookingStatus = itemView.findViewById(R.id.chip_booking_status);
            ivBookingIcon = itemView.findViewById(R.id.iv_booking_icon);
            cardView = itemView.findViewById(R.id.card_operator_booking);
            
            // Set click listener
            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onBookingClickListener != null) {
                    onBookingClickListener.onBookingClick(bookings.get(position));
                }
            });
        }

        /**
         * Bind booking data to view
         * 
         * @param booking Booking data
         */
        public void bind(Booking booking) {
            // Set booking ID
            tvBookingId.setText("Booking #" + booking.getId());
            
            // Set customer NIC (masked for privacy)
            String maskedNIC = maskNIC(booking.getEVOwnerNIC());
            tvCustomerNIC.setText("Customer: " + maskedNIC);
            
            // Set booking date/time
            tvBookingDateTime.setText(formatDateTime(booking.getReservationDateTime()));
            
            // Set station ID
            tvStationId.setText("Station: " + booking.getStationId());
            
            // Set booking status
            chipBookingStatus.setText(booking.getStatus());
            setStatusChipStyle(booking.getStatus());
            
            // Set booking icon based on status
            setBookingIcon(booking.getStatus());
        }

        /**
         * Mask NIC for privacy
         * 
         * @param nic NIC number
         * @return Masked NIC
         */
        private String maskNIC(String nic) {
            if (nic == null || nic.length() < 4) {
                return "****";
            }
            return nic.substring(0, 3) + "****" + nic.substring(nic.length() - 2);
        }

        /**
         * Format date/time string
         * 
         * @param dateTimeString Date/time string
         * @return Formatted date/time
         */
        private String formatDateTime(String dateTimeString) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a", Locale.getDefault());
                Date date = inputFormat.parse(dateTimeString);
                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                return outputFormat.format(date);
            } catch (ParseException e) {
                return dateTimeString; // Return original if parsing fails
            }
        }

        /**
         * Set status chip style based on booking status
         * 
         * @param status Booking status
         */
        private void setStatusChipStyle(String status) {
            switch (status.toLowerCase()) {
                case "pending":
                    chipBookingStatus.setChipBackgroundColorResource(R.color.status_pending);
                    chipBookingStatus.setTextColor(itemView.getContext().getColor(R.color.text_white));
                    break;
                case "approved":
                    chipBookingStatus.setChipBackgroundColorResource(R.color.status_approved);
                    chipBookingStatus.setTextColor(itemView.getContext().getColor(R.color.text_white));
                    break;
                case "cancelled":
                    chipBookingStatus.setChipBackgroundColorResource(R.color.status_cancelled);
                    chipBookingStatus.setTextColor(itemView.getContext().getColor(R.color.text_white));
                    break;
                case "completed":
                    chipBookingStatus.setChipBackgroundColorResource(R.color.status_completed);
                    chipBookingStatus.setTextColor(itemView.getContext().getColor(R.color.text_white));
                    break;
                default:
                    chipBookingStatus.setChipBackgroundColorResource(R.color.text_secondary);
                    chipBookingStatus.setTextColor(itemView.getContext().getColor(R.color.text_white));
                    break;
            }
        }

        /**
         * Set booking icon based on status
         * 
         * @param status Booking status
         */
        private void setBookingIcon(String status) {
            switch (status.toLowerCase()) {
                case "pending":
                    ivBookingIcon.setImageResource(R.drawable.ic_pending);
                    break;
                case "approved":
                    ivBookingIcon.setImageResource(R.drawable.ic_approved);
                    break;
                case "cancelled":
                    ivBookingIcon.setImageResource(R.drawable.ic_cancelled);
                    break;
                case "completed":
                    ivBookingIcon.setImageResource(R.drawable.ic_completed);
                    break;
                default:
                    ivBookingIcon.setImageResource(R.drawable.ic_bookings);
                    break;
            }
        }
    }
}
