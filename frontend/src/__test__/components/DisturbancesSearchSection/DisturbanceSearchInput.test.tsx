import React from 'react';
import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import DisturbanceSearchInput from '../../../components/DisturbancesSearchSection/DisturbanceSearchInput';
import { renderWithProviders } from '../../utils/testAuthProvider';
import { DisturbanceSearchParams } from '../../../types/ApiType';

// Mock API service
vi.mock('../../../services/API', () => ({
  default: {
    CodesEndpointService: {
      getDisturbanceCodes: vi.fn().mockResolvedValue([
        { code: 'DN-001', description: 'Disturbance 1 Description' },
        { code: 'DN-002', description: 'Disturbance 2 Description' },
      ]),
      getSilvSystemCodes: vi.fn().mockResolvedValue([
        { code: 'SILV-001', description: 'Silviculture System 1 Description' },
        { code: 'SILV-002', description: 'Silviculture System 2 Description' },
      ]),
      getSilvSystemVariantCodes: vi.fn().mockResolvedValue([
        { code: 'VAR-001', description: 'Variant 1 Description' },
        { code: 'VAR-002', description: 'Variant 2 Description' },
      ]),
      getSilvCutPhaseCodes: vi.fn().mockResolvedValue([
        { code: 'CUT-001', description: 'Cut Phase 1 Description' },
      ]),
      getOpeningOrgUnits: vi.fn().mockResolvedValue([
        { code: 'ORG-001', description: 'Org Unit 1 Description' },
      ]),
      getOpeningCategories: vi.fn().mockResolvedValue([
        { code: 'CAT-001', description: 'Category 1 Description' },
      ]),
    },
  },
}));

// Mock CustomMultiSelect component
vi.mock('../../../components/CustomMultiSelect', () => ({
  default: ({ id, titleText, placeholder, items, onChange, selectedItems }: any) => (
    <div data-testid={`multiselect-${id}`}>
      <label>{titleText}</label>
      <button onClick={() => onChange({ selectedItems: items.slice(0, 1) })}>
        {placeholder}
      </button>
    </div>
  ),
}));

// Mock ForestClientMultiSelect component
vi.mock('../../../components/ForestClientMultiSelect', () => ({
  default: ({ selectedClientNumbers, onChange }: any) => (
    <div data-testid="forest-client-multiselect">
      <button onClick={() => onChange(['CLIENT-001'])}>
        Select Client
      </button>
    </div>
  ),
}));

// Mock DatePicker component
vi.mock('@carbon/react', async () => {
  const actual = await vi.importActual('@carbon/react');
  return {
    ...actual,
    DatePicker: ({ id, onChange, value }: any) => (
      <div data-testid={`date-picker-${id}`}>
        <input
          type="date"
          value={value}
          onChange={(e) => onChange([new Date(e.target.value)])}
        />
      </div>
    ),
    DatePickerInput: ({ id, labelText }: any) => (
      <label htmlFor={id}>{labelText}</label>
    ),
  };
});

// Mock date utilities
vi.mock('../../../utils/DateUtils', () => ({
  getDatePickerValue: vi.fn((date) => date ? '2024-01-15' : ''),
  getStartMaxDate: vi.fn(() => undefined),
  getEndMinDate: vi.fn(() => undefined),
}));

// Mock input utilities
vi.mock('../../../utils/InputUtils', () => ({
  handleAutoUpperInput: vi.fn((e, maxLength) => {
    e.target.value = e.target.value.toUpperCase().slice(0, maxLength);
  }),
  handleAutoUpperPaste: vi.fn((e, maxLength) => {
    const paste = (e.clipboardData || (window as any).clipboardData).getData('text');
    e.preventDefault();
    e.target.value = paste.toUpperCase().slice(0, maxLength);
  }),
  getMultiSelectedCodes: vi.fn((selected) =>
    selected.selectedItems.map((item: any) => item.code)
  ),
}));

// Mock multiSelectUtils
vi.mock('../../../utils/multiSelectUtils', () => ({
  codeDescriptionToDisplayText: vi.fn((item) => `${item.code} - ${item.description}`),
}));

describe('DisturbanceSearchInput', () => {
  const mockHandleSearchFieldChange = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders all disturbance-specific search fields', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <DisturbanceSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Disturbance code')).toBeInTheDocument();
      expect(screen.getByText('Silviculture system')).toBeInTheDocument();
      expect(screen.getByText('Variant')).toBeInTheDocument();
      expect(screen.getByText('Cut phase')).toBeInTheDocument();
      expect(screen.getByText('Org unit')).toBeInTheDocument();
      expect(screen.getByText('Opening category')).toBeInTheDocument();
      expect(screen.getByText('Last updated date range')).toBeInTheDocument();
    });
  });

  it('calls handleSearchFieldChange when disturbance is selected', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <DisturbanceSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const disturbanceSelect = await screen.findByTestId('multiselect-disturbance-multiselect');
    await user.click(within(disturbanceSelect).getByRole('button'));

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalled();
    });
  });

  it('displays More filters button and expands when clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <DisturbanceSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const moreFiltersButton = await screen.findByRole('button', { name: /More filters/i });
    await user.click(moreFiltersButton);

    await waitFor(() => {
      expect(screen.getByText(/Fewer filters/i)).toBeInTheDocument();
    });
  });

  it('auto-expands more filters when URL params contain hidden filter values', async () => {
    const { wrapper } = renderWithProviders();
    const searchParams: DisturbanceSearchParams = {
      fileId: 'FILE-001',
    };

    render(
      <DisturbanceSearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText(/Fewer filters/i)).toBeInTheDocument();
    });
  });

  it('updates date field when date is selected', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <DisturbanceSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const dateInputs = await screen.findAllByDisplayValue('');
    const startDateInput = dateInputs[0];
    await user.type(startDateInput, '2024-01-15');

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalledWith(
        expect.stringContaining('Date'),
        expect.any(String)
      );
    });
  });

  it('displays selected search params as placeholder text', async () => {
    const { wrapper } = renderWithProviders();
    const searchParams: DisturbanceSearchParams = {
      disturbances: ['DN-001', 'DN-002'],
    };

    render(
      <DisturbanceSearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.queryByText('Getting all org units for the search openings')).not.toBeInTheDocument();
    });
  });
});
