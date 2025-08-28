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
    expect(screen.getByText(/Opening ID:\s*456/)).toBeInTheDocument();
    expect(screen.getByText(/Region:/)).toBeInTheDocument();
    expect(screen.getByText(/Cariboo/)).toBeInTheDocument();
    expect(screen.getByText(/District:/)).toBeInTheDocument();
    expect(screen.getByText(/Williams Lake/)).toBeInTheDocument();
    expect(screen.getByText(/Year created:/)).toBeInTheDocument();
    expect(screen.getByText(/2020/)).toBeInTheDocument();
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
    expect(screen.getByText(/Standard Unit:\s*SU-001/)).toBeInTheDocument();
    expect(screen.getByText(/Opening ID:/)).toBeInTheDocument();
    expect(screen.getByText(/SSID:/)).toBeInTheDocument();
    expect(screen.getByText(/Net area \(ha\):/)).toBeInTheDocument();
    expect(screen.getByText(/789/)).toBeInTheDocument();
    expect(screen.getByText(/SSID-123/)).toBeInTheDocument();
    expect(screen.getByText(/12\.34/)).toBeInTheDocument();
  });

  it('renders forest cover INV header and content', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={101}
        data={{
          mapKindType: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
          polygon: 'POLY-123',
          forestCoverId: 'FC-456',
          referenceYear: 2021,
          netArea: 10.5,
          polygonArea: 11.2,
        }}
      />
    );
    expect(screen.getByText(/Polygon ID:\s*POLY-123/)).toBeInTheDocument();
    expect(screen.getByText(/Forest Cover ID:/)).toBeInTheDocument();
    expect(screen.getByText(/FC-456/)).toBeInTheDocument();
    expect(screen.getByText(/Reference Year:/)).toBeInTheDocument();
    expect(screen.getByText(/2021/)).toBeInTheDocument();
    expect(screen.getByText(/Net Area \(ha\):/)).toBeInTheDocument();
    expect(screen.getByText(/10\.5/)).toBeInTheDocument();
    expect(screen.getByText(/Polygon Area \(ha\):/)).toBeInTheDocument();
    expect(screen.getByText(/11\.2/)).toBeInTheDocument();
  });

  it('renders forest cover RESERVE header and content', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={102}
        data={{
          mapKindType: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW',
          polygon: 'POLY-789',
          polygonArea: 8.8,
        }}
      />
    );
    expect(screen.getByText(/Polygon ID:\s*POLY-789/)).toBeInTheDocument();
    expect(screen.getByText(/Polygon Area \(ha\):/)).toBeInTheDocument();
    expect(screen.getByText(/8\.8/)).toBeInTheDocument();
  });

  it('renders forest cover SILV header and content', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={103}
        data={{
          mapKindType: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
          polygon: 'POLY-555',
          forestCoverId: 'FC-999',
          referenceYear: 2019,
          netArea: 7.7,
          polygonArea: 7.9,
        }}
      />
    );
    expect(screen.getByText(/Polygon ID:\s*POLY-555/)).toBeInTheDocument();
    expect(screen.getByText(/Forest Cover ID:/)).toBeInTheDocument();
    expect(screen.getByText(/FC-999/)).toBeInTheDocument();
    expect(screen.getByText(/Reference Year:/)).toBeInTheDocument();
    expect(screen.getByText(/2019/)).toBeInTheDocument();
    expect(screen.getByText(/Net Area \(ha\):/)).toBeInTheDocument();
    expect(screen.getByText(/7\.7/)).toBeInTheDocument();
    expect(screen.getByText(/Polygon Area \(ha\):/)).toBeInTheDocument();
    expect(screen.getByText(/7\.9/)).toBeInTheDocument();
  });

  it('renders cut block header and content', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={104}
        data={{
          mapKindType: 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW',
          cutBlockId: 'CB-123',
          forestFileId: 'FF-456',
          cuttingPermitId: 'CP-789',
          client: 'Test Client',
        }}
      />
    );
    expect(screen.getByText(/Cut Block:\s*CB-123/)).toBeInTheDocument();
    expect(screen.getByText(/Forest File:/)).toBeInTheDocument();
    expect(screen.getByText(/FF-456/)).toBeInTheDocument();
    expect(screen.getByText(/Cutting Permit:/)).toBeInTheDocument();
    expect(screen.getByText(/CP-789/)).toBeInTheDocument();
    expect(screen.getByText(/Client:/)).toBeInTheDocument();
    expect(screen.getByText(/Test Client/)).toBeInTheDocument();
  });

  it('renders activity treatment header and content', () => {
    render(
      <OpeningsMapEntryPopup
        openingId={105}
        data={{
          mapKindType: 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',
          silvBaseCode: 'DN',
          activityId: 'ACT-123',
          area: 5.5,
          endDate: '2024-01-01Z',
        }}
      />
    );
    expect(screen.getByText(/Disturbance \(DN\)/)).toBeInTheDocument();
    expect(screen.getByText(/Opening ID:/)).toBeInTheDocument();
    expect(screen.getByText(/105/)).toBeInTheDocument();
    expect(screen.getByText(/Activity ID:/)).toBeInTheDocument();
    expect(screen.getByText(/ACT-123/)).toBeInTheDocument();
    expect(screen.getByText(/Disturbance area \(ha\):/)).toBeInTheDocument();
    expect(screen.getByText(/5\.5/)).toBeInTheDocument();
    expect(screen.getByText(/End Date:/)).toBeInTheDocument();
    expect(screen.getByText(/Jan 01, 2024/)).toBeInTheDocument();
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
    expect(screen.getByText(/Opening ID:\s*321/)).toBeInTheDocument();
    expect(screen.getByText(/foo:\s*bar/)).toBeInTheDocument();
    expect(screen.getByText(/baz:\s*42/)).toBeInTheDocument();
  });

  it('does not show pin hint when isSelected is true', () => {
    render(<OpeningsMapEntryPopup openingId={123} isSelected={true} />);
    expect(screen.queryByText(/Click polygon to pin/)).not.toBeInTheDocument();
  });
});
