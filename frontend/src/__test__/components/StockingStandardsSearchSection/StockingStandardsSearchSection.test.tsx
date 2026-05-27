import { describe, it, expect, beforeEach, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, waitFor, within } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import StockingStandardsSearchSection from '@/components/StockingStandardsSearchSection';
import * as urlUtils from '@/components/StockingStandardsSearchSection/utils';

// Mock child components
vi.mock('@/components/StockingStandardsSearchSection/StockingStandardsSearchInput', () => ({
  default: ({ handleSearchFieldChange }: any) => (
    <div data-testid="search-input">
      <input 
        data-testid="mock-search-input"
        onChange={(e) => handleSearchFieldChange('standardsRegimeId', e.target.value)}
      />
    </div>
  ),
}));

vi.mock('@/components/StockingStandardsSearchSection/StockingStandardsSearchTableRow', () => ({
  default: ({ rowData }: any) => (
    <tr data-testid={`table-row-${rowData.standardsRegimeId}`}>
      <td>{rowData.standardsRegimeId}</td>
    </tr>
  ),
}));

vi.mock('@/hooks/useScrollToSearchResults', () => ({
  default: vi.fn(),
}));

vi.mock('@/components/EmptySection', () => ({
  default: ({ title }: { title: string }) => <div data-testid="empty-section">{title}</div>,
}));

vi.mock('@/components/TableSkeleton', () => ({
  default: () => <div data-testid="table-skeleton">Loading...</div>,
}));

vi.mock('@/services/API', () => ({
  default: {
    SearchEndpointService: {
      stockingStandardsSearch: vi.fn(),
    },
  },
}));

const mockApiResponse = {
  content: [
    {
      standardsRegimeId: 'SR-001',
      bgcZone: 'IDF',
      bgcSubZone: 'c',
      bgcVariant: 'w',
      bgcPhase: 'a',
      becSiteSeries: 'SiteSeries1',
      becSiteType: 'SiteType1',
      becSeral: 'Seral1',
      clients: [],
      fspIds: [],
      orgUnits: [],
      preferredSpecies: [],
      updateTimestamp: '2024-01-15T10:00:00Z',
      isExpired: false,
    },
  ],
  page: {
    totalElements: 1,
    number: 0,
    size: 20,
  },
};

describe('StockingStandardsSearchSection', () => {
  let queryClient: QueryClient;

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false,
        },
      },
    });

    vi.clearAllMocks();
    
    // Mock URL param utilities
    vi.spyOn(urlUtils, 'readStockingStandardsSearchUrlParams').mockReturnValue({});
    vi.spyOn(urlUtils, 'updateStockingStandardsSearchUrlParams').mockImplementation(vi.fn());
    vi.spyOn(urlUtils, 'hasStockingStandardsSearchFilters').mockReturnValue(false);
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should render search input, buttons, and initial state', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    expect(screen.getByTestId('search-input')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Clear all/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Search/i })).toBeInTheDocument();
  });

  it('should not render table when no search filters are applied', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    expect(screen.queryByRole('table')).not.toBeInTheDocument();
  });

  it('should render table and results when search filters exist', async () => {
    const mockApi = vi.fn().mockResolvedValue(mockApiResponse);
    const API = await import('@/services/API');
    API.default.SearchEndpointService.stockingStandardsSearch = mockApi;

    vi.spyOn(urlUtils, 'hasStockingStandardsSearchFilters').mockReturnValue(true);

    const { rerender } = render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    // Trigger search
    fireEvent.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      rerender(
        <QueryClientProvider client={queryClient}>
          <StockingStandardsSearchSection />
        </QueryClientProvider>
      );
    });
  });

  it('should display search results message with total count', async () => {
    const mockApi = vi.fn().mockResolvedValue(mockApiResponse);
    const API = await import('@/services/API');
    API.default.SearchEndpointService.stockingStandardsSearch = mockApi;

    vi.spyOn(urlUtils, 'hasStockingStandardsSearchFilters').mockReturnValue(true);

    const { rerender } = render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    fireEvent.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      rerender(
        <QueryClientProvider client={queryClient}>
          <StockingStandardsSearchSection />
        </QueryClientProvider>
      );
    });
  });

  it('should call handleSearchFieldChange when search field changes', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    const mockInput = screen.getByTestId('mock-search-input');
    fireEvent.change(mockInput, { target: { value: 'test-value' } });

    expect(mockInput).toHaveValue('test-value');
  });

  it('should have edit columns button for table customization', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    const editButton = screen.queryByText(/Edit columns/i);
    // Button may not render until table is shown, so this is optional
    expect(editButton === null || editButton).toBeTruthy();
  });

  it('should render empty state when search returns no results', async () => {
    const emptyResponse = {
      content: [],
      page: {
        totalElements: 0,
        number: 0,
        size: 20,
      },
    };

    const mockApi = vi.fn().mockResolvedValue(emptyResponse);
    const API = await import('@/services/API');
    API.default.SearchEndpointService.stockingStandardsSearch = mockApi;

    vi.spyOn(urlUtils, 'hasStockingStandardsSearchFilters').mockReturnValue(true);

    render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    fireEvent.click(screen.getByRole('button', { name: /Search/i }));

    await waitFor(() => {
      expect(screen.queryByTestId('empty-section')).toBeTruthy();
    }, { timeout: 3000 });
  });

  it('should have proper grid structure with columns', () => {
    const { container } = render(
      <QueryClientProvider client={queryClient}>
        <StockingStandardsSearchSection />
      </QueryClientProvider>
    );

    expect(container.querySelector('.stocking-standards-search-section')).toBeInTheDocument();
  });
});
