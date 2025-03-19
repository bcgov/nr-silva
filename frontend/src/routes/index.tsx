
import Landing from "@/screens/Landing";
import ProtectedRoute from '@/routes/ProtectedRoute';
import ErrorHandling from '@/screens/ErrorHandling';
import { Navigate, type RouteObject } from "react-router-dom";
import { DashboardRoute, OpeningDetailsRoute, OpeningsRoute, SilvicultureSearchRoute } from "./config";

export const publicRoutes: RouteObject[] = [
  {
    path: '*',
    element: <Landing />
  }
];


export const protectedRoutes: RouteObject[] = [
  {
    element: <ProtectedRoute />,
    errorElement: <ErrorHandling />, // Global error element for protected routes
    children: [
      {
        path: "/",
        element: <Navigate to={DashboardRoute.path!} replace /> // Redirect `/` to `/dashboard` for logged-in users
      },
      DashboardRoute,
      SilvicultureSearchRoute,
      OpeningsRoute,
      OpeningDetailsRoute
    ]
  },
  // Catch-all route for unmatched paths
  {
    path: "*",
    element: <ErrorHandling />
  }
];
