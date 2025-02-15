import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import ThemeToggle from '../../components/ThemeToggle';
import { ThemePreference } from '../../utils/ThemePreference';

beforeAll(() => {
  Object.defineProperty(window, "matchMedia", {
    writable: true,
    value: vi.fn().mockImplementation((query) => ({
      matches: false,
      media: query,
      onchange: null,
      addEventListener: vi.fn(),
      removeEventListener: vi.fn(),
      addListener: vi.fn(),
      removeListener: vi.fn(),
      dispatchEvent: vi.fn(),
    })),
  });
});

describe('Theme toggle component tests', () => {
  const renderWithThemeProvider = (component: JSX.Element) =>
    render(<ThemePreference>{component}</ThemePreference>);

  it('should render correctly', () => {
    const { container } = renderWithThemeProvider(<ThemeToggle />);
    expect(container).toMatchSnapshot();
  });

  it('should toggle theme when button is clicked', () => {
    const { container } = renderWithThemeProvider(<ThemeToggle />);
    const button = container.querySelector('button');
    button?.click();
    expect(container).toMatchSnapshot();
  });
});
