import React from 'react';
import { render, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import BCHeader from '../../components/BCHeader';
import { ThemePreference } from '../../utils/ThemePreference';

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
  it('should have a Header with proper class name', async () => {
    const { getByTestId } = render(
      <ThemePreference>
        <BCHeader />
      </ThemePreference>
    );

    const element: HTMLElement | null = await waitFor(() => getByTestId('bc-header__header'));
    expect(element).toBeDefined();
    expect(element).not.toBeNull();
    expect(element?.classList.contains('bcgov-header')).toBe(true);
  });
});
