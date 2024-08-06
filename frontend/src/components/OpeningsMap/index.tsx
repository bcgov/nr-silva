import React, { useEffect, useRef, useState } from 'react';
import { useMap } from 'react-leaflet';
import { MapContainer, TileLayer, Marker, Popup, Rectangle } from 'react-leaflet';
import { OpeningPolygon } from '../../types/OpeningPolygon';
import { createPopupFromProps, getOpeningsPolygonFromWfs } from '../../map-services/BcGwWfsApi';
import { LayersControl } from 'react-leaflet';
import { MapLayer } from '../../types/MapLayer';
import { WMSTileLayer } from 'react-leaflet';
import { allLayers } from './constants';
import { Polygon } from 'react-leaflet';
import axios from 'axios';
import { getAuthIdToken } from '../../services/AuthService';
import { env } from '../../env';
import { shiftBcGwLngLat2LatLng } from '../../map-services/BcGwLatLongUtils';

const backendUrl = env.VITE_BACKEND_URL;

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
  const [layers, setLayers] = useState<MapLayer[]>(allLayers);
  const authToken = getAuthIdToken();

  const resultsStyle = {
    color: 'black'
  };

  const getPolygonTest = async (openingId: number | null): Promise<OpeningPolygon | null> => {
    const urlApi = `/api/feature-service/polygon-and-props/${openingId}`;
    const response = await axios.get(backendUrl.concat(urlApi), {
      headers: {
        Authorization: `Bearer ${authToken}`
        }
    });

    const { data } = response;

    if (data.features && data.features.length) {
      const openingsList: OpeningPolygon[] = [];
      for (let i = 0, len = data.features.length; i < len; i++) {
        if (data.features[i].geometry) {
          const openingGeometry = shiftBcGwLngLat2LatLng(data.features[i].geometry.coordinates);

          const openingObj: OpeningPolygon = {
            key: data.features[i].id,
            bounds: openingGeometry,
            properties: data.features[i].properties,
            id: data.features[i].id,
            positionLat: (data.bbox[1] + data.bbox[3]) / 2,
            positionLong: (data.bbox[0] + data.bbox[2]) / 2,
            popup: createPopupFromProps(data.features[i].properties)
          };
          openingsList.push(openingObj);
        }
      }

      // Handling arrays to allow querying more than 1 at once.
      if (openingsList.length) {
        return openingsList[0];
      }
    }

    return null;
  };

  useEffect(() => {
    setOpeningPolygonNotFound(false);

    const callBcGwApi = async () => {
      const openingGeom: OpeningPolygon | null = await getPolygonTest(openingId);
      if (openingGeom) {
        setOpenings([openingGeom]);
        setPosition([openingGeom.positionLat, openingGeom.positionLong]);
        setReloadMap(true);
      } else {
        setOpeningPolygonNotFound(true);
      }
    };

    if (openingId) {
      callBcGwApi();
    }
  }, [openingId]);

  useEffect(() => {
    //
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
      
      {/* Centers the map autimatically when a different opening get selected. */}
      {position && (
        <RecenterAutomatically lat={position[0]} long={position[1]} />
      )}

      {/* Default layers */}
      {layers.length && (
        <LayersControl position="topright">
          {layers.map((layer: MapLayer) => (
            <LayersControl.Overlay key={layer.name} name={layer.name}>
            <WMSTileLayer
              url="https://openmaps.gov.bc.ca/geo/ows"
              params={{
                format: layer.format,
                layers: layer.layers,
                transparent: layer.transparent,
                styles: layer.styles
              }}
            />
          </LayersControl.Overlay>
          ))}
        </LayersControl>
      )}

    </MapContainer>
  );
};

export default OpeningsMap;
