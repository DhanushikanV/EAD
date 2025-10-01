import React, { useState, useEffect } from 'react';

const EVOwnerForm = ({ initialData = null, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    nic: '',
    name: '',
    email: '',
    phone: '',
    status: 'Active',
    evModels: [''], // start with one empty field
  });

  useEffect(() => {
    if (initialData) {
      // Ensure evModels exists and is at least one element
      setFormData({
        ...initialData,
        evModels: initialData.evModels && initialData.evModels.length > 0 ? initialData.evModels : [''],
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleEvModelChange = (index, value) => {
    const updatedModels = [...formData.evModels];
    updatedModels[index] = value;
    setFormData((prev) => ({ ...prev, evModels: updatedModels }));
  };

  const addEvModel = () => {
    setFormData((prev) => ({ ...prev, evModels: [...prev.evModels, ''] }));
  };

  const removeEvModel = (index) => {
    const updatedModels = [...formData.evModels];
    updatedModels.splice(index, 1);
    setFormData((prev) => ({ ...prev, evModels: updatedModels.length ? updatedModels : [''] }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Filter out empty strings in evModels
    onSubmit({ ...formData, evModels: formData.evModels.filter((m) => m.trim() !== '') });
  };

  return (
    <div className="bg-white shadow rounded p-6 max-w-md">
      <h2 className="text-xl font-bold mb-4">
        {initialData ? 'Edit EV Owner' : 'Add EV Owner'}
      </h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block mb-1 font-medium">NIC</label>
          <input
            type="text"
            name="nic"
            value={formData.nic}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className="w-full border rounded p-2"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Phone</label>
          <input
            type="text"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            className="w-full border rounded p-2"
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
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
          </select>
        </div>

        <div>
          <label className="block mb-1 font-medium">EV Models</label>
          {formData.evModels.map((model, idx) => (
            <div key={idx} className="flex gap-2 mb-2">
              <input
                type="text"
                value={model}
                onChange={(e) => handleEvModelChange(idx, e.target.value)}
                className="flex-1 border rounded p-2"
                placeholder="Enter EV model"
                required
              />
              <button
                type="button"
                onClick={() => removeEvModel(idx)}
                className="px-2 py-1 bg-red-500 text-white rounded hover:bg-red-600"
              >
                Remove
              </button>
            </div>
          ))}
          <button
            type="button"
            onClick={addEvModel}
            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
          >
            Add EV Model
          </button>
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

export default EVOwnerForm;
