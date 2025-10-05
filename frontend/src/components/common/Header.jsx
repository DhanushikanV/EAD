import React from 'react';
import Logout from '../../pages/auth/Logout'; // import your logout component

const Header = () => {
  return (
    <header className="bg-white shadow p-4 flex justify-between items-center">
      <h1 className="text-xl font-bold">EV Charging System</h1>
      <div>
        <Logout /> {/* Logout button here */}
      </div>
    </header>
  );
};

export default Header;
