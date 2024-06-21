import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import MyRecentActions from '../../components/MyRecentActions';

describe('My Recent Actions component tests', () => {
  it('should render the recent action component', () => {
    const { getByTestId } = render(
      <MyRecentActions />
    );

    const element: HTMLElement | null = getByTestId('my-recent-actions__recent-tab-header');
    expect(element).toBeDefined();
    expect(element.innerHTML).toBe('Recent');
  });

  it('should render the files and tabs action component', () => {
    const { getByTestId } = render(
      <MyRecentActions />
    );

    const element: HTMLElement | null = getByTestId('my-recent-actions__files-tab-header');
    expect(element).toBeDefined();
    expect(element.innerHTML).toBe('Files and Docs');
  });
});
