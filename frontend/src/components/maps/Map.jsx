import React from "react";
import { GoogleMap, Marker } from "@react-google-maps/api";

const containerStyle = {
  width: "100%",
  height: "400px",
};

const Map = ({ stations }) => {
  const center = stations.length
    ? { lat: stations[0].latitude, lng: stations[0].longitude }
    : { lat: 6.9271, lng: 79.8612 }; // fallback

  return (
    <GoogleMap mapContainerStyle={containerStyle} center={center} zoom={12}>
      {stations.map((station) => (
        <Marker
          key={station.id}
          position={{ lat: station.latitude, lng: station.longitude }}
          title={station.name}
        />
      ))}
    </GoogleMap>
  );
};

export default Map;
