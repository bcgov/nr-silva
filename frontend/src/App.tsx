import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './custom.scss';
import Landing from "./screens/Landing";
import SideLayout from './layouts/SideLayout';
import ProtectedRoute from './routes/ProtectedRoute';
import Opening from './screens/Opening';
import DashboardRedirect from './screens/DashboardRedirect';
import SilvicultureSearch from './screens/SilvicultureSearch';
import ErrorHandling from './screens/ErrorHandling';


// Create the router instance
const router = createBrowserRouter([
  {
    path: "/",
    element: <Landing />,
    errorElement: <ErrorHandling /> // Handle errors for the Landing page route
  },
  {
    path: "/dashboard",
    element: <DashboardRedirect />,
    errorElement: <ErrorHandling /> // Handle errors for the dashboard route
  },
  {
    element: <ProtectedRoute requireAuth />,
    errorElement: <ErrorHandling />, // Global error element for protected routes
    children: [
      {
        path: "/opening",
        element: <SideLayout pageContent={<Opening />} />
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
]);

const App: React.FC = () => {
  return <RouterProvider router={router} />;
};

export default App;
