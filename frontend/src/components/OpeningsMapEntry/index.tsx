import React, { useEffect } from 'react';
import { Polygon, useMap, Popup } from 'react-leaflet';
import L,{LatLngExpression} from 'leaflet';
import { OpeningPolygon } from '../../types/OpeningPolygon';
import OpeningsMapEntryPopup from '../OpeningsMapEntryPopup';

interface OpeningsMapEntryProps {
  polygons: OpeningPolygon[];
  defaultLocation: LatLngExpression;
  defaultZoom: number;
}

const resultsStyle = {
  color: 'black'
};

const OpeningsMapEntry: React.FC<OpeningsMapEntryProps> = ({ 
  polygons,
  defaultLocation,
  defaultZoom
}) => {
  const map = useMap();

  useEffect(() => {
    if (polygons.length > 0) {
      // Calculate bounds to include all polygons
      // This is to make sure the map resizes for the correct view
      const bounds = L.latLngBounds(
        polygons.flatMap((polygon) => polygon.bounds.flat())
      );
      map.fitBounds(bounds, { padding: [20, 20] });
    }else {
      // If no polygons are found, set the map to the default location
      map.setView(defaultLocation, defaultZoom);
    }
  }, [polygons, map]);


  return (
    <>
      {polygons.map((polygon) => (
        <Polygon
          key={polygon.key}
          positions={polygon.bounds}
          pathOptions={resultsStyle}
        >
          <Popup maxWidth={700}>
            <OpeningsMapEntryPopup 
              openingId={parseFloat(polygon.properties?.OPENING_ID || 0)} 
            />
          </Popup>
        </Polygon>
      ))}
    </>
  );
};

export default OpeningsMapEntry;
