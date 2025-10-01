import React from 'react';
import OperatorStationsTable from '../../components/tables/operatorTables/OperatorStationsTable';

const StationManagement = () => {
  // Sample data for UI testing
  const sampleStations = [
    { name: 'Colombo Station', location: 'Colombo 07', type: 'AC', slots: 4 },
    { name: 'Kandy Station', location: 'Kandy City', type: 'DC', slots: 2 },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Station Management</h1>
      <OperatorStationsTable stations={sampleStations} />
    </div>
  );
};

export default StationManagement;
