import { describe, it, expect, vi, beforeEach } from 'vitest';
import React from 'react';
import { render, act, waitFor, screen } from '@testing-library/react';
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
    (fetchRecentOpenings as vi.Mock).mockResolvedValue([]);
    (fetchOpeningsPerYear as vi.Mock).mockResolvedValue([]);
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
    expect(screen.getByText('My recent actions')).toBeInTheDocument();
    expect(screen.getByText('Check your recent requests and files')).toBeInTheDocument();
  });

  it('should call fetchOpeningFavourites and set submissionTrends state', async () => {
    
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningMetricsTab /></NotificationProvider>));
    });

    await waitFor(() => {
      expect(fetchOpeningFavourites).toHaveBeenCalled();      
    });

    expect(container).not.toBeNull();
    
    expect(container.querySelector('.row.activity-history-container.gx-4')).toBeInTheDocument();
    
    expect(
      container
      .querySelector('.row.activity-history-container.gx-4')
      .innerHTML
    ).toContain('<span class="trend-title">Opening ID</span>&nbsp;1');
    
    expect(
      container
      .querySelector('.row.activity-history-container.gx-4')
      .innerHTML
    ).toContain('<span class="trend-title">Opening ID</span>&nbsp;2');
    
    expect(
      container
      .querySelector('.row.activity-history-container.gx-4')
      .innerHTML
    ).toContain('<span class="trend-title">Opening ID</span>&nbsp;3');

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