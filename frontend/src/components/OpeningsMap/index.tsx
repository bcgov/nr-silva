import React, { useEffect, useRef, useState } from 'react';
import { useMap } from 'react-leaflet';
import { Polygon } from 'react-leaflet';
import { MapContainer, TileLayer, Marker, Popup, Rectangle } from 'react-leaflet';

interface MapProps {
  selectedBasemap: any;
  openingId: number | null;
}

interface OpeningMap {
  key: string;
  bounds: any;
  properties: object;
  id: string;
}

const OpeningsMap: React.FC<MapProps> = ({ selectedBasemap, openingId }) => {
  //const position: [number, number] = [49.11941257871176, -122.83461402566606];
  const lastClickedLayerRef = useRef<any>(null); // Replace 'any' with the specific type if known
  const [openings, setOpenings] = useState<OpeningMap[]>([]);
  const [position, setPosition] = useState<number[]>([59.30407, -122.0567]);
  const [reloadMap, setReloadMap] = useState<boolean>(false);

  const resultsStyle = {
    color: 'black'
  };

  const invertXandY = (coordinates: any) => {
    const newCoord = [];
    for (let i = 0, len = coordinates.length; i < len; i++) {
      const polygon = coordinates[i];
      const newSubArray = []

      for (let j = 0, lenj = polygon.length; j < lenj; j++) {
        const lat = polygon[j][1];
        const long = polygon[j][0];
        const polygonPoint = [lat, long];
        newSubArray.push(polygonPoint);
      }
      newCoord.push(newSubArray);
    }
    return newCoord;
  }

  const getOpeningsData = async (openingId: number) => {
    let uri = 'https://openmaps.gov.bc.ca/geo/ows';
    // service
    uri += '?service=WFS';
    // version
    uri += '&version=2.0.0';
    // request
    uri += '&request=GetFeature';
    // typeName (layer !?)
    uri += '&typeName=WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW';
    // output format
    uri += '&outputFormat=application/json';
    // Srs name
    uri += '&SrsName=EPSG:4326';
    // Properties name
    uri += '&PROPERTYNAME=OPENING_ID,APPROVE_DATE,LICENSEE_OPENING_ID,GEOMETRY';
    // CQL Filters
    uri += `&CQL_FILTER=OPENING_ID=${openingId}`;

    const resultJson = await fetch(uri);
    if (resultJson.ok) {
      const json = await resultJson.json();

      if (json.features && json.features.length) {
        const openingsList: OpeningMap[] = [];
        for (let i = 0, len = json.features.length; i < len; i++) {
          if (json.features[i].geometry) {
            // get the position from the bounding box
            setPosition([json.bbox[1], json.bbox[0]]);

            const openingGeometry = invertXandY(json.features[i].geometry.coordinates);

            const openingObj: OpeningMap = {
              key: json.features[i].id,
              bounds: openingGeometry,
              properties: json.features[i].properties,
              id: json.features[i].id,
            };
            openingsList.push(openingObj);
          }
        }

        if (openingsList.length) {
          setOpenings(openingsList);
          setReloadMap(true);
        }
      } else {
        window.alert('No features found for the Opening ID ' + openingId);
      }
    }
  };

  useEffect(() => {
    if (openingId) {
      getOpeningsData(openingId);
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
      zoom={12}
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
