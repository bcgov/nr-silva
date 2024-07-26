import React from 'react';
import { describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { Provider } from 'react-redux'; // Import Provider
import store from '../../store';
import Dashboard from '../../screens/Dashboard';
import * as redux from 'react-redux'

const state = {
  userDetails: {
    user: {
      firstName: 'Test'
    },
    loading: false,
    error: null
  },
  selectedClientRoles: {

  }
};

vi.spyOn(redux, 'useSelector')
  .mockImplementation((callback) => callback(state));

describe('Dashboard screen test cases', () => {
  it('should render the dashboard component', () => {
    render(
      <Provider store={store}>
        <MemoryRouter>
          <Dashboard />
        </MemoryRouter>
      </Provider>
    );
  
    const headerElement = screen.getByText(/welcome to the SILVA/i);
    expect(headerElement).toBeDefined();
  })
});
