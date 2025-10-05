import React, { useEffect, useState } from 'react';
import api from '../../../services/api';

const OperatorBookingsTable = () => {
  const [bookingList, setBookingList] = useState([]);
  const [originalList, setOriginalList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingIds, setLoadingIds] = useState([]); // tracks bookings being updated
  const [errorMessage, setErrorMessage] = useState('');
  const [stationIdToName, setStationIdToName] = useState({});

  // Fetch bookings from backend
  const fetchBooking = async () => {
    try {
      const response = await api.get("/Booking");
      console.log("Bookings from API:", response.data);

      // ✅ save data into state
      setBookingList(response.data);
      setOriginalList(response.data);
    } catch (error) {
      console.error("Failed to fetch bookings:", error);
      setErrorMessage("Failed to fetch bookings from server.");
    } finally {
      setLoading(false);
    }
  };

  const fetchStations = async () => {
    try {
      const res = await api.get('/ChargingStation');
      const map = {};
      (res.data || []).forEach(station => {
        if (station && station.id) {
          map[station.id] = station.name || station.Name || `Station ${station.id}`;
        }
      });
      setStationIdToName(map);
    } catch (err) {
      console.error('Failed to fetch stations:', err);
    }
  };

  useEffect(() => {
    fetchBooking();
    fetchStations();
  }, []);

  const handleConfirm = async (bookingId) => {
    try {
      setLoadingIds(prev => [...prev, bookingId]);
      const res = await api.put(`/Booking/${bookingId}/confirm`);
      if (res.status !== 200) {
        const msg = res.data?.error || 'Confirm failed';
        setErrorMessage(msg);
      }
      await fetchBooking();
    } catch (error) {
      const msg = error?.response?.data?.error || 'Confirm failed';
      setErrorMessage(msg);
    } finally {
      setLoadingIds(prev => prev.filter(id => id !== bookingId));
    }
  };

  const handleCancel = async (bookingId) => {
    try {
      setLoadingIds(prev => [...prev, bookingId]);
      const res = await api.put(`/Booking/${bookingId}/cancel`);
      if (res.status !== 204) {
        const msg = res.data?.error || 'Cancel failed';
        setErrorMessage(msg);
      }
      await fetchBooking();
    } catch (error) {
      const msg = error?.response?.data?.error || 'Cancel failed';
      setErrorMessage(msg);
    } finally {
      setLoadingIds(prev => prev.filter(id => id !== bookingId));
    }
  };

  if (loading) {
    return <p className="p-4">Loading bookings...</p>;
  }

  if (bookingList.length === 0) {
    return <p className="p-4">No bookings found.</p>;
  }

  return (
    <div className="overflow-x-auto p-4">
      {errorMessage && (
        <div className="mb-4 p-2 bg-red-100 text-red-700 rounded">
          {errorMessage}
        </div>
      )}
      <table className="min-w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="text-left p-3">Booking ID</th>
            <th className="text-left p-3">EV Owner NIC</th>
            <th className="text-left p-3">Station ID</th>
            <th className="text-left p-3">Date / Time</th>
            <th className="text-left p-3">Status</th>
            <th className="text-left p-3">Actions</th>
          </tr>
        </thead>
        <tbody>
          {bookingList.map((booking) => {
            const isLoading = loadingIds.includes(booking.id);
            return (
              <tr key={booking.id} className="border-b hover:bg-gray-50">
                <td className="p-3">{booking.id}</td>
                <td className="p-3">{booking.evOwnerNIC}</td>
                <td className="p-3">{stationIdToName[booking.stationId] || booking.stationId}</td>
                <td className="p-3">{new Date(booking.reservationDateTime).toLocaleString()}</td>
                <td className="p-3">{booking.status}</td>
                <td className="p-3 flex gap-2">
                  <button
                    onClick={() => handleConfirm(booking.id)}
                    disabled={booking.status !== 'Pending' || isLoading}
                    className={`px-2 py-1 rounded text-white ${
                      booking.status === 'Pending'
                        ? 'bg-green-500 hover:bg-green-600'
                        : 'bg-gray-400 cursor-not-allowed'
                    }`}
                  >
                    {isLoading && booking.status === 'Pending' ? '...' : 'Confirm'}
                  </button>
                  <button
                    onClick={() => handleCancel(booking.id)}
                    disabled={booking.status !== 'Pending' || isLoading}
                    className={`px-2 py-1 rounded text-white ${
                      booking.status === 'Pending'
                        ? 'bg-red-500 hover:bg-red-600'
                        : 'bg-gray-400 cursor-not-allowed'
                    }`}
                  >
                    {isLoading && booking.status === 'Pending' ? '...' : 'Cancel'}
                  </button>
                  
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default OperatorBookingsTable;
