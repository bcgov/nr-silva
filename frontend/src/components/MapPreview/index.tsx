import { useEffect } from "react";

import { MapContainer, TileLayer, GeoJSON as RLGeoJSON, useMap } from "react-leaflet";
import { geoJSON } from "leaflet";

type MapPreviewProps = {
  geojson?: GeoJSON.FeatureCollection | null
}

/** Fits the map to the geojson bounds. Lives inside MapContainer so useMap() is always valid. */
const FitBoundsLayer = ({ geojson }: { geojson: GeoJSON.FeatureCollection }) => {
  const map = useMap();
  useEffect(() => {
    // Omit non-RFC7946 crs from individual geometries; Leaflet assumes WGS84
    const normalized = {
      ...geojson,
      features: geojson.features.map((f) => {
        const { crs: _crs, ...geo } = f.geometry as GeoJSON.Geometry & { crs?: unknown };
        return { ...f, geometry: geo as GeoJSON.Geometry };
      }),
    };
    const temp = geoJSON(normalized);
    const bounds = temp.getBounds();
    if (bounds?.isValid()) map.fitBounds(bounds, { padding: [24, 24] });
    temp.remove();
  }, [map, geojson]);
  return null;
};

const MapPreview = ({ geojson }: MapPreviewProps) => {

  if (!geojson) return null;

  return (
    <div className="map-preview" style={{ height: 294 }}>
      <MapContainer
        style={{ height: "100%", width: "100%" }}
        center={[49.25, -123.1]}
        zoom={6}
        zoomControl
        scrollWheelZoom={false}
      >
        <TileLayer
          url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}"
          attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community"
          zIndex={-10000}
        />
        <RLGeoJSON data={geojson} />
        <FitBoundsLayer geojson={geojson} />
      </MapContainer>
    </div>
  )
}

export default MapPreview;
