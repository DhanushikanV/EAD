import React from 'react';
import OperatorBookingsTable from '../../components/tables/operatorTables/OperatorBookingsTable';

const OperatorBookings = () => {
  // Sample data for UI testing
  const sampleBookings = [
    { id: 'B001', evOwner: 'Alice', station: 'Colombo Station', dateTime: '2025-10-01 10:00 AM', status: 'Pending' },
    { id: 'B002', evOwner: 'Bob', station: 'Colombo Station', dateTime: '2025-10-02 02:00 PM', status: 'Confirmed' },
    { id: 'B003', evOwner: 'Charlie', station: 'Kandy Station', dateTime: '2025-10-03 11:30 AM', status: 'Pending' },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Bookings Management</h1>
      <OperatorBookingsTable bookings={sampleBookings} />
    </div>
  );
};

export default OperatorBookings;
