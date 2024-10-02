import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { Provider } from 'react-redux';
import store from '../../store';
import LoginOrgSelection from '../../views/LoginOrgSelection';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { UserClientRolesType } from '../../types/UserRoleType';
import * as redux from 'react-redux'

const clientRoles: UserClientRolesType[] = [
  {
    clientId: '00012797',
    roles: ['ONE', 'TWO', 'THREE'],
    clientName: 'MINISTRY OF FORESTS'
  }
];

const state = {
  userDetails: {
    id: 1,
    name: 'User',
    user: {
      firstName: 'John',
      lastName: 'Doe',
      providerUsername: 'johndoe123',
      clientRoles: clientRoles
    },
    loading: false,
    error: null
  },
};

vi.spyOn(redux, 'useSelector')
  .mockImplementation((callback) => callback(state));

describe('LoginOrgSelection', () => {
  it('renders organization selection view with user details', () => {
    const qc = new QueryClient();

    render(
      <QueryClientProvider client={qc}>
        <Provider store={store}>
          <LoginOrgSelection />
        </Provider>
      </QueryClientProvider>
    );

    // Check if elements are rendered correctly
    expect(screen.getByText('Organization selection')).toBeDefined();
    expect(screen.getByText('John Doe (johndoe123) select which organization you\'re representing.')).toBeDefined();
  });
});
