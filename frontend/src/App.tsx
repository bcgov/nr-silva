import { createBrowserRouter, Navigate, type RouteObject, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import Landing from "./screens/Landing";
import SideLayout from './layouts/SideLayout';
import ProtectedRoute from './routes/ProtectedRoute';
import Dashboard from './screens/Dashboard';
import SilvicultureSearch from './screens/SilvicultureSearch';
import ErrorHandling from './screens/ErrorHandling';
import { useAuth } from './contexts/AuthProvider';
import { Loading } from '@carbon/react';

import './styles/theme.scss';
import './styles/default-components.scss'
import { queryClientConfig } from './constants/tanstackConfig';

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

const queryClient = new QueryClient(queryClientConfig);

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
