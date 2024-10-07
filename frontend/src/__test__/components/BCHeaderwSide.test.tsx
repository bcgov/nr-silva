import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import BCHeaderwSide from '../../components/BCHeaderwSide';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import {leftMenu } from '../../components/BCHeaderwSide/constants';
import * as redux from 'react-redux';
import { Provider } from 'react-redux';
import store from '../../store';
import { UserClientRolesType } from '../../types/UserRoleType';
import '@testing-library/jest-dom';

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

const renderComponent = () => {
  const qc = new QueryClient();

  render(
    <QueryClientProvider client={qc}>
      <Provider store={store}>
        <BrowserRouter>
          <BCHeaderwSide />
        </BrowserRouter>
      </Provider>
    </QueryClientProvider>
  );
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

vi.spyOn(redux, 'useSelector')
  .mockImplementation((callback) => callback(state));

describe('BCHeaderwSide', () => {
  it('should renders the component', () => {
    renderComponent();
    expect(screen.getByTestId('header')).toBeDefined();
  });

  it('should renders the site name', () => {
    renderComponent();
    expect(screen.getByText('SILVA')).toBeDefined();
  });

  it('opens and closes the My Profile panel', () => {
    renderComponent();
    const userSettingsButton = screen.getByTestId('header-button__user');
    fireEvent.click(userSettingsButton);
    expect(screen.getByText('My Profile')).toBeDefined();
    fireEvent.click(userSettingsButton);
    // expect(screen.queryByText('My Profile')).not.toBeVisible();
  });

  // it('renders the correct number of top-level menu items', () => {
  //   renderComponent();
  //   const menuItems = screen.getAllByRole('button', { name: /.*Category.*/ });
  //   expect(menuItems).toHaveLength(leftMenu.length);
  // });

  it('renders the correct menu item names', () => {
    renderComponent();
    leftMenu.forEach(item => {
      expect(screen.getByText(item.name)).toBeInTheDocument();
    });
  });

  it('renders sub menu items', () => {
    renderComponent();
    const subMenuItem = leftMenu[0].items[0]; // Assuming the first item has sub items
    const menuItem = screen.getByText(subMenuItem.name);
    fireEvent.click(menuItem);
    subMenuItem.subItems?.forEach(subSubItem => {
      expect(screen.getByText(subSubItem.name)).toBeInTheDocument();
    });
  });
});

