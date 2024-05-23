import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { Provider } from 'react-redux'; // Import Provider
import store from '../../store'; // Import your Redux store
import Dashboard from '../../screens/Dashboard';


test('renders dashboard page header', () => {
  render(
    <Provider store={store}>
      <MemoryRouter>
        <Dashboard />
      </MemoryRouter>
    </Provider>
  );

  const headerElement = screen.getByText(/welcome to the SILVA/i);
  expect(headerElement).toBeInTheDocument();
});
