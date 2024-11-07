import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import ThemeToggle from '../../components/ThemeToggle';

describe('Theme toggle component tests', () => {
  it('should render correctly', () => {
    const { container } = render(<ThemeToggle />);
    expect(container).toMatchSnapshot();
  });

  it('should toggle theme when button is clicked', () => {
    const { container } = render(<ThemeToggle />);
    const button = container.querySelector('button');
    button?.click();
    expect(container).toMatchSnapshot();
  });
});
