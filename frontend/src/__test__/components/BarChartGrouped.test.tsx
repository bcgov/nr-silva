import React from 'react';
import { render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import BarChartGrouped from '../../components/BarChartGrouped';
import { useDistrictListQuery, useFetchOpeningsPerYear } from '../../services/queries/dashboard/dashboardQueries';
import { describe, expect, it } from 'vitest';
import { vi } from 'vitest';
import '@testing-library/jest-dom';
// Mock the hook
vi.mock('../../services/queries/dashboard/dashboardQueries', () => ({
  useFetchOpeningsPerYear: vi.fn(),
  useDistrictListQuery: vi.fn(),
}));

const queryClient = new QueryClient();

describe('BarChartGrouped component', () => {
  it('should display loading state when data is fetching', () => {
    // Mock loading state for openings data
    (useFetchOpeningsPerYear as any).mockReturnValue({
      data: [],
      isLoading: true,
    });

    // If you're using useDistrictListQuery, mock it too
    (useDistrictListQuery as any).mockReturnValue({
      data: [],
      isLoading: false,
    });

    render(
      <QueryClientProvider client={queryClient}>
        <BarChartGrouped />
      </QueryClientProvider>
    );

    // Check if loading text is displayed
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });
});