import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/auth/Login';
import Signup from './pages/auth/Signup';
import AdminLayout from './layouts/AdminLayout';
import OperatorLayout from './layouts/OperatorLayout';
import DashboardBackoffice from './pages/backoffice/Dashboard';
import DashboardOperator from './pages/stationOperator/Dashboard';
import UsersManagement from './pages/backoffice/UserManagement';
import EVOwnersManagement from './pages/backoffice/EVOwnersManagement';
import StationsManagement from './pages/backoffice/StationsManagement';
import BookingsManagement from './pages/backoffice/BookingsManagement';
import OperatorBookings from './pages/stationOperator/BookingsManagement';
import StationManagement from './pages/stationOperator/StationManagement';
import CommonDashboard from './pages/shared/Dashboard';
import ProtectedRoute from './routes/ProtectedRoute';

import Test from './pages/test';
//
function App() {
  
  return (
    <Router>
      <Routes>
        {/* Auth Pages */}
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />

        {/* Backoffice Dashboard */}
        <Route
  path="/backoffice"
  element={
    <ProtectedRoute>
      <AdminLayout>
        <DashboardBackoffice />
      </AdminLayout>
    </ProtectedRoute>
  }
/>

<Route
  path="/operator"
  element={
    <ProtectedRoute>
      <OperatorLayout>
        <DashboardOperator />
      </OperatorLayout>
    </ProtectedRoute>
  }
/>
        {/* Backoffice pages */}
        <Route
          path="/backoffice/users"
          element={
            <AdminLayout>
              <UsersManagement />
            </AdminLayout>
          }
        />
        <Route
          path="/backoffice/evowners"
          element={
            <AdminLayout>
              <EVOwnersManagement />
            </AdminLayout>
          }
        />
        <Route
          path="/backoffice/stations"
          element={
            <AdminLayout>
              <StationsManagement />
            </AdminLayout>
          }
        />
        <Route
          path="/backoffice/bookings"
          element={
            <AdminLayout>
              <BookingsManagement />
            </AdminLayout>
          }
        />

        {/* Operator pages */}
        <Route
          path="/operator/bookings"
          element={
            <OperatorLayout>
              <OperatorBookings />
            </OperatorLayout>
          }
        />
        <Route
          path="/operator/stations"
          element={
            <OperatorLayout>
              <StationManagement />
            </OperatorLayout>
          }
        />

        <Route
          path="/test"
          element={
            <AdminLayout>
              <Test />
            </AdminLayout>
          }
        />
         <Route path="/dashboard"  />

        {/* Default redirect */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
