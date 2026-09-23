import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, vi, expect, beforeEach } from 'vitest';
import OpeningsSearch from '../../screens/OpeningsSearch';
import { renderWithProviders } from '../utils/testAuthProvider';
import * as openingParamUtils from '../../utils/OpeningSearchParamsUtils';
import * as OpeningSearchService from '../../services/OpeningSearchService';

// Mock child components
vi.mock('@/components/OpeningsMap', () => ({
  default: ({ openingIds }: { openingIds: number[] }) => (
    <div data-testid="openings-map">Map with {openingIds.length} openings</div>
  ),
}));

vi.mock('@/components/FavouriteOpenings/FavOpeningSection', () => ({
  default: () => <div data-testid="fav-openings">Favorites</div>,
}));

vi.mock('@/hooks/usePolygonAvailability', () => ({
  default: () => ({ isAvailable: true, isLoading: false }),
}));

vi.mock('@/components/OpeningBookmarkBtn', () => ({
  default: () => <button data-testid="bookmark-btn">Bookmark</button>,
}));

vi.mock('@/services/API', () => ({
  default: {
    CodesEndpointService: {
      getOpeningOrgUnits: vi.fn().mockResolvedValue([]),
      getOpeningCategories: vi.fn().mockResolvedValue([]),
      getOpeningCategoryCodes: vi.fn().mockResolvedValue([]),
      getOpeningStatusCodes: vi.fn().mockResolvedValue([]),
    },
    ForestClientEndpointService: {
      searchByClientNumbers: vi.fn().mockResolvedValue([]),
      searchForestClients: vi.fn().mockResolvedValue([]),
    },
  },
}));

vi.mock('../../services/OpeningSearchService', () => ({
  openingSearch: vi.fn(),
}));

const mockOpeningData = {
  content: [
    {
      openingId: 101,
      openingCategory: { code: 'FTML', description: 'Category 1' },
      status: { code: 'APP', description: 'Status 1' },
      orgUnit: { code: 'DCS', description: 'District 1' },
      forestFileId: 'LIC001',
      cuttingPermitId: 'CP1',
      cutBlockId: 'CB1',
      timberMark: 'TM1',
      updateTimestamp: '2024-01-01',
    },
  ],
  page: { totalElements: 1, size: 10, page: 0, totalPages: 1 },
};

describe('OpeningsSearch screen', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.spyOn(openingParamUtils, 'readOpeningSearchUrlParams').mockReturnValue({});
    vi.spyOn(OpeningSearchService, 'openingSearch').mockResolvedValue(mockOpeningData as any);
  });

  it('renders title and action buttons', async () => {
    const { wrapper } = renderWithProviders();
    render(<OpeningsSearch />, { wrapper });

    expect(screen.getByText('Openings Search')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Clear all/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Search/i })).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });

  it('pre-fills filters from URL params and executes query on mount', async () => {
    vi.spyOn(openingParamUtils, 'readOpeningSearchUrlParams').mockReturnValue({
      openingId: 101,
      page: 0,
      size: 10,
    });

    const { wrapper } = renderWithProviders();
    render(<OpeningsSearch />, { wrapper });

    await waitFor(() => {
      expect(OpeningSearchService.openingSearch).toHaveBeenCalledWith(
        expect.objectContaining({ openingId: 101, page: 0, size: 10 })
      );
      expect(screen.getByText('Search results')).toBeInTheDocument();
      expect(screen.getByText('101')).toBeInTheDocument();
    });
  });

  it('triggers search when Search button is clicked with active filters', async () => {
    const updateSpy = vi.spyOn(openingParamUtils, 'updateOpeningSearchUrlParams');
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(<OpeningsSearch />, { wrapper });

    const openingIdInput = screen.getByPlaceholderText('Enter opening ID');
    fireEvent.change(openingIdInput, { target: { value: '101' } });
    fireEvent.blur(openingIdInput);

    const searchBtn = screen.getByRole('button', { name: /Search/i });
    await user.click(searchBtn);

    await waitFor(() => {
      expect(updateSpy).toHaveBeenCalledWith(
        expect.objectContaining({ openingId: 101, page: 0, size: 20 })
      );
      expect(OpeningSearchService.openingSearch).toHaveBeenCalledWith(
        expect.objectContaining({ openingId: 101 })
      );
    });
  });

  it('does not trigger search when Search button is clicked with no active filters', async () => {
    const updateSpy = vi.spyOn(openingParamUtils, 'updateOpeningSearchUrlParams');
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(<OpeningsSearch />, { wrapper });

    const searchBtn = screen.getByRole('button', { name: /Search/i });
    await user.click(searchBtn);

    expect(updateSpy).not.toHaveBeenCalled();
    expect(OpeningSearchService.openingSearch).not.toHaveBeenCalled();
  });

  it('resets search parameters when Clear all is clicked', async () => {
    const updateSpy = vi.spyOn(openingParamUtils, 'updateOpeningSearchUrlParams');
    vi.spyOn(openingParamUtils, 'readOpeningSearchUrlParams').mockReturnValue({
      openingId: 101,
    });

    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(<OpeningsSearch />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
    });

    const clearBtn = screen.getByRole('button', { name: /Clear all/i });
    await user.click(clearBtn);

    expect(updateSpy).toHaveBeenCalledWith(undefined);
    expect(screen.queryByText('Search results')).not.toBeInTheDocument();
  });

  it('handles row selection to show and hide map', async () => {
    vi.spyOn(openingParamUtils, 'readOpeningSearchUrlParams').mockReturnValue({
      openingId: 101,
    });

    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(<OpeningsSearch />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
    });

    expect(screen.queryByTestId('openings-map')).not.toBeInTheDocument();

    const spatialBtn = screen.getByRole('button', { name: /Click to view this opening on the map/i });
    await user.click(spatialBtn);

    expect(screen.getByTestId('openings-map')).toBeInTheDocument();

    await user.click(spatialBtn);
    expect(screen.queryByTestId('openings-map')).not.toBeInTheDocument();
  });
});
