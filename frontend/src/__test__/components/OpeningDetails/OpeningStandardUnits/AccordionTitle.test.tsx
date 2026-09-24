import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import AcoordionTitle from '@/components/OpeningDetails/OpeningStandardUnits/AccordionTitle';
import { OpeningDetailsStockingDto } from '@/services/OpenApi';

describe('AcoordionTitle', () => {
  const baseStandardUnit: OpeningDetailsStockingDto = {
    stocking: {
      ssuId: 1,
      stockingStandardUnit: 'SU-1',
      srid: 12345,
      standardsObjective: 'Timber production',
      netArea: 10,
      soilDisturbancePercent: 5,
      bec: {} as any,
      possibleFspIds: [],
      regenDelay: 2,
      freeGrowingEarly: 5,
      freeGrowingLate: 10,
    },
    comments: [],
    preferredSpecies: [],
    acceptableSpecies: [],
    layers: [],
  };

  it('renders stocking standard unit name, SSID and objective', () => {
    render(<AcoordionTitle standardUnit={baseStandardUnit} />);

    expect(screen.getByText('SU-1')).toBeInTheDocument();
    expect(screen.getByText('SSID: 12345')).toBeInTheDocument();
    expect(screen.getByText('Objective: Timber production')).toBeInTheDocument();
  });

  it('renders fallback placeholder and manual stocking requirement when fields are missing', () => {
    const suWithoutSrid: OpeningDetailsStockingDto = {
      ...baseStandardUnit,
      stocking: {
        ...baseStandardUnit.stocking,
        stockingStandardUnit: undefined as any,
        srid: undefined,
        standardsObjective: undefined,
      },
    };

    render(<AcoordionTitle standardUnit={suWithoutSrid} />);

    expect(screen.getByText('--')).toBeInTheDocument();
    expect(screen.getByText('Manual stocking requirement')).toBeInTheDocument();
    expect(screen.getByText('No objective')).toBeInTheDocument();
  });

  it('renders NR milestone tag when noRegenIndicated and noRegenDeclaredDate are set', () => {
    const suWithNR: OpeningDetailsStockingDto = {
      ...baseStandardUnit,
      stocking: {
        ...baseStandardUnit.stocking,
        milestones: {
          noRegenIndicated: true,
          noRegenDeclaredDate: '2023-01-01',
          extentDeclared: false,
          comments: [],
        },
      },
    };

    render(<AcoordionTitle standardUnit={suWithNR} />);
    expect(screen.getByText('No Regeneration')).toBeInTheDocument();
  });

  it('renders FG milestone tag when freeGrowingDeclaredDate is set', () => {
    const suWithFG: OpeningDetailsStockingDto = {
      ...baseStandardUnit,
      stocking: {
        ...baseStandardUnit.stocking,
        milestones: {
          freeGrowingDeclaredDate: '2023-05-15',
          extentDeclared: false,
          comments: [],
        },
      },
    };

    render(<AcoordionTitle standardUnit={suWithFG} />);
    expect(screen.getByText('Free Growing')).toBeInTheDocument();
  });

  it('renders RG milestone tag when regenDeclaredDate is set', () => {
    const suWithRG: OpeningDetailsStockingDto = {
      ...baseStandardUnit,
      stocking: {
        ...baseStandardUnit.stocking,
        milestones: {
          regenDeclaredDate: '2022-10-10',
          extentDeclared: false,
          comments: [],
        },
      },
    };

    render(<AcoordionTitle standardUnit={suWithRG} />);
    expect(screen.getByText('Regeneration')).toBeInTheDocument();
  });

  it('renders PH milestone tag when postHarvestDeclaredDate is set', () => {
    const suWithPH: OpeningDetailsStockingDto = {
      ...baseStandardUnit,
      stocking: {
        ...baseStandardUnit.stocking,
        milestones: {
          postHarvestDeclaredDate: '2021-08-01',
          extentDeclared: false,
          comments: [],
        },
      },
    };

    render(<AcoordionTitle standardUnit={suWithPH} />);
    expect(screen.getByText('Post Harvest')).toBeInTheDocument();
  });

  it('renders no milestone tag when status is UN (undeclared)', () => {
    const suWithUN: OpeningDetailsStockingDto = {
      ...baseStandardUnit,
      stocking: {
        ...baseStandardUnit.stocking,
        milestones: {
          extentDeclared: false,
          comments: [],
        },
      },
    };

    const { container } = render(<AcoordionTitle standardUnit={suWithUN} />);
    expect(container.querySelector('.milestone-tag')).toBeNull();
  });
});
