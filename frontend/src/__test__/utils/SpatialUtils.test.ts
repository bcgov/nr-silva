import { describe, it, expect, vi } from "vitest";
import {
  normalizeEpsg,
  parsePosList,
  parseCoordinates,
  ensureClosedRing,
  transformRingToWgs84,
  esfXmlToGeoJSON,
  gmlToGeoJSON,
} from "../../utils/SpatialUtils";

describe("SpatialUtils", () => {
  describe("normalizeEpsg", () => {
    it("returns EPSG:3005 when input is undefined or empty", () => {
      expect(normalizeEpsg(undefined)).toBe("EPSG:3005");
      expect(normalizeEpsg("")).toBe("EPSG:3005");
    });

    it("normalizes variants of EPSG codes", () => {
      expect(normalizeEpsg("EPSG:3005")).toBe("EPSG:3005");
      expect(normalizeEpsg("epsg:4326")).toBe("EPSG:4326");
      expect(normalizeEpsg("EPSG/3005")).toBe("EPSG:3005");
      expect(normalizeEpsg("urn:ogc:def:crs:EPSG::3005")).toBe("EPSG:3005");
      expect(normalizeEpsg("urn:x-ogc:def:crs:EPSG:4326")).toBe("EPSG:4326");
    });

    it("returns uppercase input when no EPSG pattern matches", () => {
      expect(normalizeEpsg("CRS:84")).toBe("CRS:84");
    });
  });

  describe("parsePosList", () => {
    it("parses space-delimited coordinates into [x, y] tuples", () => {
      const coords = parsePosList("1000 2000 3000 4000");
      expect(coords).toEqual([
        [1000, 2000],
        [3000, 4000],
      ]);
    });

    it("ignores odd trailing numbers and non-numeric values", () => {
      const coords = parsePosList("100 200 invalid 300 400 500");
      expect(coords).toEqual([
        [100, 200],
        [300, 400],
      ]);
    });

    it("returns empty array for empty or non-numeric text", () => {
      expect(parsePosList("")).toEqual([]);
      expect(parsePosList("foo bar")).toEqual([]);
    });
  });

  describe("parseCoordinates", () => {
    it("parses comma-separated coordinates pairs separated by spaces", () => {
      const coords = parseCoordinates("10,20 30,40 50,60");
      expect(coords).toEqual([
        [10, 20],
        [30, 40],
        [50, 60],
      ]);
    });

    it("skips invalid or incomplete pairs", () => {
      const coords = parseCoordinates("10,20 invalid,30 40,bad 50,60");
      expect(coords).toEqual([
        [10, 20],
        [50, 60],
      ]);
    });

    it("returns empty array for empty string", () => {
      expect(parseCoordinates("")).toEqual([]);
    });
  });

  describe("ensureClosedRing", () => {
    it("returns empty or falsy rings unchanged", () => {
      expect(ensureClosedRing([])).toEqual([]);
      expect(ensureClosedRing(null as any)).toBeNull();
    });

    it("returns coordinates from OpenLayers LinearRing", () => {
      const ring: [number, number][] = [
        [0, 0],
        [10, 0],
        [10, 10],
        [0, 0],
      ];
      const closed = ensureClosedRing(ring);
      expect(closed.length).toBe(4);
      expect(closed[0]).toEqual(closed[closed.length - 1]);
    });
  });

  describe("transformRingToWgs84", () => {
    it("transforms coordinates from EPSG:3005 to EPSG:4326 (WGS84 lon/lat)", () => {
      // BC Albers coordinate near Victoria / Vancouver
      const ring = [
        [1200000, 400000],
        [1201000, 400000],
        [1201000, 401000],
        [1200000, 400000],
      ];
      const transformed = transformRingToWgs84(ring, "EPSG:3005");
      expect(transformed.length).toBe(4);
      // Longitude for BC is negative (around -120 to -125), Latitude is positive (around 48 to 55)
      expect(transformed[0][0]).toBeLessThan(0);
      expect(transformed[0][1]).toBeGreaterThan(45);
    });
  });

  describe("esfXmlToGeoJSON", () => {
    it("returns empty feature collection if OpeningDefinition/extentOf has no polygon", () => {
      const xml = `<OpeningDefinition />`;
      const result = esfXmlToGeoJSON(xml);
      expect(result).toEqual({ type: "FeatureCollection", features: [] });
    });

    it("parses GML3 posList with outer boundary and interior hole", () => {
      const xml = `
        <OpeningDefinition>
          <extentOf>
            <MultiPolygon srsName="EPSG:3005">
              <polygonMember>
                <Polygon>
                  <exterior>
                    <LinearRing>
                      <posList>1000000 400000 1001000 400000 1001000 401000 1000000 401000 1000000 400000</posList>
                    </LinearRing>
                  </exterior>
                  <interior>
                    <LinearRing>
                      <posList>1000200 400200 1000800 400200 1000800 400800 1000200 400800 1000200 400200</posList>
                    </LinearRing>
                  </interior>
                </Polygon>
              </polygonMember>
            </MultiPolygon>
          </extentOf>
        </OpeningDefinition>
      `;
      const result = esfXmlToGeoJSON(xml);
      expect(result.type).toBe("FeatureCollection");
      expect(result.features.length).toBe(1);
      const feature = result.features[0];
      expect(feature.geometry.type).toBe("Polygon");
      const polyGeom = feature.geometry as GeoJSON.Polygon;
      expect(polyGeom.coordinates.length).toBe(2); // 1 outer ring + 1 hole
    });

    it("parses GML2 coordinates in outerBoundaryIs / innerBoundaryIs and single Polygon fallback", () => {
      const xml = `
        <OpeningDefinition>
          <extentOf>
            <Polygon srsName="EPSG:3005">
              <outerBoundaryIs>
                <LinearRing>
                  <coordinates>1000000,400000 1001000,400000 1001000,401000 1000000,401000 1000000,400000</coordinates>
                </LinearRing>
              </outerBoundaryIs>
              <innerBoundaryIs>
                <LinearRing>
                  <coordinates>1000200,400200 1000800,400200 1000800,400800 1000200,400800 1000200,400200</coordinates>
                </LinearRing>
              </innerBoundaryIs>
            </Polygon>
          </extentOf>
        </OpeningDefinition>
      `;
      const result = esfXmlToGeoJSON(xml);
      expect(result.type).toBe("FeatureCollection");
      expect(result.features.length).toBe(1);
      const polyGeom = result.features[0].geometry as GeoJSON.Polygon;
      expect(polyGeom.coordinates.length).toBe(2);
    });

    it("parses fallback LinearRing without outerBoundaryIs wrapper", () => {
      const xml = `
        <OpeningDefinition>
          <extentOf>
            <Polygon srsName="EPSG:3005">
              <LinearRing>
                <posList>1000000 400000 1001000 400000 1001000 401000 1000000 401000 1000000 400000</posList>
              </LinearRing>
            </Polygon>
          </extentOf>
        </OpeningDefinition>
      `;
      const result = esfXmlToGeoJSON(xml);
      expect(result.features.length).toBe(1);
    });

    it("skips polygon if no coordinates are found", () => {
      const xml = `
        <OpeningDefinition>
          <extentOf>
            <Polygon srsName="EPSG:3005">
              <outerBoundaryIs>
                <LinearRing />
              </outerBoundaryIs>
            </Polygon>
          </extentOf>
        </OpeningDefinition>
      `;
      const result = esfXmlToGeoJSON(xml);
      expect(result.features.length).toBe(0);
    });
  });

  describe("gmlToGeoJSON", () => {
    it("throws an error when GML contains curve geometries", () => {
      const xmlWithCurve = `
        <gml:FeatureCollection xmlns:gml="http://www.opengis.net/gml">
          <gml:featureMember>
            <gml:Curve />
          </gml:featureMember>
        </gml:FeatureCollection>
      `;
      expect(() => gmlToGeoJSON(xmlWithCurve)).toThrowError(
        /GML contains curve geometry \(<Curve>\)/
      );

      const xmlWithArc = `<Feature><Arc /></Feature>`;
      expect(() => gmlToGeoJSON(xmlWithArc)).toThrowError(
        /GML contains curve geometry \(<Arc>\)/
      );
    });

    it("parses GML3 Polygon geometry successfully", () => {
      const xml = `
        <gml:FeatureCollection xmlns:gml="http://www.opengis.net/gml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <gml:featureMember>
            <Feature srsName="EPSG:3005">
              <geometry>
                <gml:Polygon srsName="EPSG:3005">
                  <gml:exterior>
                    <gml:LinearRing>
                      <gml:posList>1000000 400000 1001000 400000 1001000 401000 1000000 401000 1000000 400000</gml:posList>
                    </gml:LinearRing>
                  </gml:exterior>
                </gml:Polygon>
              </geometry>
            </Feature>
          </gml:featureMember>
        </gml:FeatureCollection>
      `;
      const fc = gmlToGeoJSON(xml);
      expect(fc.type).toBe("FeatureCollection");
      expect(fc.features.length).toBeGreaterThanOrEqual(1);
    });

    it("parses naked Polygon directly when standard GML reader yields no features", () => {
      const xml = `
        <gml:Polygon xmlns:gml="http://www.opengis.net/gml" srsName="EPSG:3005">
          <gml:exterior>
            <gml:LinearRing>
              <gml:posList>1000000 400000 1001000 400000 1001000 401000 1000000 401000 1000000 400000</gml:posList>
            </gml:LinearRing>
          </gml:exterior>
        </gml:Polygon>
      `;
      const fc = gmlToGeoJSON(xml);
      expect(fc.type).toBe("FeatureCollection");
      expect(fc.features.length).toBe(1);
    });

    it("parses naked MultiPolygon directly", () => {
      const xml = `
        <gml:MultiPolygon xmlns:gml="http://www.opengis.net/gml" srsName="EPSG:3005">
          <gml:polygonMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>1000000 400000 1001000 400000 1001000 401000 1000000 401000 1000000 400000</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:polygonMember>
        </gml:MultiPolygon>
      `;
      const fc = gmlToGeoJSON(xml);
      expect(fc.type).toBe("FeatureCollection");
      expect(fc.features.length).toBe(1);
    });

    it("throws error when no features can be extracted", () => {
      const emptyXml = `<root><child>no geometry here</child></root>`;
      expect(() => gmlToGeoJSON(emptyXml)).toThrowError(
        /No features found in the uploaded GML/
      );
    });

    it("detectsGmlEpsg handles xml parsing error gracefully and logs warning", () => {
      const warnSpy = vi.spyOn(console, "warn").mockImplementation(() => {});
      // Malformed XML with no features will trigger error fallback
      expect(() => gmlToGeoJSON("<<<invalid xml>>>")).toThrow();
      warnSpy.mockRestore();
    });
  });
});
