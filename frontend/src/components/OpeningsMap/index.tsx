import React, { useEffect, useRef, useState } from 'react';
import { useMap } from 'react-leaflet';
import { Polygon } from 'react-leaflet';
import { MapContainer, TileLayer, Marker, Popup, Rectangle } from 'react-leaflet';
import { OpeningPolygon } from '../../types/OpeningPolygon';
import { getOpeningsPolygonFromWfs } from '../../map-services/BcGwWfsApi';

interface MapProps {
  selectedBasemap: any;
  openingId: number | null;
  setOpeningPolygonNotFound: Function;
}

const OpeningsMap: React.FC<MapProps> = ({
  selectedBasemap,
  openingId,
  setOpeningPolygonNotFound,
}) => {
  const lastClickedLayerRef = useRef<any>(null); // Replace 'any' with the specific type if known
  const [openings, setOpenings] = useState<OpeningPolygon[]>([]);
  const [position, setPosition] = useState<number[]>([48.43737, -123.35883]);
  const [reloadMap, setReloadMap] = useState<boolean>(false);

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

  return (
    <MapContainer
      center={position}
      zoom={13}
      style={{ height: "400px", width: "100%" }}
    >
      <TileLayer
        url={"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"}
        attribution={'&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'}
      />

      {/* Display Opening polygons, if any */}
      {openings.length && (
        openings.map((opening) => (
          <Polygon
            key={opening.key}
            positions={opening.bounds}
            pathOptions={resultsStyle}  
          >
            <Popup maxWidth="700">
              {opening.popupHtml}
            </Popup>
          </Polygon>
        ))
      )}

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
    </MapContainer>
  );
};

export default OpeningsMap;
