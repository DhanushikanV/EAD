package com.evcharging.mobile.ui.bookings.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.bookings.model.Booking;
import java.util.ArrayList;
import java.util.List;

/**
 * Bookings Adapter
 * 
 * RecyclerView adapter for displaying user bookings.
 */
public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnBookingClickListener clickListener;

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }

    public BookingsAdapter(List<Booking> bookings, OnBookingClickListener clickListener) {
        this.bookings = bookings != null ? bookings : new ArrayList<>();
        this.clickListener = clickListener;
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

    public void updateBookings(List<Booking> newBookings) {
        this.bookings = newBookings != null ? newBookings : new ArrayList<>();
        notifyDataSetChanged();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStationName;
        private TextView tvDateTime;
        private TextView tvStatus;
        private TextView tvType;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStationName = itemView.findViewById(R.id.tv_booking_station);
            tvDateTime = itemView.findViewById(R.id.tv_booking_datetime);
            tvStatus = itemView.findViewById(R.id.tv_booking_status);
            tvType = itemView.findViewById(R.id.tv_booking_type);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onBookingClick(bookings.get(position));
                    }
                }
            });
        }

        public void bind(Booking booking) {
            tvStationName.setText(booking.getStationName());
            tvDateTime.setText(booking.getDateTime());
            tvStatus.setText(booking.getStatus());
            tvType.setText(booking.getType());

            // Set status text color to white for better visibility on green background
            tvStatus.setTextColor(android.graphics.Color.WHITE);
        }
    }
}