import React from 'react';
import { render, screen, act } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Help from '../../screens/Help';
import { describe, expect, it } from 'vitest';

describe('Help component test cases', () => {
  it('should renders the help page header', () => {
    render(<Help />, { wrapper: MemoryRouter });
    const headerElement = screen.getByText('Help Page');
    expect(headerElement).not.toBeNull();
  });
  
  it('should renders the help page content', () => {
    render(<Help />, { wrapper: MemoryRouter });
    const contentElement = screen.getByText(/Welcome to the Help Page/i);
    expect(contentElement).not.toBeNull();
  });
  
  it('should navigates back to home page on button click', () => {
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
});
