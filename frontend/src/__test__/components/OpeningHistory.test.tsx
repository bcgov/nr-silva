import React from 'react';
import { render, act } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import OpeningHistory from '../../components/OpeningHistory';
import History from '../../types/History';
import { deleteOpeningFavorite } from '../../services/OpeningFavoriteService';

const mockHistories: History[] = [
  {
    id: 1,
    steps: [],
  },
  {
    id: 2,
    steps: [
      { step: 1, status: 'complete', description: 'Step 1', subtitle: 'Completed' },
      { step: 2, status: 'invalid', description: 'Step 2', subtitle: 'Invalid' },
      { step: 3, status: 'disabled', description: 'Step 3', subtitle: 'Disabled' },
    ],
  },
];

vi.mock('../../services/OpeningFavoriteService', () => ({
  deleteOpeningFavorite: vi.fn(),
}));

describe('OpeningHistory Component', () => {
  it('renders correctly with given histories', async () => {
    let getByText;
    await act(async () => {
      ({ getByText } = render(<OpeningHistory histories={mockHistories} /> ));
    });

    // Check for the presence of Opening Ids
    expect(getByText('Opening Id 1')).toBeInTheDocument();
    expect(getByText('Opening Id 2')).toBeInTheDocument();

    // Check for the presence of step descriptions
    expect(getByText('Step 1')).toBeInTheDocument();
    expect(getByText('Step 2')).toBeInTheDocument();
    expect(getByText('Step 3')).toBeInTheDocument();
  });

  it('renders correctly with empty histories', async () => {
    let container;
    await act(async () => {
      ({ container } = render(<OpeningHistory histories={[]} /> ));
    });

    // Select the div with the specific class
    const activityHistoryContainer = container.querySelector('.row.activity-history-container.gx-4');
    
    // Check if the container is empty
    expect(activityHistoryContainer).toBeInTheDocument(); // Ensure the element exists
    expect(activityHistoryContainer?.children.length).toBe(0); // Confirm it's empty by checking for no children
  });

  // check if when clicked on the FavoriteButton, the deleteOpeningFavorite function is called
  it('should call deleteOpeningFavorite when FavoriteButton is clicked', async () => {    
    let container;
    await act(async () => {
      ({ container } = render(<OpeningHistory histories={mockHistories} /> ));
    });

    const favoriteButton = container.querySelector('.favorite-icon button')
    favoriteButton && favoriteButton.click();
    expect(deleteOpeningFavorite).toHaveBeenCalled();
  });
});
