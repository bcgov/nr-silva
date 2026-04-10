import { type RouteObject, Outlet, Navigate } from "react-router-dom";
import SideLayout from '@/layouts/SideLayout';
import Dashboard from '@/screens/Dashboard';
import Openings from '@/screens/Openings';
import OpeningDetails from '@/screens/Openings/OpeningDetails';
import LoginClientSelection from "@/screens/LoginClientSelection";
import CreateOpening from "@/screens/CreateOpening";
import OpeningsSearch from "@/screens/OpeningsSearch";
import ActivitySearch from "@/screens/ActivitySearch";
import ForestCoverSearch from "@/screens/ForestCoverSearch";
import StandardsUnitSearch from "../screens/StandardsUnitSearch";

export const DashboardRoute: RouteObject = {
  path: "/dashboard",
  element: <SideLayout pageContent={<Dashboard />} />
}

export const OpeningsRoute: RouteObject = {
  path: "/openings",
  element: <SideLayout pageContent={<Openings />} />,
}


export const OpeningsSearchRoute: RouteObject = {
  path: "/openings-search",
  element: <SideLayout pageContent={<OpeningsSearch />} />,
}

export const CreateOpeningRoute: RouteObject = {
  path: "/openings/create",
  element: <SideLayout pageContent={<CreateOpening />} />,
}

export const OpeningDetailsRoute: RouteObject = {
  path: "/openings/:openingId",
  element: <SideLayout pageContent={<OpeningDetails />} />,
}

export const ClientSelectionRoute: RouteObject = {
  path: "*",
  element: <LoginClientSelection />
}

export const ActivitySearchRoute: RouteObject = {
  path: "/activity-search",
  element: <SideLayout pageContent={<Outlet />} />,
  children: [
    {
      path: "activities",
      element: <ActivitySearch type="activities" />,
    },
    {
      path: "disturbances",
      element: <ActivitySearch type="disturbances" />,
    },
    {
      index: true,
      element: <Navigate to="activities" replace />,
    },
    {
      path: "*",
      element: <Navigate to="/activity-search/activities" replace />,
    },
  ],
}

export const ForestCoverSearchRoute: RouteObject = {
  path: "/forest-cover-search",
  element: <SideLayout pageContent={<ForestCoverSearch />} />,
}

export const StandardsUnitSearchRoute: RouteObject = {
  path: "/standards-unit-search",
  element: <SideLayout pageContent={<StandardsUnitSearch />} />,
}
