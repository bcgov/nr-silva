import { type RouteObject } from "react-router-dom";
import SideLayout from '@/layouts/SideLayout';
import Dashboard from '@/screens/Dashboard';
import SilvicultureSearch from '@/screens/SilvicultureSearch';
import Openings from '@/screens/Openings';
import OpeningDetails from '@/screens/Openings/OpeningDetails';
import LoginClientSelection from "@/screens/LoginClientSelection";
import CreateOpening from "@/screens/CreateOpening";
import OpeningsSearch from "@/screens/OpeningsSearch";

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
