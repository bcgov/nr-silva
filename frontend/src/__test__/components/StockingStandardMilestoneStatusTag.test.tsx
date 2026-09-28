import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import StockingStandardMilestoneStatusTag from '@/components/Tags/StockingStandardMilestoneStatusTag';
import '@testing-library/jest-dom';

describe('StockingStandardMilestoneStatusTag', () => {
  it('renders known milestone status codes correctly', () => {
    const { rerender } = render(
      <StockingStandardMilestoneStatusTag status="FG" />
    );
    expect(screen.getByText('Free Growing')).toBeInTheDocument();

    rerender(<StockingStandardMilestoneStatusTag status="RG" />);
    expect(screen.getByText('Regeneration')).toBeInTheDocument();

    rerender(<StockingStandardMilestoneStatusTag status="PH" />);
    expect(screen.getByText('Post Harvest')).toBeInTheDocument();

    rerender(<StockingStandardMilestoneStatusTag status="NR" />);
    expect(screen.getByText('No Regeneration')).toBeInTheDocument();

    rerender(<StockingStandardMilestoneStatusTag status="EX" />);
    expect(
      screen.getByText('Declared to the extent practicable')
    ).toBeInTheDocument();

    rerender(<StockingStandardMilestoneStatusTag status="UN" />);
    expect(screen.getByText('Unknown')).toBeInTheDocument();
  });

  it('renders Unknown Status for unknown status codes', () => {
    render(<StockingStandardMilestoneStatusTag status="INVALID_CODE" />);
    expect(screen.getByText('Unknown Status')).toBeInTheDocument();
  });
});
