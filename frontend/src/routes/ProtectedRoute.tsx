import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthProvider';

interface ProtectedRouteProps {
  requireAuth?: boolean;
  redirectTo?: string;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  redirectTo = '/'
}) => {
  const { isLoggedIn } = useAuth();

  // If the user is not logged in, redirect to the landing page.
  // Do NOT call logout() here — AuthProvider already cleared session state,
  // and firing signOut() async during render creates a race condition where
  // a subsequent signInWithRedirect() call on the Landing page is silently
  // dropped by Amplify while signOut() is still in flight.
  if (!isLoggedIn) {
    return <Navigate to={redirectTo} replace />;
  }

  // 2. If all checks pass, render child routes
  return <Outlet />;
};

export default ProtectedRoute;
