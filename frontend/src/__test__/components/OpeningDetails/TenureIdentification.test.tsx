import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import * as featureFlags from '@/utils/featureFlags';

const { mockNavigate, mockUseQuery } = vi.hoisted(() => ({
  mockNavigate: vi.fn(),
  mockUseQuery: vi.fn(),
}));

vi.mock('@/services/API', () => ({
  default: {
    OpeningEndpointService: {
      getTenures: vi.fn().mockResolvedValue({}),
    },
  },
}));
vi.mock('@tanstack/react-query', () => ({ useQuery: mockUseQuery }));
vi.mock('@/utils/featureFlags');
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return { ...actual, useNavigate: () => mockNavigate };
});

vi.mock('@carbon/react', async (importOriginal) => {
  const actual = await importOriginal<typeof import('@carbon/react')>();
  return {
    ...actual,
    DefinitionTooltip: ({ children, definition, ...props }: any) => (
      <span data-testid="definition-tooltip" {...props}>
        {children}
        {definition}
      </span>
    ),
    Pagination: ({ onChange, page, pageSize, ...props }: any) => (
      <div data-testid="pagination">
        <button
          data-testid="pagination-change"
          onClick={() => onChange?.({ page: 2, pageSize: 15 })}
        >
          Change page
        </button>
      </div>
    ),
  };
});

import TenureIdentification from '@/components/OpeningDetails/TenureIdentification';

const primaryTenure = {
  cboaId: 11,
  revisionCount: 1,
  primaryTenure: true,
  fileId: 'F1',
  cutBlock: 'B1',
  cuttingPermit: 'CP1',
  timberMark: 'TM1',
  status: { code: 'A', description: 'Active' },
  plannedGrossArea: 15.2,
  plannedNetArea: 12.0,
};

const secondaryTenure = {
  cboaId: 12,
  revisionCount: 1,
  primaryTenure: false,
  fileId: 'F2',
  cutBlock: null,
  cuttingPermit: null,
  timberMark: null,
  status: null,
  plannedGrossArea: null,
  plannedNetArea: null,
};

describe('TenureIdentification', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(false);

    mockUseQuery.mockImplementation(({ queryFn }) => {
      // Execute queryFn to cover its sorting and parameter logic
      queryFn();

      return {
        data: {
          content: [primaryTenure, secondaryTenure],
          page: { number: 0, size: 5, totalElements: 2, totalPages: 1 },
          primary: primaryTenure,
          totalUnfiltered: 2,
        },
        isFetching: false,
        isLoading: false,
      };
    });
  });

  it('renders loading skeleton when tenure query is loading', () => {
    mockUseQuery.mockReturnValue({
      data: undefined,
      isFetching: true,
      isLoading: true,
    });

    const { container } = render(<TenureIdentification openingId={123} />);
    expect(container.querySelector('.cds--skeleton')).toBeInTheDocument();
  });

  it('renders empty section when totalUnfiltered is 0', () => {
    mockUseQuery.mockReturnValue({
      data: {
        content: [],
        page: { number: 0, size: 5, totalElements: 0, totalPages: 0 },
        primary: null,
        totalUnfiltered: 0,
      },
      isFetching: false,
      isLoading: false,
    });

    render(<TenureIdentification openingId={123} />);

    expect(screen.getByText('Nothing to show yet!')).toBeInTheDocument();
    expect(screen.getByText('No tenures have been added to this opening yet.')).toBeInTheDocument();
  });

  it('renders header, primary tenure tooltip, and table rows with cells', () => {
    render(<TenureIdentification openingId={123} />);

    expect(screen.getByText(/2 tenures in this opening/i)).toBeInTheDocument();
    expect(screen.getByTestId('definition-tooltip')).toBeInTheDocument();

    // Primary tenure fileId and badge
    expect(screen.getAllByText('F1').length).toBeGreaterThan(0);
    expect(screen.getByText('Primary tenure')).toBeInTheDocument();
    expect(screen.getAllByText('TM1').length).toBeGreaterThan(0);

    // Secondary tenure fileId and placeholders
    expect(screen.getByText('F2')).toBeInTheDocument();
    expect(screen.getAllByText('--').length).toBeGreaterThanOrEqual(1);
  });

  it('does not render Edit tenure information when the feature is gated', () => {
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(true);

    render(<TenureIdentification openingId={123} />);

    expect(screen.queryByRole('button', { name: 'Edit tenure information' })).toBeNull();
  });

  it('takes the user to the Edit Tenure route when the feature is not gated', () => {
    render(<TenureIdentification openingId={123} />);
    fireEvent.click(screen.getByRole('button', { name: 'Edit tenure information' }));

    expect(mockNavigate).toHaveBeenCalledWith('/openings/123/edit-tenure');
  });

  it('handles search input change, submit via search button, and enter key', () => {
    render(<TenureIdentification openingId={123} />);

    const searchInput = screen.getByPlaceholderText('Search by keyword');
    fireEvent.change(searchInput, { target: { value: 'CP1' } });

    // Submit by clicking Search button
    const searchBtn = screen.getByRole('button', { name: 'Search' });
    fireEvent.click(searchBtn);

    // Press Enter in search input
    fireEvent.keyDown(searchInput, { key: 'Enter' });

    // Clear search with empty string
    fireEvent.change(searchInput, { target: { value: '' } });
    fireEvent.click(searchBtn);

    // Clear search via clear button
    const clearBtn = screen.getByRole('button', { name: 'Clear search input' });
    fireEvent.click(clearBtn);
  });

  it('handles sorting when sort button is clicked', () => {
    render(<TenureIdentification openingId={123} />);

    // In Carbon TableHeader, the sort button is inside the th
    const sortBtn = screen.getByRole('button', { name: /File ID/i });
    fireEvent.click(sortBtn); // ASC
    fireEvent.click(sortBtn); // DESC
    fireEvent.click(sortBtn); // NONE
  });

  it('renders OpeningTenureTooltip component details', () => {
    render(<TenureIdentification openingId={123} />);
    const tooltipEl = screen.getByTestId('definition-tooltip');
    expect(tooltipEl).toBeInTheDocument();
  });

  it('handles pagination page and size changes', () => {
    render(<TenureIdentification openingId={123} />);

    const paginationBtn = screen.getByTestId('pagination-change');
    fireEvent.click(paginationBtn);
  });

  it('renders empty search results section when totalElements is 0 and filtered', () => {
    mockUseQuery.mockReturnValue({
      data: {
        content: [],
        page: { number: 0, size: 5, totalElements: 0, totalPages: 0 },
        primary: primaryTenure,
        totalUnfiltered: 2,
      },
      isFetching: false,
      isLoading: false,
    });

    render(<TenureIdentification openingId={123} />);

    expect(screen.getByText(/No results for/i)).toBeInTheDocument();
    expect(screen.getByText(/Consider adjusting your search term and try again./i)).toBeInTheDocument();
  });
});
