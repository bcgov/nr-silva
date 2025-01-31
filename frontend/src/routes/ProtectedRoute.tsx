import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthProvider';

interface ProtectedRouteProps {
  requireAuth?: boolean;
  requiredRoles?: string[];
  redirectTo?: string;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  requiredRoles = [],
  redirectTo = '/'
}) => {
  const { isLoggedIn, userRoles, logout } = useAuth();

  // 1. If authentication is required and the user is not logged in, redirect to login
  if (!isLoggedIn) {
    logout();
    return <Navigate to={redirectTo} replace />;
  }

  // 2. If specific roles are required and user does not have them, redirect to unauthorized page
  if (requiredRoles.length > 0 && !requiredRoles.some(role => userRoles?.includes(role))) {
    return <Navigate to="/unauthorized" replace />;
  }

  // 3. If all checks pass, render child routes
  return <Outlet />;
};

export default ProtectedRoute;
