package com.evcharging.mobile.ui.bookings.adapter;

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
 * BookingsAdapter
 * 
 * This adapter displays user bookings in a RecyclerView.
 * It handles booking cards with status indicators and actions.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

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
    public BookingsAdapter(OnBookingClickListener onBookingClickListener) {
        this.onBookingClickListener = onBookingClickListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
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
        
        private TextView tvStationName;
        private TextView tvBookingDateTime;
        private TextView tvCreatedDate;
        private Chip chipBookingStatus;
        private ImageView ivBookingIcon;
        private View cardView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStationName = itemView.findViewById(R.id.tv_station_name);
            tvBookingDateTime = itemView.findViewById(R.id.tv_booking_date_time);
            tvCreatedDate = itemView.findViewById(R.id.tv_created_date);
            chipBookingStatus = itemView.findViewById(R.id.chip_booking_status);
            ivBookingIcon = itemView.findViewById(R.id.iv_booking_icon);
            cardView = itemView.findViewById(R.id.card_booking);
            
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
            // Set station name (mock for now)
            tvStationName.setText("Station " + booking.getStationId());
            
            // Set booking date/time
            tvBookingDateTime.setText(formatDateTime(booking.getReservationDateTime()));
            
            // Set created date
            tvCreatedDate.setText("Created: " + formatDateTime(booking.getCreatedAt()));
            
            // Set booking status
            chipBookingStatus.setText(booking.getStatus());
            setStatusChipStyle(booking.getStatus());
            
            // Set booking icon based on status
            setBookingIcon(booking.getStatus());
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
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy\nh:mm a", Locale.getDefault());
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
