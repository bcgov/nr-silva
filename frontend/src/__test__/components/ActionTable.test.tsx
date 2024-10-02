import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import ActionsTable from '../../components/ActionsTable';
import { RecentAction } from '../../types/RecentAction';
import { ITableHeader } from '../../types/TableHeader';

const mockHeaders: ITableHeader[] = [
  { key: 'activityType', header: 'Activity' },
  { key: 'openingId', header: 'Opening ID' },
  { key: 'statusCode', header: 'Status' },
  { key: 'lastUpdatedLabel', header: 'Last Updated' }
];

const mockRows: RecentAction[] = [
  {
    activityType: 'Update',
    openingId: '58993',
    statusCode: 'APP',
    statusDescription: 'Approved',
    lastUpdated: '2023-10-26T16:12:05.921Z',
    lastUpdatedLabel: '1 minute ago',
    createdAt: '2023-10-26T16:12:05.921Z'
  }
];

describe('ActionsTable Component', () => {
  it('should render table headers correctly', () => {
    render(<ActionsTable rows={[]} headers={mockHeaders} />);

    mockHeaders.forEach((header) => {
      expect(screen.getByText(header.header)).toBeDefined();
    });
  });

  it('should render table rows and cell content correctly', () => {
    render(<ActionsTable rows={mockRows} headers={mockHeaders} />);

    mockRows.forEach((row) => {
      // Activity type (Either ActivityTag or StatusTag)
      const element = screen.getByText(row.activityType);
      expect(element).toBeDefined();

      // Opening Id
      const openingIdEl = screen.getByText(row.openingId);
      expect(openingIdEl).toBeDefined();

      // Status
      const statusEl = screen.getByText(row.statusCode);
      expect(statusEl).toBeDefined();

      // Last update
      const lastUpdateEl = screen.getByText(row.lastUpdatedLabel);
      expect(lastUpdateEl).toBeDefined();
    });
  });
});
