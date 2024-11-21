window.global ||= window;
import React from 'react';
import './index.css';
import { ClassPrefix } from '@carbon/react';
import { Provider } from 'react-redux'
import App from './App';
import { ThemePreference } from './utils/ThemePreference';
import { createRoot } from 'react-dom/client';
import PaginationProvider from './contexts/PaginationProvider';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { isAxiosError } from 'axios';
import { OpeningsSearchProvider } from './contexts/search/OpeningsSearch';
import { Amplify } from 'aws-amplify';
import amplifyconfig from './amplifyconfiguration';
import { CookieStorage } from 'aws-amplify/utils';
import { cognitoUserPoolsTokenProvider } from 'aws-amplify/auth/cognito';
import { AuthProvider } from './contexts/AuthProvider';

const container: HTMLElement | null = document.getElementById('root');
if (container) {
  const root = createRoot(container);
  const HTTP_STATUS_TO_NOT_RETRY = [400, 401, 403, 404];
  const MAX_RETRIES = 3;
  const queryClient = new QueryClient(
    {
      defaultOptions: {
        queries: {
          refetchOnMount: false,
          refetchOnWindowFocus: false,
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

    Amplify.configure(amplifyconfig);
    cognitoUserPoolsTokenProvider.setKeyValueStorage(new CookieStorage());


  root.render(
    <React.StrictMode>
      <ClassPrefix prefix='bx'>
        <ThemePreference>
          <AuthProvider>
            <QueryClientProvider client={queryClient}>
                <App />
            </QueryClientProvider>
          </AuthProvider>
        </ThemePreference>
      </ClassPrefix>
    </React.StrictMode>
  );
}