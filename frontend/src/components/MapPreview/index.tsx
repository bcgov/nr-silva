import { useEffect, useRef, useState } from "react";

import { MapContainer, TileLayer, GeoJSON as RLGeoJSON } from "react-leaflet";
import * as L from "leaflet";

type MapPreviewProps = {
  geojson: GeoJSON.FeatureCollection
}

const MapPreview = ({ geojson }: MapPreviewProps) => {
  const [mapReady, setMapReady] = useState<boolean>(false);
  const mapRef = useRef<L.Map | null>(null);

  // Fit map to uploaded geometry when it changes and the map is ready
  useEffect(() => {
    if (!geojson || !mapRef.current || !mapReady) return;
    const temp = L.geoJSON(geojson);
    const bounds = temp.getBounds();
    if (bounds && bounds.isValid()) {
      mapRef.current.fitBounds(bounds, { padding: [24, 24] });
    }
    temp.remove();
  }, [geojson, mapReady]);

  return (
    <div className="map-preview" style={{ height: 294 }}>
      <MapContainer
        ref={mapRef}
        style={{ height: "100%", width: "100%" }}
        center={[49.25, -123.1]}
        zoom={6}
        zoomControl={false}
        whenReady={() => setMapReady(true)}
      >
        <TileLayer
          url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}"
          attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community"
          zIndex={-10000}
        />
        <RLGeoJSON data={geojson} />
      </MapContainer>
    </div>
  )
}

export default MapPreview;
