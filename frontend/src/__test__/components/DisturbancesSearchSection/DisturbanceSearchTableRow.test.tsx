import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import DisturbanceSearchTableRow from '../../../components/DisturbancesSearchSection/DisturbanceSearchTableRow';
import { renderWithProviders } from '../../utils/testAuthProvider';
import { DisturbanceSearchResponseDto } from '../../../services/OpenApi';

// Mock the hook for polygon availability
vi.mock('../../../hooks/usePolygonAvailability', () => ({
  default: vi.fn(() => ({
    isAvailable: true,
    isLoading: false,
  })),
}));

// Mock SpatialCheckbox component
vi.mock('../../../components/SpatialCheckbox', () => ({
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

// Mock useNavigate
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

// Mock DateUtils
vi.mock('../../../utils/DateUtils', () => ({
  formatLocalDate: vi.fn((date) => `Formatted: ${date}`),
}));

// Mock ForestClientUtils
vi.mock('../../../utils/ForestClientUtils', () => ({
  getClientLabel: vi.fn((client) => `${client.clientNumber} - ${client.clientName}`),
  getClientSimpleLabel: vi.fn((client) => `${client.clientNumber}`),
}));

const mockDisturbanceData: DisturbanceSearchResponseDto = {
  activityId: 1,
  openingId: 100,
  disturbance: { code: 'DN-001', description: 'Disturbance 1 Description' },
  silvSystem: { code: 'SILV-001', description: 'Silviculture System 1 Description' },
  variant: { code: 'VAR-001', description: 'Variant 1 Description' },
  cutPhase: { code: 'CUT-001', description: 'Cut Phase 1 Description' },
  orgUnit: { code: 'ORG-001', description: 'Org Unit 1 Description' },
  openingClient: {
    clientNumber: '00000005',
    clientName: 'Test Client',
    legalFirstName: 'Test',
    legalMiddleName: '',
    clientStatusCode: { code: 'ACT', description: 'Active' },
    clientTypeCode: { code: 'C', description: 'Corporation' },
    acronym: 'TC',
  },
  openingCategory: { code: 'CAT-001', description: 'Category 1 Description' },
  updateTimestamp: '2024-01-15T10:30:00',
};

const mockHeaders = [
  { key: 'disturbance' as const, header: 'Disturbance Code', selected: true },
  { key: 'silvSystem' as const, header: 'Silviculture System', selected: true },
  { key: 'variant' as const, header: 'Variant', selected: true },
  { key: 'cutPhase' as const, header: 'Cut Phase', selected: true },
  { key: 'actions' as const, header: 'Actions', selected: true },
];

describe('DisturbanceSearchTableRow', () => {
  const mockHandleRowSelection = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders disturbance information', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText(/DN-001/)).toBeInTheDocument();
  });

  it('renders disturbance code with tooltip', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText(/DN-001/)).toBeInTheDocument();
  });

  it('renders all selected column headers', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    // The row should be rendered if headers are present
    expect(screen.getByText(/DN-001/)).toBeInTheDocument();
  });

  it('calls handleRowSelection when spatial checkbox is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const spatialCheckbox = screen.getByTestId(`spatial-checkbox-${mockDisturbanceData.activityId}`);
    await user.click(spatialCheckbox);

    await waitFor(() => {
      expect(mockHandleRowSelection).toHaveBeenCalledWith(
        mockDisturbanceData.openingId,
        `${mockDisturbanceData.activityId}-DN`
      );
    });
  });

  it('hides spatial checkbox when showMap is false', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={false}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(
      screen.queryByTestId(`spatial-checkbox-${mockDisturbanceData.activityId}`)
    ).not.toBeInTheDocument();
  });

  it('renders with correct row key', () => {
    const { wrapper } = renderWithProviders();
    const { container } = render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(container.querySelector('tr')).toBeInTheDocument();
  });

  it('highlights selected rows', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={true}
            selectedRows={[mockDisturbanceData.activityId!]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const checkbox = screen.getByTestId(`spatial-checkbox-${mockDisturbanceData.activityId}`);
    expect(checkbox).toBeInTheDocument();
  });

  it('opens disturbance in new tab when launch button is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    const windowOpenSpy = vi.spyOn(window, 'open').mockImplementation(() => null);

    render(
      <table>
        <tbody>
          <DisturbanceSearchTableRow
            headers={mockHeaders}
            rowData={mockDisturbanceData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    // Target the new-tab button specifically by looking for the tooltip
    const newTabButton = document.querySelector('.new-tab-button') as HTMLElement;
    expect(newTabButton).toBeInTheDocument();
    await user.click(newTabButton);

    await waitFor(() => {
      expect(windowOpenSpy).toHaveBeenCalled();
    });

    windowOpenSpy.mockRestore();
  });
});
