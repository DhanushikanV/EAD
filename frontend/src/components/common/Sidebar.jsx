import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = ({ role }) => {
  return (
    <aside className="w-64 bg-gray-800 text-white min-h-screen p-6">
      <h2 className="text-2xl font-bold mb-8">Menu</h2>
      <nav className="flex flex-col gap-4">
        <Link to={role === 'backoffice' ? '/backoffice' : '/operator'} className="hover:bg-gray-700 px-3 py-2 rounded">
          Dashboard
        </Link>

        {role === 'backoffice' && (
          <>
            <Link to="/backoffice/users" className="hover:bg-gray-700 px-3 py-2 rounded">
              Users Management
            </Link>
            <Link to="/backoffice/evowners" className="hover:bg-gray-700 px-3 py-2 rounded">
              EV Owners Management
            </Link>
            <Link to="/backoffice/stations" className="hover:bg-gray-700 px-3 py-2 rounded">
              Stations Management
            </Link>
            <Link to="/backoffice/bookings" className="hover:bg-gray-700 px-3 py-2 rounded">
              Bookings Management
            </Link>
          </>
        )}

        {role === 'operator' && (
          <>
            <Link to="/operator/bookings" className="hover:bg-gray-700 px-3 py-2 rounded">
              Bookings
            </Link>
            <Link to="/operator/stations" className="hover:bg-gray-700 px-3 py-2 rounded">
              Station Management
            </Link>
          </>
        )}
      </nav>
    </aside>
  );
};

export default Sidebar;
