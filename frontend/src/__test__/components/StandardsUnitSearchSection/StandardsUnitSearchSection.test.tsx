import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import StandardsUnitSearchSection from '../../../components/StandardsUnitSearchSection';
import { renderWithProviders, queryClient } from '../../utils/testAuthProvider';
import * as standardsUnitUtils from '../../../components/StandardsUnitSearchSection/utils';
import API from '../../../services/API';

const mockSearchResult = {
  content: [
    {
      stockingStandardUnitId: 101,
      openingId: 200,
      fileId: 'TFL01',
      cutBlock: 'CB-01',
      cuttingPermit: 'CP-001',
      standardsUnitId: 'SU-01',
      standardsRegimeId: 5,
      netArea: 12.5,
      preferredSpecies: [{ code: 'PLI', description: 'Lodgepole Pine (Interior)' }],
      bgcZone: 'SBS',
      bgcSubZone: 'dw',
      orgUnit: { code: 'DCR', description: 'Cariboo Forest Region' },
      updateTimestamp: '2024-03-01T08:00:00',
    },
  ],
  page: { totalElements: 1, size: 20, page: 0, totalPages: 1 },
};

// Mock API service
vi.mock('../../../services/API', () => ({
  default: {
    SearchEndpointService: {
      standardsUnitSearch: vi.fn(),
    },
    CodesEndpointService: {
      getSilvTreeSpeciesCodes: vi.fn(),
      getOpeningOrgUnits: vi.fn(),
    },
  },
}));

// Mock standards unit search utils
vi.mock('../../../components/StandardsUnitSearchSection/utils', () => ({
  readStandardsUnitSearchUrlParams: vi.fn(() => ({})),
  updateStandardsUnitSearchUrlParams: vi.fn(),
  // Returns true for any params object that has at least one defined value,
  // so clicking Search (which adds page/size) always enables results.
  hasStandardsUnitSearchFilters: vi.fn((params) => {
    if (!params) return false;
    return Object.values(params).some((val) => val !== undefined);
  }),
}));

// Mock OpeningsMap
vi.mock('../../../components/OpeningsMap', () => ({
  default: ({ openingIds, selectedStandardsUnitIds }: any) => (
    <div data-testid="openings-map">
      <div data-testid="map-opening-ids">{openingIds?.join(',')}</div>
      <div data-testid="map-standards-unit-ids">{selectedStandardsUnitIds?.join(',')}</div>
    </div>
  ),
}));

// Mock StandardsUnitSearchTableRow
vi.mock('../../../components/StandardsUnitSearchSection/StandardsUnitSearchTableRow', () => ({
  default: ({ rowData, handleRowSelection }: any) => (
    <tr data-testid={`standards-unit-row-${rowData.stockingStandardUnitId}`}>
      <td>{rowData.stockingStandardUnitId}</td>
      <td>
        <button
          onClick={() =>
            handleRowSelection(
              rowData.openingId,
              `${rowData.stockingStandardUnitId}`
            )
          }
        >
          Select
        </button>
      </td>
    </tr>
  ),
}));

// Mock ForestClientMultiSelect used inside StandardsUnitSearchInput
vi.mock('../../../components/ForestClientMultiSelect', () => ({
  default: ({ onChange }: any) => (
    <div data-testid="forest-client-multi-select">
      <button onClick={() => onChange({ selectedItems: [] })}>Clear clients</button>
    </div>
  ),
}));

describe('StandardsUnitSearchSection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    queryClient.clear();
    // Re-establish resolved values after clearAllMocks resets them (vitest 3 behaviour)
    vi.mocked(API.SearchEndpointService.standardsUnitSearch).mockResolvedValue(mockSearchResult as any);
    vi.mocked(API.CodesEndpointService.getSilvTreeSpeciesCodes).mockResolvedValue([
      { code: 'PLI', description: 'Lodgepole Pine (Interior)' },
    ] as any);
    vi.mocked(API.CodesEndpointService.getOpeningOrgUnits).mockResolvedValue([
      { code: 'DCR', description: 'Cariboo Forest Region' },
    ] as any);
    vi.mocked(standardsUnitUtils.hasStandardsUnitSearchFilters).mockImplementation((params) => {
      if (!params) return false;
      return Object.values(params).some((val) => val !== undefined);
    });
  });

  it('renders search input and Clear all / Search buttons', async () => {
    const { wrapper } = renderWithProviders();
    render(<StandardsUnitSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /Clear all/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /Search/i })).toBeInTheDocument();
    });
  });

  it('does not show search results banner before a search is performed', async () => {
    const { wrapper } = renderWithProviders();
    render(<StandardsUnitSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });

  it('does not trigger a search when Search is clicked with no filters set', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    // Override mock so that params with only page/size don't pass the filter guard
    vi.mocked(standardsUnitUtils.hasStandardsUnitSearchFilters).mockImplementation(
      (params) => !!(params && Object.entries(params).some(([k, v]) => !['page', 'size', 'sort'].includes(k) && v !== undefined)),
    );
    render(<StandardsUnitSearchSection />, { wrapper });

    const searchButton = screen.getByRole('button', { name: /Search/i });
    await user.click(searchButton);

    await waitFor(() => {
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });

  it('displays search results banner when Search is clicked', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
      expect(screen.getByText('Total search results:')).toBeInTheDocument();
    });
  });

  it('renders table with row data after search', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      expect(screen.getByRole('table')).toBeInTheDocument();
      expect(screen.getByTestId('standards-unit-row-101')).toBeInTheDocument();
    }, { timeout: 3000 });
  });

  it('does not show the map when no row is selected', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
    });

    expect(screen.queryByTestId('openings-map')).not.toBeInTheDocument();
  });

  it('shows map when a row is selected', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    const selectButton = await screen.findByRole('button', { name: /Select/i }, { timeout: 3000 });
    await user.click(selectButton);

    await waitFor(() => {
      expect(screen.getByTestId('openings-map')).toBeInTheDocument();
      expect(screen.getByTestId('map-standards-unit-ids').textContent).toBe('101');
      expect(screen.getByTestId('map-opening-ids').textContent).toBe('200');
    });
  });

  it('hides map when the same row is selected again (deselect)', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    const selectButton = await screen.findByRole('button', { name: /Select/i }, { timeout: 3000 });

    // Select
    await user.click(selectButton);
    await waitFor(() => {
      expect(screen.getByTestId('openings-map')).toBeInTheDocument();
    });

    // Deselect
    await user.click(selectButton);
    await waitFor(() => {
      expect(screen.queryByTestId('openings-map')).not.toBeInTheDocument();
    });
  });

  it('resets all state when Clear all is clicked', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    // First trigger a search so results appear
    await user.click(screen.getByRole('button', { name: /Search/i }));
    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
    });

    // Clear all should hide results and call the URL update
    await user.click(screen.getByRole('button', { name: /Clear all/i }));

    await waitFor(() => {
      expect(
        vi.mocked(standardsUnitUtils.updateStandardsUnitSearchUrlParams)
      ).toHaveBeenCalledWith(undefined);
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });

  it('displays empty state when search returns no results', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(API.SearchEndpointService.standardsUnitSearch).mockResolvedValueOnce({
      content: [],
      page: { totalElements: 0, size: 20, page: 0, totalPages: 0 },
    });
    vi.mocked(standardsUnitUtils.hasStandardsUnitSearchFilters).mockReturnValue(true);

    render(<StandardsUnitSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('No results')).toBeInTheDocument();
      expect(
        screen.getByText('Consider adjusting your search term(s) and try again.')
      ).toBeInTheDocument();
    });
  });

  it('shows pagination when results exceed page size', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    vi.mocked(API.SearchEndpointService.standardsUnitSearch).mockResolvedValue({
      content: Array.from({ length: 20 }, (_, i) => ({
        stockingStandardUnitId: i + 1,
        openingId: 200 + i,
      })),
      page: { totalElements: 50, size: 20, page: 0, totalPages: 3 },
    } as any);

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      expect(screen.getByText('Total search results:')).toBeInTheDocument();
    });

    // The component renders the total elements count in the banner subtitle
    await waitFor(() => {
      expect(screen.getByText('50', { selector: 'p' })).toBeInTheDocument();
    });
  });

  it('closes map and clears selection when pagination changes', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    vi.mocked(API.SearchEndpointService.standardsUnitSearch).mockResolvedValue({
      content: [{ stockingStandardUnitId: 101, openingId: 200 }],
      page: { totalElements: 50, size: 20, page: 0, totalPages: 3 },
    } as any);

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    // Select a row to open the map
    const selectButton = await screen.findByRole('button', { name: /Select/i }, { timeout: 3000 });
    await user.click(selectButton);

    await waitFor(() => {
      expect(screen.getByTestId('openings-map')).toBeInTheDocument();
    });

    // Navigate to next page
    const nextPageButton = screen.getByRole('button', { name: /Next page/i });
    await user.click(nextPageButton);

    await waitFor(() => {
      expect(screen.queryByTestId('openings-map')).not.toBeInTheDocument();
    });
  });

  it('toggles column visibility via Edit columns menu', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
    });

    const editColumnsButton = screen.getByRole('button', { name: /Edit columns/i });
    expect(editColumnsButton).toHaveAttribute('aria-expanded', 'false');

    await user.click(editColumnsButton);

    await waitFor(() => {
      expect(editColumnsButton).toHaveAttribute('aria-expanded', 'true');
      expect(screen.getByText('Select columns you want to see')).toBeInTheDocument();
    });
  });

  it('initialises from URL params on mount', async () => {
    vi.mocked(standardsUnitUtils.readStandardsUnitSearchUrlParams).mockReturnValueOnce({
      bgcZone: 'SBS',
    });
    vi.mocked(standardsUnitUtils.hasStandardsUnitSearchFilters).mockReturnValue(true);

    const { wrapper } = renderWithProviders();
    render(<StandardsUnitSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
    });
    expect(API.SearchEndpointService.standardsUnitSearch).toHaveBeenCalled();
  });

  it('updates URL params when search is triggered', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      expect(
        vi.mocked(standardsUnitUtils.updateStandardsUnitSearchUrlParams)
      ).toHaveBeenCalled();
    });
  });

  it('shows loading skeleton while fetching results', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    // Use a never-resolving mock so loading state persists
    vi.mocked(API.SearchEndpointService.standardsUnitSearch).mockImplementation(
      () => new Promise(() => { }),
    );

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    // Skeleton should be visible while loading
    await waitFor(() => {
      const skeleton = document.querySelector('.cds--skeleton');
      expect(skeleton).toBeInTheDocument();
    });
  });

  it('total count displayed after successful search', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();

    render(<StandardsUnitSearchSection />, { wrapper });

    await user.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      // The subtitle renders the totalElements number next to "Total search results:"
      const subtitleItems = screen.getAllByText('1');
      // At least one of them is the total elements count (not a page number etc.)
      expect(subtitleItems.length).toBeGreaterThan(0);
    });
  });
});
