import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';
import API from '@/services/API';
import { ForestClientAutocompleteResultDto, ForestClientDto } from '@/services/OpenApi';
import * as breakpointHook from '@/hooks/UseBreakpoint';

vi.mock('@/services/API', () => ({
  default: {
    ForestClientEndpointService: {
      searchByClientNumbers: vi.fn(),
      searchForestClients: vi.fn(),
    },
  },
}));

vi.mock('@/hooks/UseBreakpoint', () => ({
  default: vi.fn(() => 'md'),
}));

const mockPrefetchedClients: ForestClientDto[] = [
  { clientNumber: '00012797', name: 'MINISTRY OF FORESTS', acronym: 'MOF' },
  { clientNumber: '00012798', name: 'SAMPLE FORESTRY CORP.', acronym: 'SFC' },
];

const mockSearchClients: ForestClientAutocompleteResultDto[] = [
  { id: '00012799', name: 'NORTHERN TIMBER CORP.', acronym: 'NTC' },
];

describe('ForestClientMultiSelect', () => {
  const mockOnChange = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(breakpointHook.default).mockReturnValue('md');
    vi.mocked(API.ForestClientEndpointService.searchByClientNumbers).mockResolvedValue(mockPrefetchedClients);
    vi.mocked(API.ForestClientEndpointService.searchForestClients).mockResolvedValue(mockSearchClients);
  });

  const renderComponent = (props: Partial<React.ComponentProps<typeof ForestClientMultiSelect>> = {}) => {
    const queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false },
        mutations: { retry: false },
      },
    });

    const defaultProps = {
      selectedClientNumbers: undefined,
      onChange: mockOnChange,
      ...props,
    };

    const result = render(
      <QueryClientProvider client={queryClient}>
        <ForestClientMultiSelect {...defaultProps} />
      </QueryClientProvider>
    );

    return {
      ...result,
      rerenderWithProps: (newProps: Partial<React.ComponentProps<typeof ForestClientMultiSelect>>) =>
        result.rerender(
          <QueryClientProvider client={queryClient}>
            <ForestClientMultiSelect {...defaultProps} {...newProps} />
          </QueryClientProvider>
        ),
    };
  };

  it('renders default placeholder when no clients are selected', () => {
    renderComponent();
    expect(screen.getByRole('combobox')).toHaveAttribute('placeholder', 'Choose one or more options');
  });

  it('prefetches clients on mount when selectedClientNumbers is provided', async () => {
    renderComponent({ selectedClientNumbers: ['00012797'] });

    await waitFor(() => {
      expect(API.ForestClientEndpointService.searchByClientNumbers).toHaveBeenCalledWith(['00012797'], 0, 1);
    });

    await waitFor(() => {
      expect(screen.getByRole('combobox')).toHaveAttribute('placeholder', 'MOF');
    });
  });

  it('captures initialClientNumbers asynchronously when selectedClientNumbers arrives after initial render', async () => {
    const { rerenderWithProps } = renderComponent({ selectedClientNumbers: [] });

    rerenderWithProps({ selectedClientNumbers: ['00012798'] });

    await waitFor(() => {
      expect(API.ForestClientEndpointService.searchByClientNumbers).toHaveBeenCalledWith(['00012798'], 0, 1);
    });
  });

  it('searches for clients when typing at least 3 characters', async () => {
    const user = userEvent.setup();
    renderComponent();

    const input = screen.getByRole('combobox');
    await user.type(input, 'NOR');

    await waitFor(() => {
      expect(API.ForestClientEndpointService.searchForestClients).toHaveBeenCalledWith('NOR');
    }, { timeout: 1500 });
  });

  it('does not search when typing fewer than 3 characters', async () => {
    const user = userEvent.setup();
    renderComponent();

    const input = screen.getByRole('combobox');
    await user.type(input, 'NO');

    await new Promise((r) => setTimeout(r, 250));
    expect(API.ForestClientEndpointService.searchForestClients).not.toHaveBeenCalled();
  });

  it('calls onChange when items are selected or deselected', async () => {
    renderComponent({ selectedClientNumbers: ['00012797'] });

    await waitFor(() => {
      expect(API.ForestClientEndpointService.searchByClientNumbers).toHaveBeenCalled();
    });

    const input = screen.getByRole('combobox');
    fireEvent.click(input);

    const option = await screen.findByText(/MINISTRY OF FORESTS/i);
    fireEvent.click(option);

    await waitFor(() => {
      expect(mockOnChange).toHaveBeenCalled();
    });
  });

  it('adjusts tooltip alignment to top when breakpoint is sm', () => {
    vi.mocked(breakpointHook.default).mockReturnValue('sm');
    renderComponent();
    expect(screen.getByText('Client')).toBeInTheDocument();
  });

  it('calls onChange with undefined when all selected items are deselected', async () => {
    renderComponent({ selectedClientNumbers: ['00012797'] });

    await waitFor(() => {
      expect(API.ForestClientEndpointService.searchByClientNumbers).toHaveBeenCalled();
    });

    const input = screen.getByRole('combobox');
    fireEvent.click(input);

    const option = await screen.findByText(/MINISTRY OF FORESTS/i);
    fireEvent.click(option);

    await waitFor(() => {
      expect(mockOnChange).toHaveBeenCalledWith(undefined);
    });
  });

  it('handles empty input change events safely', async () => {
    renderComponent();

    const input = screen.getByRole('combobox');
    fireEvent.change(input, { target: { value: 'FOREST' } });
    await waitFor(() => {
      expect(API.ForestClientEndpointService.searchForestClients).toHaveBeenCalledWith('FOREST');
    });

    fireEvent.change(input, { target: { value: '' } });
    expect(screen.getByRole('combobox')).toBeInTheDocument();
  });
});

