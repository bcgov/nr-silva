import React, { useEffect, useRef, useState } from 'react';
import { OpeningPolygon } from '../../types/OpeningPolygon';
import { createPopupFromProps, getOpeningsPolygonFromWfs } from '../../map-services/BcGwWfsApi';
import { BaseMapLayer, MapLayer } from '../../types/MapLayer';
import { allBaseMaps, allLayers } from './constants';
import axios from 'axios';
import { getAuthIdToken } from '../../services/AuthService';
import { env } from '../../env';
import { shiftBcGwLngLat2LatLng } from '../../map-services/BcGwLatLongUtils';
import { LayersControl, MapContainer, Polygon, Popup, TileLayer, useMap, WMSTileLayer } from 'react-leaflet';
import { LatLngExpression } from 'leaflet';

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
  const [position, setPosition] = useState<LatLngExpression>([48.43737, -123.35883]);
  const [reloadMap, setReloadMap] = useState<boolean>(false);
  const [layers, setLayers] = useState<MapLayer[]>([]);
  const [baseMaps, setBaseMaps] = useState<BaseMapLayer[]>([]);
  const authToken = getAuthIdToken();

  const resultsStyle = {
    color: 'black'
  };

  const getOpeningPolygonAndProps = async (openingId: number | null): Promise<OpeningPolygon | null> => {
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
      const opening: OpeningPolygon | null = await getOpeningPolygonAndProps(openingId);
      if (opening) {
        setOpenings([opening]);

        const positionLatLng: LatLngExpression = [opening.positionLat, opening.positionLong];
        setPosition(positionLatLng);
      } else {
        setOpeningPolygonNotFound(true);
      }
      setReloadMap(true);
    };

    if (openingId) {
      callBcGwApi();
    } else if (openingId === 0) {
      setOpenings([]);
      setReloadMap(true);
    }

    const filtered = allLayers.filter(l => l.name.length > 0);
    if (filtered.length) {
      setLayers(filtered);
    }

    setBaseMaps(allBaseMaps);
  }, [openingId]);

  useEffect(() => {}, [openings, reloadMap]);

  const RecenterAutomatically = ({latLong}: {latLong: LatLngExpression}) => {
    const map = useMap();
    const zoom = 13;
    useEffect(() => {
      map.setView(latLong, zoom);
    }, [latLong]);
    return null;
  };

  return (
    <MapContainer
      center={position}
      zoom={13}
      style={{ height: "400px", width: "100%" }}
    >
      <TileLayer
        url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}"
        attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community"
        zIndex={-10000}
      />
      {/* Add base maps, if any 
      <LayersControl position="bottomright">
        {baseMaps.map((base: BaseMapLayer) => (
          <LayersControl.BaseLayer key={base.name} checked={base.default} name={base.name}>
            <TileLayer
              url={base.url}
              attribution={base.attribution}
              zIndex={-10000}
            />
          </LayersControl.BaseLayer>
        ))}
      </LayersControl>
      */}

      {/* Display Opening polygons, if any */}
      {openings.length ? (
        openings.map((opening: OpeningPolygon) => (
          <Polygon
            key={opening.key}
            positions={opening.bounds}
            pathOptions={resultsStyle}  
          >
            <Popup maxWidth={700}>
              {opening.popup}
            </Popup>
          </Polygon>
        ))
      ) : null }
      
      {/* Centers the map automatically when a different opening get selected. */}
      {position && (
        <RecenterAutomatically latLong={position} />
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
                styles: layer.styles.map(s => s.name).join(',')
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
