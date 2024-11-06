import React from 'react';
import { describe, expect, it, vi } from 'vitest';
import { render, waitFor, act } from '@testing-library/react';
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

  describe('FavoriteCards test cases', () => {

    it('should render FavoriteCard component', async () => {
      
      let container: HTMLElement = document.createElement('div');
      await act(async () => {
      ({ container } = render(
        <BrowserRouter>
          <PaginationContext.Provider value={paginationValueMock}>
            <Opening />
          </PaginationContext.Provider>
        </BrowserRouter>
      ));
    });
      
      // check if first FavoriteCard has the correct title and is active      
      expect(container.querySelector('#fav-card-1')).toBeDefined();
      expect(container.querySelector('#fav-card-1')?.textContent).toContain('Silviculture search');
      expect(container.querySelector('#fav-card-1')?.className).not.contain('cds--link--disable');
      
      // check if the second FavoriteCard has the correct title and is inactive
      expect(container.querySelector('#fav-card-2')).toBeDefined();
      expect(container.querySelector('#fav-card-2')?.textContent).toContain('Create an opening');
      expect(container.querySelector('#fav-card-2')?.className).toContain('cds--link--disable');
      
      // check if the third FavoriteCard has the correct title and is inactive
      expect(container.querySelector('#fav-card-3')).toBeDefined();
      expect(container.querySelector('#fav-card-3')?.textContent).toContain('Reports');
      expect(container.querySelector('#fav-card-3')?.className).toContain('cds--link--disable');

      // check if the fourth FavoriteCard has the correct title and is inactive
      expect(container.querySelector('#fav-card-4')).toBeDefined();
      expect(container.querySelector('#fav-card-4')?.textContent).toContain('Upcoming activities');
      expect(container.querySelector('#fav-card-4')?.className).toContain('cds--link--disable');

    });

    it('should not render tab when not selected', async () => {
      let container: HTMLElement = document.createElement('div');
      let getByText: any;
      await act(async () => {
      ({ container, getByText } = render(
        <BrowserRouter>
          <PaginationContext.Provider value={paginationValueMock}>
            <Opening />
          </PaginationContext.Provider>
        </BrowserRouter>
      ));
    });

      // check if the tab is not rendered
      expect(container.querySelector('div.tab-openings')?.childNodes).toHaveLength(2);
      expect(container.querySelector('div.tab-metrics')?.childNodes).toHaveLength(0);

      await act(async () => getByText('Dashboard').click());

      expect(container.querySelector('div.tab-openings')?.childNodes).toHaveLength(2);
      expect(container.querySelector('div.tab-metrics')?.childNodes).toHaveLength(1);

    });


  });

});
