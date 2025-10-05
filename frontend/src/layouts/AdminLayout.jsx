import React from 'react';
import Sidebar from '../components/common/Sidebar';
import Header from '../components/common/Header';

const AdminLayout = ({ children }) => {
  return (
    <div className="flex h-screen">
      <Sidebar role="backoffice" />
      <div className="flex-1 flex flex-col">
        <Header />
        <main className="p-6 bg-gray-100 flex-1">{children}</main>
      </div>
    </div>
  );
};

export default AdminLayout;
