import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import BarChartTooltip from '../../components/BarChartTooltip';
import { OpeningPerYearChart } from '../../types/OpeningPerYearChart';

vi.mock('../../services/search/openings', () => ({
  status: [
    { value: 'open', text: 'Open' },
    { value: 'closed', text: 'Closed' },
  ],
}));

describe('BarChartTooltip', () => {
  it('renders the chart tooltip correctly', () => {
    const mockDatum: OpeningPerYearChart = {
      value: '2023',
      group: 'Engineering',
      key: 'Devs',
      statusCount: {
        open: 10,
        closed: 5,
      },
    };

    render(<BarChartTooltip datum={mockDatum} />);

    // Header with value and group
    expect(screen.getByText('2023 Engineering')).toBeInTheDocument();

    // Status counts and descriptions
    expect(screen.getByText('10 open')).toBeInTheDocument();
    expect(screen.getByText('5 closed')).toBeInTheDocument();
  });

  it('falls back to the status code if description is unavailable', () => {
    const mockDatum: OpeningPerYearChart = {
      value: '2023',
      group: 'HR',
      key: 'Recruiters',
      statusCount: {
        unknown: 3,
      },
    };

    render(<BarChartTooltip datum={mockDatum} />);

    expect(screen.getByText('3 unknown')).toBeInTheDocument();
  });
});