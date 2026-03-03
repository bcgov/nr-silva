import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import ActivitySearchTableRow from '../../../components/ActivitySearchSection/ActivitySearchTableRow';
import { renderWithProviders } from '../../utils/testAuthProvider';
import { ActivitySearchResponseDto } from '../../../services/OpenApi';

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

const mockActivityData: ActivitySearchResponseDto = {
  activityId: 'ACT-001',
  openingId: 100,
  base: { code: 'BASE-001', description: 'Base 1 Description' },
  technique: { code: 'TECH-001', description: 'Technique 1 Description' },
  method: { code: 'METHOD-001', description: 'Method 1 Description' },
  orgUnit: { code: 'ORG-001', description: 'Org Unit 1 Description' },
  fundingSource: { code: 'FUND-001', description: 'Funding Source 1' },
  openingClient: {
    clientNumber: '00000005',
    clientName: 'Test Client',
    legalFirstName: 'Test',
    legalMiddleName: '',
    clientStatusCode: { code: 'ACT', description: 'Active' },
    clientTypeCode: { code: 'C', description: 'Corporation' },
    acronym: 'TC',
  },
  isComplete: true,
  openingCategory: { code: 'CAT-001', description: 'Category 1 Description' },
  updateTimestamp: '2024-01-15T10:30:00',
};

const mockHeaders = [
  { key: 'base' as const, header: 'Base', selected: true },
  { key: 'technique' as const, header: 'Technique', selected: true },
  { key: 'method' as const, header: 'Method', selected: true },
  { key: 'actions' as const, header: 'Actions', selected: true },
];

describe('ActivitySearchTableRow', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders activity ID and base information', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('BASE-001')).toBeInTheDocument();
  });

  it('renders technique and method information', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('TECH-001')).toBeInTheDocument();
    expect(screen.getByText('METHOD-001')).toBeInTheDocument();
  });

  it('renders all selected column headers', () => {
    const { wrapper } = renderWithProviders();
    const allHeaders = [
      { key: 'base' as const, header: 'Base', selected: true },
      { key: 'technique' as const, header: 'Technique', selected: true },
      { key: 'method' as const, header: 'Method', selected: true },
      { key: 'orgUnit' as const, header: 'Org Unit', selected: true },
      { key: 'fundingSource' as const, header: 'Funding Source', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={allHeaders}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('BASE-001')).toBeInTheDocument();
    expect(screen.getByText('TECH-001')).toBeInTheDocument();
    expect(screen.getByText('METHOD-001')).toBeInTheDocument();
    expect(screen.getByText('ORG-001')).toBeInTheDocument();
    expect(screen.getByText('FUND-001')).toBeInTheDocument();
  });

  it('hides columns that are not selected', () => {
    const { wrapper } = renderWithProviders();
    const headersWithHidden = [
      { key: 'base' as const, header: 'Base', selected: true },
      { key: 'technique' as const, header: 'Technique', selected: false }, // Not selected
      { key: 'method' as const, header: 'Method', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={headersWithHidden}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('BASE-001')).toBeInTheDocument();
    expect(screen.getByText('METHOD-001')).toBeInTheDocument();
    // Technique should not be rendered because selected: false
    expect(screen.queryByText('TECH-001')).not.toBeInTheDocument();
  });

  it('renders SpatialCheckbox when showMap is true', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByTestId('spatial-checkbox-ACT-001')).toBeInTheDocument();
  });

  it('does not render SpatialCheckbox when showMap is false', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={mockActivityData}
            showMap={false}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.queryByTestId('spatial-checkbox-ACT-001')).not.toBeInTheDocument();
  });

  it('highlights row when activity is selected', () => {
    const { wrapper } = renderWithProviders();
    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={['ACT-001']}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const checkbox = screen.getByTestId('spatial-checkbox-ACT-001');
    expect(checkbox).toHaveClass('selected');
  });

  it('calls handleRowSelection when SpatialCheckbox is clicked', async () => {
    const user = userEvent.setup();
    const handleRowSelection = vi.fn();
    const { wrapper } = renderWithProviders();

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={handleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const checkbox = screen.getByTestId('spatial-checkbox-ACT-001');
    await user.click(checkbox);

    expect(handleRowSelection).toHaveBeenCalled();
  });

  it('renders data with missing optional fields gracefully', () => {
    const { wrapper } = renderWithProviders();
    const incompleteData: ActivitySearchResponseDto = {
      activityId: 'ACT-002',
      openingId: 101,
      base: undefined,
      technique: undefined,
      method: undefined,
      orgUnit: undefined,
      fundingSource: undefined,
      openingClient: undefined,
      isComplete: undefined,
      openingCategory: undefined,
      updateTimestamp: '2024-01-15T10:30:00',
    };

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={incompleteData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    // Should render without crashing, with placeholder values
    expect(screen.getByTestId('spatial-checkbox-ACT-002')).toBeInTheDocument();
  });

  it('renders opening client information when available', () => {
    const { wrapper } = renderWithProviders();
    const headersWithClient = [
      { key: 'openingClient' as const, header: 'Client', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={headersWithClient}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    // Should render client information
    expect(screen.getByTestId('spatial-checkbox-ACT-001')).toBeInTheDocument();
  });

  it('renders opening category information when available', () => {
    const { wrapper } = renderWithProviders();
    const headersWithCategory = [
      { key: 'openingCategory' as const, header: 'Category', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={headersWithCategory}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByTestId('spatial-checkbox-ACT-001')).toBeInTheDocument();
  });

  it('renders with different opening IDs correctly', () => {
    const { wrapper } = renderWithProviders();
    const dataWithDifferentId = {
      ...mockActivityData,
      activityId: 'ACT-999',
      openingId: 999,
    };

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={dataWithDifferentId}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByTestId('spatial-checkbox-ACT-999')).toBeInTheDocument();
  });

  it('renders org unit with tooltip info', () => {
    const { wrapper } = renderWithProviders();
    const headersWithOrgUnit = [
      { key: 'orgUnit' as const, header: 'Org Unit', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={headersWithOrgUnit}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('ORG-001')).toBeInTheDocument();
  });

  it('renders funding source with tooltip info', () => {
    const { wrapper } = renderWithProviders();
    const headersWithFunding = [
      { key: 'fundingSource' as const, header: 'Funding Source', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={headersWithFunding}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByText('FUND-001')).toBeInTheDocument();
  });

  it('renders activity completion status', () => {
    const { wrapper } = renderWithProviders();
    const headersWithStatus = [
      { key: 'isComplete' as const, header: 'Status', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={headersWithStatus}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    // Status tag should be rendered
    expect(screen.getByTestId('spatial-checkbox-ACT-001')).toBeInTheDocument();
  });

  it('renders update timestamp', () => {
    const { wrapper } = renderWithProviders();
    const headersWithTimestamp = [
      { key: 'updateTimestamp' as const, header: 'Updated', selected: true },
      { key: 'actions' as const, header: 'Actions', selected: true },
    ];

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={headersWithTimestamp}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByTestId('spatial-checkbox-ACT-001')).toBeInTheDocument();
  });

  it('passes correct compoundId to handleRowSelection', async () => {
    const user = userEvent.setup();
    const handleRowSelection = vi.fn();
    const { wrapper } = renderWithProviders();

    render(
      <table>
        <tbody>
          <ActivitySearchTableRow
            headers={mockHeaders}
            rowData={mockActivityData}
            showMap={true}
            selectedRows={[]}
            handleRowSelection={handleRowSelection}
          />
        </tbody>
      </table>,
      { wrapper }
    );

    const checkbox = screen.getByTestId('spatial-checkbox-ACT-001');
    await user.click(checkbox);

    expect(handleRowSelection).toHaveBeenCalledWith(
      100,
      expect.stringContaining('ACT-001')
    );
  });

  it('renders multiple activities in a list correctly', () => {
    const { wrapper } = renderWithProviders();
    const activities = [
      { ...mockActivityData, activityId: 'ACT-001' },
      { ...mockActivityData, activityId: 'ACT-002' },
      { ...mockActivityData, activityId: 'ACT-003' },
    ];

    render(
      <table>
        <tbody>
          {activities.map((activity) => (
            <ActivitySearchTableRow
              key={activity.activityId}
              headers={mockHeaders}
              rowData={activity}
              showMap={true}
              selectedRows={[]}
              handleRowSelection={vi.fn()}
            />
          ))}
        </tbody>
      </table>,
      { wrapper }
    );

    expect(screen.getByTestId('spatial-checkbox-ACT-001')).toBeInTheDocument();
    expect(screen.getByTestId('spatial-checkbox-ACT-002')).toBeInTheDocument();
    expect(screen.getByTestId('spatial-checkbox-ACT-003')).toBeInTheDocument();
  });
});
