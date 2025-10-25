import React, { useState, useEffect } from "react";
import api from "../../../services/api";

const OperatorStationsTable = () => {
  const [stationList, setStationList] = useState([]);
  const [editingStation, setEditingStation] = useState(null);
  const [newSlotValue, setNewSlotValue] = useState(0);

  // Fetch stations when component mounts
  useEffect(() => {
    fetchStations();
  }, []);

  const fetchStations = async () => {
    try {
      const response = await api.get("/ChargingStation");
      setStationList(response.data);
    } catch (error) {
      console.error("Error fetching stations:", error);
      alert("Failed to load stations. Please check backend.");
    }
  };

  const handleEditClick = (station) => {
    setEditingStation(station);
    setNewSlotValue(station.availableSlots);
  };

const handleSave = async () => {
  if (newSlotValue < 0) return alert("Slots cannot be negative");

  const stationToUpdate = editingStation; // copy
  if (!stationToUpdate) return;

  if (newSlotValue > stationToUpdate.totalSlots) {
    return alert(`Slots cannot exceed total slots (${stationToUpdate.totalSlots})`);
  }

  try {
    await api.put(
      `/ChargingStation/${stationToUpdate.id}`,
      { ...stationToUpdate, availableSlots: Number(newSlotValue) }
    );

    // Refresh data from backend
    await fetchStations();

    setEditingStation(null);
  } catch (error) {
    console.error("Error updating slots:", error);
    alert("Failed to update slots.");
  }
};


  return (
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="text-left p-3">Station Name</th>
            <th className="text-left p-3">Location</th>
            <th className="text-left p-3">Type</th>
            <th className="text-left p-3">Total Slots</th>
            <th className="text-left p-3">Available Slots</th>
            <th className="text-left p-3">Status</th>
            <th className="text-left p-3">Actions</th>
          </tr>
        </thead>
        <tbody>
          {stationList.map((station) => (
            <tr key={station.id} className="border-b hover:bg-gray-50">
              <td className="p-3">{station.name}</td>
              <td className="p-3">{station.location}</td>
              <td className="p-3">{station.type}</td>
              <td className="p-3">{station.totalSlots}</td>
              <td className="p-3">{station.availableSlots}</td>
              <td className="p-3">{station.status}</td>
              <td className="p-3">
                {editingStation?.id === station.id ? (
                  <>
                    <input
                      type="number"
                      value={newSlotValue}
                      onChange={(e) => setNewSlotValue(e.target.value)}
                      className="border px-2 py-1 w-16"
                    />
                    <button
                      onClick={handleSave}
                      className="ml-2 bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600"
                    >
                      Save
                    </button>
                    <button
                      onClick={() => setEditingStation(null)}
                      className="ml-2 bg-gray-300 px-2 py-1 rounded hover:bg-gray-400"
                    >
                      Cancel
                    </button>
                  </>
                ) : (
                  <button
                    onClick={() => handleEditClick(station)}
                    className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
                  >
                    Update Slots
                  </button>
                )}
              </td>
            </tr>
          ))}
          {stationList.length === 0 && (
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

export default OperatorStationsTable;
