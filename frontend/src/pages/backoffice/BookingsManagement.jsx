import React, { useState, useEffect } from "react";
import axios from "axios";
import BookingsTable from "../../components/tables/backofficeTables/BookingTable";

const BookingsManagement = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingBookingId, setEditingBookingId] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState("");
  const [viewMode, setViewMode] = useState("table"); // toggle between 'table' and 'card'

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      setLoading(true);
      const response = await axios.get("http://localhost:5263/api/Booking");
      setBookings(response.data);
    } catch (error) {
      console.error("Error fetching bookings:", error);
      alert("Failed to load bookings. Please check backend.");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateStatus = async (booking, newStatus) => {
    try {
      await axios.put(`http://localhost:5263/api/Booking/${booking.id}`, {
        ...booking,
        status: newStatus,
      });
      fetchBookings();
      setEditingBookingId(null);
    } catch (error) {
      console.error("Error updating booking:", error);
      alert("Failed to update booking status.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this booking?")) return;
    try {
      await axios.delete(`http://localhost:5263/api/Booking/${id}`);
      fetchBookings();
    } catch (error) {
      console.error("Error deleting booking:", error);
      alert("Failed to delete booking.");
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Bookings Management</h1>
        <button
          onClick={() => setViewMode(viewMode === "table" ? "card" : "table")}
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
        >
          {viewMode === "table" ? "Card View" : "Table View"}
        </button>
      </div>

      {loading ? (
        <p>Loading bookings...</p>
      ) : viewMode === "table" ? (
        <BookingsTable
          bookings={bookings}
          onEdit={(booking) => {
            setEditingBookingId(booking.id);
            setSelectedStatus(booking.status);
          }}
          onDelete={(booking) => handleDelete(booking.id)}
          editingBookingId={editingBookingId}
          selectedStatus={selectedStatus}
          setSelectedStatus={setSelectedStatus}
          handleUpdateStatus={handleUpdateStatus}
          setEditingBookingId={setEditingBookingId}
        />
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {bookings.map((booking) => (
            <div
              key={booking.id}
              className="bg-white shadow-lg rounded-lg p-4 hover:shadow-xl transition transform hover:-translate-y-1"
            >
              <h2 className="text-lg font-bold text-gray-800 mb-2">
                Booking ID: {booking.id}
              </h2>
              <p className="text-gray-600">User: {booking.userId}</p>
              <p className="text-gray-600">Station: {booking.stationId}</p>
              <p className="text-gray-600">Date: {new Date(booking.date).toLocaleString()}</p>
              <p className="text-gray-600 font-semibold">
                Status:{" "}
                <span
                  className={`px-2 py-1 rounded text-white ${
                    booking.status === "Pending"
                      ? "bg-yellow-500"
                      : booking.status === "Completed"
                      ? "bg-green-500"
                      : "bg-gray-500"
                  }`}
                >
                  {booking.status}
                </span>
              </p>

              <div className="mt-4 flex justify-end gap-2">
                <select
                  value={editingBookingId === booking.id ? selectedStatus : booking.status}
                  onChange={(e) => {
                    setEditingBookingId(booking.id);
                    setSelectedStatus(e.target.value);
                  }}
                  className="border px-2 py-1 rounded"
                >
                  <option value="Pending">Pending</option>
                  <option value="Completed">Completed</option>
                  <option value="Cancelled">Cancelled</option>
                </select>

                <button
                  onClick={() => handleUpdateStatus(booking, selectedStatus)}
                  className="px-3 py-1 bg-yellow-400 text-white rounded hover:bg-yellow-500 transition"
                >
                  Update
                </button>

                <button
                  onClick={() => handleDelete(booking.id)}
                  className="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600 transition"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default BookingsManagement;
