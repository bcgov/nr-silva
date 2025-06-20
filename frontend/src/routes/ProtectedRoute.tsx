import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthProvider';

interface ProtectedRouteProps {
  requireAuth?: boolean;
  redirectTo?: string;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  redirectTo = '/'
}) => {
  const { isLoggedIn, logout } = useAuth();

  // 1. If authentication is required and the user is not logged in, redirect to login
  if (!isLoggedIn) {
    logout();
    return <Navigate to={redirectTo} replace />;
  }

  // 2. If all checks pass, render child routes
  return <Outlet />;
};

export default ProtectedRoute;
