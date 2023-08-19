import React from 'react';
import { render, screen, act } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Help from '../../screens/Help';

test('renders help page header', () => {
  render(<Help />, { wrapper: MemoryRouter });
  const headerElement = screen.getByText('Help Page');
  expect(headerElement).toBeInTheDocument();
});

test('renders help page content', () => {
  render(<Help />, { wrapper: MemoryRouter });
  const contentElement = screen.getByText(/Welcome to the Help Page/i);
  expect(contentElement).toBeInTheDocument();
});

test('navigates back to home page on button click', () => {
  act(() => {
    render(<Help />, { wrapper: MemoryRouter });
  });

  act(() => {
    const backButton = screen.getByRole('button', { name: /Back to Home/i });
    backButton.click();
  });

  // Add assertions to test if navigation to the home page occurs
  expect(location.pathname).toBe('/');
});
