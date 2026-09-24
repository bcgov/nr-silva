import React from "react";
import { render, screen, fireEvent, act } from "@testing-library/react";
import { beforeEach, afterEach, describe, expect, it, vi } from "vitest";
import OpeningsMapEntry from "@/components/OpeningsMapEntry";
import { FeatureCollection } from "geojson";

const mockFlyToBounds = vi.fn();
const mockEachLayer = vi.fn();
const mockGetZoom = vi.fn(() => 13);
const mockContains = vi.fn(() => true);
const mockGetBounds = vi.fn(() => ({
  contains: mockContains,
}));

let capturedMapEvents: Record<string, () => void> = {};

vi.mock("react-leaflet", () => ({
  useMapEvents: vi.fn((handlers: any) => {
    capturedMapEvents = handlers;
    return {
      getZoom: mockGetZoom,
      getBounds: mockGetBounds,
      flyToBounds: mockFlyToBounds,
      eachLayer: mockEachLayer,
    };
  }),
  GeoJSON: ({ data, style, onEachFeature, "data-testid": testId }: any) => {
    const layer = {
      bringToFront: vi.fn(),
      on: vi.fn((events: any) => {
        (layer as any)._events = events;
      }),
    };
    if (onEachFeature) {
      onEachFeature(data, layer);
    }
    const computedStyle = typeof style === "function" ? style() : style;
    return (
      <div
        data-testid={testId || "geojson-layer"}
        data-feature-id={data?.id}
        data-style={JSON.stringify(computedStyle)}
        onClick={() => (layer as any)._events?.click?.()}
        onMouseOver={() => (layer as any)._events?.mouseover?.()}
        onMouseOut={() => (layer as any)._events?.mouseout?.()}
      />
    );
  },
  Marker: ({ position, icon, eventHandlers, "data-testid": testId }: any) => (
    <div
      data-testid={testId || "marker"}
      data-position={JSON.stringify(position)}
      data-icon={icon?.options?.iconUrl}
      onClick={() => eventHandlers?.click?.()}
      onMouseOver={() => eventHandlers?.mouseover?.()}
      onMouseOut={() => eventHandlers?.mouseout?.()}
    />
  ),
}));

const mockGeoJsonBounds = {
  isValid: vi.fn(() => true),
};

vi.mock("leaflet", () => {
  return {
    default: {
      Icon: class MockIcon {
        options: any;
        constructor(options: any) {
          this.options = options;
        }
      },
      geoJSON: vi.fn(() => ({
        getBounds: vi.fn(() => mockGeoJsonBounds),
      })),
    },
  };
});

describe("OpeningsMapEntry", () => {
  const setHoveredFeature = vi.fn();
  const setSelectedFeature = vi.fn();

  const samplePolygon1: FeatureCollection = {
    type: "FeatureCollection",
    features: [
      {
        type: "Feature",
        id: "feat-1",
        geometry: {
          type: "Polygon",
          coordinates: [
            [
              [-123.35, 48.43],
              [-123.36, 48.44],
              [-123.35, 48.44],
              [-123.35, 48.43],
            ],
          ],
        },
        properties: { OPENING_ID: 101 },
      },
    ],
  };

  const samplePolygon2: FeatureCollection = {
    type: "FeatureCollection",
    features: [
      {
        type: "Feature",
        id: "feat-2",
        geometry: {
          type: "Polygon",
          coordinates: [
            [
              [-123.45, 48.53],
              [-123.46, 48.54],
              [-123.45, 48.54],
              [-123.45, 48.53],
            ],
          ],
        },
        properties: { OPENING_ID: 102 },
      },
    ],
  };

  beforeEach(() => {
    vi.clearAllMocks();
    vi.useFakeTimers();
    mockGetZoom.mockReturnValue(13);
    mockContains.mockReturnValue(true);
    capturedMapEvents = {};
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it("renders GeoJSON polygon when zoom > 10", () => {
    mockGetZoom.mockReturnValue(13);

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1, samplePolygon2]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    expect(screen.getByTestId("geojson-feat-1-0-0")).toBeInTheDocument();
    expect(screen.getByTestId("geojson-feat-2-1-0")).toBeInTheDocument();
    expect(screen.queryByTestId("marker")).not.toBeInTheDocument();
  });

  it("renders GeoJSON polygon when zoom <= 10 if totalFeatures <= 1", () => {
    mockGetZoom.mockReturnValue(8);

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    expect(screen.getByTestId("geojson-feat-1-0-0")).toBeInTheDocument();
    expect(screen.queryByTestId("marker")).not.toBeInTheDocument();
  });

  it("renders Marker when zoom <= 10 and totalFeatures > 1", () => {
    mockGetZoom.mockReturnValue(8);

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1, samplePolygon2]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    // Initial render has zoom 13 from default useState, let's trigger zoomend event to update zoom
    act(() => {
      capturedMapEvents.zoomend?.();
    });

    const markers = screen.getAllByTestId("marker");
    expect(markers).toHaveLength(2);
    expect(screen.queryByTestId("geojson-feat-1-0-0")).not.toBeInTheDocument();
  });

  it("handles mouseover and mouseout on GeoJSON feature", () => {
    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    const geoElement = screen.getByTestId("geojson-feat-1-0-0");

    // Mouse over
    fireEvent.mouseOver(geoElement);
    expect(setHoveredFeature).toHaveBeenCalledWith(samplePolygon1.features[0]);

    // Mouse out with no popup hover ref
    fireEvent.mouseOut(geoElement);
    act(() => {
      vi.advanceTimersByTime(50);
    });
    expect(setHoveredFeature).toHaveBeenCalledWith(null);
  });

  it("does not clear hovered feature on mouseout if popup is hovered", () => {
    const isPopupHoveredRef = { current: true };

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1]}
        hoveredFeature={samplePolygon1.features[0]}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
        isPopupHoveredRef={isPopupHoveredRef}
      />
    );

    const geoElement = screen.getByTestId("geojson-feat-1-0-0");
    fireEvent.mouseOver(geoElement);
    setHoveredFeature.mockClear();

    fireEvent.mouseOut(geoElement);
    act(() => {
      vi.advanceTimersByTime(50);
    });
    expect(setHoveredFeature).not.toHaveBeenCalledWith(null);
  });

  it("handles click on GeoJSON feature and animates map when zoom <= 14", () => {
    mockGetZoom.mockReturnValue(13);

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    const geoElement = screen.getByTestId("geojson-feat-1-0-0");
    fireEvent.click(geoElement);

    expect(setSelectedFeature).toHaveBeenCalledWith(samplePolygon1.features[0]);
    expect(mockFlyToBounds).toHaveBeenCalledWith(mockGeoJsonBounds, {
      maxZoom: 16,
      animate: true,
      duration: 0.5,
    });
  });

  it("handles click on GeoJSON feature and animates map when polygon is outside bounds", () => {
    mockGetZoom.mockReturnValue(15);
    mockContains.mockReturnValue(false); // polygon outside map bounds

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    const geoElement = screen.getByTestId("geojson-feat-1-0-0");
    fireEvent.click(geoElement);

    expect(mockFlyToBounds).toHaveBeenCalledWith(mockGeoJsonBounds, {
      maxZoom: 16,
      animate: true,
      duration: 0.5,
    });
  });

  it("does not flyToBounds on GeoJSON click when zoom is 15 and bounds are contained", () => {
    mockGetZoom.mockReturnValue(15);
    mockContains.mockReturnValue(true);

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    const geoElement = screen.getByTestId("geojson-feat-1-0-0");
    fireEvent.click(geoElement);

    expect(mockFlyToBounds).not.toHaveBeenCalled();
  });

  it("handles Marker click, mouseover, mouseout and icon changes", () => {
    mockGetZoom.mockReturnValue(8);

    const { rerender } = render(
      <OpeningsMapEntry
        polygons={[samplePolygon1, samplePolygon2]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    act(() => {
      capturedMapEvents.zoomend?.();
    });

    const markers = screen.getAllByTestId("marker");
    expect(markers[0]).toHaveAttribute("data-icon", "/marker.svg");

    // Click marker
    fireEvent.click(markers[0]);
    expect(setSelectedFeature).toHaveBeenCalledWith(samplePolygon1.features[0]);
    expect(mockFlyToBounds).toHaveBeenCalledWith(mockGeoJsonBounds, {
      maxZoom: 15,
      animate: true,
      duration: 1.0,
    });

    // Mouseover marker
    fireEvent.mouseOver(markers[0]);
    expect(setHoveredFeature).toHaveBeenCalledWith(samplePolygon1.features[0]);

    // Mouseout marker
    fireEvent.mouseOut(markers[0]);
    expect(setHoveredFeature).toHaveBeenCalledWith(null);

    // Rerender with hovered feature -> uses markerHoveredIcon
    rerender(
      <OpeningsMapEntry
        polygons={[samplePolygon1, samplePolygon2]}
        hoveredFeature={samplePolygon1.features[0]}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    const updatedMarkers = screen.getAllByTestId("marker");
    expect(updatedMarkers[0]).toHaveAttribute("data-icon", "/marker-hovered.svg");
    expect(updatedMarkers[1]).toHaveAttribute("data-icon", "/marker.svg");

    // Rerender with selected feature -> uses markerHoveredIcon
    rerender(
      <OpeningsMapEntry
        polygons={[samplePolygon1, samplePolygon2]}
        hoveredFeature={null}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={samplePolygon2.features[0]}
        setSelectedFeature={setSelectedFeature}
      />
    );

    const selectedMarkers = screen.getAllByTestId("marker");
    expect(selectedMarkers[1]).toHaveAttribute("data-icon", "/marker-hovered.svg");
  });

  it("brings hovered feature layer to front via useEffect", () => {
    const mockLayerBringToFront = vi.fn();
    const mockLayer = {
      feature: { id: "feat-1" },
      bringToFront: mockLayerBringToFront,
    };
    const mockOtherLayer = {
      feature: { id: "feat-2" },
      bringToFront: vi.fn(),
    };
    const mockNonFeatureLayer = {
      bringToFront: vi.fn(),
    };

    mockEachLayer.mockImplementation((callback: (layer: any) => void) => {
      callback(mockLayer);
      callback(mockOtherLayer);
      callback(mockNonFeatureLayer);
    });

    render(
      <OpeningsMapEntry
        polygons={[samplePolygon1]}
        hoveredFeature={samplePolygon1.features[0]}
        setHoveredFeature={setHoveredFeature}
        selectedFeature={null}
        setSelectedFeature={setSelectedFeature}
      />
    );

    act(() => {
      vi.runAllTimers();
    });

    expect(mockEachLayer).toHaveBeenCalled();
    expect(mockLayerBringToFront).toHaveBeenCalled();
    expect(mockOtherLayer.bringToFront).not.toHaveBeenCalled();
    expect(mockNonFeatureLayer.bringToFront).not.toHaveBeenCalled();
  });
});
