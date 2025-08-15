import { Column, FileUploaderDropContainer, FileUploaderItem, Stack, TextInput } from "@carbon/react";
import { useAuth } from "../../../contexts/AuthProvider";
import { useQuery } from "@tanstack/react-query";
import API from "../../../services/API";
import { formatForestClient } from "../../../utils/ForestClientUtils";
import { useEffect, useRef, useState } from "react";

import { MapContainer, TileLayer, GeoJSON as RLGeoJSON } from "react-leaflet";
import * as L from "leaflet";

import proj4 from "proj4";

import "../styles.scss";
import "./styles.scss";
import { MAX_FILE_SIZE, ACCEPTED_FILE_TYPES } from "./constants";

// ---- Projection setup -------------------------------------------------------
// Define BC Albers once (used by many BC forestry datasets)
const BC_ALBERS_DEF =
  "+proj=aea +lat_1=50 +lat_2=58.5 +lat_0=45 +lon_0=-126 +x_0=1000000 +y_0=0 +datum=NAD83 +units=m +no_defs";

// Common codes we expect; add more as needed
proj4.defs("EPSG:3005", BC_ALBERS_DEF); // BC Albers official
proj4.defs("EPSG:42102", BC_ALBERS_DEF); // Some files declare this; treat as BC Albers

// If a GML has no CRS declared, assume BC Albers by default; change to 'EPSG:4326' if your data is lat/lon
const DEFAULT_GML_EPSG = "EPSG:3005";

// --- ESF (RESULTS) XML helpers ------------------------------------------------
function normalizeEpsg(srsRaw?: string): string {
  if (!srsRaw) return "EPSG:3005";
  const upper = srsRaw.toUpperCase();
  const m = upper.match(/EPSG[:/]*([0-9]+)/);
  return m ? `EPSG:${m[1]}` : upper;
}

function parsePosList(text: string): number[][] {
  // GML posList is a sequence of numbers: x1 y1 x2 y2 ...
  const nums = text
    .trim()
    .split(/\s+/)
    .map((n) => parseFloat(n))
    .filter((n) => Number.isFinite(n));
  const coords: number[][] = [];
  for (let i = 0; i + 1 < nums.length; i += 2) {
    const x = nums[i]!;
    const y = nums[i + 1]!;
    coords.push([x, y]);
  }
  return coords;
}

function parseCoordinates(text: string): number[][] {
  // GML2 coordinates: "x,y x,y x,y" (whitespace-separated pairs)
  const pairs = text.trim().split(/\s+/);
  const coords: number[][] = [];
  for (const p of pairs) {
    const [xs, ys] = p.split(",");
    const x = Number(xs);
    const y = Number(ys);
    if (Number.isFinite(x) && Number.isFinite(y)) coords.push([x, y]);
  }
  return coords;
}

function ensureClosedRing(ring: number[][]): number[][] {
  if (ring.length === 0) return ring;
  const first = ring[0] as number[];
  const last = ring[ring.length - 1] as number[];
  if (first[0] !== last[0] || first[1] !== last[1]) {
    return [...ring, [...first]];
  }
  return ring;
}

function transformRingToWgs84(ring: number[][], srcEpsg: string): number[][] {
  const toWgs84 = proj4(srcEpsg, "EPSG:4326");
  return ring.map(([x, y]) => {
    const out = toWgs84.forward([x, y] as [number, number]) as [number, number];
    const lon = out[0];
    const lat = out[1];
    return [lon, lat];
  });
}

function esfXmlToGeoJSON(xmlText: string): GeoJSON.FeatureCollection {
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
    const epsg = findSrs(poly) || findSrs(poly.closest("gml\\:MultiPolygon, MultiPolygon"));

    // Prefer GML3-style posList, else GML2-style coordinates under outerBoundaryIs
    const posListEl = poly.querySelector(
      "gml\\:exterior gml\\:LinearRing gml\\:posList, gml\\:outerBoundaryIs gml\\:LinearRing gml\\:posList, exterior LinearRing posList, outerBoundaryIs LinearRing posList"
    );
    const coordsEl = poly.querySelector(
      "gml\\:outerBoundaryIs gml\\:LinearRing gml\\:coordinates, outerBoundaryIs LinearRing coordinates"
    );

    let outer: number[][] = [];

    if (posListEl && posListEl.textContent && posListEl.textContent.trim().length) {
      outer = ensureClosedRing(parsePosList(posListEl.textContent));
    } else if (coordsEl && coordsEl.textContent && coordsEl.textContent.trim().length) {
      outer = ensureClosedRing(parseCoordinates(coordsEl.textContent));
    } else {
      // Try fallback: any LinearRing/coordinates inside this polygon
      const anyCoords = poly.querySelector("gml\\:LinearRing gml\\:coordinates, LinearRing coordinates");
      const anyPos = poly.querySelector("gml\\:LinearRing gml\\:posList, LinearRing posList");
      if (anyPos?.textContent?.trim()) {
        outer = ensureClosedRing(parsePosList(anyPos.textContent));
      } else if (anyCoords?.textContent?.trim()) {
        outer = ensureClosedRing(parseCoordinates(anyCoords.textContent));
      }
    }

    if (!outer.length) continue; // nothing usable in this polygon

    // Transform to WGS84 for Leaflet
    const outerWgs = transformRingToWgs84(outer, epsg);

    // Holes: support both GML3 interior/posList and GML2 innerBoundaryIs/coordinates
    const holeEls = Array.from(
      poly.querySelectorAll(
        "gml\\:interior gml\\:LinearRing gml\\:posList, interior LinearRing posList, gml\\:innerBoundaryIs gml\\:LinearRing gml\\:coordinates, innerBoundaryIs LinearRing coordinates"
      )
    );

    const holesWgs: number[][][] = holeEls
      .map((el) => el.textContent?.trim() || "")
      .filter((t) => t.length > 0)
      .map((t) => (t.includes(",") ? parseCoordinates(t) : parsePosList(t)))
      .map((ring) => transformRingToWgs84(ensureClosedRing(ring), epsg));

    const geom: GeoJSON.Polygon = {
      type: "Polygon",
      coordinates: [outerWgs, ...holesWgs],
    };

    polygons.push({ type: "Feature", properties: {}, geometry: geom });
  }

  return { type: "FeatureCollection", features: polygons };
}

type GeoJSONFC = GeoJSON.FeatureCollection;

const FileUpload = () => {
  const { user, selectedClient } = useAuth();

  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [geojson, setGeojson] = useState<GeoJSONFC | null>(null);
  const [mapReady, setMapReady] = useState(false);

  const mapRef = useRef<L.Map | null>(null);

  const validate = (f: File) => {
    if (f.size > MAX_FILE_SIZE) return `"${f.name}" exceeds 25 MB.`;
    const lc = f.name.toLowerCase();
    const byExt = [".geojson", ".gml", ".xml", ".json"].some((ext) => lc.endsWith(ext));
    if (!byExt)
      return "File not supported. Please upload a valid file type: GeoJSON, GML, XML.";
    return null;
  };

  const userClientQuery = useQuery({
    queryKey: ["forest-clients", "search", user?.associatedClients],
    queryFn: () =>
      API.ForestClientEndpointService.searchByClientNumbers(
        user!.associatedClients,
        0,
        user!.associatedClients.length
      ),
    enabled: !!user?.associatedClients.length,
    select: (data) => data.find((client) => client.clientNumber === selectedClient),
  });

  // Convert uploaded file to GeoJSON FeatureCollection in EPSG:4326 for Leaflet
  const parseToGeoJSON = async (f: File): Promise<GeoJSONFC> => {
    const text = await f.text();
    const name = f.name.toLowerCase();

    // Already GeoJSON / JSON
    if (name.endsWith(".geojson") || name.endsWith(".json")) {
      const obj = JSON.parse(text);
      if (obj.type === "FeatureCollection") return obj as GeoJSONFC;
      if (obj.type === "Feature") return { type: "FeatureCollection", features: [obj] } as GeoJSONFC;
      if (obj.type && (obj as any).coordinates) {
        return { type: "FeatureCollection", features: [{ type: "Feature", properties: {}, geometry: obj as any }] } as GeoJSONFC;
      }
      throw new Error("JSON is not valid GeoJSON.");
    }

    // ESF XML / GML wrapped inside ESF → extract polygons and reproject to WGS84
    const fc = esfXmlToGeoJSON(text);
    if (!fc.features.length) {
      throw new Error("No features found in the uploaded GML/XML.");
    }
    return fc as GeoJSONFC;
  };

  const handleAddFile = async (addedFiles: File[]) => {
    setError(null);
    setGeojson(null);
    if (!addedFiles?.length) return;

    const f = addedFiles[0];
    if (!f) return;

    const err = validate(f);
    if (err) {
      setFile(null);
      setError(err);
      return;
    }

    try {
      const fc = await parseToGeoJSON(f);
      setFile(f);
      setGeojson(fc);
    } catch (e: any) {
      setFile(null);
      setGeojson(null);
      setError(e?.message || "Failed to parse file. Please upload a valid GeoJSON or GML/XML file.");
    }
  };

  const handleFileDelete = () => {
    setFile(null);
    setError(null);
    setGeojson(null);
  };

  // Fit map to uploaded geometry when it changes and the map is ready
  useEffect(() => {
    if (!geojson || !mapRef.current || !mapReady) return;
    const temp = L.geoJSON(geojson);
    const bounds = temp.getBounds();
    // Debug: uncomment if you need to verify
    // console.log('fitBounds:', bounds?.toBBoxString(), 'valid:', bounds?.isValid());
    if (bounds && bounds.isValid()) {
      mapRef.current.fitBounds(bounds, { padding: [16, 16] });
    }
    temp.remove();
  }, [geojson, mapReady]);

  // A11y ids to associate label and helper text with the drop container
  const labelId = "opening-map-label";
  const helpId = "opening-map-help";

  return (
    <>
      <Column sm={4} md={8} lg={16}>
        <h2 className="create-opening-step-title">File Upload</h2>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <TextInput
          labelText="Client"
          id="selected-client"
          readOnly
          required
          defaultValue={formatForestClient(userClientQuery.data)}
        />
      </Column>

      <Column sm={4} md={8} lg={16}>
        <Stack gap={5} className="file-uploader-container">
          <div className="file-uploader-title">
            <label id={labelId} className="default-label" htmlFor="opening-map-file-drop-container">
              Upload opening map geometry
            </label>
            <p id={helpId} className="file-type-p">
              Acceptable file types: GeoJSON, GML, XML
            </p>
          </div>

          <FileUploaderDropContainer
            id="opening-map-file-drop-container"
            accept={ACCEPTED_FILE_TYPES}
            multiple={false}
            labelText="Click to upload or drag and drop the map file here (max 25 MB)"
            onAddFiles={(_evt, { addedFiles }) => handleAddFile(addedFiles)}
            aria-labelledby={labelId}
            aria-describedby={helpId}
          />

          {(file || error) && (
            <FileUploaderItem
              id="opening-file-uploader-item"
              name={file?.name ?? "Invalid file"}
              status="edit"
              onDelete={handleFileDelete}
              invalid={!!error}
              errorSubject={error ?? undefined}
            />
          )}

          {/* Map preview (no extra controls) */}
          {geojson && (
            <div className="map-preview" style={{ height: 400 }}>
              <MapContainer
                ref={mapRef}
                style={{ height: "100%", width: "100%" }}
                center={[49.25, -123.1]} // fallback center
                zoom={6}
                zoomControl={false} // no buttons
                attributionControl={false}
                whenReady={() => setMapReady(true)}
              >
                <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                <RLGeoJSON data={geojson} />
              </MapContainer>
            </div>
          )}
        </Stack>
      </Column>
    </>
  );
};

export default FileUpload;
