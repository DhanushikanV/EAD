import React, { useState, useEffect } from 'react';

const BookingForm = ({ initialData = null, onSubmit, onCancel, evOwners, stations }) => {
  const [formData, setFormData] = useState({
    evNic: '',
    stationName: '',
    reservationDate: '',
    reservationTime: '',
    status: 'Pending',
  });

  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Validation: reservation within 7 days
    const today = new Date();
    const selectedDate = new Date(formData.reservationDate);
    const maxDate = new Date();
    maxDate.setDate(today.getDate() + 7);

    if (selectedDate < today || selectedDate > maxDate) {
      alert('Reservation date must be within 7 days from today.');
      return;
    }

    onSubmit(formData);
  };

  return (
    <div className="bg-white shadow rounded p-6 max-w-md">
      <h2 className="text-xl font-bold mb-4">{initialData ? 'Edit Booking' : 'Add Booking'}</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block mb-1 font-medium">EV Owner</label>
          <select
            name="evNic"
            value={formData.evNic}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          >
            <option value="">Select EV Owner</option>
            {evOwners.map((owner) => (
              <option key={owner.nic} value={owner.nic}>
                {owner.name} ({owner.nic})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block mb-1 font-medium">Charging Station</label>
          <select
            name="stationName"
            value={formData.stationName}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          >
            <option value="">Select Station</option>
            {stations.map((station, idx) => (
              <option key={idx} value={station.name}>
                {station.name} ({station.location})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block mb-1 font-medium">Reservation Date</label>
          <input
            type="date"
            name="reservationDate"
            value={formData.reservationDate}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Reservation Time</label>
          <input
            type="time"
            name="reservationTime"
            value={formData.reservationTime}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Status</label>
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="w-full border rounded p-2"
          >
            <option value="Pending">Pending</option>
            <option value="Approved">Approved</option>
            <option value="Cancelled">Cancelled</option>
          </select>
        </div>

        <div className="flex justify-end gap-2 mt-4">
          <button
            type="button"
            onClick={onCancel}
            className="px-4 py-2 rounded border hover:bg-gray-100"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-600"
          >
            {initialData ? 'Update' : 'Add'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default BookingForm;
