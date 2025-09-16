import proj4 from "proj4";
import GeoJSONFormat from "ol/format/GeoJSON";
import GML2 from "ol/format/GML2";
import GML3 from "ol/format/GML3";
import { LinearRing } from "ol/geom";

/**
 * PROJ.4 definition for BC Albers (EPSG:3005).
 */
const BC_ALBERS_DEF =
  "+proj=aea +lat_1=50 +lat_2=58.5 +lat_0=45 +lon_0=-126 +x_0=1000000 +y_0=0 +datum=NAD83 +units=m +no_defs";

/** Register EPSG:3005 and EPSG:42102 as BC Albers so proj4 can reproject. */
proj4.defs("EPSG:3005", BC_ALBERS_DEF);
proj4.defs("EPSG:42102", BC_ALBERS_DEF);

/** Default CRS to assume when a GML geometry has no srsName. */
const DEFAULT_GML_EPSG = "EPSG:3005";

/**
 * Attempt to detect the EPSG code from a GML string by looking for the nearest srsName.
 * Falls back to DEFAULT_GML_EPSG when not found.
 */
const detectGmlEpsg = (xmlText: string): string => {
  try {
    const doc = new DOMParser().parseFromString(xmlText, "application/xml");
    const elWithSrs = doc.querySelector("[srsName]");
    if (elWithSrs) {
      const srs = elWithSrs.getAttribute("srsName") || undefined;
      return normalizeEpsg(srs);
    }
  } catch (err) {
    // ignore parsing errors and use default
    console.warn("Failed to parse GML for EPSG detection:", err, { xmlText });
  }
  return DEFAULT_GML_EPSG;
};

/**
 * Normalize a CRS string to the canonical form "EPSG:####".
 * Accepts variants like "EPSG/3005" or URNs (e.g., "urn:ogc:def:crs:EPSG::3005").
 * @param srsRaw Raw CRS value from XML (e.g., srsName).
 * @returns Canonical code like "EPSG:3005" (or the uppercased input if not recognized).
 */
export const normalizeEpsg = (srsRaw?: string): string => {
  if (!srsRaw) return "EPSG:3005";
  const upper = srsRaw.toUpperCase();
  const m = upper.match(/EPSG[:/]*([0-9]+)/);
  return m ? `EPSG:${m[1]}` : upper;
};

/**
 * Parse a GML3 posList string (e.g., "x1 y1 x2 y2 …") into an array of [x,y] pairs.
 * @param text Space-separated coordinate numbers.
 * @returns Array of coordinate pairs in source projection order.
 */
export const parsePosList = (text: string): [number, number][] => {
  const nums = text
    .trim()
    .split(/\s+/)
    .map((n) => parseFloat(n))
    .filter((n) => Number.isFinite(n));

  const coords: [number, number][] = [];
  for (let i = 0; i + 1 < nums.length; i += 2) {
    const x = nums[i];
    const y = nums[i + 1];
    if (x !== undefined && y !== undefined) {
      coords.push([x, y]);
    }
  }
  return coords;
};

/**
 * Parse a GML2 coordinates string (e.g., "x1,y1 x2,y2 …") into an array of [x,y] pairs.
 * @param text Whitespace-separated "x,y" tuples.
 * @returns Array of coordinate pairs in source projection order.
 */
export const parseCoordinates = (text: string): [number, number][] => {
  const pairs = text.trim().split(/\s+/);
  const coords: [number, number][] = [];
  for (const p of pairs) {
    const [xs, ys] = p.split(",");
    const x = Number(xs);
    const y = Number(ys);
    if (Number.isFinite(x) && Number.isFinite(y)) coords.push([x, y]);
  }
  return coords;
};

/**
 * Ensure a linear ring is closed using OpenLayers' geometry.
 * @param ring Coordinates of a ring as [[x,y], …].
 * @returns A closed ring (OL guarantees first === last).
 */
export const ensureClosedRing = (ring: [number, number][]): [number, number][] => {
  if (!ring || ring.length === 0) return ring;
  const lr = new LinearRing(ring);
  // getCoordinates() returns a closed ring (first point repeated at the end)
  return lr.getCoordinates() as [number, number][];
};

/**
 * Reproject a ring from a source CRS to WGS84 (EPSG:4326) for Leaflet/GeoJSON.
 * @param ring Coordinates in the source CRS.
 * @param srcEpsg Canonical CRS code, e.g., "EPSG:3005".
 * @returns Ring coordinates as [lon, lat] pairs in WGS84.
 */
export const transformRingToWgs84 = (ring: number[][], srcEpsg: string): number[][] => {
  const toWgs84 = proj4(srcEpsg, "EPSG:4326");
  return ring.map(([x, y]) => {
    const out = toWgs84.forward([x, y] as [number, number]) as [number, number];
    const lon = out[0];
    const lat = out[1];
    return [lon, lat];
  });
};

/**
 * Extract polygons from an ESF/RESULTS XML payload containing GML geometries and
 * convert them to a GeoJSON FeatureCollection in WGS84.
 * Supports GML3 (posList) and GML2 (coordinates), outer rings and holes, and
 * inherits srsName from enclosing elements when present; falls back to EPSG:3005.
 * @param xmlText ESF submission XML as a string.
 * @returns GeoJSON FeatureCollection of Polygon features in EPSG:4326.
 */
export const esfXmlToGeoJSON = (xmlText: string): GeoJSON.FeatureCollection => {
  const doc = new DOMParser().parseFromString(xmlText, "application/xml");

  // Helper to find nearest srsName up the tree
  const findSrs = (el: Element | null): string => {
    let cur: Element | null = el;
    while (cur) {
      const srs = cur.getAttribute("srsName");
      if (srs) return normalizeEpsg(srs);
      cur = cur.parentElement;
    }
    return DEFAULT_GML_EPSG; // default BC Albers
  };

  const polygons: GeoJSON.Feature[] = [];
  const polygonElems = Array.from(
    doc.querySelectorAll("gml\\:Polygon, Polygon")
  );

  for (const poly of polygonElems) {
    // Find the coordinate reference system (CRS) for this polygon by searching for srsName attribute,
    // first on the polygon element itself, then on its closest MultiPolygon ancestor if any.
    const epsg = findSrs(poly) || findSrs(poly.closest("gml\\:MultiPolygon, MultiPolygon"));

    // Attempt to find the outer boundary coordinates using GML3 posList elements inside exterior or outerBoundaryIs
    const posListEl = poly.querySelector(
      "gml\\:exterior gml\\:LinearRing gml\\:posList, gml\\:outerBoundaryIs gml\\:LinearRing gml\\:posList, exterior LinearRing posList, outerBoundaryIs LinearRing posList"
    );
    // Alternatively, try to find GML2 coordinates elements inside outerBoundaryIs
    const coordsEl = poly.querySelector(
      "gml\\:outerBoundaryIs gml\\:LinearRing gml\\:coordinates, outerBoundaryIs LinearRing coordinates"
    );

    let outer: number[][] = [];

    if (posListEl && posListEl.textContent && posListEl.textContent.trim().length) {
      // If GML3 posList found, parse it into coordinate pairs and ensure the ring is closed.
      outer = ensureClosedRing(parsePosList(posListEl.textContent));
    } else if (coordsEl && coordsEl.textContent && coordsEl.textContent.trim().length) {
      // Else if GML2 coordinates found, parse and close the ring similarly.
      outer = ensureClosedRing(parseCoordinates(coordsEl.textContent));
    } else {
      // Fallback: look for any LinearRing/coordinates or LinearRing/posList inside this polygon,
      // in case the structure is different or missing the usual outerBoundaryIs/exterior wrappers.
      const anyCoords = poly.querySelector("gml\\:LinearRing gml\\:coordinates, LinearRing coordinates");
      const anyPos = poly.querySelector("gml\\:LinearRing gml\\:posList, LinearRing posList");
      if (anyPos?.textContent?.trim()) {
        outer = ensureClosedRing(parsePosList(anyPos.textContent));
      } else if (anyCoords?.textContent?.trim()) {
        outer = ensureClosedRing(parseCoordinates(anyCoords.textContent));
      }
    }

    // Skip this polygon if no valid outer ring coordinates were found.
    if (!outer.length) continue; // nothing usable in this polygon

    // Reproject the outer ring coordinates from source CRS to WGS84 (EPSG:4326) for Leaflet/GeoJSON.
    const outerWgs = transformRingToWgs84(outer, epsg);

    // Now handle holes (interior rings) inside the polygon.
    // Holes can be defined in GML3 as interior/LinearRing/posList or in GML2 as innerBoundaryIs/LinearRing/coordinates.
    // We select all such hole elements to process them.
    const holeEls = Array.from(
      poly.querySelectorAll(
        "gml\\:interior gml\\:LinearRing gml\\:posList, interior LinearRing posList, gml\\:innerBoundaryIs gml\\:LinearRing gml\\:coordinates, innerBoundaryIs LinearRing coordinates"
      )
    );

    // For each hole element:
    // 1. Extract the text content (coordinate strings).
    // 2. Determine if it's using GML2 (comma-separated) or GML3 (space-separated) format.
    // 3. Parse the coordinates accordingly.
    // 4. Ensure the hole ring is closed.
    // 5. Reproject the hole coordinates to WGS84.
    const holesWgs: number[][][] = holeEls
      .map((el) => el.textContent?.trim() || "")
      .filter((t) => t.length > 0)
      .map((t) => (t.includes(",") ? parseCoordinates(t) : parsePosList(t)))
      .map((ring) => transformRingToWgs84(ensureClosedRing(ring), epsg));

    // Construct a GeoJSON Polygon geometry with the outer ring and any holes.
    const geom: GeoJSON.Polygon = {
      type: "Polygon",
      coordinates: [outerWgs, ...holesWgs],
    };

    // Add the polygon feature with empty properties to the output array.
    polygons.push({ type: "Feature", properties: {}, geometry: geom });
  }

  // Return the complete FeatureCollection of all extracted polygons.
  return { type: "FeatureCollection", features: polygons };
};


/**
 * Converts a GML string to a GeoJSON FeatureCollection.
 * Attempts to parse using GML3 and GML2 formats and reprojects coordinates to EPSG:4326.
 *
 * @param xmlText - GML XML string content.
 * @returns A GeoJSON FeatureCollection representing the parsed geometries.
 */
export const gmlToGeoJSON = (xmlText: string): GeoJSON.FeatureCollection => {
  const dataProjection = detectGmlEpsg(xmlText);
  const featureProjection = "EPSG:4326"; // Leaflet/GeoJSON expects WGS84 lon/lat

  // Try GML3 first, then fall back to GML2
  let features = [] as any[];
  try {
    const gml3 = new GML3();
    features = gml3.readFeatures(xmlText, { dataProjection, featureProjection });
  } catch (_) {
    // ignore and try GML2
  }

  if (!features || features.length === 0) {
    const gml2 = new GML2();
    try {
      features = gml2.readFeatures(xmlText, { dataProjection, featureProjection });
    } catch (_) {
      // no-op; will handle empty features below
    }
  }

  if (!features || features.length === 0) {
    throw new Error("No features found in the uploaded GML.");
  }

  const geojsonFormat = new GeoJSONFormat();
  const fcObj = geojsonFormat.writeFeaturesObject(features, {
    featureProjection,
    dataProjection,
  }) as GeoJSON.FeatureCollection;

  // Ensure we always return a FeatureCollection
  if (!fcObj || fcObj.type !== "FeatureCollection") {
    const featuresArr = Array.isArray((fcObj as any).features) ? (fcObj as any).features : [];
    return { type: "FeatureCollection", features: featuresArr } as GeoJSON.FeatureCollection;
  }

  return fcObj as GeoJSON.FeatureCollection;
};


/**
 * Parses geospatial input data (e.g., GML/XML or ESF wrapper) into a GeoJSON FeatureCollection.
 *
 * @param f - file uploaded by user.
 * @returns A valid GeoJSON FeatureCollection representing the parsed geometries and properties.
 *
 * @remarks
 * - Handles traversal into ESF wrappers to extract embedded GML.
 * - Supports converting MultiPolygon, Polygon, and LinearRing coordinates into GeoJSON format.
 * - Filters out invalid or empty geometries.
 * - Returned GeoJSON can be fed directly into Leaflet, Mapbox, or other libraries supporting GeoJSON.
 *
 * @example
 * ```ts
 * const geojson = parseToGeoJSON(file);
 * L.geoJSON(geojson).addTo(map);
 * ```
 */
export const parseToGeoJSON = async (f: File): Promise<GeoJSON.FeatureCollection> => {
  const text = await f.text();
  const name = f.name.toLowerCase();

  // Helper: validate geometry type is a simple feature
  const isSimpleGeometryType = (type: string) => [
    "Point",
    "MultiPoint",
    "LineString",
    "MultiLineString",
    "Polygon",
    "MultiPolygon"
  ].includes(type);

  // Already GeoJSON / JSON
  if (name.endsWith(".geojson") || name.endsWith(".json")) {
    const obj = JSON.parse(text);
    let fc: GeoJSON.FeatureCollection;
    if (obj.type === "FeatureCollection") {
      fc = obj as GeoJSON.FeatureCollection;
    } else if (obj.type === "Feature") {
      fc = { type: "FeatureCollection", features: [obj] } as GeoJSON.FeatureCollection;
    } else if (obj.type && obj.coordinates) {
      fc = { type: "FeatureCollection", features: [{ type: "Feature", properties: {}, geometry: obj }] } as GeoJSON.FeatureCollection;
    } else {
      throw new Error("JSON is not valid GeoJSON.");
    }
    // Validate all geometry types
    for (const [i, feature] of fc.features.entries()) {
      if (!feature.geometry || !feature.geometry.type) {
        throw new Error(`Feature ${i + 1} missing geometry or geometry type.`);
      }
      if (!isSimpleGeometryType(feature.geometry.type)) {
        throw new Error(`Feature ${i + 1} geometry type '${feature.geometry.type}' is not a simple feature (no curves allowed).`);
      }
    }
    return fc;
  }

  if (name.endsWith(".gml")) {
    return gmlToGeoJSON(text);
  }

  // ESF XML / GML wrapped inside ESF → extract polygons and reproject to WGS84
  const fc = esfXmlToGeoJSON(text);
  if (!fc.features.length) {
    throw new Error("No features found in the uploaded GML/XML.");
  }
  return fc as GeoJSON.FeatureCollection;
};
