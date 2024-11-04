import React from 'react';
import { render, act } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import OpeningHistory from '../../components/OpeningHistory';
import History from '../../types/History';

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
});
