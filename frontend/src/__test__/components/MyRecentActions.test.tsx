import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import MyRecentActions from '../../components/MyRecentActions';

describe('My Recent Actions component tests', () => {
  it('should render the recent action component', () => {
    const { getByTestId, getByText } = render(
      <MyRecentActions />
    );
    const text = 'Recent';

    const element: HTMLElement | null = getByTestId('my-recent-actions__recent-tab-header');
    expect(element).toBeDefined();
    expect(element).not.toBeNull();
    expect(element?.classList.contains('tab-header-recent')).toBe(true);
    const elementText: HTMLElement = getByText(text);
    expect(elementText.textContent).toEqual(text);
  });

  it('should render the files and tabs action component', () => {
    const { getByTestId, getByText } = render(
      <MyRecentActions />
    );
    const text = 'Files and Docs';

    const element: HTMLElement | null = getByTestId('my-recent-actions__files-tab-header');
    expect(element).toBeDefined();
    expect(element).not.toBeNull();
    expect(element?.classList.contains('tab-header-recent')).toBe(true);
    const elementText: HTMLElement = getByText(text);
    expect(elementText.textContent).toEqual(text);
  });
});
