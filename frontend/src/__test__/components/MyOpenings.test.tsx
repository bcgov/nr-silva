import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import MyOpenings from '../../components/MyOpenings';
import '@testing-library/jest-dom';

// Mock sub-components and services
vi.mock('@/components/OpeningsMap', () => ({
  default: ({ openingIds }: any) => <div data-testid="openings-map">Map: {openingIds.length} openings</div>
}));

vi.mock('@/components/SectionTitle', () => ({
  default: ({ title, subtitle }: any) => (
    <div data-testid="section-title">
      <h2>{title}</h2>
      <p>{subtitle}</p>
    </div>
  )
}));

vi.mock('@/components/TableSkeleton', () => ({
  default: () => <div data-testid="table-skeleton">Loading...</div>
}));

vi.mock('@/components/EmptySection', () => ({
  default: ({ icon, title, description }: any) => (
    <div data-testid="empty-section">
      <div>{icon}</div>
      <h3>{title}</h3>
      <p>{description}</p>
    </div>
  )
}));

vi.mock('@/components/OpeningTableRow', () => ({
  default: ({ opening, onSelect, isSelected }: any) => (
    <tr data-testid={`opening-row-${opening?.id}`}>
      <td>{opening?.id}</td>
      <td>{isSelected ? 'selected' : 'not selected'}</td>
    </tr>
  )
}));

vi.mock('@/hooks/UseBreakpoint', () => ({
  default: () => 'lg'
}));

const createQueryClient = () => new QueryClient({
  defaultOptions: {
    queries: { retry: false },
  },
});

const wrapper = ({ children }: any) => (
  <BrowserRouter>
    <QueryClientProvider client={createQueryClient()}>
      {children}
    </QueryClientProvider>
  </BrowserRouter>
);

describe('MyOpenings Component', () => {
  it('renders the section title', () => {
    render(<MyOpenings />, { wrapper });
    expect(screen.getByText('My openings')).toBeInTheDocument();
  });

  it('renders toggle map button', () => {
    render(<MyOpenings />, { wrapper });
    const button = screen.getByTestId('toggle-map-button');
    expect(button).toBeInTheDocument();
    expect(button).toHaveTextContent('Show map');
  });

  it('renders with defaultMapOpen=true shows Hide map', () => {
    render(<MyOpenings defaultMapOpen={true} />, { wrapper });
    const button = screen.getByTestId('toggle-map-button');
    expect(button).toHaveTextContent('Hide map');
  });

  it('renders loading state with table skeleton', async () => {
    render(<MyOpenings />, { wrapper });
    // The table skeleton appears while loading
    expect(screen.getByTestId('table-skeleton')).toBeInTheDocument();
  });

  it('has map button disabled initially (no data)', () => {
    render(<MyOpenings />, { wrapper });
    const button = screen.getByTestId('toggle-map-button');
    expect(button).toBeDisabled();
  });

  it('renders with correct props types', () => {
    const { container } = render(<MyOpenings defaultMapOpen={false} />, { wrapper });
    expect(container.querySelector('.my-openings-container')).toBeInTheDocument();
  });

  it('renders title and subtitle from SectionTitle', () => {
    render(<MyOpenings />, { wrapper });
    expect(screen.getByText(/View all openings you have created/)).toBeInTheDocument();
  });
});
