import React, { useState, useEffect } from "react";
import api from "../../services/api";
import UserForm from "../../components/forms/backofficeForms/UserForm";
// Import your table version
import UsersTable from "../../components/tables/backofficeTables/UserTable";

const UsersManagement = () => {
  const [users, setUsers] = useState([]);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [viewMode, setViewMode] = useState("card"); // 'card' or 'table'

  const apiBase = "/User";

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const response = await api.get(apiBase);
      setUsers(response.data);
    } catch (error) {
      console.error("Error fetching users:", error);
      alert("Failed to fetch users.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleAddClick = () => {
    setEditingUser(null);
    setIsFormOpen(true);
  };

  const handleEditClick = (user) => {
    setEditingUser(user);
    setIsFormOpen(true);
  };

  const handleFormSubmit = async (data) => {
    try {
      if (editingUser) {
        const payload = {
          Username: data.username || editingUser.username,
          Email: data.email || editingUser.email,
          Role: data.role,
          Status: data.status,
        };

        if (data.passwordHash?.trim()) {
          payload.Password = data.passwordHash;
        }

        await api.put(`${apiBase}/${editingUser.id}`, payload);
      } else {
        const payload = {
          Username: data.username,
          Email: data.email,
          Role: data.role,
          Status: data.status,
          PasswordHash: data.passwordHash,
        };

        await api.post(apiBase, payload);
      }

      await fetchUsers();
      setIsFormOpen(false);
    } catch (error) {
      console.error("Error saving user:", error.response?.data || error);
      alert("Failed to save user.");
    }
  };

  const handleFormCancel = () => setIsFormOpen(false);

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this user?")) return;

    try {
      await api.delete(`${apiBase}/${id}`);
      setUsers((prev) => prev.filter((u) => u.id !== id));
    } catch (error) {
      console.error("Error deleting user:", error);
      alert("Failed to delete user.");
    }
  };

  const statusColor = (status) => {
    switch (status) {
      case "Active":
        return "bg-green-200 text-green-800";
      case "Inactive":
        return "bg-red-200 text-red-800";
      default:
        return "bg-gray-200 text-gray-800";
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6 flex justify-between items-center">
        Users Management
        <div className="flex gap-2">
          <button
            onClick={() => setViewMode(viewMode === "card" ? "table" : "card")}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
          >
            {viewMode === "card" ? "Table View" : "Card View"}
          </button>
          <button
            onClick={handleAddClick}
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
          >
            Add User
          </button>
        </div>
      </h1>

      {isFormOpen && (
        <UserForm
          initialData={editingUser}
          onSubmit={handleFormSubmit}
          onCancel={handleFormCancel}
        />
      )}

      {loading ? (
        <p>Loading users...</p>
      ) : viewMode === "table" ? (
        <UsersTable users={users} onEdit={handleEditClick} onDelete={handleDelete} />
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {users.map((user) => (
            <div
              key={user.id}
              className="bg-white shadow-lg rounded-lg p-4 hover:shadow-xl transition transform hover:-translate-y-1"
            >
              <h2 className="text-xl font-bold">{user.username}</h2>
              <p className="text-gray-500">{user.email}</p>
              <div className="flex items-center justify-between mt-3">
                <span
                  className={`px-3 py-1 rounded-full text-sm font-semibold ${statusColor(
                    user.status
                  )}`}
                >
                  {user.status}
                </span>
                <span className="px-3 py-1 rounded-full text-sm bg-blue-200 text-blue-800 font-semibold">
                  {user.role}
                </span>
              </div>
              <div className="mt-4 flex justify-end gap-2">
                <button
                  onClick={() => handleEditClick(user)}
                  className="px-3 py-1 bg-yellow-400 text-white rounded hover:bg-yellow-500 transition"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(user.id)}
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

export default UsersManagement;
