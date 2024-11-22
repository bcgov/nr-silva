import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import { vi } from 'vitest';
import OpeningsMapEntryPopup from '../../components/OpeningsMapEntryPopup';
import { isOpeningFavourite, setOpeningFavorite, deleteOpeningFavorite } from '../../services/OpeningFavouriteService';
import { useNotification } from '../../contexts/NotificationProvider';

// Mock services and context
vi.mock('../../services/OpeningFavouriteService', () => ({
  isOpeningFavourite: vi.fn(),
  setOpeningFavorite: vi.fn(),
  deleteOpeningFavorite: vi.fn(),
}));

vi.mock('../../contexts/NotificationProvider', () => ({
  useNotification: vi.fn(),
}));

describe('OpeningsMapEntryPopup', () => {
  const mockDisplayNotification = vi.fn();

  beforeEach(() => {
    vi.resetAllMocks();
    (useNotification as vi.Mock).mockReturnValue({ displayNotification: mockDisplayNotification });
  });

  it('renders correctly with given openingId', () => {
    render(<OpeningsMapEntryPopup openingId={123} />);
    expect(screen.getByText('Opening ID')).toBeInTheDocument();
    expect(screen.getByText('123')).toBeInTheDocument();
  });

  it('fetches the favorite status on mount', async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(true);
    await act(async () => render(<OpeningsMapEntryPopup openingId={123} />));
    await waitFor(() => { expect(isOpeningFavourite).toHaveBeenCalledWith(123); });    
    expect(screen.getByRole('button', { name: /favorite/i })).toHaveAttribute('aria-pressed', 'true');
  });

  it('handles setting the opening as favorite', async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(false);
    (setOpeningFavorite as vi.Mock).mockResolvedValue();

    await act(async () => render(<OpeningsMapEntryPopup openingId={123} />));

    const favoriteButton = screen.getByRole('button', { name: /favorite/i });
    await act(async () => fireEvent.click(favoriteButton));

    await waitFor(() => {
      expect(setOpeningFavorite).toHaveBeenCalledWith(123);
    });

    expect(mockDisplayNotification).toHaveBeenCalledWith(
      expect.objectContaining({
        title: 'Opening Id 123 favourited',
        type: 'success',
      }),
    );
  });

  it('handles removing the opening from favorites', async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(true);
    (deleteOpeningFavorite as vi.Mock).mockResolvedValue();

    await act(async () => render(<OpeningsMapEntryPopup openingId={123} />));

    const favoriteButton = screen.getByRole('button', { name: /favorite/i });
    await act(async () => fireEvent.click(favoriteButton));

    await waitFor(() => {
      expect(deleteOpeningFavorite).toHaveBeenCalledWith(123);
    });

    expect(mockDisplayNotification).toHaveBeenCalledWith(
      expect.objectContaining({
        title: 'Opening Id 123 unfavourited',
        type: 'success',
      }),
    );
  });

  it('displays an error notification on failure', async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(false);
    (setOpeningFavorite as vi.Mock).mockRejectedValue(new Error('Failed to favorite'));

    await act(async () => render(<OpeningsMapEntryPopup openingId={123} />));

    const favoriteButton = screen.getByRole('button', { name: /favorite/i });
    await act(async () => fireEvent.click(favoriteButton));

    await waitFor(() => {
      expect(mockDisplayNotification).toHaveBeenCalledWith(
        expect.objectContaining({
          title: 'Error',
          subTitle: 'Failed to update favorite status for 123',
          type: 'error',
        }),
      );
    });
  });
});
