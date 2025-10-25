import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import EVOwnersTable from '../../components/tables/backofficeTables/EVOwnersTable';
import EVOwnerForm from '../../components/forms/backofficeForms/EVOwnerForm';

const EVOwnersManagement = () => {
  const [evOwners, setEvOwners] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingOwner, setEditingOwner] = useState(null);
  const [viewMode, setViewMode] = useState('table'); // 'table' or 'card'

  // Fetch EV Owners from backend
  useEffect(() => {
    fetchEVOwners();
  }, []);

  const fetchEVOwners = async () => {
    try {
      setLoading(true);
      const response = await api.get('/EVOwner');
      setEvOwners(response.data);
    } catch (error) {
      console.error('Error fetching EV owners:', error);
      alert('Failed to load EV owners.');
    } finally {
      setLoading(false);
    }
  };

  const handleAddClick = () => {
    setEditingOwner(null);
    setIsFormOpen(true);
  };

  const handleEditClick = (owner) => {
    setEditingOwner(owner);
    setIsFormOpen(true);
  };

  const handleFormSubmit = async (data) => {
    try {
      if (editingOwner) {
        await api.put(`/EVOwner/${editingOwner.nic}`, data);
      } else {
        await api.post('/EVOwner', data);
      }
      fetchEVOwners(); // refresh the list
      setIsFormOpen(false);
    } catch (error) {
      console.error('Error saving EV owner:', error);
      alert('Failed to save EV owner.');
    }
  };

  const handleDelete = async (nic) => {
    if (!window.confirm('Are you sure you want to delete this EV owner?')) return;
    try {
      await api.delete(`/EVOwner/${nic}`);
      fetchEVOwners();
    } catch (error) {
      console.error('Error deleting EV owner:', error);
      alert('Failed to delete EV owner.');
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">EV Owners Management</h1>
        <div className="flex items-center gap-2">
          <button
            onClick={() => setViewMode(viewMode === 'table' ? 'card' : 'table')}
            className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition"
          >
            {viewMode === 'table' ? 'Card View' : 'Table View'}
          </button>
          <button
            onClick={handleAddClick}
            className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600 transition"
          >
            Add EV Owner
          </button>
        </div>
      </div>

      {isFormOpen && (
        <EVOwnerForm
          initialData={editingOwner}
          onSubmit={handleFormSubmit}
          onCancel={() => setIsFormOpen(false)}
        />
      )}

      {loading ? (
        <p>Loading EV owners...</p>
      ) : viewMode === 'table' ? (
        <EVOwnersTable
          evOwners={evOwners}
          onEdit={handleEditClick}
          onDelete={handleDelete}
        />
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {evOwners.map((owner) => (
            <div
              key={owner.nic}
              className="bg-white shadow-lg rounded-lg p-4 hover:shadow-xl transition transform hover:-translate-y-1"
            >
              <h2 className="text-xl font-bold">{owner.name}</h2>
              <p className="text-gray-600 mt-1">NIC: {owner.nic}</p>
              <p className="text-gray-600 mt-1">Email: {owner.email}</p>
              <p className="text-gray-600 mt-1">Phone: {owner.phone}</p>
              <div className="mt-4 flex justify-end gap-2">
                <button
                  onClick={() => handleEditClick(owner)}
                  className="px-3 py-1 bg-yellow-400 text-white rounded hover:bg-yellow-500 transition"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(owner.nic)}
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

export default EVOwnersManagement;
