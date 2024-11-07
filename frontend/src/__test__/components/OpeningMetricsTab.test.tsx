import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';
import { render, act, waitFor, fireEvent, screen } from '@testing-library/react';
import OpeningMetricsTab from '../../components/OpeningMetricsTab';
import { NotificationProvider } from '../../contexts/NotificationProvider';
import { fetchOpeningFavourites } from '../../services/OpeningFavouriteService';
import { fetchFreeGrowingMilestones, fetchOpeningsPerYear, fetchRecentOpenings } from '../../services/OpeningService';

vi.mock('../../services/OpeningFavouriteService', () => ({
  fetchOpeningFavourites: vi.fn(),
}));
vi.mock('../../services/OpeningService', async () => {
  const actual = await vi.importActual('../../services/OpeningService');
  return {
    ...actual,
    fetchRecentOpenings: vi.fn(),
    fetchOpeningsPerYear: vi.fn(),
    fetchFreeGrowingMilestones: vi.fn(),    
  };
});

describe('OpeningMetricsTab', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    (fetchRecentOpenings as vi.Mock).mockResolvedValue([{
      id: '123',
      openingId: '123',
      fileId: '1',
      cuttingPermit: '1',
      timberMark: '1',
      cutBlock: '1',
      grossAreaHa: 1,
      statusDesc: 'Approved',
      categoryDesc: 'Another:Another',
      disturbanceStart: '1',
      entryTimestamp: '1',
      updateTimestamp: '1',
    }]);
    (fetchOpeningsPerYear as vi.Mock).mockResolvedValue([
      { group: '2022', key: 'Openings', value: 10 },
      { group: '2023', key: 'Openings', value: 15 },
    ]);
    (fetchFreeGrowingMilestones as vi.Mock).mockResolvedValue([{ group: '1-5', value: 11 }]);
    (fetchOpeningFavourites as vi.Mock).mockResolvedValue([1, 2, 3]);
    
  });

  it('should render the OpeningMetricsTab component with all sections', async () => {
    
    await act(async () => render(<NotificationProvider><OpeningMetricsTab /></NotificationProvider>));

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

  it('should call fetchOpeningFavourites and set submissionTrends state', async () => {
    

    await act(async () => {
      render(<NotificationProvider><OpeningMetricsTab /></NotificationProvider>);
    });

    await waitFor(() => {
      expect(fetchOpeningFavourites).toHaveBeenCalled();
      expect(screen.getByText('Opening Id 1')).toBeInTheDocument();
      expect(screen.getByText('Opening Id 2')).toBeInTheDocument();
      expect(screen.getByText('Opening Id 3')).toBeInTheDocument();
    });
  });

  it('should scroll to "Track Openings" section when scrollTo parameter is "trackOpenings"', async () => {
    
    const mockScrollIntoView = vi.fn();
    window.HTMLElement.prototype.scrollIntoView = mockScrollIntoView;

    const originalLocation = window.location;
    delete window.location;
    window.location = { search: '?scrollTo=trackOpenings' } as any;

    await act(async () => render(<NotificationProvider><OpeningMetricsTab /></NotificationProvider>));

    expect(mockScrollIntoView).toHaveBeenCalledWith({ behavior: 'smooth' });

    window.location = originalLocation;
  });

  it('should not scroll to "Track Openings" section when scrollTo parameter is not "trackOpenings"', async () => {
    
    const mockScrollIntoView = vi.fn();
    window.HTMLElement.prototype.scrollIntoView = mockScrollIntoView;

    const originalLocation = window.location;
    delete window.location;
    window.location = { search: '' } as any;

    await act(async () => render(<NotificationProvider><OpeningMetricsTab /></NotificationProvider>));

    expect(mockScrollIntoView).not.toHaveBeenCalled();

    window.location = originalLocation;
  });
});