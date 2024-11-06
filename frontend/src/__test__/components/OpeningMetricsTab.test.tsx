import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';
import { render, act, waitFor, fireEvent, screen } from '@testing-library/react';
import OpeningMetricsTab from '../../components/OpeningMetricsTab';
import { fetchOpeningTrends } from '../../services/OpeningFavoriteService';

vi.mock('../../services/OpeningFavoriteService', () => ({
  fetchOpeningTrends: vi.fn(),
}));

describe('OpeningMetricsTab', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render the OpeningMetricsTab component with all sections', async () => {
    const mockTrends = [1, 2, 3];
    (fetchOpeningTrends as vi.Mock).mockResolvedValue(mockTrends);
    await act(async () => render(<OpeningMetricsTab />));

    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Manage and track silvicultural information about openings')).toBeInTheDocument();
    expect(screen.getByText('Openings submission trends')).toBeInTheDocument();
    expect(screen.getByText('Check quantity and evolution of openings')).toBeInTheDocument();
    expect(screen.getByText('Track Openings')).toBeInTheDocument();
    expect(screen.getByText('Follow your favourite openings')).toBeInTheDocument();
    expect(screen.getByText('Free growing milestone declarations')).toBeInTheDocument();
    expect(screen.getByText('Check opening standards unit for inspections purposes')).toBeInTheDocument();
    expect(screen.getByText('My recent actions')).toBeInTheDocument();
    expect(screen.getByText('Check your recent requests and files')).toBeInTheDocument();
  });

  it('should call fetchOpeningTrends and set submissionTrends state', async () => {
    const mockTrends = [1, 2, 3];
    (fetchOpeningTrends as vi.Mock).mockResolvedValue(mockTrends);

    await act(async () => {
      render(<OpeningMetricsTab />);
    });

    await waitFor(() => {
      expect(fetchOpeningTrends).toHaveBeenCalled();
      expect(screen.getByText('Opening Id 1')).toBeInTheDocument();
      expect(screen.getByText('Opening Id 2')).toBeInTheDocument();
      expect(screen.getByText('Opening Id 3')).toBeInTheDocument();
    });
  });

  it('should scroll to "Track Openings" section when scrollTo parameter is "trackOpenings"', async () => {
    const mockTrends = [1, 2, 3];
    (fetchOpeningTrends as vi.Mock).mockResolvedValue(mockTrends);
    const mockScrollIntoView = vi.fn();
    window.HTMLElement.prototype.scrollIntoView = mockScrollIntoView;

    const originalLocation = window.location;
    delete window.location;
    window.location = { search: '?scrollTo=trackOpenings' } as any;

    await act(async () => render(<OpeningMetricsTab />));

    expect(mockScrollIntoView).toHaveBeenCalledWith({ behavior: 'smooth' });

    window.location = originalLocation;
  });

  it('should not scroll to "Track Openings" section when scrollTo parameter is not "trackOpenings"', async () => {
    const mockTrends = [1, 2, 3];
    (fetchOpeningTrends as vi.Mock).mockResolvedValue(mockTrends);
    const mockScrollIntoView = vi.fn();
    window.HTMLElement.prototype.scrollIntoView = mockScrollIntoView;

    const originalLocation = window.location;
    delete window.location;
    window.location = { search: '' } as any;

    await act(async () => render(<OpeningMetricsTab />));

    expect(mockScrollIntoView).not.toHaveBeenCalled();

    window.location = originalLocation;
  });
});