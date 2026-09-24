import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import SpeciesTooltipList from '@/components/OpeningDetails/OpeningStandardUnits/SpeciesTooltipList';
import { OpeningDetailsStockingSpeciesDto } from '@/services/OpenApi';

vi.mock('@carbon/react', async (importOriginal) => {
  const actual = await importOriginal<typeof import('@carbon/react')>();
  return {
    ...actual,
    DefinitionTooltip: ({ children, definition, ...props }: any) => (
      <span title={typeof definition === 'string' ? definition : undefined} {...props}>
        {children}
      </span>
    ),
  };
});

describe('SpeciesTooltipList', () => {
  const speciesList: OpeningDetailsStockingSpeciesDto[] = [
    {
      species: { code: 'FD', description: 'Douglas Fir' },
      layer: '1',
      minHeight: 1.5,
    },
    {
      species: { code: 'CW', description: 'Western Red Cedar' },
      layer: '1',
      minHeight: null as any,
    },
    {
      species: { code: '', description: '' },
      layer: '1',
      minHeight: null as any,
    },
    {
      species: { code: 'PLI', description: 'Lodgepole Pine' },
      layer: '2',
      minHeight: 2.0,
    },
  ];

  it('renders species matching the layerCode with minHeight suffix when present', () => {
    render(<SpeciesTooltipList speciesList={speciesList} layerCode="1" />);

    expect(screen.getByText(/FD/)).toBeInTheDocument();
    expect(screen.getByText(/1\.5 m/)).toBeInTheDocument();
    expect(screen.getByTitle(/Douglas Fir/)).toBeInTheDocument();
  });

  it('renders species without minHeight suffix when minHeight is absent', () => {
    render(<SpeciesTooltipList speciesList={speciesList} layerCode="1" />);

    expect(screen.getByText('CW')).toBeInTheDocument();
    expect(screen.getByTitle('CW - Western Red Cedar')).toBeInTheDocument();
  });

  it('renders placeholder when species code is empty', () => {
    render(<SpeciesTooltipList speciesList={speciesList} layerCode="1" />);

    expect(screen.getByText('--')).toBeInTheDocument();
  });

  it('filters out species for different layers', () => {
    render(<SpeciesTooltipList speciesList={speciesList} layerCode="2" />);

    expect(screen.getByText(/PLI/)).toBeInTheDocument();
    expect(screen.queryByText(/FD/)).toBeNull();
    expect(screen.queryByText('CW')).toBeNull();
  });
});
