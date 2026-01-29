import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import OpeningsSearchInput from '../../components/OpeningsSearchInput';
import { renderWithProviders } from '../utils/testAuthProvider';

// Mock API used by the component
vi.mock('../../services/API', () => ({
  default: {
    CodesEndpointService: {
      getOpeningCategories: vi.fn().mockResolvedValue([{ code: 'CAT1', description: 'Category 1' }]),
      getOpeningOrgUnits: vi.fn().mockResolvedValue([{ code: 'DAS', description: 'District A' }]),
    },
    ForestClientEndpointService: {
      searchByClientNumbers: vi.fn().mockResolvedValue([]),
      searchForestClients: vi.fn().mockResolvedValue([]),
    },
  },
}));

describe('OpeningsSearchInput', () => {
  it('renders basic inputs and calls onSearchParamsChange on blur', async () => {
    const handler = vi.fn();
    const { wrapper } = renderWithProviders();

    render(<OpeningsSearchInput searchParams={undefined} onSearchParamsChange={handler} />, { wrapper });

    // Label should exist
    expect(screen.getByLabelText('Opening ID')).toBeInTheDocument();

    // Simulate entering an opening id and blurring
    const input = screen.getByPlaceholderText('Enter opening ID');
    fireEvent.change(input, { target: { value: '123' } });
    fireEvent.blur(input);

    await waitFor(() => {
      expect(handler).toHaveBeenCalledWith('openingId', 123);
    });
  });
});
