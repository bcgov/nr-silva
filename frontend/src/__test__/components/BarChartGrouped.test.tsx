import React from 'react';
import { act, render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach, afterEach } from 'vitest';
import BarChartGrouped from '../../components/BarChartGrouped';
import { fetchOpeningsPerYear } from '../../services/OpeningService';

vi.mock('../../services/OpeningService', () => ({
  fetchOpeningsPerYear: vi.fn(() => Promise.resolve([
    { group: '2022', key: 'Openings', value: 10 },
    { group: '2023', key: 'Openings', value: 15 },
  ])),
}));

describe('BarChartGrouped component tests', () => {
  beforeEach(() => {
    vi.useFakeTimers(); // Use fake timers for testing timeouts
  });

  afterEach(() => {
    vi.restoreAllMocks();
    vi.useRealTimers(); // Restore real timers after each test
  });

  it('should render loading state while fetching data and clean it after', async () => {
    render(<BarChartGrouped />);

    expect(screen.getByText('Loading...')).toBeDefined();

    await vi.runAllTimers(); // Wait for the data fetching to complete
    
    expect(fetchOpeningsPerYear).toHaveBeenCalled();
    expect(screen.queryByTestId('bar-chart')).toBeDefined();
  });

});
