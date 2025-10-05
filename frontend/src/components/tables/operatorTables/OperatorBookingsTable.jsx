import React, { useEffect, useState } from 'react';
import axios from 'axios';

const OperatorBookingsTable = () => {
  const [bookingList, setBookingList] = useState([]);
  const [originalList, setOriginalList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingIds, setLoadingIds] = useState([]); // tracks bookings being updated
  const [errorMessage, setErrorMessage] = useState('');

  // Fetch bookings from backend
  const fetchBooking = async () => {
    try {
      const response = await axios.get("http://localhost:5263/api/Booking");
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

  useEffect(() => {
    fetchBooking();
  }, []);

  const updateBookingStatus = async (bookingId, status) => {
    try {
      const booking = bookingList.find(b => b.id === bookingId);
      if (!booking) return;

      setLoadingIds(prev => [...prev, bookingId]);

      const updatedBooking = { ...booking, status };
      await axios.put(`http://localhost:5263/api/Booking/${bookingId}`, updatedBooking);

      setBookingList(prev => prev.map(b => (b.id === bookingId ? updatedBooking : b)));
    } catch (error) {
      console.error(`Error updating booking ${status}:`, error);
      setErrorMessage(`Failed to ${status.toLowerCase()} booking.`);
    } finally {
      setLoadingIds(prev => prev.filter(id => id !== bookingId));
    }
  };

  const handleConfirm = bookingId => updateBookingStatus(bookingId, 'Confirmed');
  const handleCancel = bookingId => updateBookingStatus(bookingId, 'Cancelled');
  const handleReset = async bookingId => {
    try {
      const originalBooking = originalList.find(b => b.id === bookingId);
      if (!originalBooking) return;

      setLoadingIds(prev => [...prev, bookingId]);
      await axios.put(`http://localhost:5263/api/Booking/${bookingId}`, originalBooking);
      setBookingList(prev => prev.map(b => (b.id === bookingId ? originalBooking : b)));
    } catch (error) {
      console.error('Error resetting booking:', error);
      setErrorMessage('Failed to reset booking.');
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
                <td className="p-3">{booking.eVOwnerNIC}</td>
                <td className="p-3">{booking.stationId}</td>
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
                  <button
                    onClick={() => handleReset(booking.id)}
                    disabled={isLoading}
                    className="px-2 py-1 rounded bg-gray-600 text-white hover:bg-gray-700"
                  >
                    {isLoading ? '...' : 'Reset'}
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
