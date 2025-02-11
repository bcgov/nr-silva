import { createBrowserRouter, Navigate, type RouteObject, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { isAxiosError } from 'axios';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import Landing from "./screens/Landing";
import SideLayout from './layouts/SideLayout';
import ProtectedRoute from './routes/ProtectedRoute';
import Dashboard from './screens/Dashboard';
import SilvicultureSearch from './screens/SilvicultureSearch';
import ErrorHandling from './screens/ErrorHandling';
import { useAuth } from './contexts/AuthProvider';
import { Loading } from '@carbon/react';
import { THREE_HOURS } from './config/TimeUnits';

import './styles/theme.scss';
import './styles/default-components.scss'

const publicRoutes: RouteObject[] = [
  {
    path: '*',
    element: <Landing />
  }
];

const protectedRoutes: RouteObject[] = [
  {
    element: <ProtectedRoute />,
    errorElement: <ErrorHandling />, // Global error element for protected routes
    children: [
      {
        path: "/",
        element: <Navigate to="/opening" replace /> // Redirect `/` to `/opening` for logged-in users
      },
      {
        path: "/dashboard",
        element: <Navigate to="/opening" replace />
      },
      {
        path: "/opening",
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

// Tanstack Query Config
const HTTP_STATUS_TO_NOT_RETRY = [400, 401, 403, 404];
const MAX_RETRIES = 3;
const queryClient = new QueryClient(
  {
    defaultOptions: {
      queries: {
        refetchOnMount: false, // Default is caching fetched values
        refetchOnWindowFocus: false,
        staleTime: THREE_HOURS,
        gcTime: THREE_HOURS,
        // Do not retry on errors defined above
        retry: (failureCount, error) => {
          if (failureCount > MAX_RETRIES) {
            return false;
          }
          if (
            isAxiosError(error)
            && HTTP_STATUS_TO_NOT_RETRY.includes(error.response?.status ?? 0)
          ) {
            return false;
          }
          return true;
        }
      }
    }
  });

const App: React.FC = () => {
  const auth = useAuth();

  if (auth.isLoading) {
    return <Loading withOverlay={true} />;
  }

  const browserRouter = createBrowserRouter(auth.isLoggedIn ? protectedRoutes : publicRoutes);

  return (
    <QueryClientProvider client={queryClient}>
      <ReactQueryDevtools initialIsOpen={false} />
      <RouterProvider router={browserRouter} />
    </QueryClientProvider>
  );
};

export default App;
