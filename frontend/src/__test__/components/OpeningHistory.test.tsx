import React from 'react';
import { render, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import OpeningHistory from '../../components/OpeningHistory';
import { NotificationProvider } from '../../contexts/NotificationProvider';
import { deleteOpeningFavorite, fetchOpeningFavourites } from '../../services/OpeningFavouriteService';

vi.mock('../../services/OpeningFavouriteService', () => ({
  deleteOpeningFavorite: vi.fn(),
  fetchOpeningFavourites: vi.fn(),
}));

describe('OpeningHistory Component', () => {
  it('renders correctly with given histories', async () => {
    (fetchOpeningFavourites as vi.Mock).mockReturnValueOnce(Promise.resolve([1, 2]));
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningHistory /></NotificationProvider>));
    });

    // Check for the presence of Opening Ids
    expect(container.querySelector('div[data-id="1"').innerHTML).toContain('Opening Id 1');
    expect(container.querySelector('div[data-id="2"').innerHTML).toContain('Opening Id 2');    
  });

  it('renders correctly with empty histories', async () => {
    (fetchOpeningFavourites as vi.Mock).mockReturnValueOnce(Promise.resolve([]));
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningHistory /></NotificationProvider>));
    });

    // Select the div with the specific class
    const activityHistoryContainer = container.querySelector('.row.activity-history-container.gx-4');
    
    // Check if the container is showing the EmptySection component, with the title, description and icon
    expect(activityHistoryContainer.innerHTML).toContain('empty-section-container');
    expect(activityHistoryContainer.innerHTML).toContain('You don\'t have any favourites to show yet!');
    expect(activityHistoryContainer.innerHTML).toContain('You can favourite your openings by clicking on the heart icon inside opening details page');

  });

  // check if when clicked on the FavoriteButton, the deleteOpeningFavorite function is called
  it('should call deleteOpeningFavorite when FavoriteButton is clicked', async () => {    
    (fetchOpeningFavourites as vi.Mock).mockReturnValueOnce(Promise.resolve([1, 2]));
    let container;
    await act(async () => {
      ({ container } = render(<NotificationProvider><OpeningHistory /></NotificationProvider>));
    });

    const favoriteButton = container.querySelector('.favorite-icon button')
    await act(async () => favoriteButton && favoriteButton.click());

    expect(deleteOpeningFavorite).toHaveBeenCalled();
  });

  it('should call deleteOpeningFavorite and handle error when FavoriteButton is clicked', async () => {
    (fetchOpeningFavourites as vi.Mock).mockReturnValueOnce(Promise.resolve([1, 2]));
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
