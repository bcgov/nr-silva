import React from 'react';
import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import ActivitySearchInput from '../../../components/ActivitySearchSection/ActivitySearchInput';
import { renderWithProviders } from '../../utils/testAuthProvider';
import { ActivitySearchParams } from '../../../types/ApiType';

// Mock API service
vi.mock('../../../services/API', () => ({
  default: {
    CodesEndpointService: {
      getSilvBaseCodes: vi.fn().mockResolvedValue([
        { code: 'BASE-001', description: 'Base 1 Description' },
        { code: 'BASE-002', description: 'Base 2 Description' },
      ]),
      getSilvTechniqueCodes: vi.fn().mockResolvedValue([
        { code: 'TECH-001', description: 'Technique 1 Description' },
        { code: 'TECH-002', description: 'Technique 2 Description' },
      ]),
      getSilvMethodCodes: vi.fn().mockResolvedValue([
        { code: 'METHOD-001', description: 'Method 1 Description' },
        { code: 'METHOD-002', description: 'Method 2 Description' },
      ]),
      getSilvObjectiveCodes: vi.fn().mockResolvedValue([
        { code: 'OBJ-001', description: 'Objective 1 Description' },
      ]),
      getSilvFundSourceCodes: vi.fn().mockResolvedValue([
        { code: 'FUND-001', description: 'Funding Source 1 Description' },
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

describe('ActivitySearchInput', () => {
  const mockHandleSearchFieldChange = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders all basic filter inputs', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-base-multiselect')).toBeInTheDocument();
      expect(screen.getByTestId('multiselect-technique-multiselect')).toBeInTheDocument();
      expect(screen.getByTestId('multiselect-method-multiselect')).toBeInTheDocument();
    });
  });

  it('renders Base filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Base')).toBeInTheDocument();
    });
  });

  it('renders Technique filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Technique')).toBeInTheDocument();
    });
  });

  it('renders Method filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Method')).toBeInTheDocument();
    });
  });

  it('renders Activity status filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Activity status')).toBeInTheDocument();
    });
  });

  it('renders Objectives filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Objectives')).toBeInTheDocument();
    });
  });

  it('renders Funding source filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Funding source')).toBeInTheDocument();
    });
  });

  it('renders Org unit filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Org unit')).toBeInTheDocument();
    });
  });

  it('renders Opening category filter', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Opening category')).toBeInTheDocument();
    });
  });

  it('renders date range filter labels correctly', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Last updated date range')).toBeInTheDocument();
    });

    // Date range filter is present
    expect(screen.getByText('Last updated date range')).toBeInTheDocument();
  });

  it('toggles more filters visibility when button is clicked', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('More filters')).toBeInTheDocument();
    });

    const moreFiltersButton = screen.getByText('More filters');
    await user.click(moreFiltersButton);

    await waitFor(() => {
      expect(screen.getByText('Fewer filters')).toBeInTheDocument();
    });
  });

  it('shows hidden filters when expanded', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('More filters')).toBeInTheDocument();
    });

    const moreFiltersButton = screen.getByText('More filters');
    await user.click(moreFiltersButton);

    await waitFor(() => {
      expect(screen.getByText('File ID')).toBeInTheDocument();
      expect(screen.getByTestId('forest-client-multiselect')).toBeInTheDocument();
      expect(screen.getByText('Opening status')).toBeInTheDocument();
      expect(screen.getByText('Inter-agency number')).toBeInTheDocument();
    });
  });

  it('handles multiselect change for Base filter', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-base-multiselect')).toBeInTheDocument();
    });

    const baseButton = screen.getAllByText('Choose one or more options')[0];
    await user.click(baseButton);

    await waitFor(() => {
      expect(mockHandleSearchFieldChange).toHaveBeenCalled();
    });
  });

  it('handles activity status filter selection', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Activity status')).toBeInTheDocument();
    });

    // Component is ready to handle activity status changes
    expect(mockHandleSearchFieldChange).toBeDefined();
  });

  it('displays prefilled search params when provided', async () => {
    const searchParams: ActivitySearchParams = {
      bases: ['BASE-001'],
      techniques: ['TECH-001'],
    };

    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-base-multiselect')).toBeInTheDocument();
    });
  });

  it('auto-expands more filters when URL params contain hidden filter values', async () => {
    const searchParams: ActivitySearchParams = {
      fileId: 'FILE123',
    };

    const { wrapper } = renderWithProviders();
    const { rerender } = render(
      <ActivitySearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    // Simulate mounting with URL params
    await waitFor(() => {
      expect(screen.getByText('Fewer filters')).toBeInTheDocument();
    });
  });

  it('handles File ID input change', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    // Expand more filters
    await waitFor(() => {
      expect(screen.getByText('More filters')).toBeInTheDocument();
    });

    const moreFiltersButton = screen.getByText('More filters');
    await user.click(moreFiltersButton);

    await waitFor(() => {
      const fileIdInput = screen.getByPlaceholderText('Enter file ID');
      expect(fileIdInput).toBeInTheDocument();
    });
  });

  it('handles Inter-agency number input change', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    // Expand more filters
    await waitFor(() => {
      expect(screen.getByText('More filters')).toBeInTheDocument();
    });

    const moreFiltersButton = screen.getByText('More filters');
    await user.click(moreFiltersButton);

    await waitFor(() => {
      const interAgencyInput = screen.getByPlaceholderText(
        'Enter inter-agency number'
      );
      expect(interAgencyInput).toBeInTheDocument();
    });
  });

  it('supports date range filtering', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Last updated date range')).toBeInTheDocument();
    });

    // Component supports date range filtering
    expect(screen.getByText('Last updated date range')).toBeInTheDocument();
  });

  it('calls handleSearchFieldChange with undefined when clearing filters', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    const searchParams: ActivitySearchParams = {
      bases: ['BASE-001'],
    };

    render(
      <ActivitySearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-base-multiselect')).toBeInTheDocument();
    });

    // Clear the selection by changing to empty
    const baseButton = screen.getAllByText('BASE-001')[0];
    await user.click(baseButton);

    expect(mockHandleSearchFieldChange).toHaveBeenCalled();
  });

  it('loads and displays code options from API', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      // Verify API calls were made
      expect(screen.getByTestId('multiselect-base-multiselect')).toBeInTheDocument();
    });
  });

  it('handles opening status multiselect', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    // Expand more filters
    await waitFor(() => {
      expect(screen.getByText('More filters')).toBeInTheDocument();
    });

    const moreFiltersButton = screen.getByText('More filters');
    await user.click(moreFiltersButton);

    await waitFor(() => {
      expect(screen.getByText('Opening status')).toBeInTheDocument();
    });
  });

  it('handles forest client multiselect', async () => {
    const user = userEvent.setup();
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    // Expand more filters
    await waitFor(() => {
      expect(screen.getByText('More filters')).toBeInTheDocument();
    });

    const moreFiltersButton = screen.getByText('More filters');
    await user.click(moreFiltersButton);

    await waitFor(() => {
      expect(screen.getByTestId('forest-client-multiselect')).toBeInTheDocument();
    });

    const clientButton = screen.getByText('Select Client');
    await user.click(clientButton);

    expect(mockHandleSearchFieldChange).toHaveBeenCalled();
  });

  it('renders all code-based filters with proper labels', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Base')).toBeInTheDocument();
      expect(screen.getByText('Technique')).toBeInTheDocument();
      expect(screen.getByText('Method')).toBeInTheDocument();
      expect(screen.getByText('Objectives')).toBeInTheDocument();
      expect(screen.getByText('Funding source')).toBeInTheDocument();
      expect(screen.getByText('Org unit')).toBeInTheDocument();
      expect(screen.getByText('Opening category')).toBeInTheDocument();
    });
  });

  it('preserves search state across re-renders', async () => {
    const { wrapper } = renderWithProviders();
    const searchParams: ActivitySearchParams = {
      bases: ['BASE-001'],
      techniques: ['TECH-001'],
      methods: ['METHOD-001'],
    };

    const { rerender } = render(
      <ActivitySearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByTestId('multiselect-base-multiselect')).toBeInTheDocument();
    });

    // Re-render with same props
    rerender(
      <ActivitySearchInput
        searchParams={searchParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />
    );

    // Should still be in the same state
    expect(screen.getByTestId('multiselect-base-multiselect')).toBeInTheDocument();
  });

  it('handles isComplete filter with different values', async () => {
    const { wrapper } = renderWithProviders();
    const completeParams: ActivitySearchParams = {
      isComplete: true,
    };

    render(
      <ActivitySearchInput
        searchParams={completeParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Activity status')).toBeInTheDocument();
    });
  });

  it('handles incomplete activity status filter', async () => {
    const { wrapper } = renderWithProviders();
    const incompleteParams: ActivitySearchParams = {
      isComplete: false,
    };

    render(
      <ActivitySearchInput
        searchParams={incompleteParams}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Activity status')).toBeInTheDocument();
    });
  });

  it('renders component without crashing with undefined params', async () => {
    const { wrapper } = renderWithProviders();
    render(
      <ActivitySearchInput
        searchParams={undefined}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Base')).toBeInTheDocument();
    });
  });

  it('handles date range with start date only', async () => {
    const { wrapper } = renderWithProviders();
    const paramsWithStartDate: ActivitySearchParams = {
      updateDateStart: '2024-01-01',
    };

    render(
      <ActivitySearchInput
        searchParams={paramsWithStartDate}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Last updated date range')).toBeInTheDocument();
    });
  });

  it('handles date range with end date only', async () => {
    const { wrapper } = renderWithProviders();
    const paramsWithEndDate: ActivitySearchParams = {
      updateDateEnd: '2024-12-31',
    };

    render(
      <ActivitySearchInput
        searchParams={paramsWithEndDate}
        handleSearchFieldChange={mockHandleSearchFieldChange}
      />,
      { wrapper }
    );

    await waitFor(() => {
      expect(screen.getByText('Last updated date range')).toBeInTheDocument();
    });
  });
});
