//vite test for the ComingSoonModal component
import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import ComingSoonModal from '../../components/ComingSoonModal';

describe('ComingSoonModal', () => {
  it('renders the modal', () => {
    render(<ComingSoonModal openingDetails="123" setOpeningDetails={vi.fn()} />);
    expect(screen.getByText('Coming Soon')).toBeInTheDocument();
    expect(screen.getByText('An opening details page is in development.')).toBeInTheDocument();
  });

  it('renders the modal with the correct opening ID', () => {
    render(<ComingSoonModal openingDetails="1234" setOpeningDetails={vi.fn()} />);
    expect(screen.getByText('Opening ID: 1234')).toBeInTheDocument();
  });

  it('calls the setOpeningDetails function when the modal is closed', async() => {
    const setOpeningDetails = vi.fn();
    render(<ComingSoonModal openingDetails="123" setOpeningDetails={setOpeningDetails} />);
    const closeButton = screen.getByRole('button', { name: 'Close' });
    await closeButton.click();
    expect(setOpeningDetails).toHaveBeenCalled();
  });

});