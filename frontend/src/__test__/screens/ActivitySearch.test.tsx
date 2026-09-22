import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import ActivitySearch from '@/screens/ActivitySearch';
import { renderWithProviders } from '../utils/testAuthProvider';
import * as actUtils from '@/components/ActivitySearchSection/utils';
import * as distUtils from '@/components/DisturbancesSearchSection/utils';

// Mock child sections
vi.mock('@/components/ActivitySearchSection', () => ({
  default: () => <div data-testid="activities-search-section">Activities Section</div>,
}));

vi.mock('@/components/DisturbancesSearchSection', () => ({
  default: () => <div data-testid="disturbances-search-section">Disturbances Section</div>,
}));

// Mock useNavigate
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe('ActivitySearch screen', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders activities section when type is activities', () => {
    const { wrapper } = renderWithProviders();
    render(<ActivitySearch type="activities" />, { wrapper });

    expect(screen.getByLabelText('Activities')).toBeInTheDocument();
    expect(screen.getByTestId('activities-search-section')).toBeInTheDocument();
    expect(screen.queryByTestId('disturbances-search-section')).not.toBeInTheDocument();
  });

  it('renders disturbances section when type is disturbances', () => {
    const { wrapper } = renderWithProviders();
    render(<ActivitySearch type="disturbances" />, { wrapper });

    expect(screen.getByTestId('disturbances-search-section')).toBeInTheDocument();
    expect(screen.queryByTestId('activities-search-section')).not.toBeInTheDocument();
  });

  it('redirects to home if type is not provided', () => {
    const { wrapper } = renderWithProviders();
    render(<ActivitySearch type={undefined as any} />, { wrapper });

    expect(mockNavigate).toHaveBeenCalledWith('/');
  });

  it('switches tabs directly when no active filters exist', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    vi.spyOn(actUtils, 'hasActivitySearchFilters').mockReturnValue(false);

    render(<ActivitySearch type="activities" />, { wrapper });

    const distRadio = screen.getByLabelText('Disturbances');
    await user.click(distRadio);

    expect(mockNavigate).toHaveBeenCalledWith(expect.stringContaining('/disturbances'));
    expect(document.querySelector('.default-confirm-switch-modal.is-visible')).toBeNull();
  });

  it('shows confirmation modal when switching tabs with active filters', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    vi.spyOn(actUtils, 'hasActivitySearchFilters').mockReturnValue(true);
    vi.spyOn(actUtils, 'readActivitySearchUrlParams').mockReturnValue({ activityId: '10' } as any);

    render(<ActivitySearch type="activities" />, { wrapper });

    const distRadio = screen.getByLabelText('Disturbances');
    await user.click(distRadio);

    expect(document.querySelector('.default-confirm-switch-modal.is-visible')).toBeInTheDocument();
  });

  it('proceeds with tab switch when confirm modal is accepted', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    vi.spyOn(actUtils, 'hasActivitySearchFilters').mockReturnValue(true);

    render(<ActivitySearch type="activities" />, { wrapper });

    const distRadio = screen.getByLabelText('Disturbances');
    await user.click(distRadio);

    const proceedBtn = screen.getByRole('button', { name: /Proceed/i });
    await user.click(proceedBtn);

    expect(mockNavigate).toHaveBeenCalledWith(expect.stringContaining('/disturbances'));
  });

  it('closes confirmation modal when cancel is clicked', async () => {
    const { wrapper } = renderWithProviders();
    const user = userEvent.setup();
    vi.spyOn(actUtils, 'hasActivitySearchFilters').mockReturnValue(true);

    render(<ActivitySearch type="activities" />, { wrapper });

    const distRadio = screen.getByLabelText('Disturbances');
    await user.click(distRadio);

    const cancelBtn = screen.getByRole('button', { name: /Cancel/i });
    await user.click(cancelBtn);

    await waitFor(() => {
      const modal = document.querySelector('.default-confirm-switch-modal.is-visible');
      expect(modal).toBeNull();
    });
  });
});
