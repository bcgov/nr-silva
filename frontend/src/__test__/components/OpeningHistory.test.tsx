import React from 'react';
import { render, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import OpeningHistory from '../../components/OpeningHistory';
import { NotificationProvider } from '../../contexts/NotificationProvider';
import { deleteOpeningFavorite, fetchOpeningTrends } from '../../services/OpeningFavoriteService';

vi.mock('../../services/OpeningFavoriteService', () => ({
  deleteOpeningFavorite: vi.fn(),
  fetchOpeningTrends: vi.fn(),
}));

describe('OpeningHistory Component', () => {
  it('renders correctly with given histories', async () => {
    (fetchOpeningTrends as vi.Mock).mockReturnValueOnce(Promise.resolve([1, 2]));
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningHistory /></NotificationProvider>));
    });

    // Check for the presence of Opening Ids
    expect(container.querySelector('div[data-id="1"').innerHTML).toContain('Opening Id 1');
    expect(container.querySelector('div[data-id="2"').innerHTML).toContain('Opening Id 2');    
  });

  it('renders correctly with empty histories', async () => {
    (fetchOpeningTrends as vi.Mock).mockReturnValueOnce(Promise.resolve([]));
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningHistory /></NotificationProvider>));
    });

    // Select the div with the specific class
    const activityHistoryContainer = container.querySelector('.row.activity-history-container.gx-4');
    
    // Check if the container is empty
    expect(activityHistoryContainer).toBeInTheDocument(); // Ensure the element exists
    expect(activityHistoryContainer?.children.length).toBe(0); // Confirm it's empty by checking for no children
  });

  // check if when clicked on the FavoriteButton, the deleteOpeningFavorite function is called
  it('should call deleteOpeningFavorite when FavoriteButton is clicked', async () => {    
    (fetchOpeningTrends as vi.Mock).mockReturnValueOnce(Promise.resolve([1, 2]));
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningHistory /></NotificationProvider>));
    });

    const favoriteButton = container.querySelector('.favorite-icon button')
    await act(async () => favoriteButton && favoriteButton.click());

    expect(deleteOpeningFavorite).toHaveBeenCalled();
  });

  it('should call deleteOpeningFavorite and handle error when FavoriteButton is clicked', async () => {
    (fetchOpeningTrends as vi.Mock).mockReturnValueOnce(Promise.resolve([1, 2]));
    (deleteOpeningFavorite as vi.Mock).mockRejectedValueOnce(new Error('Failed to delete favorite'));
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningHistory /></NotificationProvider>));
    });

    const favoriteButton = container.querySelector('.favorite-icon button')
    await act(async () => favoriteButton && favoriteButton.click());

    expect(deleteOpeningFavorite).toHaveBeenCalled();
  });
});
