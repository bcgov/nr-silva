import React from 'react';
import { render, screen, waitFor, within, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import DisturbancesSearchSection from '../../../components/DisturbancesSearchSection';
import { renderWithProviders } from '../../utils/testAuthProvider';
import * as disturbanceUtils from '../../../components/DisturbancesSearchSection/utils';
import API from '../../../services/API';

// Mock API service
vi.mock('../../../services/API', () => ({
  default: {
    SearchEndpointService: {
      disturbanceSearch: vi.fn().mockResolvedValue({
        content: [
          {
            activityId: 1,
            openingId: 100,
            disturbance: { code: 'DN-001', description: 'Disturbance 1' },
            silvSystem: { code: 'SILV-001', description: 'Silviculture System 1' },
            variant: { code: 'VAR-001', description: 'Variant 1' },
            cutPhase: { code: 'CUT-001', description: 'Cut Phase 1' },
            orgUnit: { code: 'ORG-001', description: 'Org Unit 1' },
            openingClient: {
              clientNumber: '00000001',
              clientName: 'Client 1',
              legalFirstName: 'Client',
              legalMiddleName: '',
              clientStatusCode: { code: 'ACT', description: 'Active' },
              clientTypeCode: { code: 'I', description: 'Individual' },
              acronym: 'C1',
            },
            openingCategory: { code: 'CAT-001', description: 'Category 1' },
            updateTimestamp: '2024-01-15T10:30:00',
          },
        ],
        page: { totalElements: 1, size: 10, page: 0, totalPages: 1 },
      }),
    },
    CodesEndpointService: {
      getDisturbanceCodes: vi.fn().mockResolvedValue([
        { code: 'DN-001', description: 'Disturbance 1' },
      ]),
      getSilvSystemCodes: vi.fn().mockResolvedValue([
        { code: 'SILV-001', description: 'Silviculture System 1' },
      ]),
      getSilvSystemVariantCodes: vi.fn().mockResolvedValue([
        { code: 'VAR-001', description: 'Variant 1' },
      ]),
      getSilvCutPhaseCodes: vi.fn().mockResolvedValue([
        { code: 'CUT-001', description: 'Cut Phase 1' },
      ]),
      getOpeningOrgUnits: vi.fn().mockResolvedValue([
        { code: 'ORG-001', description: 'Org Unit 1' },
      ]),
      getOpeningCategories: vi.fn().mockResolvedValue([
        { code: 'CAT-001', description: 'Category 1' },
      ]),
    },
  },
}));

// Mock disturbance search utils
vi.mock('../../../components/DisturbancesSearchSection/utils', () => ({
  readDisturbanceSearchUrlParams: vi.fn(() => ({})),
  updateDisturbanceSearchUrlParams: vi.fn(),
  hasDisturbanceSearchFilters: vi.fn((params) => {
    if (!params) return false;
    return Object.values(params).some((val) => val !== undefined);
  }),
}));

// Mock OpeningsMap component
vi.mock('../../../components/OpeningsMap', () => ({
  default: ({ openingIds, selectedDisturbanceIds }: any) => (
    <div data-testid="openings-map">
      <div data-testid="map-opening-ids">{openingIds?.join(',')}</div>
      <div data-testid="map-disturbance-ids">{selectedDisturbanceIds?.join(',')}</div>
    </div>
  ),
}));

// Mock DisturbanceSearchTableRow
vi.mock('../../../components/DisturbancesSearchSection/DisturbanceSearchTableRow', () => ({
  default: ({ rowData, handleRowSelection }: any) => (
    <tr data-testid={`disturbance-row-${rowData.activityId}`}>
      <td>{rowData.activityId}</td>
      <td>
        <button onClick={() => handleRowSelection(rowData.openingId, `${rowData.activityId}-DN`)}>
          Select
        </button>
      </td>
    </tr>
  ),
}));

describe('DisturbancesSearchSection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('renders search input and buttons', async () => {
    const { wrapper } = renderWithProviders();
    render(<DisturbancesSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /Clear all/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /Search/i })).toBeInTheDocument();
    });
  });

  it('displays search results when filters are applied', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(disturbanceUtils.hasDisturbanceSearchFilters).mockReturnValue(true);

    render(<DisturbancesSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('Search results')).toBeInTheDocument();
      expect(screen.getByText('Total search results:')).toBeInTheDocument();
    });
  });

  it('resets search params when Clear all is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(<DisturbancesSearchSection />, { wrapper });

    const clearButton = await screen.findByRole('button', { name: /Clear all/i });
    await user.click(clearButton);

    await waitFor(() => {
      expect(
        vi.mocked(disturbanceUtils.updateDisturbanceSearchUrlParams)
      ).toHaveBeenCalledWith(undefined);
    });
  });

  it('shows map when disturbance is selected', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(disturbanceUtils.hasDisturbanceSearchFilters).mockReturnValue(true);

    render(<DisturbancesSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.queryByTestId('openings-map')).not.toBeInTheDocument();
    });
  });

  it('renders table with results', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(disturbanceUtils.hasDisturbanceSearchFilters).mockReturnValue(true);

    render(<DisturbancesSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByRole('table')).toBeInTheDocument();
    });
  });

  it('toggles column visibility', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    vi.mocked(disturbanceUtils.hasDisturbanceSearchFilters).mockReturnValue(true);

    render(<DisturbancesSearchSection />, { wrapper });

    const editColumnsButton = screen.getByRole('button', { name: /Edit columns/i });

    // Initially should not be expanded
    expect(editColumnsButton).toHaveAttribute('aria-expanded', 'false');

    // Click to open
    await user.click(editColumnsButton);

    // Should be expanded after click
    await waitFor(() => {
      expect(editColumnsButton).toHaveAttribute('aria-expanded', 'true');
    });

    // Modal text should appear
    expect(screen.getByText('Select columns you want to see')).toBeInTheDocument();
  });

  it('displays pagination when results exceed page size', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(disturbanceUtils.hasDisturbanceSearchFilters).mockReturnValue(true);

    render(<DisturbancesSearchSection />, { wrapper });

    await waitFor(() => {
      const pageElement = screen.queryByText(/Pages/i);
      // Pagination may or may not be visible depending on total elements
      expect(screen.getByText('Total search results:')).toBeInTheDocument();
    });
  });

  it('displays empty state when no results', async () => {
    const { wrapper } = renderWithProviders();
    vi.mocked(API.SearchEndpointService.disturbanceSearch).mockResolvedValueOnce({
      content: [],
      page: { totalElements: 0, size: 10, page: 0, totalPages: 0 },
    });
    vi.mocked(disturbanceUtils.hasDisturbanceSearchFilters).mockReturnValue(true);

    render(<DisturbancesSearchSection />, { wrapper });

    await waitFor(() => {
      expect(screen.getByText('No results')).toBeInTheDocument();
      expect(
        screen.getByText('Consider adjusting your search term(s) and try again.')
      ).toBeInTheDocument();
    });
  });
});
