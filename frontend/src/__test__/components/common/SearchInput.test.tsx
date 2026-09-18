import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import {
  BgcSearchInputs,
  FileIdSearchInput,
  MoreFiltersToggle,
  OpeningCategoriesMultiSelect,
  OpeningStatusMultiSelect,
  OrgUnitMultiSelect,
  SearchDateRange,
} from '@/components/common/SearchInput';
import { renderWithProviders } from '../../utils/testAuthProvider';

vi.mock('@/services/API', () => ({
  default: {
    CodesEndpointService: {
      getOpeningCategories: vi.fn().mockResolvedValue([
        { code: 'CAT1', description: 'Category One' },
      ]),
      getOpeningOrgUnits: vi.fn().mockResolvedValue([
        { code: 'ORG1', description: 'Org One' },
      ]),
    },
  },
}));

describe('Common SearchInput Components', () => {
  describe('SearchDateRange', () => {
    it('renders label and DatePickers with default and custom props', () => {
      render(
        <SearchDateRange
          label="Custom date range"
          labelHtmlFor="custom-id"
          startDate="2024-01-01"
          endDate="2024-01-10"
        />
      );

      expect(screen.getByText('Custom date range')).toBeInTheDocument();
      expect(screen.getByLabelText('Start Date')).toBeInTheDocument();
      expect(screen.getByLabelText('End Date')).toBeInTheDocument();
    });

    it('renders children slot when provided', () => {
      render(
        <SearchDateRange>
          <div data-testid="child-slot">Child Slot Content</div>
        </SearchDateRange>
      );

      expect(screen.getByTestId('child-slot')).toBeInTheDocument();
    });
  });

  describe('BgcSearchInputs', () => {
    it('renders all 6 BGC inputs with labels and handles field changes', async () => {
      const user = userEvent.setup();
      const mockChange = vi.fn();

      render(
        <BgcSearchInputs
          values={{ bgcZone: 'SBS' }}
          onFieldChange={mockChange}
        />
      );

      expect(screen.getByLabelText('BGC zone')).toBeInTheDocument();
      expect(screen.getByLabelText('BGC sub zone')).toBeInTheDocument();
      expect(screen.getByLabelText('Variant')).toBeInTheDocument();
      expect(screen.getByLabelText('Phase')).toBeInTheDocument();
      expect(screen.getByLabelText('Site series')).toBeInTheDocument();
      expect(screen.getByLabelText('Site phase')).toBeInTheDocument();

      const zoneInput = screen.getByLabelText('BGC zone');
      await user.clear(zoneInput);
      await user.type(zoneInput, 'IDF');
      zoneInput.blur();

      expect(mockChange).toHaveBeenCalledWith('bgcZone', 'IDF');
    });

    it('renders wrapped in row when wrapInRow is true', () => {
      const { container } = render(
        <BgcSearchInputs
          onFieldChange={vi.fn()}
          wrapInRow
        />
      );

      expect(container.querySelector('.cds--subgrid, .cds--grid, .default-search-input-grid')).toBeInTheDocument();
    });
  });

  describe('FileIdSearchInput', () => {
    it('renders file ID input and handles onBlur changes', async () => {
      const user = userEvent.setup();
      const mockChange = vi.fn();

      render(
        <FileIdSearchInput
          value="FILE1"
          onChange={mockChange}
        />
      );

      const input = screen.getByLabelText('File ID');
      expect(input).toBeInTheDocument();
      expect(input).toHaveValue('FILE1');

      await user.clear(input);
      await user.type(input, 'NEWFILE');
      input.blur();

      expect(mockChange).toHaveBeenCalledWith('NEWFILE');
    });
  });

  describe('MoreFiltersToggle', () => {
    it('renders toggle button and calls onToggle', async () => {
      const user = userEvent.setup();
      const mockToggle = vi.fn();

      const { rerender } = render(
        <MoreFiltersToggle isExpanded={false} onToggle={mockToggle} />
      );

      const button = screen.getByRole('button', { name: /More filters/i });
      expect(button).toBeInTheDocument();

      await user.click(button);
      expect(mockToggle).toHaveBeenCalledWith(true);

      rerender(<MoreFiltersToggle isExpanded={true} onToggle={mockToggle} />);
      expect(screen.getByRole('button', { name: /Fewer filters/i })).toBeInTheDocument();
    });
  });

  describe('OpeningCategoriesMultiSelect', () => {
    it('renders categories multiselect and queries data', async () => {
      const { wrapper } = renderWithProviders();
      render(
        <OpeningCategoriesMultiSelect
          selectedCategories={['CAT1']}
          onChange={vi.fn()}
        />,
        { wrapper }
      );

      await waitFor(() => {
        expect(screen.getByText('Opening category')).toBeInTheDocument();
      });
    });
  });

  describe('OpeningStatusMultiSelect', () => {
    it('renders opening status multiselect', () => {
      render(
        <OpeningStatusMultiSelect
          selectedStatuses={['APP']}
          onChange={vi.fn()}
        />
      );

      expect(screen.getByText('Opening status')).toBeInTheDocument();
    });
  });

  describe('OrgUnitMultiSelect', () => {
    it('renders org unit multiselect with default and district query', async () => {
      const { wrapper } = renderWithProviders();
      render(
        <OrgUnitMultiSelect
          type="district"
          selectedOrgUnits={['ORG1']}
          onChange={vi.fn()}
        />,
        { wrapper }
      );

      await waitFor(() => {
        expect(screen.getByText('Org unit')).toBeInTheDocument();
      });
    });
  });
});
