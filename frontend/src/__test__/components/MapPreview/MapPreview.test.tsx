import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import MapPreview from '../../../components/MapPreview';
import { openingStyle, activityStyle } from '../../../types/MapLayer';
import { FeatureCollection } from 'geojson';

const mockFitBounds = vi.fn();
const mockContains = vi.fn(() => false);
const mockMapBounds = {
  isValid: vi.fn(() => true),
  contains: mockContains,
};

vi.mock('react-leaflet', () => ({
  MapContainer: ({ children }: any) => <div data-testid="map-container">{children}</div>,
  TileLayer: () => <div data-testid="tile-layer" />,
  ZoomControl: () => <div data-testid="zoom-control" />,
  useMap: () => ({
    fitBounds: mockFitBounds,
    getBounds: () => mockMapBounds,
  }),
  GeoJSON: ({ data, style }: any) => (
    <div
      data-testid="geojson-layer"
      data-geojson={JSON.stringify(data)}
      data-style={JSON.stringify(style)}
    />
  ),
}));

const mockBounds = {
  isValid: vi.fn(() => true),
};

vi.mock('leaflet', () => ({
  geoJSON: vi.fn(() => ({
    getBounds: vi.fn(() => mockBounds),
    remove: vi.fn(),
  })),
}));

describe('MapPreview', () => {
  const sampleActivityGeojson: FeatureCollection = {
    type: 'FeatureCollection',
    features: [
      {
        type: 'Feature',
        id: 'activity-1',
        properties: {},
        geometry: {
          type: 'Polygon',
          coordinates: [
            [
              [-123.1, 49.2],
              [-123.1, 49.3],
              [-123.2, 49.3],
              [-123.2, 49.2],
              [-123.1, 49.2],
            ],
          ],
        },
      },
    ],
  };

  const sampleOpeningGeojson: FeatureCollection = {
    type: 'FeatureCollection',
    features: [
      {
        type: 'Feature',
        id: 'opening-1',
        properties: {},
        geometry: {
          type: 'Polygon',
          coordinates: [
            [
              [-123.0, 49.1],
              [-123.0, 49.4],
              [-123.3, 49.4],
              [-123.3, 49.1],
              [-123.0, 49.1],
            ],
          ],
        },
      },
    ],
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders nothing when geojson is null or undefined', () => {
    const { container: container1 } = render(<MapPreview geojson={null} />);
    expect(container1.firstChild).toBeNull();

    const { container: container2 } = render(<MapPreview geojson={undefined} />);
    expect(container2.firstChild).toBeNull();
  });

  it('renders opening geojson with openingStyle by default', () => {
    render(<MapPreview geojson={sampleOpeningGeojson} />);

    const geoJsonLayer = screen.getByTestId('geojson-layer');
    expect(JSON.parse(geoJsonLayer.getAttribute('data-style') || '{}')).toEqual(
      expect.objectContaining({
        fillColor: openingStyle.fillColor,
        color: openingStyle.color,
      })
    );
    expect(screen.queryByRole('button', { name: /hold to show opening spatial/i })).not.toBeInTheDocument();
  });

  it('renders activity geojson with activityStyle when mapType is activity', () => {
    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        mapType="activity"
      />
    );

    const geoJsonLayer = screen.getByTestId('geojson-layer');
    expect(JSON.parse(geoJsonLayer.getAttribute('data-style') || '{}')).toEqual(
      expect.objectContaining({
        fillColor: activityStyle.fillColor,
        color: activityStyle.color,
      })
    );
    expect(screen.queryByRole('button', { name: /hold to show opening spatial/i })).not.toBeInTheDocument();
  });

  it('renders hold button when mapType is activity and openingGeojson is provided', () => {
    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        openingGeojson={sampleOpeningGeojson}
        mapType="activity"
      />
    );

    const button = screen.getByRole('button', { name: /hold to show opening spatial/i });
    expect(button).toBeInTheDocument();
  });

  it('does not render hold button if openingGeojson has empty features', () => {
    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        openingGeojson={{ type: 'FeatureCollection', features: [] }}
        mapType="activity"
      />
    );

    expect(screen.queryByRole('button', { name: /hold to show opening spatial/i })).not.toBeInTheDocument();
  });

  it('switches to opening spatial on mouse down and restores activity on mouse up', () => {
    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        openingGeojson={sampleOpeningGeojson}
        mapType="activity"
      />
    );

    const button = screen.getByRole('button', { name: /hold to show opening spatial/i });
    let geoJsonLayer = screen.getByTestId('geojson-layer');

    // Initially activity style and geometry
    expect(JSON.parse(geoJsonLayer.getAttribute('data-style') || '{}')).toEqual(
      expect.objectContaining({ fillColor: activityStyle.fillColor })
    );
    expect(JSON.parse(geoJsonLayer.getAttribute('data-geojson') || '{}')).toEqual(sampleActivityGeojson);

    // Mouse down: hold to show opening
    fireEvent.mouseDown(button);
    expect(screen.getByRole('button', { name: /showing opening spatial/i })).toBeInTheDocument();

    geoJsonLayer = screen.getByTestId('geojson-layer');
    expect(JSON.parse(geoJsonLayer.getAttribute('data-style') || '{}')).toEqual(
      expect.objectContaining({ fillColor: openingStyle.fillColor })
    );
    expect(JSON.parse(geoJsonLayer.getAttribute('data-geojson') || '{}')).toEqual(sampleOpeningGeojson);

    // Mouse up: release back to activity
    fireEvent.mouseUp(button);
    expect(screen.getByRole('button', { name: /hold to show opening spatial/i })).toBeInTheDocument();

    geoJsonLayer = screen.getByTestId('geojson-layer');
    expect(JSON.parse(geoJsonLayer.getAttribute('data-style') || '{}')).toEqual(
      expect.objectContaining({ fillColor: activityStyle.fillColor })
    );
    expect(JSON.parse(geoJsonLayer.getAttribute('data-geojson') || '{}')).toEqual(sampleActivityGeojson);
  });

  it('restores activity on mouse leave', () => {
    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        openingGeojson={sampleOpeningGeojson}
        mapType="activity"
      />
    );

    const button = screen.getByRole('button', { name: /hold to show opening spatial/i });
    fireEvent.mouseDown(button);
    expect(screen.getByRole('button', { name: /showing opening spatial/i })).toBeInTheDocument();

    fireEvent.mouseLeave(button);
    expect(screen.getByRole('button', { name: /hold to show opening spatial/i })).toBeInTheDocument();
  });

  it('handles touch events', () => {
    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        openingGeojson={sampleOpeningGeojson}
        mapType="activity"
      />
    );

    const button = screen.getByRole('button', { name: /hold to show opening spatial/i });
    fireEvent.touchStart(button);
    expect(screen.getByRole('button', { name: /showing opening spatial/i })).toBeInTheDocument();

    fireEvent.touchEnd(button);
    expect(screen.getByRole('button', { name: /hold to show opening spatial/i })).toBeInTheDocument();

    fireEvent.touchStart(button);
    expect(screen.getByRole('button', { name: /showing opening spatial/i })).toBeInTheDocument();

    fireEvent.touchCancel(button);
    expect(screen.getByRole('button', { name: /hold to show opening spatial/i })).toBeInTheDocument();
  });

  it('handles keyboard space and enter press and release', () => {
    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        openingGeojson={sampleOpeningGeojson}
        mapType="activity"
      />
    );

    const button = screen.getByRole('button', { name: /hold to show opening spatial/i });
    fireEvent.keyDown(button, { key: ' ' });
    expect(screen.getByRole('button', { name: /showing opening spatial/i })).toBeInTheDocument();

    fireEvent.keyUp(button, { key: ' ' });
    expect(screen.getByRole('button', { name: /hold to show opening spatial/i })).toBeInTheDocument();

    fireEvent.keyDown(button, { key: 'Enter' });
    expect(screen.getByRole('button', { name: /showing opening spatial/i })).toBeInTheDocument();

    fireEvent.blur(button);
    expect(screen.getByRole('button', { name: /hold to show opening spatial/i })).toBeInTheDocument();
  });

  it('skips fitBounds when target bounds are already contained in current map bounds', () => {
    mockFitBounds.mockClear();
    mockContains.mockReturnValue(true);

    render(
      <MapPreview
        geojson={sampleActivityGeojson}
        openingGeojson={sampleOpeningGeojson}
        mapType="activity"
      />
    );

    expect(mockFitBounds).not.toHaveBeenCalled();
  });
});