import React, { useEffect, useState } from "react";
import Map from '../../components/maps/Map';
import Card from '../../components/common/Card';
import api from "../../services/api";

const CommonDashboard = ({ role }) => {
  const [stations, setStations] = useState([]);
  const [dashboard, setDashboard] = useState({
    totalUsers: 0,
    totalEVOwners: 0,
    activeStations: 0,
    pendingBookings: 0,
    pendingReservations: 0,
    approvedReservations: 0
  });

  const apiBaseUsers = '/User';
  const apiBaseOwners = '/EVOwner';
  const apiBaseStations = '/ChargingStation';
  const apiBaseBookings = '/Booking';

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [stationsRes, bookingsRes] = await Promise.all([
          api.get(apiBaseStations),
          api.get(apiBaseBookings),
        ]);

        const activeStations = stationsRes.data.filter(s => s.status === "Active");
        setStations(activeStations);

        if (role === 'Backoffice') {
          const [usersRes, ownersRes] = await Promise.all([
            api.get(apiBaseUsers),
            api.get(apiBaseOwners),
          ]);

          setDashboard({
            totalUsers: usersRes.data.length,
            totalEVOwners: ownersRes.data.length,
            activeStations: activeStations.length,
            pendingBookings: bookingsRes.data.filter(b => b.status === "Pending").length,
          });
        } else if (role === 'Operator') {
          setDashboard({
            pendingReservations: bookingsRes.data.filter(b => b.status === "Pending").length,
            approvedReservations: bookingsRes.data.filter(b => b.status === "Approved").length,
          });
        }

      } catch (error) {
        console.error("Failed to fetch dashboard data:", error);
      }
    };

    fetchData();
  }, [role]);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">{role} Dashboard</h1>

      {/* Google Map */}
      <div className="mb-6 h-96">
        <Map stations={stations} />
      </div>

      {/* Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {role === 'Backoffice' && (
          <>
            <Card title="Total Users" value={dashboard.totalUsers} color="bg-blue-100" />
            <Card title="Total EV Owners" value={dashboard.totalEVOwners} color="bg-green-100" />
            <Card title="Active Stations" value={dashboard.activeStations} color="bg-yellow-100" />
            <Card title="Pending Bookings" value={dashboard.pendingBookings} color="bg-red-100" />
          </>
        )}

        {role === 'Operator' && (
          <>
            <Card title="Pending Reservations" value={dashboard.pendingReservations} color="bg-red-100" />
            <Card title="Approved Reservations" value={dashboard.approvedReservations} color="bg-green-100" />
            <Card title="Nearby Stations" value={stations.length} color="bg-blue-100" />
          </>
        )}
      </div>
    </div>
  );
};

export default CommonDashboard;
