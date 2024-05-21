import React, { useEffect, useRef, useState } from 'react';
import { useMap } from 'react-leaflet';
import { Polygon } from 'react-leaflet';
import { MapContainer, TileLayer, Marker, Popup, Rectangle } from 'react-leaflet';
import { OpeningPolygon } from '../../types/OpeningPolygon';
import { getInitialLayers, getOpeningsPolygonFromWfs } from '../../map-services/BcGwWfsApi';
import { LayersControl } from 'react-leaflet';
import { MapLayer } from '../../types/MapLayer';
import { Polyline } from 'react-leaflet';

interface MapProps {
  openingId: number | null;
  setOpeningPolygonNotFound: Function;
}

const OpeningsMap: React.FC<MapProps> = ({
  openingId,
  setOpeningPolygonNotFound,
}) => {
  const lastClickedLayerRef = useRef<any>(null); // Replace 'any' with the specific type if known
  const [openings, setOpenings] = useState<OpeningPolygon[]>([]);
  const [position, setPosition] = useState<number[]>([48.43737, -123.35883]);
  const [reloadMap, setReloadMap] = useState<boolean>(false);
  const [layers, setLayers] = useState<MapLayer[]>([]);

  const resultsStyle = {
    color: 'black'
  };

  useEffect(() => {
    setOpeningPolygonNotFound(false);

    const callBcGwApi = async () => {
      const openingGeom: OpeningPolygon | null = await getOpeningsPolygonFromWfs(openingId);
      if (openingGeom) {
        setOpenings([openingGeom]);
        setPosition([openingGeom.positionLat, openingGeom.positionLong]);
        setReloadMap(true);
      } else {
        setOpeningPolygonNotFound(true);
      }
    };

    const getDefaultLayers = async () => {
      const layer: MapLayer | null = await getInitialLayers();
      if (layer) {
        setLayers([layer]);
      }
    };

    getDefaultLayers();

    if (openingId) {
      callBcGwApi();
    }
  }, [openingId]);

  useEffect(() => {
    console.log('Reloading map..position (bounding box)', position);
  }, [openings, reloadMap]);

  const RecenterAutomatically = ({lat, long}:{lat: number, long:number}) => {
    const map = useMap();
    useEffect(() => {
      map.setView([lat, long]);
    }, [lat,long]);
    return null;
  };

  const { BaseLayer } = LayersControl;

  return (
    <MapContainer
      center={position}
      zoom={13}
      style={{ height: "400px", width: "100%" }}
    >
      <LayersControl position="bottomleft">
        <BaseLayer checked name="OpenStreetMap">
          <TileLayer
            url={"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"}
            attribution="&copy; <a href=&quot;https://www.openstreetmap.org/copyright&quot;>OpenStreetMap</a>"
          />
        </BaseLayer>
        <BaseLayer name="Google Maps Satelite">
          <TileLayer
            url={"https://www.google.ca/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}"}
            attribution="&copy; Google Maps"
          />
        </BaseLayer>
      </LayersControl>
      

      {/* Display Opening polygons, if any */}
      {openings.length ? (
        openings.map((opening) => (
          <Polygon
            key={opening.key}
            positions={opening.bounds}
            pathOptions={resultsStyle}  
          >
            <Popup maxWidth="700">
              {opening.popup}
            </Popup>
          </Polygon>
        ))
      ) : null }

      {/* Display a simple marker if no Opening */}
      {openings.length === 0 && (
        <Marker position={position}>
          <Popup>
            Caffe Fantastico<br />965 Kings Rd, Victoria, BC
          </Popup>
        </Marker>
      )}

      {/* Centers the map autimatically when a different opening get selected. */}
      {position && (
        <RecenterAutomatically lat={position[0]} long={position[1]} />
      )}

      {/* Add layers and Layer controls */}
      {layers.length > 0 && (
        <LayersControl position="topright">
          {layers.map((layer) => (
            <LayersControl.Overlay key={layer.key} name={layer.name}>
              {layer.popup && (
                <Popup>{layer.popup}</Popup>
              )}
              <Polyline pathOptions={layer.pathOptions} positions={layer.bounds} />
            </LayersControl.Overlay>
          ))}
        </LayersControl>
      )}
      
    </MapContainer>
  );
};

export default OpeningsMap;
