import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import StockingStandardsSearchInput from '@/components/StockingStandardsSearchSection/StockingStandardsSearchInput';
import { renderWithProviders } from '../../utils/testAuthProvider';

// Mock API
vi.mock('@/services/API', () => ({
  default: {
    CodesEndpointService: {
      getSilvTreeSpeciesCodes: vi.fn().mockResolvedValue([
        { code: 'CW', description: 'Western Red Cedar' },
        { code: 'FD', description: 'Douglas Fir' },
      ]),
      getOpeningOrgUnits: vi.fn().mockResolvedValue([
        { code: 'DJA', description: 'Fort St. James' },
      ]),
    },
    ForestClientEndpointService: {
      searchByClientNumbers: vi.fn().mockResolvedValue([]),
      searchForestClients: vi.fn().mockResolvedValue([]),
    },
  },
}));

describe('StockingStandardsSearchInput', () => {
  const mockHandleSearchFieldChange = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders standard inputs and handles stocking standards ID change', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsSearchInput
        searchParams={{}}
        queryParams={{}}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const ssidInput = screen.getByLabelText('Stocking standards ID');
    expect(ssidInput).toBeInTheDocument();

    fireEvent.change(ssidInput, { target: { value: '10023' } });
    fireEvent.blur(ssidInput);
    expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('standardsRegimeId', 10023);
  });

  it('displays SSID override warning when standardsRegimeId and other query filters exist', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsSearchInput
        searchParams={{}}
        queryParams={{
          standardsRegimeId: '10023',
          fspId: '55',
        }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    expect(screen.getByText('Stocking standards ID takes priority')).toBeInTheDocument();
    expect(
      screen.getByText('Other filters are ignored when a Stocking standards ID is provided.')
    ).toBeInTheDocument();
  });

  it('does not display SSID override warning when only standardsRegimeId exists', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsSearchInput
        searchParams={{}}
        queryParams={{
          standardsRegimeId: '10023',
        }}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    expect(screen.queryByText('Stocking standards ID takes priority')).not.toBeInTheDocument();
  });

  it('toggles more filters when More filters button is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    render(
      <StockingStandardsSearchInput
        searchParams={{}}
        queryParams={{}}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const toggleButton = screen.getByRole('button', { name: /More filters/i });
    expect(toggleButton).toBeInTheDocument();

    await user.click(toggleButton);

    expect(screen.getByLabelText('FSP ID')).toBeInTheDocument();
  });

  it('auto-expands more filters when hidden filter values exist in searchParams', () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsSearchInput
        searchParams={{
          fspId: '999',
        }}
        queryParams={{}}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    expect(screen.getByLabelText('FSP ID')).toBeInTheDocument();
  });

  it('updates fspId on input change', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <StockingStandardsSearchInput
        searchParams={{ fspId: '123' }}
        queryParams={{}}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const fspInput = screen.getByLabelText('FSP ID');
    fireEvent.change(fspInput, { target: { value: '456' } });
    fireEvent.blur(fspInput);

    expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('fspId', '456');
  });
});
