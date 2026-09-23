import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import StockingStandardsCommentSearchInput from '@/components/StockingStandardsCommentSearchSection/StockingStandardsCommentSearchInput';
import { renderWithProviders } from '../../utils/testAuthProvider';

// Mock child components or API
vi.mock('@/services/API', () => ({
  default: {
    CodesEndpointService: {
      getOpeningOrgUnits: vi.fn().mockResolvedValue([]),
    },
    ForestClientEndpointService: {
      searchByClientNumbers: vi.fn().mockResolvedValue([]),
      searchForestClients: vi.fn().mockResolvedValue([]),
    },
  },
}));

describe('StockingStandardsCommentSearchInput', () => {
  const mockHandleSearchFieldChange = vi.fn();
  const mockOnSearch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders keyword input and triggers handleSearchFieldChange on change', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsCommentSearchInput
        searchParams={{}}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    const input = screen.getByLabelText('Keyword');
    fireEvent.change(input, { target: { value: 'regime' } });

    expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('searchTerm', 'regime');
  });

  it('triggers onSearch when Enter key is pressed', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsCommentSearchInput
        searchParams={{ searchTerm: 'regime' }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    const input = screen.getByLabelText('Keyword');
    fireEvent.keyDown(input, { key: 'Enter' });

    expect(mockOnSearch).toHaveBeenCalled();
  });

  it('displays minimum character validation error when showValidation is true and keyword is short', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsCommentSearchInput
        searchParams={{ searchTerm: 'x' }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        showValidation={true}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    expect(screen.getByText(/Minimum 3 characters required/i)).toBeInTheDocument();
  });

  it('displays maximum character validation error when keyword exceeds limit', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsCommentSearchInput
        searchParams={{ searchTerm: 'z'.repeat(2001) }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        showValidation={true}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    expect(screen.getByText(/Maximum 2000 characters allowed/i)).toBeInTheDocument();
  });

  it('handles clearing searchTerm by passing undefined', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsCommentSearchInput
        searchParams={{ searchTerm: 'existing' }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    const input = screen.getByLabelText('Keyword');
    fireEvent.change(input, { target: { value: '' } });

    expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('searchTerm', undefined);
  });
});
