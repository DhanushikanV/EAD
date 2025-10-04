package com.evcharging.mobile.ui.stations.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evcharging.mobile.R;
import com.evcharging.mobile.network.models.ChargingStation;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

/**
 * StationsAdapter
 * 
 * This adapter displays charging stations in a RecyclerView.
 * It handles station cards with information and click events.
 * 
 * @author EV Charging Mobile Team
 * @version 1.0
 */
public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationViewHolder> {

    private List<ChargingStation> stations = new ArrayList<>();
    private OnStationClickListener onStationClickListener;

    /**
     * Interface for station click events
     */
    public interface OnStationClickListener {
        void onStationClick(ChargingStation station);
    }

    /**
     * Constructor
     * 
     * @param onStationClickListener Click listener for station items
     */
    public StationsAdapter(OnStationClickListener onStationClickListener) {
        this.onStationClickListener = onStationClickListener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        ChargingStation station = stations.get(position);
        holder.bind(station);
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    /**
     * Update stations list
     * 
     * @param stations New list of stations
     */
    public void updateStations(List<ChargingStation> stations) {
        this.stations = stations != null ? stations : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for station items
     */
    class StationViewHolder extends RecyclerView.ViewHolder {
        
        private TextView tvStationName;
        private TextView tvStationLocation;
        private Chip chipStationType;
        private TextView tvAvailableSlots;
        private TextView tvTotalSlots;
        private ImageView ivStationIcon;
        private View cardView;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStationName = itemView.findViewById(R.id.tv_station_name);
            tvStationLocation = itemView.findViewById(R.id.tv_station_location);
            chipStationType = itemView.findViewById(R.id.chip_station_type);
            tvAvailableSlots = itemView.findViewById(R.id.tv_available_slots);
            tvTotalSlots = itemView.findViewById(R.id.tv_total_slots);
            ivStationIcon = itemView.findViewById(R.id.iv_station_icon);
            cardView = itemView.findViewById(R.id.card_station);
            
            // Set click listener
            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onStationClickListener != null) {
                    onStationClickListener.onStationClick(stations.get(position));
                }
            });
        }

        /**
         * Bind station data to view
         * 
         * @param station Charging station data
         */
        public void bind(ChargingStation station) {
            tvStationName.setText(station.getName());
            tvStationLocation.setText(station.getLocation());
            
            // Set station type chip
            chipStationType.setText(station.getType());
            if ("AC".equals(station.getType())) {
                chipStationType.setChipBackgroundColorResource(R.color.ac_charging_color);
            } else {
                chipStationType.setChipBackgroundColorResource(R.color.dc_charging_color);
            }
            
            // Set slot information
            tvAvailableSlots.setText(String.valueOf(station.getAvailableSlots()));
            tvTotalSlots.setText(String.valueOf(station.getTotalSlots()));
            
            // Set station icon based on type
            if ("AC".equals(station.getType())) {
                ivStationIcon.setImageResource(R.drawable.ic_ac_charging);
            } else {
                ivStationIcon.setImageResource(R.drawable.ic_dc_charging);
            }
            
            // Set availability indicator
            if (station.getAvailableSlots() > 0) {
                tvAvailableSlots.setTextColor(itemView.getContext().getColor(R.color.status_approved));
            } else {
                tvAvailableSlots.setTextColor(itemView.getContext().getColor(R.color.status_cancelled));
            }
        }
    }
}
