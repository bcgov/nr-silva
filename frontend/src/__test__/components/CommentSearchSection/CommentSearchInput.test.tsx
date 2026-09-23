import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import CommentSearchInput from '@/components/CommentSearchSection/CommentSearchInput';
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

describe('CommentSearchInput', () => {
  const mockHandleSearchFieldChange = vi.fn();
  const mockOnSearch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders keyword input and triggers handleSearchFieldChange on change', () => {
    const { wrapper } = renderWithProviders();
    render(
      <CommentSearchInput
        searchParams={{}}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    const input = screen.getByLabelText('Keyword');
    fireEvent.change(input, { target: { value: 'harvesting' } });

    expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('searchTerm', 'harvesting');
  });

  it('triggers onSearch when Enter key is pressed in keyword input', () => {
    const { wrapper } = renderWithProviders();
    render(
      <CommentSearchInput
        searchParams={{ searchTerm: 'harvesting' }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    const input = screen.getByLabelText('Keyword');
    fireEvent.keyDown(input, { key: 'Enter' });

    expect(mockOnSearch).toHaveBeenCalled();
  });

  it('displays minimum character validation error when showValidation is true and keyword is too short', () => {
    const { wrapper } = renderWithProviders();
    render(
      <CommentSearchInput
        searchParams={{ searchTerm: 'ab' }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
        showValidation={true}
        onSearch={mockOnSearch}
      />,
      { wrapper }
    );

    expect(screen.getByText(/Minimum 3 characters required/i)).toBeInTheDocument();
  });

  it('displays maximum character validation error when keyword exceeds max length', () => {
    const { wrapper } = renderWithProviders();
    render(
      <CommentSearchInput
        searchParams={{ searchTerm: 'a'.repeat(2001) }}
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
      <CommentSearchInput
        searchParams={{ searchTerm: 'old' }}
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
