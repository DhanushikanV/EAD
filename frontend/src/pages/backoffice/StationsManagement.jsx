import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import StationForm from '../../components/forms/backofficeForms/StationForm';
import StationsTable from '../../components/tables/backofficeTables/StationsTable';

const StationsManagement = () => {
  const [stations, setStations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingStation, setEditingStation] = useState(null);
  const [viewMode, setViewMode] = useState("card"); // toggle state: 'card' | 'table'

  const apiBase = '/ChargingStation';

  const fetchStations = async () => {
    try {
      setLoading(true);
      const response = await api.get(apiBase);
      setStations(response.data);
    } catch (error) {
      console.error('Error fetching stations:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStations();
  }, []);

  const handleAddClick = () => {
    setEditingStation(null);
    setIsFormOpen(true);
  };

  const handleEditClick = (station) => {
    setEditingStation(station);
    setIsFormOpen(true);
  };

  const handleFormSubmit = async (data) => {
    try {
      if (editingStation) {
        await api.put(`${apiBase}/${editingStation.id}`, data);
        setStations(prev =>
          prev.map(s => (s.id === editingStation.id ? { ...s, ...data } : s))
        );
      } else {
        const response = await api.post(apiBase, data);
        setStations(prev => [...prev, response.data]);
      }
      setIsFormOpen(false);
    } catch (error) {
      console.error('Error saving station:', error);
      alert('Failed to save station.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this station?')) return;
    try {
      await api.delete(`${apiBase}/${id}`);
      setStations(stations.filter((s) => s.id !== id));
    } catch (error) {
      console.error('Error deleting station:', error);
      alert('Failed to delete station.');
    }
  };

  const handleFormCancel = () => setIsFormOpen(false);

  const statusColor = (status) => {
    switch (status) {
      case 'Active':
        return 'bg-green-200 text-green-800';
      case 'Inactive':
        return 'bg-red-200 text-red-800';
      default:
        return 'bg-gray-200 text-gray-800';
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Stations Management</h1>
        <div className="flex gap-2">
          <button
            onClick={handleAddClick}
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
          >
            Add Station
          </button>
          <button
            onClick={() => setViewMode(viewMode === "card" ? "table" : "card")}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
          >
            {viewMode === "card" ? "Table View" : "Card View"}
          </button>
        </div>
      </div>

      {isFormOpen && (
        <StationForm
          initialData={editingStation}
          onSubmit={handleFormSubmit}
          onCancel={handleFormCancel}
        />
      )}

      {loading ? (
        <p>Loading stations...</p>
      ) : viewMode === "table" ? (
        <StationsTable
          stations={stations}
          onEdit={handleEditClick}
          onDelete={handleDelete}
        />
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {stations.map((station) => (
            <div
              key={station.id}
              className="bg-white shadow-lg rounded-lg p-4 hover:shadow-xl transition transform hover:-translate-y-1"
            >
              <h2 className="text-xl font-bold">{station.name}</h2>
              <p className="text-gray-500">{station.location}</p>
              {station.numberOfChargers && (
                <p className="text-gray-600 mt-1">
                  Chargers: {station.numberOfChargers}
                </p>
              )}
              <div className="flex items-center justify-between mt-3">
                <span
                  className={`px-3 py-1 rounded-full text-sm font-semibold ${statusColor(
                    station.status
                  )}`}
                >
                  {station.status}
                </span>
              </div>
              <div className="mt-4 flex justify-end gap-2">
                <button
                  onClick={() => handleEditClick(station)}
                  className="px-3 py-1 bg-yellow-400 text-white rounded hover:bg-yellow-500 transition"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(station.id)}
                  className="px-3 py-1 bg-red-400 text-white rounded hover:bg-red-500 transition"
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

export default StationsManagement;
