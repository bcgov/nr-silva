import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import OpeningsMapEntryPopup from '../../components/OpeningsMapEntryPopup';

describe('OpeningsMapEntryPopup', () => {
  it('renders default header and pin hint when no data', () => {
    render(<OpeningsMapEntryPopup openingId={123} />);
    expect(screen.getByText('Click polygon to pin')).toBeInTheDocument();
  });

  it('renders WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW header and content', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={456}
        data={{
          mapKindType: 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
          region: 'Cariboo',
          district: 'Williams Lake',
          yearCreated: 2020,
        }}
      />
    );
    expect(screen.getByText('Opening ID: 456')).toBeInTheDocument();
    expect(screen.getByText('Region: Cariboo')).toBeInTheDocument();
    expect(screen.getByText('District: Williams Lake')).toBeInTheDocument();
    expect(screen.getByText('Year created: 2020')).toBeInTheDocument();
  });

  it('renders WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW header and content', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={789}
        data={{
          mapKindType: 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW',
          standardUnitId: 'SU-001',
          ssid: 'SSID-123',
          netArea: 12.34,
        }}
      />
    );
    expect(screen.getByText('Standard Unit: SU-001')).toBeInTheDocument();
    expect(screen.getByText('Opening ID:')).toBeInTheDocument();
    expect(screen.getByText('SSID:')).toBeInTheDocument();
    expect(screen.getByText('Net area (ha):')).toBeInTheDocument();
    expect(screen.getByText('789')).toBeInTheDocument();
    expect(screen.getByText('SSID-123')).toBeInTheDocument();
    expect(screen.getByText('12.34')).toBeInTheDocument();
  });

  it('renders default header and generic content for unknown mapKindType', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={321}
        data={{
          mapKindType: 'UNKNOWN_TYPE',
          foo: 'bar',
          baz: 42,
        }}
      />
    );
    expect(screen.getByText('Opening ID: 321')).toBeInTheDocument();
    expect(screen.getByText('foo: bar')).toBeInTheDocument();
    expect(screen.getByText('baz: 42')).toBeInTheDocument();
  });

  it('does not show pin hint when isSelected is true', () => {
    render(<OpeningsMapEntryPopup openingId={123} isSelected={true} />);
    expect(screen.queryByText('Click polygon to pin')).not.toBeInTheDocument();
  });
});
