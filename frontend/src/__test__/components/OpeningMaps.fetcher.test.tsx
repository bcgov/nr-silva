import { describe, it, expect, vi, beforeEach } from "vitest";
import { renderHook, act } from "@testing-library/react";
import * as fetcher from "@/components/OpeningsMap/fetcher";
import { MapKindType } from "@/types/MapLayer";

// Mock fetchMapPoligons for getMapQueries
vi.mock("@/services/OpeningMapService", () => ({
  fetchMapPoligons: vi.fn((id: number, kind: MapKindType) =>
    Promise.resolve({ id, kind, type: "FeatureCollection", features: [] })
  ),
}));

// Mock react-query
vi.mock("@tanstack/react-query", async (importOriginal) => {
  const actual = await importOriginal();
  return {
    ...actual,
    useQueries: vi.fn(({ queries }) =>
      queries.map((q: any) => ({
        status: "success",
        data: {
          id: q.queryKey[2],
          kind: q.queryKey[3].kind,
          type: "FeatureCollection",
          features: [],
        },
      }))
    ),
  };
});

describe("getMapQueries", () => {
  it("returns queries for each openingId", () => {
    const openingIds = [1, 2];
    const kind = "SOME_KIND" as MapKindType;
    const result = fetcher.getMapQueries(openingIds, kind);
    expect(result).toHaveLength(2);
    expect(result[0].data.id).toBe(1);
    expect(result[1].data.id).toBe(2);
  });
});

describe("getUserLocation", () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it("returns fallback location if geolocation is not available", async () => {
    // @ts-ignore
    global.navigator.geolocation = undefined;
    const loc = await fetcher.getUserLocation();
    expect(loc).toEqual({
      lat: 51.339506220208065,
      lng: -121.40991210937501,
      zoom: 6,
    });
  });

  it("returns current location if geolocation and permission granted", async () => {
    // Mock permissions
    // @ts-ignore
    global.navigator.permissions = {
      query: vi.fn().mockResolvedValue({ state: "granted" }),
    };
    // Mock geolocation
    // @ts-ignore
    global.navigator.geolocation = {
      getCurrentPosition: (success: any, _error: any, _opts: any) => {
        success({
          coords: { latitude: 10, longitude: 20 },
        });
      },
    };
    const loc = await fetcher.getUserLocation();
    expect(loc).toEqual({
      lat: 10,
      lng: 20,
      zoom: 8,
    });
  });

  it("returns fallback if geolocation errors", async () => {
    // Mock permissions
    // @ts-ignore
    global.navigator.permissions = {
      query: vi.fn().mockResolvedValue({ state: "granted" }),
    };
    // Mock geolocation
    // @ts-ignore
    global.navigator.geolocation = {
      getCurrentPosition: (_success: any, error: any, _opts: any) => {
        error();
      },
    };
    const loc = await fetcher.getUserLocation();
    expect(loc).toEqual({
      lat: 51.339506220208065,
      lng: -121.40991210937501,
      zoom: 6,
    });
  });

  it("returns fallback if permission is denied", async () => {
    // Mock permissions
    // @ts-ignore
    global.navigator.permissions = {
      query: vi.fn().mockResolvedValue({ state: "denied" }),
    };
    // @ts-ignore
    global.navigator.geolocation = {
      getCurrentPosition: vi.fn(),
    };
    const loc = await fetcher.getUserLocation();
    expect(loc).toEqual({
      lat: 51.339506220208065,
      lng: -121.40991210937501,
      zoom: 6,
    });
  });
});
