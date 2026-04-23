import { lazy, Suspense } from "react";
import { type RouteObject, Outlet, Navigate } from "react-router-dom";
import { Loading } from "@carbon/react";
import SideLayout from '@/layouts/SideLayout';
import LoginClientSelection from "@/screens/LoginClientSelection";

const Dashboard = lazy(() => import('@/screens/Dashboard'));
const Openings = lazy(() => import('@/screens/Openings'));
const OpeningDetails = lazy(() => import('@/screens/Openings/OpeningDetails'));
const CreateOpening = lazy(() => import('@/screens/CreateOpening'));
const OpeningsSearch = lazy(() => import('@/screens/OpeningsSearch'));
const ActivitySearch = lazy(() => import('@/screens/ActivitySearch'));
const ForestCoverSearch = lazy(() => import('@/screens/ForestCoverSearch'));
const StandardsUnitSearch = lazy(() => import('@/screens/StandardsUnitSearch'));

const PageLoader = () => <Loading withOverlay={false} />;

export const DashboardRoute: RouteObject = {
  path: "/dashboard",
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><Dashboard /></Suspense>} />
}

export const OpeningsRoute: RouteObject = {
  path: "/openings",
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><Openings /></Suspense>} />,
}

export const OpeningsSearchRoute: RouteObject = {
  path: "/openings-search",
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><OpeningsSearch /></Suspense>} />,
}

export const CreateOpeningRoute: RouteObject = {
  path: "/openings/create",
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><CreateOpening /></Suspense>} />,
}

export const OpeningDetailsRoute: RouteObject = {
  path: "/openings/:openingId",
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><OpeningDetails /></Suspense>} />,
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
      element: <Suspense fallback={<PageLoader />}><ActivitySearch type="activities" /></Suspense>,
    },
    {
      path: "disturbances",
      element: <Suspense fallback={<PageLoader />}><ActivitySearch type="disturbances" /></Suspense>,
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
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><ForestCoverSearch /></Suspense>} />,
}

export const StandardsUnitSearchRoute: RouteObject = {
  path: "/standards-unit-search",
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><StandardsUnitSearch /></Suspense>} />,
}
