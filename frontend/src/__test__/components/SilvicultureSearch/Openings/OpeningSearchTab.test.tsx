import React from 'react';
import { render, screen, act, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';
import OpeningSearchTab from '../../../../components/SilvicultureSearch/Openings/OpeningsSearchTab';
import { QueryClient, QueryClientProvider, useQuery } from '@tanstack/react-query';
import { OpeningsSearchProvider, useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import { useOpeningsQuery } from "../../../../services/queries/search/openingQueries";
import { BrowserRouter } from 'react-router-dom';
import { NotificationProvider } from '../../../../contexts/NotificationProvider';
import PaginationProvider from '../../../../contexts/PaginationProvider';

const data = [{
  id: '114207',
  openingId: '114207',
  fileId: 'TFL47',
  cuttingPermit: '12S',
  timberMark: '47/12S',
  cutBlock: '12-69',
  grossAreaHa: '12.9',
  status: 'Free growing',
  category: 'FTML',
  disturbanceStart: '-',
  createdAt: '2022-10-27',
  orgUnit: 'DCC - Cariboo chilcotin natural resources',
  lastViewed: '2022-10-27'
}];

vi.mock("../../../../services/queries/search/openingQueries",  async () => {
  const actual = await vi.importActual<typeof import("../../../../services/queries/search/openingQueries")>("../../../../services/queries/search/openingQueries");
  return {
    ...actual,
    useOpeningsQuery: vi.fn(),
  };
});

describe('OpeningSearchTab', () => {
  const queryClient = new QueryClient(); // here's the problem
  
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render without crashing', () => {
    (useOpeningsQuery as vi.Mock).mockReturnValue({ data: [], isLoading: false });
    const { getByText} = render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PaginationProvider>
            <OpeningsSearchProvider>
              <NotificationProvider>
                <OpeningSearchTab />              
              </NotificationProvider>
            </OpeningsSearchProvider>
          </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );
    expect(getByText('Advanced Search')).toBeInTheDocument();
  });

  it('should display the empty state', () => {
    (useOpeningsQuery as vi.Mock).mockReturnValue({ data: [], isLoading: false });
    const { getByText} = render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PaginationProvider>
            <OpeningsSearchProvider>
              <NotificationProvider>
                <OpeningSearchTab />              
              </NotificationProvider>
            </OpeningsSearchProvider>
          </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );
    expect(screen.getByText('Nothing to show yet')).toBeInTheDocument();
  });

  it('should have a search input field', () => {
    (useOpeningsQuery as vi.Mock).mockReturnValue({ data: [], isLoading: false });
    render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PaginationProvider>
            <OpeningsSearchProvider>
              <NotificationProvider>
                <OpeningSearchTab />              
              </NotificationProvider>
            </OpeningsSearchProvider>
          </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );
    expect(screen.getByPlaceholderText('Search by opening ID, opening number, timber mark or file ID')).toBeInTheDocument();
  });

  it('should display search results when search is performed', async () => {
    (useOpeningsQuery as vi.Mock).mockReturnValue({ data, isFetching: false });
    
    const { container } = render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PaginationProvider>
            <OpeningsSearchProvider>
              <NotificationProvider>
                <OpeningSearchTab />              
              </NotificationProvider>
            </OpeningsSearchProvider>
          </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );
    const searchInput = screen.getByPlaceholderText('Search by opening ID, opening number, timber mark or file ID') as HTMLInputElement;    
    await act(async () => userEvent.type(searchInput, 'test'));
    await act(async () => (await screen.findByTestId('search-button')).click());
    await act(async () => await screen.findByText('Actions'));
  });

  it('should display a message when no results are found', async () => {
    (useOpeningsQuery as vi.Mock).mockReturnValue({ data: [], isLoading: false });
    
    render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PaginationProvider>
            <OpeningsSearchProvider>
              <NotificationProvider>
                <OpeningSearchTab />              
              </NotificationProvider>
            </OpeningsSearchProvider>
          </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );
    const searchInput = screen.getByPlaceholderText('Search by opening ID, opening number, timber mark or file ID');
    await act(async () => await userEvent.type(searchInput, 'potato'));
    await act(async () => (await screen.findByTestId('search-button')).click());
    expect(screen.getByText('Results not found')).toBeInTheDocument();
  });
});