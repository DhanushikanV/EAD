package com.evcharging.mobile.ui.stations.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.stations.model.Station;
import java.util.ArrayList;
import java.util.List;

/**
 * Stations Adapter
 * 
 * RecyclerView adapter for displaying charging stations.
 */
public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationViewHolder> {

    private List<Station> stations;
    private OnStationClickListener clickListener;

    public interface OnStationClickListener {
        void onStationClick(Station station);
    }

    public StationsAdapter(List<Station> stations, OnStationClickListener clickListener) {
        this.stations = stations != null ? stations : new ArrayList<>();
        this.clickListener = clickListener;
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
        Station station = stations.get(position);
        holder.bind(station);
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public void updateStations(List<Station> newStations) {
        this.stations = newStations != null ? newStations : new ArrayList<>();
        notifyDataSetChanged();
    }

    class StationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvType;
        private TextView tvAddress;
        private TextView tvSlots;
        private TextView tvStatus;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_station_name);
            tvType = itemView.findViewById(R.id.tv_station_type);
            tvAddress = itemView.findViewById(R.id.tv_station_address);
            tvSlots = itemView.findViewById(R.id.tv_station_slots);
            tvStatus = itemView.findViewById(R.id.tv_station_status);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onStationClick(stations.get(position));
                    }
                }
            });
        }

        public void bind(Station station) {
            tvName.setText(station.getName());
            tvType.setText(station.getType());
            tvAddress.setText(station.getAddress());
            tvSlots.setText(station.getAvailableSlots() + "/" + station.getTotalSlots() + " slots available");
            tvStatus.setText(station.getStatus());

            // Set status color
            int statusColor;
            switch (station.getStatus().toLowerCase()) {
                case "operational":
                case "active":
                    statusColor = itemView.getContext().getResources().getColor(R.color.status_approved);
                    break;
                case "maintenance":
                case "inactive":
                    statusColor = itemView.getContext().getResources().getColor(R.color.status_pending);
                    break;
                default:
                    statusColor = itemView.getContext().getResources().getColor(R.color.text_secondary);
                    break;
            }
            tvStatus.setTextColor(statusColor);
        }
    }
}