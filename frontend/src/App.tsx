import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

import { useAuth } from '@/contexts/AuthProvider';
import { Loading } from '@carbon/react';

import { queryClientConfig } from './constants/tanstackConfig';

import './styles/theme.scss';
import './styles/default-components.scss'
import { protectedRoutes, publicRoutes } from './routes';



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
