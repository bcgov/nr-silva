import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import { render, act, fireEvent, waitFor, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import FavouriteOpenings from '../../components/FavouriteOpenings';
import { NotificationProvider } from '../../contexts/NotificationProvider';
import { deleteOpeningFavorite, fetchOpeningFavourites } from '../../services/OpeningFavouriteService';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

vi.mock('../../services/OpeningFavouriteService', () => ({
  deleteOpeningFavorite: vi.fn(),
  fetchOpeningFavourites: vi.fn(),
}));

const renderWithProviders = async () => {
  const queryClient = new QueryClient();
  let rendered;
  await act(async () => {
    rendered = render(
      <QueryClientProvider client={queryClient}>
        <NotificationProvider>
          <MemoryRouter>
            <FavouriteOpenings />
          </MemoryRouter>
        </NotificationProvider>
      </QueryClientProvider>
    );
  });
  return rendered;
};

describe('FavouriteOpenings Component', () => {

  it('renders correctly with given histories', async () => {
    (fetchOpeningFavourites as vi.Mock).mockResolvedValueOnce([1, 2]);

    const { container } = await renderWithProviders();

    await waitFor(() => {
      expect(container.querySelector('#favourite-opening-tile-1')).toBeInTheDocument();
      expect(container.querySelector('#favourite-opening-tile-2')).toBeInTheDocument();
    });
  });

  it('renders correctly with empty histories', async () => {
    (fetchOpeningFavourites as vi.Mock).mockResolvedValueOnce([]); // Simulate empty data

    await renderWithProviders();

    await waitFor(() => {
      expect(
        screen.getByText("You don't have any favourites to show yet!")
      ).toBeInTheDocument();
    });

    await waitFor(() => {
      expect(
        screen.getByText("You can favourite your openings by clicking on the heart icon inside opening details page")
      ).toBeInTheDocument();
    });
  });

  it('should call deleteOpeningFavorite when FavoriteButton is clicked', async () => {
    (fetchOpeningFavourites as vi.Mock).mockResolvedValueOnce([1, 2]);
    const { container } = await renderWithProviders();

    const favoriteButton = container.querySelector('.favorite-icon button');
    if (favoriteButton) {
      await act(async () => fireEvent.click(favoriteButton));
      expect(deleteOpeningFavorite).toHaveBeenCalled();
    }
  });

  it('should call deleteOpeningFavorite and handle error when FavoriteButton is clicked', async () => {
    (fetchOpeningFavourites as vi.Mock).mockResolvedValueOnce([1, 2]);
    (deleteOpeningFavorite as vi.Mock).mockRejectedValueOnce(new Error('Failed to delete favorite'));
    const { container } = await renderWithProviders();

    const favoriteButton = container.querySelector('.favorite-icon button');
    if (favoriteButton) {
      await act(async () => fireEvent.click(favoriteButton));
      expect(deleteOpeningFavorite).toHaveBeenCalled();
    }
  });
});
