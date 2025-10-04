package com.evcharging.mobile.ui.stations.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.evcharging.mobile.R;
import com.evcharging.mobile.ui.stations.model.Station;
import java.util.ArrayList;
import java.util.List;

/**
 * Station Selection Adapter
 * 
 * Adapter for selecting charging stations in the booking process.
 */
public class StationSelectionAdapter extends RecyclerView.Adapter<StationSelectionAdapter.StationViewHolder> {

    private List<Station> stations;
    private OnStationSelectListener selectListener;

    public interface OnStationSelectListener {
        void onStationSelected(Station station);
    }

    public StationSelectionAdapter(List<Station> stations, OnStationSelectListener selectListener) {
        this.stations = stations != null ? stations : new ArrayList<>();
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station_selection, parent, false);
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
        private TextView tvStationName;
        private TextView tvStationType;
        private TextView tvStationAddress;
        private TextView tvSlots;
        private TextView tvStatus;
        private Button btnSelect;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStationName = itemView.findViewById(R.id.tv_station_name);
            tvStationType = itemView.findViewById(R.id.tv_station_type);
            tvStationAddress = itemView.findViewById(R.id.tv_station_address);
            tvSlots = itemView.findViewById(R.id.tv_station_slots);
            tvStatus = itemView.findViewById(R.id.tv_station_status);
            btnSelect = itemView.findViewById(R.id.btn_select_station);

            btnSelect.setOnClickListener(v -> {
                if (selectListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        selectListener.onStationSelected(stations.get(position));
                    }
                }
            });
        }

        public void bind(Station station) {
            tvStationName.setText(station.getName());
            tvStationType.setText(station.getType());
            tvStationAddress.setText(station.getAddress());
            tvSlots.setText(station.getAvailableSlots() + "/" + station.getTotalSlots() + " slots available");
            tvStatus.setText(station.getStatus());

            // Set status color
            int statusColor;
            boolean isAvailable = station.getAvailableSlots() > 0;
            
            switch (station.getStatus().toLowerCase()) {
                case "operational":
                    statusColor = isAvailable ? 
                        itemView.getContext().getResources().getColor(R.color.status_approved) :
                        itemView.getContext().getResources().getColor(R.color.status_pending);
                    break;
                case "maintenance":
                    statusColor = itemView.getContext().getResources().getColor(R.color.status_cancelled);
                    break;
                default:
                    statusColor = itemView.getContext().getResources().getColor(R.color.text_secondary);
                    break;
            }
            tvStatus.setTextColor(statusColor);

            // Enable/disable select button based on availability
            btnSelect.setEnabled(isAvailable && "operational".equalsIgnoreCase(station.getStatus()));
            btnSelect.setAlpha(isAvailable && "operational".equalsIgnoreCase(station.getStatus()) ? 1.0f : 0.5f);
            
            if (isAvailable && "operational".equalsIgnoreCase(station.getStatus())) {
                btnSelect.setText("Select");
            } else if ("maintenance".equalsIgnoreCase(station.getStatus())) {
                btnSelect.setText("Under Maintenance");
            } else {
                btnSelect.setText("No Slots Available");
            }
        }
    }
}
