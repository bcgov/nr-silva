import React from 'react';
import { render } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import L from 'leaflet';
import { useMap } from 'react-leaflet';
import OpeningsMapFitBound from '@/components/OpeningsMapFitBound';
import { FeatureCollection } from 'geojson';

const mockFitBounds = vi.fn();
const mockSetView = vi.fn();

vi.mock('react-leaflet', () => ({
  useMap: vi.fn(),
}));

const mockBounds = {
  isValid: vi.fn(),
};

vi.mock('leaflet', () => {
  return {
    default: {
      geoJSON: vi.fn(() => ({})),
      featureGroup: vi.fn(() => ({
        getBounds: vi.fn(() => mockBounds),
      })),
    },
  };
});

describe('OpeningsMapFitBound', () => {
  const defaultLocation = [48.43737, -123.35883] as [number, number];
  const defaultZoom = 13;

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(useMap).mockReturnValue({
      fitBounds: mockFitBounds,
      setView: mockSetView,
    } as any);
  });

  it('sets view to default location and zoom when polygons array is empty', () => {
    render(
      <OpeningsMapFitBound
        polygons={[]}
        defaultLocation={defaultLocation}
        defaultZoom={defaultZoom}
      />
    );

    expect(mockSetView).toHaveBeenCalledWith(defaultLocation, defaultZoom, { animate: true });
    expect(mockFitBounds).not.toHaveBeenCalled();
  });

  it('fits bounds with padding when polygons have valid bounds', () => {
    mockBounds.isValid.mockReturnValue(true);

    const mockPolygons: FeatureCollection[] = [
      {
        type: 'FeatureCollection',
        features: [],
      },
    ];

    render(
      <OpeningsMapFitBound
        polygons={mockPolygons}
        defaultLocation={defaultLocation}
        defaultZoom={defaultZoom}
      />
    );

    expect(L.featureGroup).toHaveBeenCalled();
    expect(mockFitBounds).toHaveBeenCalledWith(mockBounds, { padding: [20, 20] });
    expect(mockSetView).not.toHaveBeenCalled();
  });

  it('sets view to default location when polygons bounds are invalid', () => {
    mockBounds.isValid.mockReturnValue(false);

    const mockPolygons: FeatureCollection[] = [
      {
        type: 'FeatureCollection',
        features: [],
      },
    ];

    render(
      <OpeningsMapFitBound
        polygons={mockPolygons}
        defaultLocation={defaultLocation}
        defaultZoom={defaultZoom}
      />
    );

    expect(mockSetView).toHaveBeenCalledWith(defaultLocation, defaultZoom, { animate: true });
    expect(mockFitBounds).not.toHaveBeenCalled();
  });
});
