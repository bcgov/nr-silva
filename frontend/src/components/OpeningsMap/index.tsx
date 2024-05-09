import React, { useEffect, useRef, useState } from 'react';
import { useMap } from 'react-leaflet';
import { Polygon } from 'react-leaflet';
import { MapContainer, TileLayer, Marker, Popup, Rectangle } from 'react-leaflet';
import { OpeningPolygon } from '../../types/OpeningPolygon';
import { getOpeningsPolygonFromWfs } from '../../map-services/BcGwWfsApi';

interface MapProps {
  selectedBasemap: any;
  openingId: number | null;
}

const OpeningsMap: React.FC<MapProps> = ({ selectedBasemap, openingId }) => {
  //const position: [number, number] = [49.11941257871176, -122.83461402566606];
  const lastClickedLayerRef = useRef<any>(null); // Replace 'any' with the specific type if known
  const [openings, setOpenings] = useState<OpeningPolygon[]>([]);
  const [position, setPosition] = useState<number[]>([49.2568449, -123.1289342]);
  const [reloadMap, setReloadMap] = useState<boolean>(false);

  const resultsStyle = {
    color: 'black'
  };

  useEffect(() => {
    const callBcGwApi = async () => {
      const openingGeom: OpeningPolygon | null = await getOpeningsPolygonFromWfs(openingId);
      if (openingGeom) {
        setOpenings([openingGeom]);
        setPosition([openingGeom.positionLat, openingGeom.positionLong]);
        setReloadMap(true);
      } else {
        window.alert('No features found for the Opening ID ' + openingId);
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
      {openings.length && (
        openings.map((opening) => (
          <Polygon
            key={opening.key}
            positions={opening.bounds}
            pathOptions={resultsStyle}
          />
        ))
      )}
      {openings.length === 0 && (
        <Marker position={position}>
          <Popup>
            A pretty CSS3 popup. <br /> Easily customizable. This is a fixed Marker on Map load
          </Popup>
        </Marker>
      )}
      {position && (
        <RecenterAutomatically lat={position[0]} long={position[1]} />
      )}
    </MapContainer>
  );
};

export default OpeningsMap;
