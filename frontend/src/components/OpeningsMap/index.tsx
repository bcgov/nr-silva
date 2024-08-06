import React, { useEffect, useRef, useState } from 'react';
import { useMap } from 'react-leaflet';
import { Polygon } from 'react-leaflet';
import { MapContainer, TileLayer, Marker, Popup, Rectangle } from 'react-leaflet';
import { OpeningPolygon } from '../../types/OpeningPolygon';
import { getInitialLayers, getOpeningsPolygonFromWfs } from '../../map-services/BcGwWfsApi';
import { LayersControl } from 'react-leaflet';
import { MapLayer } from '../../types/MapLayer';
import { Polyline } from 'react-leaflet';
import { ImageMapLayer } from 'react-esri-leaflet';
import * as L from 'react-leaflet';
import { WMSTileLayer } from 'react-leaflet';

interface MapProps {
  openingId: number | null;
  setOpeningPolygonNotFound: Function;
}

const ImageMapLayerComponent = (url: string) => {
  const map = useMap();

  useEffect(() => {
    const imageMapLayer = L.esri.imageMapLayer({
      url, // replace with your ImageMapLayer URL
    }).addTo(map);

    return () => {
      map.removeLayer(imageMapLayer);
    };
  }, [map]);

  return null;
};

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

    // getDefaultLayers();

    if (openingId) {
      callBcGwApi();
    }
  }, [openingId]);

  useEffect(() => {
    const imageMapLayer = L.esri
  }, [openings, reloadMap]);

  const RecenterAutomatically = ({lat, long}:{lat: number, long:number}) => {
    const map = useMap();
    useEffect(() => {
      map.setView([lat, long]);
    }, [lat,long]);
    return null;
  };

  const { BaseLayer } = LayersControl;

  const geojsonData = () => {
    let uri = 'https://openmaps.gov.bc.ca/geo/ows';
    // service
    uri += '?service=WMS';
    // version
    uri += '&version=1.1.1';
    // request
    uri += '&request=GetMap';
    // layers
    uri += '&layers=WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW';
    // format
    uri += '&format=image/png';
    // transparency
    uri += '&transparent=true'
    // height
    uri += '&height=256';
    // width
    uri += '&width=256';
    // Srs name
    uri += '&srs=EPSG:4326';
    //uri += '&srs=EPSG:3857';
    // Properties name
    //uri += '&PROPERTYNAME=OPENING_ID,GEOMETRY,REGION_NAME,REGION_CODE,DISTRICT_NAME,DISTRICT_CODE,CLIENT_NAME,CLIENT_NUMBER,OPENING_WHEN_CREATED';
    // CQL Filters
    //uri += `&CQL_FILTER=OPENING_ID=${openingId}`;
    uri += `&CQL_FILTER=OPENING_ID=10936`;
    // bbox
    uri += '&bbox=-15028131.257091936,6261721.357121641,-14401959.121379772,6887893.492833805';

    console.log('uri 2', uri);

    return encodeURI(uri); //fetch(uri);
  };

  return (
    <MapContainer
      center={position}
      zoom={13}
      style={{ height: "400px", width: "100%" }}
    >
      <LayersControl position="bottomleft">
        <BaseLayer checked name="Government of British Columbia">
          <TileLayer
            url={"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"}
            attribution="&copy; <a href=&quot;https://www.openstreetmap.org/copyright&quot;>OpenStreetMap</a>"
          />
        </BaseLayer>
      </LayersControl>
      

      {/* Add layers and Layer controls */}
      <LayersControl position="topright">
        <LayersControl.Overlay key='wms-opening-layer' name='RESULTS - Openings svw'>
          <WMSTileLayer
            url="https://openmaps.gov.bc.ca/geo/ows"
            params={{
              format:"image/png",
              layers:"WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW",
              transparent:true,
              styles:"2942"
            }}
          />
        </LayersControl.Overlay>

      </LayersControl>
      
    </MapContainer>
  );
};

export default OpeningsMap;
