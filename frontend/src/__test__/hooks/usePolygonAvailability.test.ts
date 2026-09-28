import { renderHook, waitFor } from "@testing-library/react";
import { describe, expect, it, vi, beforeEach } from "vitest";
import React from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import usePolygonAvailability from "../../hooks/usePolygonAvailability";
import { MAP_KINDS } from "../../constants/mapKindConstants";
import API from "../../services/API";

vi.mock("../../services/API", () => ({
  default: {
    OpeningMapsEndpointService: {
      getOpeningPolygonAndProperties: vi.fn(),
    },
  },
}));

const createWrapper = () => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false },
    },
  });
  return ({ children }: { children: React.ReactNode }) =>
    React.createElement(QueryClientProvider, { client: queryClient }, children);
};

describe("usePolygonAvailability", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("returns isAvailable false and isLoading false when compoundId is null", () => {
    const { result } = renderHook(
      () => usePolygonAvailability(101, MAP_KINDS.opening, null),
      { wrapper: createWrapper() }
    );

    expect(result.current.isAvailable).toBe(false);
    expect(result.current.isLoading).toBe(false);
    expect(
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties
    ).not.toHaveBeenCalled();
  });

  it("returns isAvailable false and isLoading false when kind has no extractor", () => {
    const { result } = renderHook(
      () => usePolygonAvailability(101, "UNKNOWN_KIND" as any, "123"),
      { wrapper: createWrapper() }
    );

    expect(result.current.isAvailable).toBe(false);
    expect(result.current.isLoading).toBe(false);
  });

  it("returns isLoading true while query is loading", () => {
    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockReturnValue(new Promise(() => {}));

    const { result } = renderHook(
      () => usePolygonAvailability(101, MAP_KINDS.opening, "101"),
      { wrapper: createWrapper() }
    );

    expect(result.current.isLoading).toBe(true);
    expect(result.current.isAvailable).toBe(false);
  });

  it("identifies availability for opening map kind", async () => {
    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockResolvedValueOnce({
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: { OPENING_ID: 101 },
          geometry: { type: "Polygon", coordinates: [] },
        },
      ],
    });

    const { result } = renderHook(
      () => usePolygonAvailability(101, MAP_KINDS.opening, "101"),
      { wrapper: createWrapper() }
    );

    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(result.current.isAvailable).toBe(true);
  });

  it("identifies availability for activityTreatment and planting kinds", async () => {
    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockResolvedValueOnce({
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: {
            ACTIVITY_TREATMENT_UNIT_ID: 555,
            SILV_BASE_CODE: "SP",
          },
          geometry: { type: "Polygon", coordinates: [] },
        },
      ],
    });

    const { result } = renderHook(
      () =>
        usePolygonAvailability(101, MAP_KINDS.activityTreatment, "555-SP"),
      { wrapper: createWrapper() }
    );

    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(result.current.isAvailable).toBe(true);

    // Test planting kind with matching extractor
    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockResolvedValueOnce({
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: {
            ACTIVITY_TREATMENT_UNIT_ID: 556,
            SILV_BASE_CODE: "PL",
          },
          geometry: { type: "Polygon", coordinates: [] },
        },
      ],
    });

    const { result: plantingResult } = renderHook(
      () => usePolygonAvailability(101, MAP_KINDS.planting, "556-PL"),
      { wrapper: createWrapper() }
    );

    await waitFor(() => expect(plantingResult.current.isLoading).toBe(false));
    expect(plantingResult.current.isAvailable).toBe(true);
  });

  it("identifies availability for forestCover inventory, reserve, and silviculture kinds", async () => {
    const featureData = {
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: {
            FOREST_COVER_ID: 700,
            SILV_POLYGON_NUMBER: "1",
          },
          geometry: { type: "Polygon", coordinates: [] },
        },
      ],
    };

    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockResolvedValue(featureData);

    const { result: invResult } = renderHook(
      () =>
        usePolygonAvailability(
          101,
          MAP_KINDS.forestCoverInventory,
          "700-1"
        ),
      { wrapper: createWrapper() }
    );
    await waitFor(() => expect(invResult.current.isAvailable).toBe(true));

    const { result: resResult } = renderHook(
      () =>
        usePolygonAvailability(101, MAP_KINDS.forestCoverReserve, "700-1"),
      { wrapper: createWrapper() }
    );
    await waitFor(() => expect(resResult.current.isAvailable).toBe(true));

    const { result: silvResult } = renderHook(
      () =>
        usePolygonAvailability(
          101,
          MAP_KINDS.forestCoverSilviculture,
          "700-1"
        ),
      { wrapper: createWrapper() }
    );
    await waitFor(() => expect(silvResult.current.isAvailable).toBe(true));
  });

  it("identifies availability for standardsUnit and cutBlock kinds", async () => {
    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockResolvedValueOnce({
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: { STOCKING_STANDARD_UNIT_ID: 888 },
          geometry: { type: "Polygon", coordinates: [] },
        },
      ],
    });

    const { result: suResult } = renderHook(
      () => usePolygonAvailability(101, MAP_KINDS.standardsUnit, "888"),
      { wrapper: createWrapper() }
    );
    await waitFor(() => expect(suResult.current.isAvailable).toBe(true));

    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockResolvedValueOnce({
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: {
            CUT_BLOCK_FOREST_FILE_ID: "TFL47",
            CUT_BLOCK_ID: "12A",
          },
          geometry: { type: "Polygon", coordinates: [] },
        },
      ],
    });

    const { result: cbResult } = renderHook(
      () => usePolygonAvailability(101, MAP_KINDS.cutBlock, "TFL47-12A"),
      { wrapper: createWrapper() }
    );
    await waitFor(() => expect(cbResult.current.isAvailable).toBe(true));
  });

  it("returns isAvailable false if compoundId does not match or geometry is null", async () => {
    (
      API.OpeningMapsEndpointService.getOpeningPolygonAndProperties as vi.Mock
    ).mockResolvedValueOnce({
      type: "FeatureCollection",
      features: [
        {
          type: "Feature",
          properties: { OPENING_ID: 101 },
          geometry: null,
        },
        {
          type: "Feature",
          properties: null,
          geometry: { type: "Polygon", coordinates: [] },
        },
      ],
    });

    const { result } = renderHook(
      () => usePolygonAvailability(101, MAP_KINDS.opening, "101"),
      { wrapper: createWrapper() }
    );

    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(result.current.isAvailable).toBe(false);
  });
});
