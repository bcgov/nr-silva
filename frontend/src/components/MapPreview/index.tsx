import { useEffect, useState } from "react";
import { Button } from "@carbon/react";
import { MapContainer, TileLayer, GeoJSON as RLGeoJSON, useMap, ZoomControl } from "react-leaflet";
import { geoJSON } from "leaflet";
import { openingStyle, activityStyle } from "@/types/MapLayer";

import "./styles.scss";

export type MapPreviewType = 'opening' | 'activity';

type MapPreviewProps = {
  geojson?: GeoJSON.FeatureCollection | null;
  openingGeojson?: GeoJSON.FeatureCollection | null;
  mapType?: MapPreviewType;
};

/** Fits the map to the geojson bounds whenever the geojson changes. */
const FitBoundsLayer = ({ geojson }: { geojson: GeoJSON.FeatureCollection }) => {
  const map = useMap();
  useEffect(() => {
    // Omit non-RFC7946 crs from individual geometries; Leaflet assumes WGS84
    const normalized = {
      ...geojson,
      features: (geojson.features ?? []).map((f) => {
        const { crs: _crs, ...geo } = f.geometry as GeoJSON.Geometry & { crs?: unknown };
        return { ...f, geometry: geo as GeoJSON.Geometry };
      }),
    };
    const temp = geoJSON(normalized);
    const bounds = temp.getBounds();
    if (bounds?.isValid()) {
      map.fitBounds(bounds, { padding: [24, 24] });
    }
    temp.remove();
  }, [map, geojson]);
  return null;
};

const MapPreview = ({
  geojson,
  openingGeojson,
  mapType = 'opening',
}: MapPreviewProps) => {
  const [isHoldingOpening, setIsHoldingOpening] = useState(false);

  if (!geojson) return null;

  const hasOpeningSpatial = Boolean(openingGeojson?.features && openingGeojson.features.length > 0);
  const showHoldButton = mapType === 'activity' && hasOpeningSpatial;

  // Validated activity geometry lies within the opening, so fit to the opening when it has spatial data.
  const fitGeojson = hasOpeningSpatial && openingGeojson ? openingGeojson : geojson;

  const activeGeojson = isHoldingOpening && openingGeojson ? openingGeojson : geojson;
  const activeStyle = isHoldingOpening
    ? openingStyle
    : mapType === 'activity'
      ? activityStyle
      : openingStyle;

  const startHold = () => setIsHoldingOpening(true);
  const stopHold = () => setIsHoldingOpening(false);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === ' ' || e.key === 'Enter') {
      e.preventDefault();
      startHold();
    }
  };

  const handleKeyUp = (e: React.KeyboardEvent) => {
    if (e.key === ' ' || e.key === 'Enter') {
      e.preventDefault();
      stopHold();
    }
  };

  return (
    <div className="map-preview">
      {showHoldButton && (
        <div className="map-preview__overlay-button">
          <Button
            size="sm"
            kind="secondary"
            className="map-preview__hold-btn"
            onMouseDown={startHold}
            onMouseUp={stopHold}
            onMouseLeave={stopHold}
            onTouchStart={startHold}
            onTouchEnd={stopHold}
            onTouchCancel={stopHold}
            onKeyDown={handleKeyDown}
            onKeyUp={handleKeyUp}
            onBlur={stopHold}
            onContextMenu={(e) => e.preventDefault()}
          >
            {isHoldingOpening ? "Showing opening spatial" : "Hold to show opening spatial"}
          </Button>
        </div>
      )}
      <MapContainer
        style={{ height: "100%", width: "100%" }}
        center={[49.25, -123.1]}
        zoom={6}
        zoomControl={false}
        scrollWheelZoom={false}
      >
        <ZoomControl position="bottomright" />
        <TileLayer
          url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}"
          attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community"
          zIndex={-10000}
        />
        <RLGeoJSON
          key={isHoldingOpening ? "opening-held" : `${mapType}-${activeGeojson?.features?.length ?? 0}`}
          data={activeGeojson}
          style={activeStyle}
        />
        <FitBoundsLayer geojson={fitGeojson} />
      </MapContainer>
    </div>
  );
};

export default MapPreview;
