import React, { useState, useEffect } from 'react';

const UserForm = ({ initialData = null, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    role: 'Operator',
    status: 'Active',
    passwordHash: '', // used for new user or password change
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        username: initialData.username || '',
        email: initialData.email || '',
        role: initialData.role || 'Operator',
        status: initialData.status || 'Active',
        passwordHash: '',
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const dataToSend = { ...formData };
    onSubmit(dataToSend);
    setFormData((prev) => ({ ...prev, passwordHash: '' })); // clear password
  };

  return (
    <div className="bg-white shadow rounded p-6 max-w-md mb-6">
      <h2 className="text-xl font-bold mb-4">
        {initialData ? 'Edit User' : 'Add User'}
      </h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Username */}
        <div>
          <label className="block mb-1 font-medium">Username</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            readOnly={!!initialData} // read-only if editing
            onChange={handleChange}
            className="w-full border rounded p-2 bg-gray-100"
            required
          />
        </div>

        {/* Email */}
        <div>
          <label className="block mb-1 font-medium">Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            readOnly={!!initialData} // read-only if editing
            onChange={handleChange}
            className="w-full border rounded p-2 bg-gray-100"
            required
          />
        </div>

        {/* Password */}
        <div>
          <label className="block mb-1 font-medium">Password</label>
          <input
            type="password"
            name="passwordHash"
            value={formData.passwordHash}
            onChange={handleChange}
            className="w-full border rounded p-2"
            placeholder={
              initialData
                ? 'Leave blank to keep current password'
                : 'Enter password'
            }
            required={!initialData}
          />
        </div>

        {/* Role */}
        <div>
          <label className="block mb-1 font-medium">Role</label>
          <select
            name="role"
            value={formData.role}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          >
            <option value="Backoffice">Backoffice</option>
            <option value="Operator">Operator</option>
          </select>
        </div>

        {/* Status */}
        <div>
          <label className="block mb-1 font-medium">Status</label>
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          >
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
          </select>
        </div>

        {/* Buttons */}
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

export default UserForm;
