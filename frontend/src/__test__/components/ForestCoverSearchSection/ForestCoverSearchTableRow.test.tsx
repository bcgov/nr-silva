import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import ForestCoverSearchTableRow from '../../../components/ForestCoverSearchSection/ForestCoverSearchTableRow';
import { renderWithProviders } from '../../utils/testAuthProvider';
import { ForestCoverSearchResponseDto } from '../../../services/OpenApi';

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

// Mock StockingStatusTag component
vi.mock('../../../components/Tags', () => ({
  StockingStatusTag: ({ status }: any) => (
    <span data-testid="stocking-status-tag">{status?.code}</span>
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
  formatLocalDate: vi.fn((date) => (date ? `Formatted: ${date}` : '-')),
}));

// Mock multiSelectUtils
vi.mock('../../../utils/multiSelectUtils', () => ({
  codeDescriptionToDisplayText: vi.fn((item) => `${item.code} - ${item.description}`),
}));

const mockRowData: ForestCoverSearchResponseDto = {
  forestCoverId: 42,
  polygonId: 'POLY-001',
  standardUnitId: 'STD-001',
  openingId: 100,
  stockingType: { code: 'PL', description: 'Planted' },
  stockingStatus: { code: 'STS', description: 'Satisfactorily Stocked' },
  damageAgents: [
    { code: 'IBM', description: 'Mountain Pine Beetle' },
  ],
  orgUnit: { code: 'DCS', description: 'Cariboo Forest Region' },
  openingCategory: { code: 'FTML', description: 'Forest Tenure - Misc. Licence' },
  updateTimestamp: '2024-01-15T10:30:00',
  regenDueDate: '2026-01-15',
  freeGrowingDueDate: '2030-01-15',
};

const mockHeaders = [
  { key: 'forestCoverId' as const, header: 'Forest cover', selected: true },
  { key: 'stockingType' as const, header: 'Stocking type', selected: true },
  { key: 'stockingStatus' as const, header: 'Stocking status', selected: true },
  { key: 'damageAgents' as const, header: 'Damaging agent type', selected: true },
  { key: 'actions' as const, header: 'Actions', selected: true },
];

describe('ForestCoverSearchTableRow', () => {
  const mockHandleRowSelection = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders the polygon and standards unit IDs in the forest cover cell', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText(/POLY-001/)).toBeInTheDocument();
    expect(screen.getByText(/STD-001/)).toBeInTheDocument();
  });

  it('renders stocking type code with tooltip', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('PL')).toBeInTheDocument();
  });

  it('renders stocking status tag', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByTestId('stocking-status-tag')).toBeInTheDocument();
    expect(screen.getByTestId('stocking-status-tag')).toHaveTextContent('STS');
  });

  it('renders damage agent codes when count is within display limit', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getAllByText(/IBM/).length).toBeGreaterThan(0);
  });

  it('renders "X agents" when damage agents exceed the display limit', () => {
    const { wrapper } = renderWithProviders();
    const rowWithManyAgents: ForestCoverSearchResponseDto = {
      ...mockRowData,
      damageAgents: [
        { code: 'IBM', description: 'Mountain Pine Beetle' },
        { code: 'IDS', description: 'Douglas-fir Beetle' },
        { code: 'IDW', description: 'Western Balsam Bark Beetle' },
        { code: 'IBB', description: 'Black Hills Beetle' },
      ],
    };

    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={rowWithManyAgents}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('4 agents')).toBeInTheDocument();
  });

  it('hides columns that are not selected', () => {
    const { wrapper } = renderWithProviders();
    const headersWithHidden = [
      { key: 'forestCoverId' as const, header: 'Forest cover', selected: true },
      { key: 'stockingType' as const, header: 'Stocking type', selected: false },
      { key: 'stockingStatus' as const, header: 'Stocking status', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={headersWithHidden}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText(/POLY-001/)).toBeInTheDocument();
    // Stocking type is hidden
    expect(screen.queryByText('PL')).not.toBeInTheDocument();
  });

  it('shows spatial checkbox when showMap is true', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByTestId(`spatial-checkbox-${mockRowData.forestCoverId}`)).toBeInTheDocument();
  });

  it('hides spatial checkbox when showMap is false', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={false}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(
      screen.queryByTestId(`spatial-checkbox-${mockRowData.forestCoverId}`)
    ).not.toBeInTheDocument();
  });

  it('calls handleRowSelection with correct args when spatial checkbox is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const spatialCheckbox = screen.getByTestId(`spatial-checkbox-${mockRowData.forestCoverId}`);
    await user.click(spatialCheckbox);

    await waitFor(() => {
      expect(mockHandleRowSelection).toHaveBeenCalledWith(
        mockRowData.openingId,
        `${mockRowData.forestCoverId}-${mockRowData.polygonId ?? ''}`
      );
    });
  });

  it('renders all selected column headers', () => {
    const { wrapper } = renderWithProviders();
    const allHeaders = [
      { key: 'forestCoverId' as const, header: 'Forest cover', selected: true },
      { key: 'stockingType' as const, header: 'Stocking type', selected: true },
      { key: 'stockingStatus' as const, header: 'Stocking status', selected: true },
      { key: 'damageAgents' as const, header: 'Damaging agent type', selected: true },
      { key: 'orgUnit' as const, header: 'Org unit', selected: true },
      { key: 'openingCategory' as const, header: 'Opening category', selected: true },
      { key: 'regenDueDate' as const, header: 'Regen due date', selected: true },
      { key: 'freeGrowingDueDate' as const, header: 'Free growing due date', selected: true },
      { key: 'updateTimestamp' as const, header: 'Update date', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
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

    expect(screen.getByText(/POLY-001/)).toBeInTheDocument();
    expect(screen.getByText('PL')).toBeInTheDocument();
    expect(screen.getByText('DCS')).toBeInTheDocument();
    expect(screen.getByText('FTML')).toBeInTheDocument();
  });

  it('renders placeholder when damage agents are empty', () => {
    const { wrapper } = renderWithProviders();
    const rowWithNoAgents: ForestCoverSearchResponseDto = {
      ...mockRowData,
      damageAgents: [],
    };

    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={rowWithNoAgents}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    // Should render placeholder (--) for empty damage agents
    expect(screen.getAllByText('--').length).toBeGreaterThan(0);
  });

  it('has correct test id on the row', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ForestCoverSearchTableRow
            headers={mockHeaders}
            rowData={mockRowData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={mockHandleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(
      screen.getByTestId(`forest-cover-search-table-row-${mockRowData.forestCoverId}`)
    ).toBeInTheDocument();
  });
});
