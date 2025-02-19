window.global ||= window;
import React from 'react';
import './index.css';
import { ClassPrefix } from '@carbon/react';
import App from './App';
import { ThemePreference } from './utils/ThemePreference';
import { createRoot } from 'react-dom/client';
import { Amplify } from 'aws-amplify';
import amplifyconfig from './amplifyconfiguration';
import { CookieStorage } from 'aws-amplify/utils';
import { cognitoUserPoolsTokenProvider } from 'aws-amplify/auth/cognito';
import { AuthProvider } from './contexts/AuthProvider';
import { NotificationProvider } from './contexts/NotificationProvider';

const container: HTMLElement | null = document.getElementById("root");
if (container) {
  const root = createRoot(container);

  Amplify.configure(amplifyconfig);
  cognitoUserPoolsTokenProvider.setKeyValueStorage(new CookieStorage());

  root.render(
    <React.StrictMode>
      <ClassPrefix prefix="bx">
        <ThemePreference>
          <AuthProvider>
            <NotificationProvider>
              <App />
            </NotificationProvider>
          </AuthProvider>
        </ThemePreference>
      </ClassPrefix>
    </React.StrictMode>
  );
}
