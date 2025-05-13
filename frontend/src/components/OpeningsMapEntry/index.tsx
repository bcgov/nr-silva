import React, { useEffect, useState } from "react";
import { useMap, Popup, GeoJSON } from "react-leaflet";
import { FeatureCollection } from "geojson";
import OpeningsMapEntryPopup from "@/components/OpeningsMapEntryPopup";
import { resultsStyle } from "./constants";

interface OpeningsMapEntryProps {
  polygons: FeatureCollection[];
}

const OpeningsMapEntry: React.FC<OpeningsMapEntryProps> = ({ polygons }) => {
  // State to hold the polygons
  // This is used to set the map view when the component mounts
  const [features, setFeatures] = useState<FeatureCollection[]>([]);

  // Get the map instance from react-leaflet
  const map = useMap();

  /**
   * Function to generate a unique key for each polygon
   * This is used to avoid duplicate keys in the map and allow React to
   * efficiently update the map when the polygons change
   * @param polygon The polygon to generate the key for
   * @param index The index of the polygon in the array
   * @returns A unique key for the polygon
   */
  const geoKey = (polygon: FeatureCollection, index: number) =>
    polygon.features
      .filter(Boolean)
      .map((feature) => feature.id)
      .filter(Boolean)
      .map((id) => String(id))
      .reduce((acc, id) => id, "") ?? `geo-${index}`;

  /**
   * Function to get the opening ID from the polygon
   * This is used to display the opening ID in the popup when a polygon is clicked
   * @param polygon The polygon to get the opening ID from
   * @returns The opening ID of the polygon or 0 if not found
   */
  const getOpeningId = (polygon: FeatureCollection) =>
    polygon.features
      .filter(Boolean)
      .map((feature) => feature.properties?.OPENING_ID)
      .filter(Boolean)
      .map((id) => String(id))
      .map((id) => parseFloat(id))
      .reduce((acc, id) => id, 0) ?? 0;

  /**
   * Effect to set the map view when the component mounts or when the polygons change
   */
  useEffect(() => {
    setFeatures([...polygons]);
  }, [polygons, map]);

  return (
    <>
      {features.filter(Boolean).map((polygon, index) => (
        <GeoJSON
          data={polygon}
          key={geoKey(polygon, index)}
          style={resultsStyle}
        >
          <Popup maxWidth={700}>
            <OpeningsMapEntryPopup openingId={getOpeningId(polygon)} />
          </Popup>
        </GeoJSON>
      ))}
    </>
  );
};

export default OpeningsMapEntry;
