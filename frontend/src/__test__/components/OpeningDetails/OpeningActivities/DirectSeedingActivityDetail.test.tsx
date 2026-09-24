import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import DirectSeedingActivityDetail from '@/components/OpeningDetails/OpeningActivities/ActivityDetail/DirectSeedingActivityDetail';

describe('DirectSeedingActivityDetail', () => {
  it('renders loading skeleton when isLoading is true', () => {
    const { container } = render(
      <DirectSeedingActivityDetail activityDetail={undefined} isLoading={true} />
    );

    expect(container.querySelector('.cds--skeleton')).toBeInTheDocument();
  });

  it('renders empty section when not loading and no species are present', () => {
    render(
      <DirectSeedingActivityDetail
        activityDetail={{ totalPlanting: 100, species: [] } as any}
        isLoading={false}
      />
    );

    expect(screen.getByText('Direct seeding specifications')).toBeInTheDocument();
    expect(screen.getByText('Total planting: 100')).toBeInTheDocument();
    expect(screen.getByText('Total species: 0')).toBeInTheDocument();
    expect(screen.getByText('There are no species to show yet')).toBeInTheDocument();
  });

  it('renders loaded species table with species, cbst, and numeric fields', () => {
    const mockDetail = {
      totalPlanting: 500,
      species: [
        {
          species: { code: 'PLI', description: 'Lodgepole Pine' },
          plantedNumber: 250,
          numberBeyondTransferLimit: 50,
          cbst: true,
          lot: 'LOT-123',
        },
        {
          species: { code: 'FD', description: 'Douglas Fir' },
          plantedNumber: null,
          numberBeyondTransferLimit: null,
          cbst: false,
          lot: null,
        },
      ],
    };

    render(
      <DirectSeedingActivityDetail
        activityDetail={mockDetail as any}
        isLoading={false}
      />
    );

    expect(screen.getByText('Direct seeding specifications')).toBeInTheDocument();
    expect(screen.getByText('Total planting: 500')).toBeInTheDocument();
    expect(screen.getByText('Total species: 2')).toBeInTheDocument();

    // Table content
    expect(screen.getByText('PLI - Lodgepole Pine')).toBeInTheDocument();
    expect(screen.getByText('250')).toBeInTheDocument();
    expect(screen.getByText('50')).toBeInTheDocument();
    expect(screen.getByText('Yes')).toBeInTheDocument();
    expect(screen.getByText('LOT-123')).toBeInTheDocument();

    expect(screen.getByText('FD - Douglas Fir')).toBeInTheDocument();
    expect(screen.getAllByText('--').length).toBeGreaterThanOrEqual(1);
  });
});
