import React from 'react';
import { describe, expect, it, vi } from 'vitest';
import { render, waitFor, act, screen } from '@testing-library/react';
import Opening from '../../screens/Opening';
import PaginationContext from '../../contexts/PaginationContext';
import { NotificationProvider } from '../../contexts/NotificationProvider';
import { BrowserRouter } from 'react-router-dom';
import { RecentOpening } from '../../types/RecentOpening';
import { fetchFreeGrowingMilestones, fetchUserSubmissionTrends, fetchRecentActions } from '../../services/OpeningService';
import { fetchOpeningFavourites } from '../../services/OpeningFavouriteService';
import { AuthProvider } from '../../contexts/AuthProvider';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const data = {
  "activityType": "Update",
  "openingId": "1541297",
  "statusCode": "APP",
  "statusDescription": "Approved",
  "lastUpdatedLabel": "1 minute ago",
  "lastUpdated": "2024-05-16T19:59:21.635Z"
};

vi.mock('../../services/OpeningFavouriteService', () => ({
  fetchOpeningFavourites: vi.fn(),
}));

vi.mock('../../services/OpeningService', async () => {
  const actual = await vi.importActual('../../services/OpeningService');
  return {
    ...actual,
    fetchUserSubmissionTrends: vi.fn(),
    fetchFreeGrowingMilestones: vi.fn(),
    fetchRecentActions: vi.fn(),
  };
});

const state = {
  userDetails: {
    id: 1,
    name: 'User'
  }
};

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

const queryClient = new QueryClient();

describe('Opening screen test cases', () => {

  beforeEach(() => {
    vi.clearAllMocks();
    (fetchUserSubmissionTrends as vi.Mock).mockResolvedValue([
      { group: '2022', key: 'Openings', value: 10 },
      { group: '2023', key: 'Openings', value: 15 },
    ]);
    (fetchFreeGrowingMilestones as vi.Mock).mockResolvedValue([{ group: '1-5', value: 11 }]);
    (fetchOpeningFavourites as vi.Mock).mockResolvedValue([1, 2, 3]);
    (fetchRecentActions as vi.Mock).mockResolvedValue([data]);

  });

  it('should renders Opening Page Title component', async () => {
    const { getByTestId } = render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PaginationContext.Provider value={paginationValueMock}>
            <NotificationProvider>
              <AuthProvider>
                <Opening />
              </AuthProvider>
            </NotificationProvider>
          </PaginationContext.Provider>
        </QueryClientProvider>
      </BrowserRouter>
    );

    const pageTitleComp = await waitFor(() => getByTestId('opening-pagetitle'));
    expect(pageTitleComp).toBeDefined();

    //const subtitle = 'Create, manage or check opening information';
    //expect(screen.getByText(subtitle)).toBeDefined();
  });

  it('should render Opening Page with dashboard tab selected', async () => {
    window.history.pushState({}, 'Opening page with metrics', '/opening?tab=metrics');

    await act(async () => render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PaginationContext.Provider value={paginationValueMock}>
            <NotificationProvider>
              <AuthProvider>
                <Opening />
              </AuthProvider>
            </NotificationProvider>
          </PaginationContext.Provider>
        </QueryClientProvider>
      </BrowserRouter>
    ));

    const headings = screen.getAllByText('Dashboard');
    expect(headings).toHaveLength(2);
  });

  describe('FavoriteCards test cases', () => {

    it('should render FavoriteCard component', async () => {

      let container: HTMLElement = document.createElement('div');
      await act(async () => {
        ({ container } = render(
          <BrowserRouter>
            <QueryClientProvider client={queryClient}>
              <PaginationContext.Provider value={paginationValueMock}>
                <NotificationProvider>
                  <AuthProvider>
                    <Opening />
                  </AuthProvider>
                </NotificationProvider>
              </PaginationContext.Provider>
            </QueryClientProvider>
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
      window.history.pushState({}, 'Opening page with metrics', '/opening?tab=openings');
      let container: HTMLElement = document.createElement('div');
      let getByText: any;
      await act(async () => {
        ({ container, getByText } = render(
          <BrowserRouter>
            <QueryClientProvider client={queryClient}>
              <PaginationContext.Provider value={paginationValueMock}>
                <NotificationProvider>
                  <AuthProvider>
                    <Opening />
                  </AuthProvider>
                </NotificationProvider>
              </PaginationContext.Provider>
            </QueryClientProvider>
          </BrowserRouter>
        ));
      });

      // check if the tab is not rendered
      expect(container.querySelector('div.tab-openings')?.childNodes).toHaveLength(2);
      expect(container.querySelector('div.tab-metrics')?.childNodes).toHaveLength(0);

    });

    it('should render tab only when selected', async () => {
      window.history.pushState({}, 'Opening page with metrics', '/opening');

      let container: HTMLElement = document.createElement('div');
      let getByText: any;
      await act(async () => {
        ({ container, getByText } = render(
          <BrowserRouter>
            <QueryClientProvider client={queryClient}>
              <PaginationContext.Provider value={paginationValueMock}>
                <NotificationProvider>
                  <AuthProvider>
                    <Opening />
                  </AuthProvider>
                </NotificationProvider>
              </PaginationContext.Provider>
            </QueryClientProvider>
          </BrowserRouter>
        ));
      });

      await act(async () => getByText('Dashboard').click());

      expect(container.querySelector('div.tab-openings')?.childNodes).toHaveLength(0);
      expect(container.querySelector('div.tab-metrics')?.childNodes).toHaveLength(1);

    });


  });

});
