import React from 'react';
import { describe, expect, it, vi } from 'vitest';
import { render, waitFor } from '@testing-library/react';
import Opening from '../../screens/Opening';
import PaginationContext from '../../contexts/PaginationContext';
import { BrowserRouter } from 'react-router-dom';
import * as redux from 'react-redux';
import { RecentOpening } from '../../types/RecentOpening';

const data = {
  "activityType": "Update",
  "openingId": "1541297",
  "statusCode": "APP",
  "statusDescription": "Approved",
  "lastUpdatedLabel": "1 minute ago",
  "lastUpdated": "2024-05-16T19:59:21.635Z"
};

vi.mock('../../services/SecretsService', () => ({
  getWmsLayersWhitelistUsers: vi.fn(() => [
    {
      userName: 'TEST'
    }
  ])
}));

vi.mock('../../services/OpeningService', () => ({
  fetchRecentOpenings: vi.fn(() => [
    {
      id: '123',
      openingId: '111',
      fileId: 'FS7',
      cuttingPermit: 'SS',
      timberMark: '207S',
      cutBlock: '111',
      grossAreaHa: 265,
      statusDesc: 'Approved',
      categoryDesc: 'FTML',
      disturbanceStart: '2023-01-02',
      entryTimestamp: '',
      updateTimestamp: ''
    }
  ]),
  fetchOpeningsPerYear: vi.fn(() => Promise.resolve([
    { group: '2022', key: 'Openings', value: 10 },
    { group: '2023', key: 'Openings', value: 15 },
  ])),
  fetchFreeGrowingMilestones: vi.fn(() => Promise.resolve([
    {
      group: '1-5',
      value: 11
    }
  ])),
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

const state = {
  userDetails: {
    id: 1,
    name: 'User'
  }
};

vi.spyOn(redux, 'useSelector')
  .mockImplementation((callback) => callback(state));

const rows: RecentOpening[] = [{
  id: '123',
  openingId: '123',
  fileId: '1',
  cuttingPermit: '1',
  timberMark: '1',
  cutBlock: '1',
  grossAreaHa: 1,
  statusDesc: 'Approved',
  categoryDesc: 'Another:Another',
  disturbanceStart: '1',
  entryTimestamp: '1',
  updateTimestamp: '1',
}];
  
const paginationValueMock = {
  getCurrentData: () => rows,
  currentPage: 0,
  totalPages: 0,
  handlePageChange: vi.fn(),
  handleItemsPerPageChange: vi.fn(),
  itemsPerPage: 5,
  setPageData: vi.fn(),
  setInitialItemsPerPage: vi.fn(),
};

describe('Opening screen test cases', () => {
  it('should renders Opening Page Title component', async () => {
    const { getByTestId } = render(
      <BrowserRouter>
        <PaginationContext.Provider value={paginationValueMock}>
          <Opening />
        </PaginationContext.Provider>
      </BrowserRouter>
    );

    const pageTitleComp = await waitFor(() => getByTestId('opening-pagetitle'));
    expect(pageTitleComp).toBeDefined();

    //const subtitle = 'Create, manage or check opening information';
    //expect(screen.getByText(subtitle)).toBeDefined();
  });
});
