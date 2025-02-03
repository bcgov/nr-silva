import { createBrowserRouter, Navigate, type RouteObject, RouterProvider } from 'react-router-dom';
import './custom.scss';
import Landing from "./screens/Landing";
import SideLayout from './layouts/SideLayout';
import ProtectedRoute from './routes/ProtectedRoute';
import Dashboard from './screens/Dashboard';
import SilvicultureSearch from './screens/SilvicultureSearch';
import ErrorHandling from './screens/ErrorHandling';
import { useAuth } from './contexts/AuthProvider';
import { Loading } from '@carbon/react';

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


const App: React.FC = () => {
  const auth = useAuth();

  if (auth.isLoading) {
    return <Loading withOverlay={true} />;
  }

  const browserRouter = createBrowserRouter(auth.isLoggedIn ? protectedRoutes : publicRoutes);

  return <RouterProvider router={browserRouter} />;
};

export default App;
