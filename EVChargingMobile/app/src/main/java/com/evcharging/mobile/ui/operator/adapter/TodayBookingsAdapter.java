package com.evcharging.mobile.ui.operator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.bookings.model.Booking;
import java.util.ArrayList;
import java.util.List;

/**
 * Today Bookings Adapter
 * 
 * Adapter for displaying today's bookings in operator dashboard.
 */
public class TodayBookingsAdapter extends RecyclerView.Adapter<TodayBookingsAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnBookingActionListener actionListener;

    public interface OnBookingActionListener {
        void onBookingClick(Booking booking);
        void onApproveBooking(Booking booking);
        void onCancelBooking(Booking booking);
    }

    public TodayBookingsAdapter(List<Booking> bookings, OnBookingActionListener actionListener) {
        this.bookings = bookings != null ? bookings : new ArrayList<>();
        this.actionListener = actionListener;
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

    public void updateBookings(List<Booking> newBookings) {
        this.bookings = newBookings != null ? newBookings : new ArrayList<>();
        notifyDataSetChanged();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookingId;
        private TextView tvCustomerName;
        private TextView tvDateTime;
        private TextView tvStatus;
        private Button btnApprove;
        private Button btnCancel;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tv_booking_id);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvDateTime = itemView.findViewById(R.id.tv_booking_datetime);
            tvStatus = itemView.findViewById(R.id.tv_booking_status);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnCancel = itemView.findViewById(R.id.btn_cancel);

            itemView.setOnClickListener(v -> {
                if (actionListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        actionListener.onBookingClick(bookings.get(position));
                    }
                }
            });

            btnApprove.setOnClickListener(v -> {
                if (actionListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        actionListener.onApproveBooking(bookings.get(position));
                    }
                }
            });

            btnCancel.setOnClickListener(v -> {
                if (actionListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        actionListener.onCancelBooking(bookings.get(position));
                    }
                }
            });
        }

        public void bind(Booking booking) {
            tvBookingId.setText("Booking #" + booking.getId());
            tvCustomerName.setText(booking.getStationName()); // Using station name as customer name in mock data
            tvDateTime.setText(booking.getDateTime());
            tvStatus.setText(booking.getStatus());

            // Set status color
            int statusColor;
            switch (booking.getStatus().toLowerCase()) {
                case "approved":
                    statusColor = itemView.getContext().getResources().getColor(R.color.status_approved);
                    break;
                case "pending":
                    statusColor = itemView.getContext().getResources().getColor(R.color.status_pending);
                    break;
                case "cancelled":
                    statusColor = itemView.getContext().getResources().getColor(R.color.status_cancelled);
                    break;
                default:
                    statusColor = itemView.getContext().getResources().getColor(R.color.text_secondary);
                    break;
            }
            tvStatus.setTextColor(statusColor);

            // Show/hide action buttons based on status
            if ("pending".equalsIgnoreCase(booking.getStatus())) {
                btnApprove.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            } else if ("approved".equalsIgnoreCase(booking.getStatus())) {
                btnApprove.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText("Complete");
            } else {
                btnApprove.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }
        }
    }
}