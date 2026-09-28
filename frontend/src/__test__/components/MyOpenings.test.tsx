import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import MyOpenings from '../../components/MyOpenings';
import API from '@/services/API';
import '@testing-library/jest-dom';

vi.mock('@/services/API', () => ({
  default: {
    OpeningEndpointService: {
      getUserCreatedOpenings: vi.fn(),
    },
  },
}));

vi.mock('@/components/OpeningsMap', () => ({
  default: ({ openingIds, setOpeningPolygonNotFound }: any) => (
    <div data-testid="openings-map">
      <span>Map with {openingIds.length} openings</span>
      <button
        data-testid="trigger-map-error-btn"
        onClick={() => setOpeningPolygonNotFound(true, 5555)}
      >
        Trigger Error
      </button>
    </div>
  ),
}));

vi.mock('@/components/SectionTitle', () => ({
  default: ({ title, subtitle }: any) => (
    <div data-testid="section-title">
      <h2>{title}</h2>
      <p>{subtitle}</p>
    </div>
  ),
}));

vi.mock('@/components/TableSkeleton', () => ({
  default: () => <div data-testid="table-skeleton">Loading...</div>,
}));

vi.mock('@/components/EmptySection', () => ({
  default: ({ icon, title, description }: any) => (
    <div data-testid="empty-section">
      <div>{icon}</div>
      <h3>{title}</h3>
      <p>{description}</p>
    </div>
  ),
}));

vi.mock('@/components/OpeningTableRow', () => ({
  default: ({ rowData, handleRowSelection, selectedRows }: any) => (
    <tr data-testid={`opening-row-${rowData?.openingId}`}>
      <td>{rowData?.openingId}</td>
      <td>
        <button
          data-testid={`select-row-${rowData?.openingId}`}
          onClick={() => handleRowSelection(rowData?.openingId)}
        >
          {selectedRows.includes(rowData?.openingId) ? 'selected' : 'not selected'}
        </button>
      </td>
    </tr>
  ),
}));

vi.mock('@/hooks/UseBreakpoint', () => ({
  default: () => 'lg',
}));

const createQueryClient = () =>
  new QueryClient({
    defaultOptions: {
      queries: { retry: false },
    },
  });

const renderComponent = (props: { defaultMapOpen?: boolean } = {}) => {
  const queryClient = createQueryClient();
  return render(
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <MyOpenings {...props} />
      </QueryClientProvider>
    </BrowserRouter>
  );
};

describe('MyOpenings Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders the section title', () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockReturnValue(
      new Promise(() => {})
    );
    renderComponent();
    expect(screen.getByText('My openings')).toBeInTheDocument();
  });

  it('renders toggle map button', () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockReturnValue(
      new Promise(() => {})
    );
    renderComponent();
    const button = screen.getByTestId('toggle-map-button');
    expect(button).toBeInTheDocument();
    expect(button).toHaveTextContent('Show map');
  });

  it('renders with defaultMapOpen=true shows Hide map', () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockReturnValue(
      new Promise(() => {})
    );
    renderComponent({ defaultMapOpen: true });
    const button = screen.getByTestId('toggle-map-button');
    expect(button).toHaveTextContent('Hide map');
  });

  it('renders loading state with table skeleton', async () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockReturnValue(
      new Promise(() => {})
    );
    renderComponent();
    expect(screen.getByTestId('table-skeleton')).toBeInTheDocument();
  });

  it('has map button disabled initially (no data)', () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockReturnValue(
      new Promise(() => {})
    );
    renderComponent();
    const button = screen.getByTestId('toggle-map-button');
    expect(button).toBeDisabled();
  });

  it('renders title and subtitle from SectionTitle', () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockReturnValue(
      new Promise(() => {})
    );
    renderComponent();
    expect(screen.getByText(/View all openings you have created/)).toBeInTheDocument();
  });

  it('renders empty section when no content returned', async () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockResolvedValueOnce({
      content: [],
    });
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('There are no openings to show yet')).toBeInTheDocument();
    });
  });

  it('renders error state when fetch fails', async () => {
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockRejectedValueOnce(
      new Error('API Server error')
    );
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('Error loading openings')).toBeInTheDocument();
      expect(screen.getByText('API Server error')).toBeInTheDocument();
    });
  });

  it('renders table data, toggles map, selects rows, handles pagination and map error', async () => {
    const mockData = {
      content: [
        { openingId: 101, status: { code: 'APP', description: 'Approved' } },
        { openingId: 102, status: { code: 'DFT', description: 'Draft' } },
      ],
      page: {
        totalElements: 50,
        number: 0,
        size: 5,
      },
    };
    (API.OpeningEndpointService.getUserCreatedOpenings as vi.Mock).mockResolvedValue(mockData);

    renderComponent();

    await waitFor(() => {
      expect(screen.getByRole('table', { name: 'My openings table' })).toBeInTheDocument();
    });

    expect(screen.getByTestId('opening-row-101')).toBeInTheDocument();
    expect(screen.getByTestId('opening-row-102')).toBeInTheDocument();

    const toggleButton = screen.getByTestId('toggle-map-button');
    expect(toggleButton).toBeEnabled();

    // Toggle map on
    fireEvent.click(toggleButton);
    expect(toggleButton).toHaveTextContent('Hide map');
    expect(screen.getByTestId('openings-map')).toBeInTheDocument();

    // Select row 101
    fireEvent.click(screen.getByTestId('select-row-101'));
    expect(screen.getByText('Map with 1 openings')).toBeInTheDocument();

    // Deselect row 101
    fireEvent.click(screen.getByTestId('select-row-101'));
    expect(screen.getByText('Map with 0 openings')).toBeInTheDocument();

    // Trigger map error
    fireEvent.click(screen.getByTestId('trigger-map-error-btn'));
    expect(
      screen.getByText('No map data available for this opening ID')
    ).toBeInTheDocument();

    // Test pagination forward button
    const nextPageBtn = screen.getByRole('button', { name: /Next page/i });
    fireEvent.click(nextPageBtn);

    await waitFor(() => {
      expect(API.OpeningEndpointService.getUserCreatedOpenings).toHaveBeenCalledWith(
        1,
        expect.any(Number)
      );
      expect(toggleButton).toBeEnabled();
    });

    // Toggle map off
    fireEvent.click(toggleButton);
    expect(toggleButton).toHaveTextContent('Show map');
  });
});

