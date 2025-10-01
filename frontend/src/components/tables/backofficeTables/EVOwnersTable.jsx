import React from 'react';

const EVOwnersTable = ({ evOwners, onEdit, onDelete }) => {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white shadow rounded">
        <thead className="bg-gray-100">
          <tr>
            <th className="text-left p-3">NIC</th>
            <th className="text-left p-3">Name</th>
            <th className="text-left p-3">Email</th>
            <th className="text-left p-3">EV Model</th>
            <th className="text-left p-3">Status</th>
            <th className="text-left p-3">Actions</th>
          </tr>
        </thead>
        <tbody>
          {evOwners.map((owner) => (
            <tr key={owner.nic} className="border-b hover:bg-gray-50">
              <td className="p-3">{owner.nic}</td>
              <td className="p-3">{owner.name}</td>
              <td className="p-3">{owner.email}</td>
              <td className="p-3">
                {owner.evModels && owner.evModels.length > 1 ? (
                  <select className="border rounded p-1">
                    {owner.evModels.map((model, idx) => (
                      <option key={idx} value={model}>
                        {model}
                      </option>
                    ))}
                  </select>
                ) : (
                  owner.evModels && owner.evModels.length === 1
                    ? owner.evModels[0]
                    : '-'
                )}
              </td>
              <td className="p-3">{owner.status}</td>
              <td className="p-3 flex gap-2">
                <button
                  onClick={() => onEdit(owner)}
                  className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
                >
                  Edit
                </button>
                <button
                  onClick={() => onDelete(owner.nic)}
                  className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
          {evOwners.length === 0 && (
            <tr>
              <td colSpan="6" className="p-3 text-center text-gray-500">
                No EV owners found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default EVOwnersTable;
