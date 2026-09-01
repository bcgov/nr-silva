import { lazy, Suspense } from "react";
import { type RouteObject, Outlet, Navigate } from "react-router-dom";
import { Loading } from "@carbon/react";
import SideLayout from '@/layouts/SideLayout';
import FeatureGateRoute from '@/routes/FeatureGateRoute';
import {
  DASHBOARD_PATH,
  OPENINGS_PATH,
  OPENINGS_SEARCH_PATH,
  OPENING_CREATE_SUCCESS_PATH,
  ACTIVITY_SEARCH_PATH,
  FOREST_COVER_SEARCH_PATH,
  STANDARDS_UNIT_SEARCH_PATH,
  COMMENT_SEARCH_PATH,
  STOCKING_STANDARDS_SEARCH_PATH,
  STOCKING_STANDARDS_COMMENT_SEARCH_PATH,
  CREATE_OPENING_PATH,
  OPENING_DETAILS_PATH,
  EDIT_TENURE_PATH,
  ACTIVITY_SEARCH_ACTIVITIES_PATH,
  ACTIVITY_SEARCH_DISTURBANCES_PATH,
  ACTIVITY_SEARCH_ACTIVITIES_FULL_PATH,
  WILDCARD_PATH,
} from './paths';

const Dashboard = lazy(() => import('@/screens/Dashboard'));
const Openings = lazy(() => import('@/screens/Openings'));
const OpeningDetails = lazy(() => import('@/screens/Openings/OpeningDetails'));
const CreateOpening = lazy(() => import('@/screens/CreateOpening'));
const CreateOpeningSuccess = lazy(() => import('@/screens/CreateOpening/CreateOpeningSuccess'));
const OpeningsSearch = lazy(() => import('@/screens/OpeningsSearch'));
const ActivitySearch = lazy(() => import('@/screens/ActivitySearch'));
const ForestCoverSearch = lazy(() => import('@/screens/ForestCoverSearch'));
const StandardsUnitSearch = lazy(() => import('@/screens/StandardsUnitSearch'));
const CommentSearch = lazy(() => import('@/screens/CommentSearch'));
const StockingStandardsSearch = lazy(() => import('@/screens/StockingStandardsSearch'));
const StockingStandardsCommentSearch = lazy(() => import('@/screens/StockingStandardsCommentSearch'));
const EditTenure = lazy(() => import('@/screens/EditTenure'));

const PageLoader = () => <Loading withOverlay />;

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
  path: CREATE_OPENING_PATH,
  element: (
    <FeatureGateRoute
      featureName="Create opening"
      title="Opening creation is unavailable"
      description="Creating new tenure-based openings is disabled in this deployment model. Please return to the openings list."
    >
      <SideLayout pageContent={<Suspense fallback={<PageLoader />}><CreateOpening /></Suspense>} />
    </FeatureGateRoute>
  ),
}

export const CreateOpeningSuccessRoute: RouteObject = {
  path: OPENING_CREATE_SUCCESS_PATH,
  element: (
    <FeatureGateRoute
      featureName="Create opening"
      title="Opening creation is unavailable"
      description="The opening creation workflow is disabled in this deployment model. Please return to the openings list."
    >
      <SideLayout pageContent={<Suspense fallback={<PageLoader />}><CreateOpeningSuccess /></Suspense>} />
    </FeatureGateRoute>
  ),
}

export const EditTenureRoute: RouteObject = {
  path: EDIT_TENURE_PATH,
  element: (
    <FeatureGateRoute
      featureName="Edit tenure"
      title="Tenure editing is unavailable"
      description="The tenure editing workflow is disabled in this deployment model. Please return to the openings list."
    >
      <SideLayout pageContent={<Suspense fallback={<PageLoader />}><EditTenure /></Suspense>} />
    </FeatureGateRoute>
  ),
}

export const OpeningDetailsRoute: RouteObject = {
  path: OPENING_DETAILS_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><OpeningDetails /></Suspense>} />,
}

export const ActivitySearchRoute: RouteObject = {
  path: ACTIVITY_SEARCH_PATH,
  element: <SideLayout pageContent={<Outlet />} />,
  children: [
    {
      path: ACTIVITY_SEARCH_ACTIVITIES_PATH,
      element: <Suspense fallback={<PageLoader />}><ActivitySearch type="activities" /></Suspense>,
    },
    {
      path: ACTIVITY_SEARCH_DISTURBANCES_PATH,
      element: <Suspense fallback={<PageLoader />}><ActivitySearch type="disturbances" /></Suspense>,
    },
    {
      index: true,
      element: <Navigate to={ACTIVITY_SEARCH_ACTIVITIES_PATH} replace />,
    },
    {
      path: WILDCARD_PATH,
      element: <Navigate to={ACTIVITY_SEARCH_ACTIVITIES_FULL_PATH} replace />,
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

export const StockingStandardsSearchRoute: RouteObject = {
  path: STOCKING_STANDARDS_SEARCH_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><StockingStandardsSearch /></Suspense>} />,
}

export const StockingStandardsCommentSearchRoute: RouteObject = {
  path: STOCKING_STANDARDS_COMMENT_SEARCH_PATH,
  element: <SideLayout pageContent={<Suspense fallback={<PageLoader />}><StockingStandardsCommentSearch /></Suspense>} />,
}
