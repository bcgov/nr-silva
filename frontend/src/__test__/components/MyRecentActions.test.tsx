import React from 'react';
import { render, act, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import MyRecentActions from '../../components/MyRecentActions';

const data = {
  "activityType": "Update",
  "openingId": "1541297",
  "statusCode": "APP",
  "statusDescription": "Approved",
  "lastUpdatedLabel": "1 minute ago",
  "lastUpdated": "2024-05-16T19:59:21.635Z"
};

vi.mock('../../services/OpeningService', () => ({
  fetchRecentActions: vi.fn(() => [
    {
      activityType: data.activityType,
      openingId: data.openingId.toString(),
      statusCode: data.statusCode,
      statusDescription: data.statusDescription,
      lastUpdated: data.lastUpdated,
      lastUpdatedLabel: data.lastUpdatedLabel
    }
  ]),
}));

describe('My Recent Actions component tests', () => {
  it('should render the recent action component', async () => {
    const { getByTestId, getByText } = render(
      <MyRecentActions />
    );
    const element: HTMLElement | null = await waitFor(() => getByTestId('my-recent-actions__recent-tab-header'));
    expect(element).toBeDefined();
    expect(element).not.toBeNull();
    expect(element?.classList.contains('tab-header-recent')).toBe(true);
    
    const text = 'Recent';
    act(() => {
      const elementText: HTMLElement = getByText(text);
      expect(elementText.textContent).toEqual(text);
    });
  });

  it('should render the files and tabs action component', async () => {
    const { getByTestId, getByText } = render(
      <MyRecentActions />
    );
    const element: HTMLElement | null = await waitFor(() => getByTestId('my-recent-actions__files-tab-header'));
    expect(element).toBeDefined();
    expect(element).not.toBeNull();
    expect(element?.classList.contains('tab-header-recent')).toBe(true);
    
    act(() => {
      const text = 'Files and Docs';
      const elementText: HTMLElement = getByText(text);
      expect(elementText.textContent).toEqual(text);
    });
  });
});
