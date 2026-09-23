import React from "react";
import { render, screen, fireEvent, act } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import OpeningsMap from "@/components/OpeningsMap";
import { getMapQueries, getUserLocation } from "@/components/OpeningsMap/fetcher";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { FeatureCollection } from "geojson";

let capturedMapEvents: Record<string, () => void> = {};

vi.mock("@/components/OpeningsMap/fetcher", () => ({
  getMapQueries: vi.fn(),
  getUserLocation: vi.fn(),
}));

vi.mock("react-leaflet", () => ({
  MapContainer: ({ children, style }: any) => (
    <div data-testid="map-container" style={style}>
      {children}
    </div>
  ),
  TileLayer: () => <div data-testid="tile-layer" />,
  WMSTileLayer: ({ params }: any) => (
    <div data-testid="wms-tile-layer" data-params={JSON.stringify(params)} />
  ),
  ZoomControl: () => <div data-testid="zoom-control" />,
  LayersControl: Object.assign(
    ({ children }: any) => <div data-testid="layers-control">{children}</div>,
    {
      BaseLayer: ({ children, name, checked }: any) => (
        <div data-testid={`base-layer-${name}`} data-checked={checked}>
          {children}
        </div>
      ),
      Overlay: ({ children, name }: any) => (
        <div data-testid={`overlay-${name}`}>{children}</div>
      ),
    }
  ),
  useMapEvents: vi.fn((handlers: any) => {
    capturedMapEvents = handlers;
    return {};
  }),
}));

vi.mock("@/components/OpeningsMapResizer", () => ({
  default: ({ height }: any) => <div data-testid="map-resizer" data-height={height} />,
}));

vi.mock("@/components/OpeningsMapFitBound", () => ({
  default: ({ polygons }: any) => (
    <div data-testid="map-fit-bound" data-count={polygons?.length ?? 0} />
  ),
}));

vi.mock("@/components/OpeningsMapEntryPopup", () => ({
  default: ({ openingId, isSelected }: any) => (
    <div
      data-testid="map-entry-popup"
      data-opening-id={openingId}
      data-selected={String(isSelected)}
    />
  ),
}));

vi.mock("@/components/OpeningsMapEntry", () => ({
  default: ({
    polygons,
    hoveredFeature,
    setHoveredFeature,
    selectedFeature,
    setSelectedFeature,
  }: any) => {
    const renderedFeatureIds =
      polygons
        ?.flatMap((fc: any) => fc.features ?? [])
        .map((f: any) => String(f.id))
        .join(",") ?? "";

    return (
      <div
        data-testid="openings-map-entry"
        data-polygon-count={polygons?.length ?? 0}
        data-rendered-features={renderedFeatureIds}
      >
        <button
          data-testid="entry-hover-btn"
          onClick={() => setHoveredFeature(polygons[0]?.features?.[0] ?? null)}
        >
          Hover Feature
        </button>
        <button
          data-testid="entry-select-btn"
          onClick={() => setSelectedFeature(polygons[0]?.features?.[0] ?? null)}
        >
          Select Feature
        </button>
        <span data-testid="entry-hovered-id">{hoveredFeature ? String(hoveredFeature.id) : "none"}</span>
        <span data-testid="entry-selected-id">{selectedFeature ? String(selectedFeature.id) : "none"}</span>
      </div>
    );
  },
}));

describe("OpeningsMap", () => {
  const mockSetOpeningPolygonNotFound = vi.fn();

  const mockFeatureCollection: FeatureCollection = {
    type: "FeatureCollection",
    features: [
      {
        type: "Feature",
        id: "feat-101",
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
        properties: {
          OPENING_ID: 101,
          FOREST_COVER_ID: "FC1",
          SILV_POLYGON_NUMBER: "P1",
          STOCKING_STANDARD_UNIT_ID: "SU1",
          ACTIVITY_TREATMENT_UNIT_ID: "ATU1",
          SILV_BASE_CODE: "PL",
        },
      },
      {
        type: "Feature",
        id: "feat-102",
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
        properties: {
          OPENING_ID: 101,
          ACTIVITY_TREATMENT_UNIT_ID: "ATU2",
          SILV_BASE_CODE: "DN",
        },
      },
    ],
  };

  beforeEach(() => {
    vi.clearAllMocks();
    capturedMapEvents = {};
    vi.mocked(getUserLocation).mockResolvedValue({
      lat: 53.7267,
      lng: -127.6476,
      zoom: 6,
    });
    vi.mocked(getMapQueries).mockReturnValue([
      {
        status: "success",
        data: mockFeatureCollection,
      } as any,
    ]);
  });

  it("fetches user location when openingIds is empty", async () => {
    vi.mocked(getMapQueries).mockReturnValue([]);

    await act(async () => {
      render(
        <OpeningsMap
          openingIds={[]}
          setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        />
      );
    });

    expect(getUserLocation).toHaveBeenCalled();
    expect(screen.getByTestId("map-container")).toBeInTheDocument();
    expect(screen.getByTestId("map-fit-bound")).toHaveAttribute("data-count", "0");
  });

  it("renders map container, resizer, fitBound, layers, and entry when openingIds provided", () => {
    render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
      />
    );

    expect(getMapQueries).toHaveBeenCalledWith([101], MAP_KINDS.opening);
    expect(screen.getByTestId("map-container")).toBeInTheDocument();
    expect(screen.getByTestId("map-resizer")).toHaveAttribute("data-height", "480");
    expect(screen.getByTestId("map-fit-bound")).toHaveAttribute("data-count", "1");
    expect(screen.getByTestId("openings-map-entry")).toBeInTheDocument();
    expect(screen.getByTestId("layers-control")).toBeInTheDocument();
  });

  it("reports error when map query fails with openingId cause", () => {
    vi.mocked(getMapQueries).mockReturnValue([
      {
        status: "error",
        error: {
          name: "Error",
          message: "Failed to load",
          cause: { openingId: "101" },
        },
      } as any,
    ]);

    render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
      />
    );

    expect(mockSetOpeningPolygonNotFound).toHaveBeenCalledWith(true, 101);
  });

  it("renders forest cover banners according to selection", () => {
    const { rerender } = render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isForestCoverMap={true}
        selectedForestCoverIds={[]}
      />
    );

    expect(
      screen.getByText(
        "No forest cover polygon is selected and displayed. Select from the table to show on map."
      )
    ).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      ""
    );

    rerender(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isForestCoverMap={true}
        selectedForestCoverIds={["FC1-P1"]}
      />
    );

    expect(
      screen.getByText("Showing selected forest cover polygons")
    ).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      "feat-101"
    );
  });

  it("renders standards unit banners according to selection", () => {
    const { rerender } = render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isStandardsUnitMap={true}
        selectedStandardsUnitIds={[]}
      />
    );

    expect(
      screen.getByText(
        "No standards unit polygon is selected and displayed. Select from the table to show on map."
      )
    ).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      ""
    );

    rerender(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isStandardsUnitMap={true}
        selectedStandardsUnitIds={["SU1"]}
      />
    );

    expect(
      screen.getByText("Showing selected standards unit polygon")
    ).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      "feat-101"
    );
  });

  it("renders activities map banners according to activity and disturbance selections", () => {
    const { rerender } = render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isActivitiesMap={true}
        selectedSilvicultureActivityIds={[]}
        selectedDisturbanceIds={[]}
      />
    );

    expect(
      screen.getByText(
        "No activities or disturbances are selected and displayed. Select from the table to show on map."
      )
    ).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      ""
    );

    rerender(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isActivitiesMap={true}
        selectedSilvicultureActivityIds={["ATU1-PL"]}
        selectedDisturbanceIds={[]}
      />
    );
    expect(screen.getByText("Showing selected activities")).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      "feat-101"
    );

    rerender(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isActivitiesMap={true}
        selectedSilvicultureActivityIds={[]}
        selectedDisturbanceIds={["ATU2-DN"]}
      />
    );
    expect(screen.getByText("Showing selected disturbances")).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      "feat-102"
    );

    rerender(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isActivitiesMap={true}
        selectedSilvicultureActivityIds={["ATU1-PL"]}
        selectedDisturbanceIds={["ATU2-DN"]}
      />
    );
    expect(
      screen.getByText("Showing selected activities and disturbances")
    ).toBeInTheDocument();
    expect(screen.getByTestId("openings-map-entry")).toHaveAttribute(
      "data-rendered-features",
      "feat-101,feat-102"
    );
  });

  it("shows and interacts with popup on feature hover and select", () => {
    render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
      />
    );

    // Hover feature
    fireEvent.click(screen.getByTestId("entry-hover-btn"));
    expect(screen.getByTestId("map-entry-popup")).toBeInTheDocument();
    expect(screen.getByTestId("map-entry-popup")).toHaveAttribute(
      "data-opening-id",
      "101"
    );

    // Mouse enter popup, then mouse leave
    const popupContainer = screen.getByTestId("map-entry-popup").parentElement!;
    fireEvent.mouseEnter(popupContainer);
    fireEvent.mouseLeave(popupContainer);

    // Select feature
    fireEvent.click(screen.getByTestId("entry-select-btn"));
    expect(screen.getByTestId("map-entry-popup")).toBeInTheDocument();
    expect(screen.getByTestId("map-entry-popup")).toHaveAttribute(
      "data-selected",
      "true"
    );
  });

  it("handles map click to deselect feature", () => {
    render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
      />
    );

    // Select a feature
    fireEvent.click(screen.getByTestId("entry-select-btn"));
    expect(screen.getByTestId("entry-selected-id")).toHaveTextContent("feat-101");

    // Click map with polygonClickedRef true (resetting flag)
    act(() => {
      capturedMapEvents.click?.();
    });

    // Second click on empty map deselects
    act(() => {
      capturedMapEvents.click?.();
    });

    expect(screen.getByTestId("entry-selected-id")).toHaveTextContent("none");
  });

  it("applies CQL_FILTER when layerFilter is true", () => {
    render(
      <OpeningsMap
        openingIds={[101, 102]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        layerFilter={true}
      />
    );

    const wmsLayers = screen.getAllByTestId("wms-tile-layer");
    const filterableLayer = wmsLayers.find((el) => {
      const params = JSON.parse(el.getAttribute("data-params") || "{}");
      return params.CQL_FILTER != null;
    });

    expect(filterableLayer).toBeDefined();
    const params = JSON.parse(filterableLayer!.getAttribute("data-params")!);
    expect(params.CQL_FILTER).toBe("OPENING_ID=101;OPENING_ID=102");
  });

  it("restores selected feature on details page when openings data updates", async () => {
    const { rerender } = render(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isDetailsPage={true}
      />
    );

    // Select feature
    fireEvent.click(screen.getByTestId("entry-select-btn"));
    expect(screen.getByTestId("entry-selected-id")).toHaveTextContent("feat-101");

    // Change openings query data (simulating a refetch/update)
    const updatedFeatureCollection: FeatureCollection = {
      type: "FeatureCollection",
      features: [
        {
          ...mockFeatureCollection.features[0]!,
          properties: {
            ...mockFeatureCollection.features[0]!.properties,
            UPDATED: true,
          },
        },
      ],
    };

    vi.mocked(getMapQueries).mockReturnValue([
      {
        status: "success",
        data: updatedFeatureCollection,
      } as any,
    ]);

    // Rerender with new data triggers restoration effect in details page
    rerender(
      <OpeningsMap
        openingIds={[101]}
        setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
        isDetailsPage={true}
      />
    );

    expect(screen.getByTestId("entry-selected-id")).toHaveTextContent("feat-101");

    // When openings data becomes empty, effect clears selected feature
    vi.mocked(getMapQueries).mockReturnValue([]);
    await act(async () => {
      rerender(
        <OpeningsMap
          openingIds={[]}
          setOpeningPolygonNotFound={mockSetOpeningPolygonNotFound}
          isDetailsPage={true}
        />
      );
    });

    expect(screen.getByTestId("entry-selected-id")).toHaveTextContent("none");
  });
});
