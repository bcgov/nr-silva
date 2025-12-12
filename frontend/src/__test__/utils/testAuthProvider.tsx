import React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { NotificationProvider } from '@/contexts/NotificationProvider';
import { PreferenceProvider } from '@/contexts/PreferenceProvider';
import { AuthContext } from '@/contexts/AuthProvider';

export const queryClient = new QueryClient();

export const TestAuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  // Minimal fake auth context implementation for component tests.
  // Tests that need full provider behavior should import and use the real AuthProvider.
  const providerValue = {
    user: {
      providerUsername: 'ttester',
      userName: 'ttester',
      displayName: 'Jack Ryan',
      email: 'jack.ryan@gov.bc.ca',
      idpProvider: undefined,
      roles: [],
      authToken: undefined,
      exp: undefined,
      privileges: {},
      associatedClients: [],
      firstName: 'Jack',
      lastName: 'Ryan'
    },
    isLoggedIn: true,
    isLoading: false,
    login: () => { },
    logout: () => { },
    selectedClient: undefined,
    setSelectedClient: () => { },
  };

  return (
    <AuthContext.Provider value={providerValue}>
      {children}
    </AuthContext.Provider>
  );
};

export const renderWithProviders = () =>
({
  wrapper: ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>
      <PreferenceProvider>
        <TestAuthProvider>
          <MemoryRouter>
            <NotificationProvider>{children}</NotificationProvider>
          </MemoryRouter>
        </TestAuthProvider>
      </PreferenceProvider>
    </QueryClientProvider>
  )
} as any);
