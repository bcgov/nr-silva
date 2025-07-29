
import Landing from "@/screens/Landing";
import ProtectedRoute from '@/routes/ProtectedRoute';
import ErrorHandling from '@/screens/ErrorHandling';
import { Navigate, type RouteObject } from "react-router-dom";
import { CreateOpeningRoute, DashboardRoute, OpeningDetailsRoute, OpeningsRoute, SilvicultureSearchRoute } from "./config";

export const publicRoutes: RouteObject[] = [
  {
    path: '*',
    element: <Landing />
  }
] as const;

const protectedRouteList: RouteObject[] = [
  {
    path: "/",
    element: <Navigate to={DashboardRoute?.path ?? "/dashboard"} replace /> // Redirect `/` to `/dashboard` for logged-in users
  },
  DashboardRoute,
  SilvicultureSearchRoute,
  OpeningsRoute,
  CreateOpeningRoute,
  OpeningDetailsRoute
] as const;

export const validPaths = protectedRouteList
  .map((route) => route?.path)
  .filter((path): path is string => typeof path === "string");

export const protectedRoutes: RouteObject[] = [
  {
    element: <ProtectedRoute />,
    errorElement: <ErrorHandling />, // Global error element for protected routes
    children: protectedRouteList
  },
  // Catch-all route for unmatched paths
  {
    path: "*",
    element: <ErrorHandling />
  }
] as const;
