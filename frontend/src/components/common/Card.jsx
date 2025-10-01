import React from 'react';
import { FaUsers, FaBolt, FaChargingStation, FaClipboardList } from 'react-icons/fa';

const TrendIndicator = ({ trend }) => {
  return (
    <span
      className={`ml-2 text-xs font-semibold ${
        trend > 0 ? 'text-green-500' : trend < 0 ? 'text-red-500' : 'text-gray-400'
      }`}
    >
      {trend > 0 && '▲'}{trend < 0 && '▼'} {Math.abs(trend)}%
    </span>
  );
};

const Card = ({ title, value, type, trend = 0 }) => {
  const iconMap = {
    users: <FaUsers />,
    owners: <FaBolt />,
    stations: <FaChargingStation />,
    bookings: <FaClipboardList />,
  };

  const colorMap = {
    users: 'bg-gradient-to-r from-blue-400 to-blue-600',
    owners: 'bg-gradient-to-r from-green-400 to-green-600',
    stations: 'bg-gradient-to-r from-yellow-400 to-yellow-600',
    bookings: 'bg-gradient-to-r from-red-400 to-red-600',
  };

  return (
    <div
      className={`relative p-6 rounded-2xl shadow-lg transform hover:scale-105 transition-transform duration-300 text-white overflow-hidden ${colorMap[type]}`}
    >
      {/* Icon background circle */}
      <div className="absolute top-4 right-4 text-white opacity-20 text-6xl">
        {iconMap[type]}
      </div>

      <div className="relative z-10">
        <h3 className="text-sm font-semibold uppercase tracking-wider">{title}</h3>
        <p className="text-3xl font-bold mt-2 flex items-center">
          {value}
          <TrendIndicator trend={trend} />
        </p>

        {/* Decorative bottom line */}
        <div className="mt-4 h-1 w-full bg-white bg-opacity-30 rounded-full"></div>
      </div>
    </div>
  );
};

export default Card;
