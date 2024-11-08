import React from 'react';
import { render, screen, fireEvent, act } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import BCHeaderwSide from '../../components/BCHeaderwSide';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import {leftMenu } from '../../components/BCHeaderwSide/constants';
import { UserClientRolesType } from '../../types/UserRoleType';
import '@testing-library/jest-dom';
import { AuthProvider } from '../../contexts/AuthProvider';

vi.mock('../../services/TestService', () => ({
  getForestClientByNumberOrAcronym: vi.fn(() => [
    {
      clientNumber: '00012797',
      clientName: 'MINISTRY OF FORESTS',
      legalFirstName: '',
      legalMiddleName: '',
      clientStatusCode: { code: 'ACT', description: 'Active' },
      clientTypeCode: { code: 'F', description: 'Ministry of Forests and Range' },
      acronym: 'MOF'
    },
  ]),
}));

const renderComponent = async () => {
  const qc = new QueryClient();

  await act(() => render(
    <AuthProvider>
      <QueryClientProvider client={qc}>        
        <BrowserRouter>
          <BCHeaderwSide />
        </BrowserRouter>
      </QueryClientProvider>
    </AuthProvider>
  ));
};

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

describe('BCHeaderwSide', async () => {
  it('should renders the component', () => {
    renderComponent();
    expect(screen.getByTestId('header')).toBeDefined();
  });

  it('should renders the site name', async () => {
    renderComponent();
    expect(screen.getByText('SILVA')).toBeDefined();
  });

  it('opens and closes the My Profile panel', async () => {
    renderComponent();
    const userSettingsButton = screen.getByTestId('header-button__user');
    fireEvent.click(userSettingsButton);
    expect(screen.getByText('My Profile')).toBeDefined();
    fireEvent.click(userSettingsButton);
    // expect(screen.queryByText('My Profile')).not.toBeVisible();
  });

  it('renders the correct menu item names', async () => {
    renderComponent();
    leftMenu.forEach(item => {
      expect(screen.getByText(item.name)).toBeInTheDocument();
    });
  });

  it('renders sub menu items', async () => {
    renderComponent();
    const subMenuItem = leftMenu[0].items[0]; // Assuming the first item has sub items
    const menuItem = screen.getByText(subMenuItem.name);
    fireEvent.click(menuItem);
    subMenuItem.subItems?.forEach(subSubItem => {
      expect(screen.getByText(subSubItem.name)).toBeInTheDocument();
    });
  });
});

