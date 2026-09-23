import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import StandardsUnitSearchTableRow from '@/components/StandardsUnitSearchSection/StandardsUnitSearchTableRow';
import { renderWithProviders } from '../../utils/testAuthProvider';
import { StandardUnitSearchResponseDto } from '@/services/OpenApi';
import { StandardsUnitHeaderType } from '@/types/TableHeader';

// Mock usePolygonAvailability
vi.mock('@/hooks/usePolygonAvailability', () => ({
  default: vi.fn(() => ({
    isAvailable: true,
    isLoading: false,
  })),
}));

// Mock SpatialCheckbox
vi.mock('@/components/SpatialCheckbox', () => ({
  default: ({ spatialType, rowId, selectedRows, handleRowSelection }: any) => (
    <button
      data-testid={`spatial-checkbox-${rowId}`}
      onClick={() => handleRowSelection(rowId)}
      className={selectedRows?.includes(rowId) ? 'selected' : ''}
    >
      View {spatialType} on map
    </button>
  ),
}));

const mockRowData: StandardUnitSearchResponseDto = {
  stockingStandardUnitId: 101,
  openingId: 500,
  standardsRegimeId: 2002,
  isStandardsRegimeExpired: true,
  stockingStatus: { code: 'NSR', description: 'Status 1' },
  netArea: 15.5,
  regenDueDate: '2026-06-01',
  freeGrowingDueDate: '2030-06-01',
  orgUnit: { code: 'DND', description: 'District 1' },
  openingCategory: { code: 'FTML', description: 'Category 1' },
  openingClient: {
    clientNumber: '00012345',
    clientName: 'Test Client',
    acronym: 'TEST',
  },
  updateTimestamp: '2024-03-01T00:00:00',
};

const allHeaders: StandardsUnitHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'standardsRegimeId', header: 'SSID', selected: true },
  { key: 'dueDates', header: 'Due dates', selected: true },
  { key: 'stockingStatus', header: 'Status', selected: true },
  { key: 'netArea', header: 'Net Area', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'openingCategory', header: 'Category', selected: true },
  { key: 'orgUnit', header: 'District', selected: true },
  { key: 'openingClient', header: 'Client', selected: true },
  { key: 'updateTimestamp', header: 'Updated', selected: true },
];

describe('StandardsUnitSearchTableRow', () => {
  const mockHandleRowSelection = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders all table cells with valid row data', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <StandardsUnitSearchTableRow
            headers={allHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('2002')).toBeInTheDocument();
    expect(screen.getByText('15.5')).toBeInTheDocument();
    expect(screen.getByText('500')).toBeInTheDocument();
    expect(screen.getAllByText(/TEST/).length).toBeGreaterThanOrEqual(1);
  });

  it('calls handleRowSelection when SpatialCheckbox is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <table>
        <tbody>
          <StandardsUnitSearchTableRow
            headers={allHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const checkbox = screen.getByTestId(`spatial-checkbox-${mockRowData.stockingStandardUnitId}`);
    await user.click(checkbox);

    expect(mockHandleRowSelection).toHaveBeenCalledWith(500, '101');
  });

  it('opens opening details in new tab on Launch button click', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    const openSpy = vi.spyOn(window, 'open').mockImplementation(() => null);

    render(
      <table>
        <tbody>
          <StandardsUnitSearchTableRow
            headers={allHeaders}
            rowData={mockRowData}
            showMap={false}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const launchBtn = document.querySelector('.new-tab-button') as HTMLElement;
    await user.click(launchBtn);

    expect(openSpy).toHaveBeenCalledWith(
      expect.stringContaining('500?tab=standards-units'),
      '_blank',
      'noopener,noreferrer'
    );
    openSpy.mockRestore();
  });

  it('renders placeholders for empty fields', () => {
    const emptyRow: StandardUnitSearchResponseDto = {
      stockingStandardUnitId: 202,
      openingId: 600,
      isStandardsRegimeExpired: false,
    };

    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <StandardsUnitSearchTableRow
            headers={allHeaders}
            rowData={emptyRow}
            showMap={false}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const cells = screen.getAllByText('--');
    expect(cells.length).toBeGreaterThanOrEqual(1);
  });
});
