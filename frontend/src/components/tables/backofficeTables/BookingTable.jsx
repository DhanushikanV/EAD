import React from "react";

const BookingsTable = ({
  bookings,
  stationIdToName = {},
  onEdit,
  onDelete,
  editingBookingId,
  selectedStatus,
  setSelectedStatus,
  handleUpdateStatus,
  setEditingBookingId, // pass this from parent
}) => {
  const statusOptions = ["Pending", "Confirmed", "Cancelled"];

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="text-left p-3">EV NIC</th>
            <th className="text-left p-3">Station</th>
            <th className="text-left p-3">Date</th>
            <th className="text-left p-3">Time</th>
            <th className="text-left p-3">Status</th>
            <th className="text-left p-3">Actions</th>
          </tr>
        </thead>
        <tbody>
          {bookings.map((b) => {
            const reservationDate = new Date(b.reservationDateTime);
            const dateStr = reservationDate.toLocaleDateString();
            const timeStr = reservationDate.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });

            return (
              <tr key={b.id} className="border-b hover:bg-gray-50">
                <td className="p-3">{b.evOwnerNIC}</td>
                <td className="p-3">{stationIdToName[b.stationId] || b.stationId}</td>
                <td className="p-3">{dateStr}</td>
                <td className="p-3">{timeStr}</td>
                <td className="p-3">
                  {editingBookingId === b.id ? (
                    <select
                      value={selectedStatus}
                      onChange={(e) => setSelectedStatus(e.target.value)}
                      className="border px-2 py-1 rounded"
                    >
                      {statusOptions.map((status) => (
                        <option key={status} value={status}>
                          {status}
                        </option>
                      ))}
                    </select>
                  ) : (
                    b.status
                  )}
                </td>
                <td className="p-3 flex gap-2">
                  {editingBookingId === b.id ? (
                    <>
                      <button
                        onClick={() => handleUpdateStatus(b, selectedStatus)}
                        className="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600"
                      >
                        Save
                      </button>
                      <button
                        onClick={() => {
                          setSelectedStatus("");
                          setEditingBookingId(null);
                        }}
                        className="bg-gray-300 px-2 py-1 rounded hover:bg-gray-400"
                      >
                        Cancel
                      </button>
                    </>
                  ) : (
                    <button
                      onClick={() => onEdit(b)}
                      className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
                    >
                      Edit
                    </button>
                  )}

                  <button
                    onClick={() => onDelete(b)}
                    className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            );
          })}
          {bookings.length === 0 && (
            <tr>
              <td colSpan="6" className="p-3 text-center text-gray-500">
                No bookings found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default BookingsTable;
