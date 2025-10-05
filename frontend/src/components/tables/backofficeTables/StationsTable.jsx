import React from 'react';

const StationsTable = ({ stations, onEdit, onDelete }) => {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="text-left p-3">Name</th>
            <th className="text-left p-3">Location</th>
            <th className="text-left p-3">Type</th>
            <th className="text-left p-3">Available Slots</th>
            <th className="text-left p-3">Total Slots</th>
            <th className="text-left p-3">Status</th>
            <th className="text-left p-3">Actions</th>
          </tr>
        </thead>
        <tbody>
          {stations.map((station) => (
            <tr key={station.id} className="border-b hover:bg-gray-50">
              <td className="p-3">{station.name}</td>
              <td className="p-3">{station.location}</td>
              <td className="p-3">{station.type}</td>
              <td className="p-3">{station.availableSlots}</td>
              <td className="p-3">{station.totalSlots}</td>
              <td className="p-3">{station.status}</td>
              <td className="p-3 flex gap-2">
                <button
                  onClick={() => onEdit(station)}
                  className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
                >
                  Edit
                </button>
                <button
                  onClick={() => onDelete(station.id)}
                  className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
          {stations.length === 0 && (
            <tr>
              <td colSpan="7" className="p-3 text-center text-gray-500">
                No stations found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default StationsTable;
