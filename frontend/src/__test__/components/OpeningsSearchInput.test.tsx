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

  it('handles other text input blur events (licenseNumber, cutBlockId, timberMark, etc.)', async () => {
    const handler = vi.fn();
    const { wrapper } = renderWithProviders();

    render(<OpeningsSearchInput searchParams={undefined} onSearchParamsChange={handler} />, { wrapper });

    const fileIdInput = screen.getByLabelText('File ID');
    fireEvent.change(fileIdInput, { target: { value: 'LIC100' } });
    fireEvent.blur(fileIdInput);
    expect(handler).toHaveBeenCalledWith('licenseNumber', 'LIC100');

    const licenseeOpeningId = screen.getByLabelText('Licensee opening ID');
    fireEvent.change(licenseeOpeningId, { target: { value: 'OP-1' } });
    fireEvent.blur(licenseeOpeningId);
    expect(handler).toHaveBeenCalledWith('licenseeOpeningId', 'OP-1');

    const cutBlock = screen.getByLabelText('Cut block');
    fireEvent.change(cutBlock, { target: { value: 'CB1' } });
    fireEvent.blur(cutBlock);
    expect(handler).toHaveBeenCalledWith('cutBlockId', 'CB1');

    const cuttingPermit = screen.getByLabelText('Cutting permit');
    fireEvent.change(cuttingPermit, { target: { value: 'CP1' } });
    fireEvent.blur(cuttingPermit);
    expect(handler).toHaveBeenCalledWith('cuttingPermitId', 'CP1');

    const timberMark = screen.getByLabelText('Timber mark');
    fireEvent.change(timberMark, { target: { value: 'TM1' } });
    fireEvent.blur(timberMark);
    expect(handler).toHaveBeenCalledWith('timberMark', 'TM1');
  });

  it('handles checkbox changes for isCreatedByUser and submittedToFrpa', () => {
    const handler = vi.fn();
    const { wrapper } = renderWithProviders();

    render(<OpeningsSearchInput searchParams={undefined} onSearchParamsChange={handler} />, { wrapper });

    const createdByMe = screen.getByLabelText('Created by me');
    fireEvent.click(createdByMe);
    expect(handler).toHaveBeenCalledWith('isCreatedByUser', true);

    const submittedToFrpa = screen.getByLabelText('FRPA Section 108');
    fireEvent.click(submittedToFrpa);
    expect(handler).toHaveBeenCalledWith('submittedToFrpa', true);
  });

  it('handles mapsheet grid, letter, quad, square and opening number', () => {
    const handler = vi.fn();
    const { wrapper } = renderWithProviders();

    const { container } = render(<OpeningsSearchInput searchParams={undefined} onSearchParamsChange={handler} />, { wrapper });

    const mapsheetSquare = container.querySelector('#mapsheet-square-input') as HTMLInputElement;
    fireEvent.change(mapsheetSquare, { target: { value: '092' } });
    fireEvent.blur(mapsheetSquare);
    expect(handler).toHaveBeenCalledWith('mapsheetSquare', '092');

    const openingNumber = container.querySelector('#opening-number-input') as HTMLInputElement;
    fireEvent.change(openingNumber, { target: { value: '0012' } });
    fireEvent.blur(openingNumber);
    expect(handler).toHaveBeenCalledWith('openingNumber', '0012');
  });

  it('handles clearing text inputs by sending undefined', () => {
    const handler = vi.fn();
    const { wrapper } = renderWithProviders();

    render(<OpeningsSearchInput searchParams={{ openingId: 123 }} onSearchParamsChange={handler} />, { wrapper });

    const input = screen.getByPlaceholderText('Enter opening ID');
    fireEvent.change(input, { target: { value: '' } });
    fireEvent.blur(input);

    expect(handler).toHaveBeenCalledWith('openingId', undefined);
  });
});
