import React, { useEffect, useState } from "react";
import Card from '../../components/common/Card';
import axios from "axios";
import Map from '../../components/maps/Map'; // make sure this path points to your Map component
import { LoadScript } from "@react-google-maps/api";

const DashboardBackoffice = () => {
  const [dashboard, setDashboard] = useState({
    totalUsers: 0,
    totalEVOwners: 0,
    activeStations: 0,
    pendingBookings: 0
  });

  const [stations, setStations] = useState([]);

  const googleMapsApiKey = process.env.REACT_APP_GOOGLE_MAPS_API_KEY;

  const apiBaseUsers = 'http://localhost:5263/api/User';
  const apiBaseOwners = 'http://localhost:5263/api/EVOwner';
  const apiBaseStations = 'http://localhost:5263/api/ChargingStation';
  const apiBaseBookings = 'http://localhost:5263/api/Booking';

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [usersRes, ownersRes, stationsRes, bookingsRes] = await Promise.all([
          axios.get(apiBaseUsers),
          axios.get(apiBaseOwners),
          axios.get(apiBaseStations),
          axios.get(apiBaseBookings),
        ]);

        console.log("Users:", usersRes.data);
        console.log("EV Owners:", ownersRes.data);
        console.log("Stations:", stationsRes.data);
        console.log("Bookings:", bookingsRes.data);

        setDashboard({
          totalUsers: usersRes.data.length,
          totalEVOwners: ownersRes.data.length,
          activeStations: stationsRes.data.filter(s => s.status === "Active").length,
          pendingBookings: bookingsRes.data.filter(b => b.status === "Pending").length,
        });

        setStations(stationsRes.data); // store stations for map
      } catch (error) {
        console.error("Failed to fetch dashboard data", error);
      }
    };

    fetchDashboardData();
  }, []);

  return (

    
    <div className="space-y-6">

      <div className="h-96 w-full">
        <LoadScript googleMapsApiKey={googleMapsApiKey}>
      <Map stations={stations} />
    </LoadScript>
      </div>
     <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
  <Card title="Total Users" value={dashboard.totalUsers} type="users" trend={5} />
  <Card title="Total EV Owners" value={dashboard.totalEVOwners} type="owners" trend={8} />
  <Card title="Active Stations" value={dashboard.activeStations} type="stations" trend={-2} />
  <Card title="Pending Bookings" value={dashboard.pendingBookings} type="bookings" trend={3} />
</div>


      
    </div>
  );
};

export default DashboardBackoffice;
