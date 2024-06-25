import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import BCHeader from '../../components/BCHeader';
import { ThemePreference } from '../../utils/ThemePreference';
import { BrowserRouter } from 'react-router-dom';

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(), // Deprecated
    removeListener: vi.fn(), // Deprecated
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn()
  }))
});

describe('BC Header component tests', () => {
  it('should have a Header with proper class name', () => {
    const { getByTestId, getByText } = render(
      <BrowserRouter>
        <ThemePreference>
          <BCHeader />
        </ThemePreference>
      </BrowserRouter>
    );

    const element: HTMLElement | null = getByTestId('bc-header__header');
    expect(element).toBeDefined();
    expect(element).not.toBeNull();
    expect(element?.classList.contains('spar-header')).toBe(true);
  });
});
