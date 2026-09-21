import { describe, it, expect, vi } from "vitest";
import {
  defaultStyle,
  mapKinds,
  getPropertyForFeature,
  getPopupCenter,
  getCenterOfFeatureCollection,
  getStyleForFeature,
  kindWeightMap,
} from "@/types/MapLayer";
import { Feature, FeatureCollection, Geometry } from "geojson";

describe("MapLayer", () => {
  describe("mapKinds popup formatting", () => {
    it("formats popup for RSLT_OPENING_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        REGION_NAME: "Cariboo",
        REGION_CODE: "RCO",
        DISTRICT_NAME: "Quesnel",
        DISTRICT_CODE: "DQU",
        OPENING_WHEN_CREATED: "2021-06-15T00:00:00",
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW",
        region: "Cariboo (RCO)",
        district: "Quesnel (DQU)",
        yearCreated: "2021",
      });
    });

    it("formats popup for FTEN_CUT_BLOCK_POLY_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        CUT_BLOCK_FOREST_FILE_ID: "TFL47",
        CUT_BLOCK_ID: "CB1",
        HARVEST_AUTH_CUTTING_PERMIT_ID: "CP12",
        CLIENT_NAME: "West Fraser",
        CLIENT_NUMBER: "00012345",
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW",
        polygonType: "Tenure / Cut Block",
        forestFileId: "TFL47",
        cutBlockId: "CB1",
        cuttingPermitId: "CP12",
        client: "West Fraser (00012345)",
      });
    });

    it("formats popup for RSLT_STANDARDS_UNIT_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        STANDARDS_REGIME_ID: 1001,
        STANDARDS_UNIT_ID: "SU1",
        NET_AREA: 15.4,
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW",
        polygonType: "Standards Unit",
        ssid: 1001,
        standardUnitId: "SU1",
        netArea: 15.4,
      });
    });

    it("formats popup for RSLT_ACTIVITY_TREATMENT_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        ACTIVITY_TREATMENT_UNIT_ID: 555,
        SILV_BASE_CODE: "PL",
        DISTURBANCE_CODE: "L",
        ACTUAL_TREATMENT_AREA: 22.1,
        ATU_COMPLETION_DATE: "2023-05-10",
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW",
        polygonType: "Activity and Disturbance",
        activityId: 555,
        silvBaseCode: "PL",
        disturbanceCode: "L",
        area: 22.1,
        endDate: "2023-05-10",
      });
    });

    it("formats popup for RSLT_FOREST_COVER_INV_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        FOREST_COVER_ID: 999,
        SILV_POLYGON_NUMBER: "1",
        SILV_POLYGON_AREA: 12.3,
        SILV_POLYGON_NET_AREA: 10.1,
        REFERENCE_YEAR: 2022,
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW",
        polygonType: "Forest Cover Inventory",
        forestCoverId: 999,
        polygon: "1",
        polygonArea: 12.3,
        netArea: 10.1,
        referenceYear: 2022,
      });
    });

    it("formats popup for RSLT_FOREST_COVER_RESERVE_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        SILV_POLYGON_NO: "RES-1",
        SILV_POLYGON_AREA: 5.6,
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW",
        polygonType: "Forest Cover Reserve",
        polygon: "RES-1",
        polygonArea: 5.6,
      });
    });

    it("formats popup for RSLT_FOREST_COVER_SILV_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        FOREST_COVER_ID: 888,
        SILV_POLYGON_NUMBER: "S2",
        SILV_POLYGON_AREA: 8.9,
        SILV_POLYGON_NET_AREA: 7.2,
        REFERENCE_YEAR: 2020,
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW",
        polygonType: "Forest Cover Silviculture",
        forestCoverId: 888,
        polygon: "S2",
        polygonArea: 8.9,
        netArea: 7.2,
        referenceYear: 2020,
      });
    });

    it("formats popup for RSLT_PLANTING_SVW", () => {
      const kind = mapKinds.find((k) => k.code === "WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW");
      expect(kind).toBeDefined();

      const popup = kind!.popup({
        ACTIVITY_TREATMENT_UNIT_ID: 777,
        MAP_LABEL: "PL-01",
        SILV_BASE_CODE: "PL",
        PLANTING_RESULTS_SEQ_NUMBER: 1,
        SILV_TREE_SPECIES_CODE: "SX",
      });

      expect(popup).toEqual({
        mapKindType: "WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW",
        "Polygon type": "Activity: Planting",
        "Activity Treatment Unit Id": 777,
        "Map Label": "PL-01",
        "Silviculture Base Code": "PL",
        "Planting Results Seq Number": 1,
        "Silviculture Tree Species Code": "SX",
      });
    });
  });

  describe("getPropertyForFeature", () => {
    it("returns formatted popup data when feature matches a map kind", () => {
      const feature: Feature<Geometry, any> = {
        type: "Feature",
        id: "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW.123",
        properties: {
          REGION_NAME: "Coast",
          REGION_CODE: "RCO",
          DISTRICT_NAME: "Chilliwack",
          DISTRICT_CODE: "DCK",
          OPENING_WHEN_CREATED: "2020-01-01",
        },
        geometry: {
          type: "Point",
          coordinates: [-121.5, 49.2],
        },
      };

      const result = getPropertyForFeature(feature);
      expect(result.mapKindType).toBe("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW");
      expect(result.region).toBe("Coast (RCO)");
    });

    it("returns empty object when feature id does not match any map kind", () => {
      const feature: Feature<Geometry, any> = {
        type: "Feature",
        id: "UNKNOWN_KIND.123",
        properties: {},
        geometry: {
          type: "Point",
          coordinates: [0, 0],
        },
      };

      expect(getPropertyForFeature(feature)).toEqual({});
    });

    it("handles feature with undefined properties gracefully", () => {
      const feature: Feature<Geometry, any> = {
        type: "Feature",
        id: "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW.123",
        properties: undefined,
        geometry: {
          type: "Point",
          coordinates: [0, 0],
        },
      };

      const result = getPropertyForFeature(feature);
      expect(result.mapKindType).toBe("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW");
    });
  });

  describe("getPopupCenter", () => {
    it("calculates center for Point geometry", () => {
      const center = getPopupCenter({
        type: "Point",
        coordinates: [-123.5, 48.5],
      });
      expect(center).toEqual([-123.5, 48.5]);
    });

    it("calculates center for LineString geometry", () => {
      const center = getPopupCenter({
        type: "LineString",
        coordinates: [
          [-120, 50],
          [-122, 52],
        ],
      });
      // average: lat avg = (50+52)/2 = 51, lng avg = (-120 + -122)/2 = -121
      expect(center).toEqual([51, -121]);
    });

    it("calculates center for MultiPoint geometry", () => {
      const center = getPopupCenter({
        type: "MultiPoint",
        coordinates: [
          [-120, 50],
          [-122, 52],
        ],
      });
      expect(center).toEqual([51, -121]);
    });

    it("calculates center for Polygon geometry using outer ring", () => {
      const center = getPopupCenter({
        type: "Polygon",
        coordinates: [
          [
            [-120, 50],
            [-120, 52],
            [-122, 52],
            [-122, 50],
          ],
        ],
      });
      expect(center).toEqual([51, -121]);
    });

    it("calculates center for MultiPolygon geometry using all outer rings", () => {
      const center = getPopupCenter({
        type: "MultiPolygon",
        coordinates: [
          [
            [
              [-120, 50],
              [-120, 52],
            ],
          ],
          [
            [
              [-122, 50],
              [-122, 52],
            ],
          ],
        ],
      });
      expect(center).toEqual([51, -121]);
    });

    it("handles unsupported geometry with warning and fallback [0, 0]", () => {
      const warnSpy = vi.spyOn(console, "warn").mockImplementation(() => {});
      const center = getPopupCenter({
        type: "GeometryCollection",
        geometries: [],
      } as any);

      expect(center).toEqual([0, 0]);
      expect(warnSpy).toHaveBeenCalled();
      warnSpy.mockRestore();
    });
  });

  describe("getCenterOfFeatureCollection", () => {
    it("calculates center for collection with mixed geometries", () => {
      const collection: FeatureCollection<Geometry> = {
        type: "FeatureCollection",
        features: [
          {
            type: "Feature",
            geometry: { type: "Point", coordinates: [-120, 50] },
            properties: {},
          },
          {
            type: "Feature",
            geometry: {
              type: "LineString",
              coordinates: [
                [-122, 52],
                [-124, 54],
              ],
            },
            properties: {},
          },
          {
            type: "Feature",
            geometry: {
              type: "Polygon",
              coordinates: [
                [
                  [-118, 48],
                  [-116, 46],
                ],
              ],
            },
            properties: {},
          },
          {
            type: "Feature",
            geometry: {
              type: "MultiPolygon",
              coordinates: [
                [
                  [
                    [-114, 44],
                    [-112, 42],
                  ],
                ],
              ],
            },
            properties: {},
          },
        ],
      };

      const center = getCenterOfFeatureCollection(collection);
      expect(center[0]).toBeGreaterThan(40);
      expect(center[1]).toBeLessThan(-100);
    });

    it("skips features without geometry", () => {
      const collection: FeatureCollection<Geometry> = {
        type: "FeatureCollection",
        features: [
          {
            type: "Feature",
            geometry: null as any,
            properties: {},
          },
          {
            type: "Feature",
            geometry: { type: "Point", coordinates: [-120, 50] },
            properties: {},
          },
        ],
      };

      const center = getCenterOfFeatureCollection(collection);
      expect(center).toEqual([50, -120]);
    });
  });

  describe("getStyleForFeature", () => {
    it("returns defaultStyle when feature is undefined or has no id", () => {
      expect(getStyleForFeature(undefined)).toEqual(defaultStyle);

      const noIdFeature: Feature<Geometry, any> = {
        type: "Feature",
        properties: {},
        geometry: { type: "Point", coordinates: [0, 0] },
      };
      expect(getStyleForFeature(noIdFeature)).toEqual(defaultStyle);
    });

    it("applies disturbance styling for SILV_BASE_CODE === DN", () => {
      const dnFeature: Feature<Geometry, any> = {
        type: "Feature",
        id: "WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW.1",
        properties: {
          SILV_BASE_CODE: "DN",
          ACTIVITY_TREATMENT_UNIT_ID: 101,
        },
        geometry: { type: "Point", coordinates: [0, 0] },
      };

      // Normal state
      const normalStyle = getStyleForFeature(dnFeature);
      expect(normalStyle.color).toBe("#939395");
      expect(normalStyle.fillColor).toBe("#D2D2D4");
      expect(normalStyle.weight).toBe(1);

      // Selected state
      const selectedStyle = getStyleForFeature(dnFeature, dnFeature);
      expect(selectedStyle.color).toBe("#000000");
      expect(selectedStyle.weight).toBe(
        kindWeightMap["WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW"]
      );

      // Hovered state
      const hoveredStyle = getStyleForFeature(dnFeature, null, dnFeature);
      expect(hoveredStyle.color).toBe("#000000");
    });

    it("applies activity styling for non-DN SILV_BASE_CODE", () => {
      const activityFeature: Feature<Geometry, any> = {
        type: "Feature",
        id: "WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW.2",
        properties: {
          SILV_BASE_CODE: "PL",
          ACTIVITY_TREATMENT_UNIT_ID: 102,
        },
        geometry: { type: "Point", coordinates: [0, 0] },
      };

      const normalStyle = getStyleForFeature(activityFeature);
      expect(normalStyle.color).toBe("#E64F02");
      expect(normalStyle.fillColor).toBe("#FFBC99");
      expect(normalStyle.weight).toBe(1);

      const selectedStyle = getStyleForFeature(activityFeature, activityFeature);
      expect(selectedStyle.color).toBe("#000000");
      expect(selectedStyle.weight).toBe(3);
    });

    it("applies mapKinds styling and spaced color when matching a known kind", () => {
      const openingFeature: Feature<Geometry, any> = {
        type: "Feature",
        id: "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW.999",
        properties: {},
        geometry: { type: "Point", coordinates: [0, 0] },
      };

      // Total features <= 1
      const singleStyle = getStyleForFeature(openingFeature, null, null, 0, 1);
      expect(singleStyle.color).toBe("#005CB8");
      expect(singleStyle.fillColor).toBe("#9FB3DF");

      // Multiple features: spaced color
      const multiStyle = getStyleForFeature(openingFeature, null, null, 2, 4);
      expect(multiStyle.fillColor).toBeDefined();

      // Selected state
      const selectedStyle = getStyleForFeature(openingFeature, openingFeature);
      expect(selectedStyle.color).toBe("#000000");
      expect(selectedStyle.weight).toBe(3);
    });

    it("returns defaultStyle when feature id does not match any map kind", () => {
      const unknownFeature: Feature<Geometry, any> = {
        type: "Feature",
        id: "NON_EXISTENT_KIND.123",
        properties: {},
        geometry: { type: "Point", coordinates: [0, 0] },
      };

      expect(getStyleForFeature(unknownFeature)).toEqual(defaultStyle);
    });
  });
});
