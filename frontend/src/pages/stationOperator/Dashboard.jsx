import React, { useState, useEffect } from "react";
import Map from '../../components/maps/Map'; // Your Google Map component
import Card from '../../components/common/Card';
import api from "../../services/api";

const DashboardOperator = () => {
  const [stations, setStations] = useState([]);
  const [pendingReservations, setPendingReservations] = useState(0);
  const [approvedReservations, setApprovedReservations] = useState(0);

  const apiStations = '/ChargingStation';
  const apiBookings = '/Booking';

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [stationsRes, bookingsRes] = await Promise.all([
          api.get(apiStations),
          api.get(apiBookings),
        ]);

        // Only active stations for map
        const activeStations = stationsRes.data.filter(s => s.status === "Active");
        setStations(activeStations);

        // Count bookings
        setPendingReservations(bookingsRes.data.filter(b => b.status === "Pending").length);
        setApprovedReservations(bookingsRes.data.filter(b => b.status === "Approved").length);

      } catch (error) {
        console.error("Failed to fetch operator dashboard data:", error);
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Operator Dashboard</h1>

      {/* Google Map */}
      <div className="mb-6 h-96">
        <Map stations={stations} />
      </div>

      {/* Cards */}
      {/* <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card title="Pending Reservations" value={pendingReservations} color="bg-red-100" />
        <Card title="Approved Reservations" value={approvedReservations} color="bg-green-100" />
        <Card title="Nearby Stations" value={stations.length} color="bg-blue-100" />
      </div> */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
  <Card title="Pending Reservations" value={pendingReservations} type="bookings" />
  <Card title="Approved Reservations" value={approvedReservations} type="owners" />
  <Card title="Nearby Stations" value={stations.length} type="stations" />
</div>


    </div>
  );
};

export default DashboardOperator;
