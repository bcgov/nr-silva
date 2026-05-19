import { lazy, Suspense } from "react";
import { type RouteObject, Outlet, Navigate } from "react-router-dom";
import { Loading } from "@carbon/react";
import SideLayout from '@/layouts/SideLayout';
import LoginClientSelection from "@/screens/LoginClientSelection";
import {
  DASHBOARD_PATH,
  OPENINGS_PATH,
  OPENINGS_SEARCH_PATH,
  ACTIVITY_SEARCH_PATH,
  FOREST_COVER_SEARCH_PATH,
  STANDARDS_UNIT_SEARCH_PATH,
  COMMENT_SEARCH_PATH,
} from './paths';

const Dashboard = lazy(() => import('@/screens/Dashboard'));
const Openings = lazy(() => import('@/screens/Openings'));
const OpeningDetails = lazy(() => import('@/screens/Openings/OpeningDetails'));
const CreateOpening = lazy(() => import('@/screens/CreateOpening'));
const OpeningsSearch = lazy(() => import('@/screens/OpeningsSearch'));
const ActivitySearch = lazy(() => import('@/screens/ActivitySearch'));
const ForestCoverSearch = lazy(() => import('@/screens/ForestCoverSearch'));
const StandardsUnitSearch = lazy(() => import('@/screens/StandardsUnitSearch'));
const CommentSearch = lazy(() => import('@/screens/CommentSearch'));

const PageLoader = () => <Loading withOverlay={false} />;

export const DashboardRoute: RouteObject = {
  path: DASHBOARD_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><Dashboard /></Suspense>} />
}

export const OpeningsRoute: RouteObject = {
  path: OPENINGS_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><Openings /></Suspense>} />,
}

export const OpeningsSearchRoute: RouteObject = {
  path: OPENINGS_SEARCH_PATH,
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
  path: ACTIVITY_SEARCH_PATH,
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
  path: FOREST_COVER_SEARCH_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><ForestCoverSearch /></Suspense>} />,
}

export const StandardsUnitSearchRoute: RouteObject = {
  path: STANDARDS_UNIT_SEARCH_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><StandardsUnitSearch /></Suspense>} />,
}

export const CommentSearchRoute: RouteObject = {
  path: COMMENT_SEARCH_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><CommentSearch /></Suspense>} />,
}
