import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, vi, expect } from 'vitest';
import OpeningsSearch from '../../screens/OpeningsSearch';
import { renderWithProviders } from '../utils/testAuthProvider';

// Mock the OpeningSearch service so the query resolves predictably
vi.mock('../../services/OpeningSearchService', () => ({
  openingSearch: vi.fn().mockResolvedValue({
    content: [],
    page: { totalElements: 0, size: 10, page: 0, totalPages: 0 },
  }),
}));

// Mock URL param utils used during mount
vi.mock('../../utils/OpeningSearchParamsUtils', () => ({
  readOpeningSearchUrlParams: () => ({}),
  updateOpeningSearchUrlParams: vi.fn(),
  hasActiveFilters: () => false,
}));

describe('OpeningsSearch screen', () => {
  it('renders title and action buttons', async () => {
    const { wrapper } = renderWithProviders();
    render(<OpeningsSearch />, { wrapper });

    // Page title
    expect(screen.getByText('Openings Search')).toBeInTheDocument();

    // Buttons
    expect(screen.getByRole('button', { name: /Clear all/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Search/i })).toBeInTheDocument();

    // With no query params and no active filters, the search results banner should not be visible
    await waitFor(() => {
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });
});
