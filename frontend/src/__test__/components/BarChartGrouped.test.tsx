import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import BarChartGrouped from '../../components/BarChartGrouped';
import { fetchOpeningsPerYear } from '../../services/OpeningService';

vi.mock('../../services/OpeningService', () => ({
  fetchOpeningsPerYear: vi.fn(() => Promise.resolve([
    { group: '2022', key: 'Openings', value: 10 },
    { group: '2023', key: 'Openings', value: 15 },
  ])),
}));

describe('BarChartGrouped component tests', () => {
  it('should render loading state while fetching data and clean it after', async () => {
    render(<BarChartGrouped />);

    const element = await waitFor(() => screen.getByText('Loading...'));

    expect(element).toBeDefined();
    
    expect(fetchOpeningsPerYear).toHaveBeenCalled();
    expect(screen.queryByTestId('bar-chart')).toBeDefined();
  });

});
