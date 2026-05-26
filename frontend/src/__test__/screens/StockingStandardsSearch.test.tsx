import { describe, it, expect, beforeEach, vi, afterEach } from 'vitest';
import { render, screen, within } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import StockingStandardsSearch from '@/screens/StockingStandardsSearch';

// Mock the child component to isolate this screen
vi.mock('@/components/StockingStandardsSearchSection', () => ({
  default: () => <div data-testid="stocking-standards-search-section">Search Section</div>,
}));

// Mock PageTitle
vi.mock('@/components/PageTitle', () => ({
  default: ({ title }: { title: string }) => <h1>{title}</h1>,
}));

describe('StockingStandardsSearch Screen', () => {
  beforeEach(() => {
    document.title = 'Silva';
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should render the page with correct title', () => {
    render(
      <BrowserRouter>
        <StockingStandardsSearch />
      </BrowserRouter>
    );

    expect(screen.getByRole('heading', { name: /Stocking standards/i })).toBeInTheDocument();
  });

  it('should set document title on mount', () => {
    render(
      <BrowserRouter>
        <StockingStandardsSearch />
      </BrowserRouter>
    );

    expect(document.title).toBe('Stocking Standards Search - Silva');
  });

  it('should restore document title on unmount', () => {
    const { unmount } = render(
      <BrowserRouter>
        <StockingStandardsSearch />
      </BrowserRouter>
    );

    unmount();

    expect(document.title).toBe('Silva');
  });

  it('should render StockingStandardsSearchSection child component', () => {
    render(
      <BrowserRouter>
        <StockingStandardsSearch />
      </BrowserRouter>
    );

    expect(screen.getByTestId('stocking-standards-search-section')).toBeInTheDocument();
  });

  it('should have proper grid structure', () => {
    const { container } = render(
      <BrowserRouter>
        <StockingStandardsSearch />
      </BrowserRouter>
    );

    const grids = container.querySelectorAll('.default-grid');
    expect(grids.length).toBeGreaterThan(0);

    const defaultSearchGrid = container.querySelector('.default-search-grid');
    expect(defaultSearchGrid).toBeInTheDocument();
  });
});
