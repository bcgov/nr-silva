import { Navigate, type RouteObject } from "react-router-dom";
import Landing from "../screens/Landing";
import ProtectedRoute from "./ProtectedRoute";
import ErrorHandling from "../screens/ErrorHandling";
import SideLayout from "../layouts/SideLayout";
import { Dashboard } from "@carbon/icons-react";
import SilvicultureSearch from "../screens/SilvicultureSearch";

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
        element: <Navigate to="/dashboard" replace /> // Redirect `/` to `/dashboard` for logged-in users
      },
      {
        path: "/dashboard",
        element: <SideLayout pageContent={<Dashboard />} />
      },
      {
        path: "/silviculture-search",
        element: <SideLayout pageContent={<SilvicultureSearch />} />
      }
    ]
  },
  // Catch-all route for unmatched paths
  {
    path: "*",
    element: <ErrorHandling />
  }
];
