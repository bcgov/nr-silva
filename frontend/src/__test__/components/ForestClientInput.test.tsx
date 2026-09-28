import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ForestClientInput from '@/components/ForestClientInput';
import API from '@/services/API';
import { ForestClientAutocompleteResultDto, CodeDescriptionDto } from '@/services/OpenApi';

vi.mock('@/services/API', () => ({
  default: {
    ForestClientEndpointService: {
      searchForestClients: vi.fn(),
      getForestClientLocations: vi.fn(),
    },
  },
}));

const mockClients: ForestClientAutocompleteResultDto[] = [
  { id: '00012797', name: 'MINISTRY OF FORESTS', acronym: 'MOF' },
  { id: '00012798', name: 'SAMPLE FORESTRY CORP.', acronym: 'SFC' },
];

const mockLocations: CodeDescriptionDto[] = [
  { code: '00', description: 'Head Office' },
  { code: '01', description: 'Branch Office' },
];

describe('ForestClientInput', () => {
  const mockSetClientNumber = vi.fn();
  const mockSetClientLocationCode = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(API.ForestClientEndpointService.searchForestClients).mockResolvedValue(mockClients);
    vi.mocked(API.ForestClientEndpointService.getForestClientLocations).mockResolvedValue(mockLocations);
  });

  const renderComponent = (props: Partial<React.ComponentProps<typeof ForestClientInput>> = {}) => {
    const queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false },
        mutations: { retry: false },
      },
    });

    const defaultProps = {
      clientInputId: 'client-input',
      locationInputId: 'location-input',
      clientNumber: undefined,
      setClientNumber: mockSetClientNumber,
      locationCode: undefined,
      setClientLocationCode: mockSetClientLocationCode,
      ...props,
    };

    const renderResult = render(
      <QueryClientProvider client={queryClient}>
        <ForestClientInput {...defaultProps} />
      </QueryClientProvider>
    );

    return {
      ...renderResult,
      rerenderWithProps: (newProps: Partial<React.ComponentProps<typeof ForestClientInput>>) =>
        renderResult.rerender(
          <QueryClientProvider client={queryClient}>
            <ForestClientInput {...defaultProps} {...newProps} />
          </QueryClientProvider>
        ),
    };
  };

  it('renders client and location ComboBox inputs with initial disabled state on location', () => {
    renderComponent();

    const clientInput = screen.getByRole('combobox', { name: /client/i });
    const locationInput = screen.getByRole('combobox', { name: /location code/i });

    expect(clientInput).toBeInTheDocument();
    expect(locationInput).toBeInTheDocument();
    expect(locationInput).toBeDisabled();
  });

  it('does not trigger client search if input length <= 2 characters', async () => {
    const user = userEvent.setup();
    renderComponent();

    const clientInput = screen.getByRole('combobox', { name: /client/i });
    await user.type(clientInput, 'MO');

    await new Promise((r) => setTimeout(r, 350));
    expect(API.ForestClientEndpointService.searchForestClients).not.toHaveBeenCalled();
  });

  it('triggers debounced client search when typing > 2 characters', async () => {
    const user = userEvent.setup();
    renderComponent();

    const clientInput = screen.getByRole('combobox', { name: /client/i });
    await user.type(clientInput, 'MIN');

    await waitFor(
      () => {
        expect(API.ForestClientEndpointService.searchForestClients).toHaveBeenCalledWith('MIN');
      },
      { timeout: 1500 }
    );
  });

  it('resets selected client and location when typing in client input', async () => {
    renderComponent();

    const clientInput = screen.getByRole('combobox', { name: /client/i });
    fireEvent.change(clientInput, { target: { value: 'Some text' } });

    expect(mockSetClientNumber).toHaveBeenCalledWith({
      target: { value: '' },
    });
    expect(mockSetClientLocationCode).toHaveBeenCalledWith({
      target: { value: '' },
    });
  });

  it('selects a client and updates clientNumber and triggers location search', async () => {
    const user = userEvent.setup();
    renderComponent();

    const clientInput = screen.getByRole('combobox', { name: /client/i });
    await user.type(clientInput, 'MIN');

    const option = await screen.findByRole('option', { name: /00012797/i });
    await user.click(option);

    expect(mockSetClientNumber).toHaveBeenCalledWith({
      target: { value: '00012797' },
    });
    expect(API.ForestClientEndpointService.getForestClientLocations).toHaveBeenCalledWith('00012797');
  });

  it('selects a location code and calls setClientLocationCode', async () => {
    const user = userEvent.setup();
    renderComponent();

    const clientInput = screen.getByRole('combobox', { name: /client/i });
    await user.type(clientInput, 'MIN');

    const clientOption = await screen.findByRole('option', { name: /00012797/i });
    await user.click(clientOption);

    const locationInput = screen.getByRole('combobox', { name: /location code/i });
    await waitFor(() => {
      expect(locationInput).not.toBeDisabled();
    });

    await user.click(locationInput);

    const locationOption = await screen.findByRole('option', { name: /00 - Head Office/i });
    await user.click(locationOption);

    expect(mockSetClientLocationCode).toHaveBeenCalledWith({
      target: { value: '00' },
    });
  });

  it('clears selected client when clientNumber prop becomes null/empty', async () => {
    const { rerenderWithProps } = renderComponent({ clientNumber: '00012797' });

    rerenderWithProps({ clientNumber: '' });

    const locationInput = screen.getByRole('combobox', { name: /location code/i });
    expect(locationInput).toBeDisabled();
  });

  it('clears selected location when locationCode prop becomes null/empty', async () => {
    const { rerenderWithProps } = renderComponent({ locationCode: '00' });

    rerenderWithProps({ locationCode: '' });

    const locationInput = screen.getByRole('combobox', { name: /location code/i });
    expect(locationInput).toHaveValue('');
  });

  it('does not reset selected client if input matches the selected client label', async () => {
    const user = userEvent.setup();
    renderComponent();

    const clientInput = screen.getByRole('combobox', { name: /client/i });
    await user.type(clientInput, 'MIN');

    const clientOption = await screen.findByRole('option', { name: /00012797/i });
    await user.click(clientOption);

    mockSetClientNumber.mockClear();

    // Re-type the exact same label
    const label = (clientInput as HTMLInputElement).value || '';
    fireEvent.change(clientInput, { target: { value: label } });

    expect(mockSetClientNumber).not.toHaveBeenCalled();
  });

});
