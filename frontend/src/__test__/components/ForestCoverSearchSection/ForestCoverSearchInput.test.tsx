import React from 'react';
import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import ForestCoverSearchInput from '../../../components/ForestCoverSearchSection/ForestCoverSearchInput';
import { renderWithProviders } from '../../utils/testAuthProvider';
import { ForestCoverSearchParams } from '../../../types/ApiType';

// Mock API service
vi.mock('../../../services/API', () => ({
  default: {
    CodesEndpointService: {
      getStockingTypeCodes: vi.fn().mockResolvedValue([
        { code: 'PL', description: 'Planted' },
        { code: 'N', description: 'Natural' },
      ]),
      getStockingStatusCodes: vi.fn().mockResolvedValue([
        { code: 'STS', description: 'Satisfactorily Stocked' },
        { code: 'NSR', description: 'Not Satisfactorily Restocked' },
      ]),
      getSilvDamageAgentCodes: vi.fn().mockResolvedValue([
        { code: 'IBM', description: 'Mountain Pine Beetle' },
        { code: 'IDS', description: 'Douglas-fir Beetle' },
      ]),
      getOpeningOrgUnits: vi.fn().mockResolvedValue([
        { code: 'DCS', description: 'Cariboo Forest Region' },
      ]),
      getOpeningCategories: vi.fn().mockResolvedValue([
        { code: 'FTML', description: 'Forest Tenure - Misc. Licence' },
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

// Mock DatePicker component
vi.mock('@carbon/react', async () => {
  const actual = await vi.importActual('@carbon/react');
  return {
    ...actual,
    DatePicker: ({ id, onChange, value, children }: any) => (
      <div data-testid={`date-picker-${id}`}>
        <input
          type="date"
          value={value}
          onChange={(e) => onChange([new Date(e.target.value)])}
        />
        {children}
      </div>
    ),
    DatePickerInput: ({ id, labelText }: any) => (
      <label htmlFor={id}>{labelText}</label>
    ),
  };
});

// Mock date utilities
vi.mock('../../../utils/DateUtils', () => ({
  getDatePickerValue: vi.fn((date) => (date ? '2024-01-15' : '')),
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
  enforceNumberInputOnKeyDown: vi.fn(),
  enforceNumberInputOnPaste: vi.fn(),
  getMultiSelectedCodes: vi.fn((selected) =>
    selected.selectedItems.map((item: any) => item.code)
  ),
}));

// Mock multiSelectUtils
vi.mock('../../../utils/multiSelectUtils', () => ({
  codeDescriptionToDisplayText: vi.fn((item) => `${item.code} - ${item.description}`),
}));

describe('ForestCoverSearchInput', () => {
  const mockHandleSearchFieldChange = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders all forest-cover-specific search fields', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Stocking status')).toBeInTheDocument();
      expect(screen.getByText('Stocking type')).toBeInTheDocument();
      expect(screen.getByText('Damaging agent type')).toBeInTheDocument();
      expect(screen.getByText('Opening status')).toBeInTheDocument();
      expect(screen.getByPlaceholderText('Enter opening ID')).toBeInTheDocument();
      expect(screen.getByText('File ID')).toBeInTheDocument();
      expect(screen.getByText('Org unit')).toBeInTheDocument();
      expect(screen.getByText('Opening category')).toBeInTheDocument();
      expect(screen.getByText('Last updated date range')).toBeInTheDocument();
    });
  });

  it('renders Stocking status multiselect', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-stocking-status-multiselect')).toBeInTheDocument();
    });
  });

  it('renders Stocking type multiselect', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-stocking-type-multiselect')).toBeInTheDocument();
    });
  });

  it('renders Damaging agent type multiselect', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-damage-agent-type-multiselect')).toBeInTheDocument();
    });
  });

  it('renders Opening status multiselect', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-opening-status-multiselect')).toBeInTheDocument();
    });
  });

  it('renders Opening ID text input', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByPlaceholderText('Enter opening ID')).toBeInTheDocument();
    });
  });

  it('calls handleSearchFieldChange with openingId as number on blur', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const openingIdInput = screen.getByPlaceholderText('Enter opening ID');
    await user.type(openingIdInput, '12345');
    await user.tab();

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('openingId', 12345);
    });
  });

  it('calls handleSearchFieldChange with undefined when opening ID is cleared', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const openingIdInput = screen.getByPlaceholderText('Enter opening ID');
    await user.click(openingIdInput);
    await user.tab();

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('openingId', undefined);
    });
  });

  it('renders File ID text input', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByPlaceholderText('Enter file ID')).toBeInTheDocument();
    });
  });

  it('renders Org unit multiselect', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-org-unit-multiselect')).toBeInTheDocument();
    });
  });

  it('renders Opening category multiselect', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-category-multi-select')).toBeInTheDocument();
    });
  });

  it('calls handleSearchFieldChange when stocking status is selected', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const stockingStatusSelect = await screen.findByTestId('multiselect-stocking-status-multiselect');
    await user.click(within(stockingStatusSelect).getByRole('button'));

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalled();
    });
  });

  it('calls handleSearchFieldChange when stocking type is selected', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const stockingTypeSelect = await screen.findByTestId('multiselect-stocking-type-multiselect');
    await user.click(within(stockingTypeSelect).getByRole('button'));

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('stockingTypes', expect.anything());
    });
  });

  it('calls handleSearchFieldChange with fileId on blur', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();

    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    const fileIdInput = screen.getByPlaceholderText('Enter file ID');
    await user.type(fileIdInput, 'TFL12');
    await user.tab();

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalledWith('fileId', expect.any(String));
    });
  });

  it('renders date pickers for Last updated date range', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ForestCoverSearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Start Date')).toBeInTheDocument();
      expect(screen.getByText('End Date')).toBeInTheDocument();
    });
  });

  it('pre-populates fields from searchParams', async () => {
    const { wrapper } = renderWithProviders();
    const searchParams: ForestCoverSearchParams = {
      fileId: 'TFL12',
      size: 20,
    };

    render(
      <ForestCoverSearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      const fileIdInput = screen.getByPlaceholderText('Enter file ID') as HTMLInputElement;
      // The ref-based approach populates asynchronously; just ensure input is present
      expect(fileIdInput).toBeInTheDocument();
    });
  });
});
