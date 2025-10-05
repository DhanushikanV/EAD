import React, { useState, useEffect } from 'react';
import { GoogleMap, Marker, LoadScript } from '@react-google-maps/api';

const StationForm = ({ initialData = null, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    name: '',
    location: '',
    type: 'AC',        
    totalSlots: 0,
    availableSlots: 0,
    status: 'Active',
    latitude: 0,
    longitude: 0,
  });

  const [useMap, setUseMap] = useState(false);

  useEffect(() => {
    if (initialData) {
      setFormData({
        ...initialData,
        totalSlots: initialData.totalSlots || 0,
        availableSlots: initialData.availableSlots || 0,
        latitude: initialData.latitude || 0,
        longitude: initialData.longitude || 0,
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: ['totalSlots', 'availableSlots', 'latitude', 'longitude'].includes(name) ? parseFloat(value) : value
    }));
  };

  const handleMapClick = (e) => {
    setFormData(prev => ({
      ...prev,
      latitude: e.latLng.lat(),
      longitude: e.latLng.lng()
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (formData.availableSlots > formData.totalSlots) {
      return alert('Available slots cannot exceed total slots');
    }
    if (!formData.latitude || !formData.longitude) {
      return alert('Latitude and Longitude are required');
    }
    onSubmit(formData);
  };

  const googleMapsApiKey = process.env.REACT_APP_GOOGLE_MAPS_API_KEY;

  return (
    <div className="bg-white shadow rounded p-6 max-w-md">
      <h2 className="text-xl font-bold mb-4">{initialData ? 'Edit Station' : 'Add Station'}</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block mb-1 font-medium">Station Name</label>
          <input type="text" name="name" value={formData.name} onChange={handleChange} className="w-full border rounded p-2" required />
        </div>

        <div>
          <label className="block mb-1 font-medium">Location</label>
          <input type="text" name="location" value={formData.location} onChange={handleChange} className="w-full border rounded p-2" required />
        </div>

        <div>
          <label className="block mb-1 font-medium">Type</label>
          <select name="type" value={formData.type} onChange={handleChange} className="w-full border rounded p-2">
            <option value="AC">AC</option>
            <option value="DC">DC</option>
          </select>
        </div>

        <div className="flex gap-2">
          <div className="flex-1">
            <label className="block mb-1 font-medium">Total Slots</label>
            <input type="number" name="totalSlots" value={formData.totalSlots} onChange={handleChange} className="w-full border rounded p-2" min="0" required />
          </div>
          <div className="flex-1">
            <label className="block mb-1 font-medium">Available Slots</label>
            <input type="number" name="availableSlots" value={formData.availableSlots} onChange={handleChange} className="w-full border rounded p-2" min="0" required />
          </div>
        </div>

        <div>
          <label className="block mb-1 font-medium">Status</label>
          <select name="status" value={formData.status} onChange={handleChange} className="w-full border rounded p-2">
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
          </select>
        </div>

        {/* Latitude & Longitude Inputs */}
        {!useMap && (
          <div className="grid grid-cols-2 gap-2">
            <div>
              <label className="block mb-1 font-medium">Latitude</label>
              <input type="number" step="0.000001" name="latitude" value={formData.latitude} onChange={handleChange} className="w-full border rounded p-2" required />
            </div>
            <div>
              <label className="block mb-1 font-medium">Longitude</label>
              <input type="number" step="0.000001" name="longitude" value={formData.longitude} onChange={handleChange} className="w-full border rounded p-2" required />
            </div>
          </div>
        )}

        {/* Google Map Selector */}
        {useMap && (
          <LoadScript googleMapsApiKey={googleMapsApiKey}>
            <GoogleMap
              mapContainerStyle={{ width: '100%', height: '300px' }}
              center={{ lat: formData.latitude || 6.9271, lng: formData.longitude || 79.8612 }}
              zoom={12}
              onClick={handleMapClick}
            >
              {formData.latitude && formData.longitude && (
                <Marker position={{ lat: formData.latitude, lng: formData.longitude }} />
              )}
            </GoogleMap>
          </LoadScript>
        )}

        <div className="flex items-center gap-2 mt-2">
          <button type="button" onClick={() => setUseMap(prev => !prev)} className="px-2 py-1 border rounded">
            {useMap ? 'Enter Lat/Lng Manually' : 'Select on Map'}
          </button>
          {useMap && <span className="text-sm text-gray-500">Click on the map to set location</span>}
        </div>

        <div className="flex justify-end gap-2 mt-4">
          <button type="button" onClick={onCancel} className="px-4 py-2 rounded border hover:bg-gray-100">Cancel</button>
          <button type="submit" className="px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-600">{initialData ? 'Update' : 'Add'}</button>
        </div>
      </form>
    </div>
  );
};

export default StationForm;
