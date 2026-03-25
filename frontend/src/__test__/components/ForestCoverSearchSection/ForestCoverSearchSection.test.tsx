import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import ForestCoverSearchSection from '../../../components/ForestCoverSearchSection';
import { renderWithProviders } from '../../utils/testAuthProvider';
import * as forestCoverUtils from '../../../components/ForestCoverSearchSection/utils';
import API from '../../../services/API';

// Mock API service
vi.mock('../../../services/API', () => ({
  default: {
    SearchEndpointService: {
      forestCoverSearch: vi.fn().mockResolvedValue({
        content: [
          {
            forestCoverId: 1,
            polygonId: 'POLY-001',
            standardUnitId: 'STD-001',
            openingId: 100,
            stockingType: { code: 'PL', description: 'Planted' },
            stockingStatus: { code: 'STS', description: 'Satisfactorily Stocked' },
            damageAgents: [{ code: 'IBM', description: 'Mountain Pine Beetle' }],
            orgUnit: { code: 'DCS', description: 'Cariboo Forest Region' },
            openingCategory: { code: 'FTML', description: 'Forest Tenure - Misc. Licence' },
            updateTimestamp: '2024-01-15T10:30:00',
            regenDueDate: '2026-01-15',
            freeGrowingDueDate: '2030-01-15',
          },
        ],
        page: { totalElements: 1, size: 20, page: 0, totalPages: 1 },
      }),
    },
    CodesEndpointService: {
      getStockingTypeCodes: vi.fn().mockResolvedValue([
        { code: 'PL', description: 'Planted' },
      ]),
      getStockingStatusCodes: vi.fn().mockResolvedValue([
        { code: 'STS', description: 'Satisfactorily Stocked' },
      ]),
      getSilvDamageAgentCodes: vi.fn().mockResolvedValue([
        { code: 'IBM', description: 'Mountain Pine Beetle' },
      ]),
      getOpeningOrgUnits: vi.fn().mockResolvedValue([
        { code: 'DCS', description: 'Cariboo Forest Region' },
      ]),
      getOpeningCategories: vi.fn().mockResolvedValue([
        { code: 'FTML', description: 'Forest Tenure - Misc. Licence' },
      ]),
    },
  },
}));

// Mock forest cover search utils
vi.mock('../../../components/ForestCoverSearchSection/utils', () => ({
  readForestCoverSearchUrlParams: vi.fn(() => ({})),
  updateForestCoverSearchUrlParams: vi.fn(),
  hasForestCoverSearchFilters: vi.fn((params) => {
    if (!params) return false;
    const excludeKeys = new Set(['page', 'size', 'sort']);
    return Object.entries(params).some(([key, value]) => {
      if (excludeKeys.has(key)) return false;
      if (Array.isArray(value)) return value.length > 0;
      return value !== undefined && value !== null && value !== '';
    });
  }),
}));

// Mock OpeningsMap component
vi.mock('../../../components/OpeningsMap', () => ({
  default: ({ openingIds, selectedForestCoverIds }: any) => (
    <div data-testid="openings-map">
      <div data-testid="map-opening-ids">{openingIds?.join(',')}</div>
      <div data-testid="map-forest-cover-ids">{selectedForestCoverIds?.join(',')}</div>
    </div>
  ),
}));

// Mock ForestCoverSearchTableRow
vi.mock('../../../components/ForestCoverSearchSection/ForestCoverSearchTableRow', () => ({
  default: ({ rowData, handleRowSelection }: any) => (
    <tr data-testid={`forest-cover-row-${rowData.forestCoverId}`}>
      <td>{rowData.forestCoverId}</td>
      <td>
        <button
          onClick={() =>
            handleRowSelection(
              rowData.openingId,
              `${rowData.forestCoverId}-${rowData.polygonId}`
            )
          }
        >
          Select
        </button>
      </td>
    </tr>
  ),
}));

describe('ForestCoverSearchSection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('renders search input and buttons', async () => {
    const { wrapper } = renderWithProviders();
    render(<ForestCoverSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /Clear all/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /Search/i })).toBeInTheDocument();
    });
  });

  it('does not show search results banner before search', async () => {
    const { wrapper } = renderWithProviders();
    render(<ForestCoverSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });

  it('displays search results when filters are applied', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(forestCoverUtils.hasForestCoverSearchFilters).mockReturnValue(true);

    render(<ForestCoverSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
      expect(screen.getByText('Total search results:')).toBeInTheDocument();
    });
  });

  it('resets search params when Clear all is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(<ForestCoverSearchSection />, { wrapper });

    const clearButton = await screen.findByRole('button', { name: /Clear all/i });
    await user.click(clearButton);

    await waitFor(() => {
      expect(
        vi.mocked(forestCoverUtils.updateForestCoverSearchUrlParams)
      ).toHaveBeenCalledWith(undefined);
    });
  });

  it('renders table with results when filters are active', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(forestCoverUtils.hasForestCoverSearchFilters).mockReturnValue(true);

    render(<ForestCoverSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByRole('table')).toBeInTheDocument();
    });
  });

  it('does not show map when no row is selected', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(forestCoverUtils.hasForestCoverSearchFilters).mockReturnValue(true);

    render(<ForestCoverSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.queryByTestId('openings-map')).not.toBeInTheDocument();
    });
  });

  it('toggles column visibility via Edit columns button', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    vi.mocked(forestCoverUtils.hasForestCoverSearchFilters).mockReturnValue(true);

    render(<ForestCoverSearchSection />, { wrapper });

    const editColumnsButton = screen.getByRole('button', { name: /Edit columns/i });
    expect(editColumnsButton).toHaveAttribute('aria-expanded', 'false');

    await user.click(editColumnsButton);

    await waitFor(() => {
      expect(editColumnsButton).toHaveAttribute('aria-expanded', 'true');
    });

    expect(screen.getByText('Select columns you want to see')).toBeInTheDocument();
  });

  it('displays pagination info after search', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(forestCoverUtils.hasForestCoverSearchFilters).mockReturnValue(true);

    render(<ForestCoverSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('Total search results:')).toBeInTheDocument();
    });
  });

  it('does not show results when search is clicked with no filters set', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(<ForestCoverSearchSection />, { wrapper });

    const searchButton = screen.getByRole('button', { name: /Search/i });
    await user.click(searchButton);

    await waitFor(() => {
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });
});
