import React, { useEffect, useState } from "react";
import { useMapEvents, Popup, GeoJSON, Marker } from "react-leaflet";
import { FeatureCollection } from "geojson";
import {
  getStyleForFeature,
  getPropertyForFeature,
  getPopupCenter,
  getCenterOfFeatureCollection,
} from "@/types/MapLayer";
import OpeningsMapEntryPopup from "@/components/OpeningsMapEntryPopup";

interface OpeningsMapEntryProps {
  polygons: FeatureCollection[];
}

const OpeningsMapEntry: React.FC<OpeningsMapEntryProps> = ({ polygons }) => {
  // State to hold the polygons
  // This is used to set the map view when the component mounts
  const [features, setFeatures] = useState<FeatureCollection[]>([]);
  const [zoom, setZoom] = useState<number>(13);

  // Get the map instance from react-leaflet
  const map = useMapEvents({
    zoomend: () => setZoom(map.getZoom()),
  });

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
      .find((id) => id && id !== 0) ?? 0;

  /**
   * Effect to set the map view when the component mounts or when the polygons change
   */
  useEffect(() => {
    setFeatures([...polygons]);
  }, [polygons]);

  return (
    <>
      {zoom > 10 &&
        features.filter(Boolean).map((featureCollection, index) => (
          <GeoJSON
            data-testid="geojson"
            data={featureCollection}
            key={geoKey(featureCollection, index)}
            style={getStyleForFeature}
          >
            {featureCollection?.features
              ?.filter((feature) => feature.geometry)
              .map((feature, index) => (
                <Popup
                  data-testid="popup"
                  key={`popup-${geoKey(featureCollection, index)}`}
                  maxWidth={700}
                  autoPan={false}
                  position={getPopupCenter(feature.geometry)}
                >
                  <OpeningsMapEntryPopup
                    openingId={getOpeningId(featureCollection)}
                    data={getPropertyForFeature(feature)}
                  />
                </Popup>
              ))}
          </GeoJSON>
        ))}
      {zoom <= 10 &&
        features.filter(Boolean).map((featureCollection, index) => (
          <Marker
            data-testid="marker"
            key={geoKey(featureCollection, index)}
            position={getCenterOfFeatureCollection(featureCollection)}
          >
            {featureCollection?.features
              ?.filter((feature) => feature.geometry)
              .map((feature, index) => (
                <Popup
                  data-testid="popup"
                  key={`popup-${geoKey(featureCollection, index)}`}
                  maxWidth={700}
                  autoPan={false}
                  position={getPopupCenter(feature.geometry)}
                >
                  <OpeningsMapEntryPopup
                    openingId={getOpeningId(featureCollection)}
                    data={getPropertyForFeature(feature)}
                  />
                </Popup>
              ))}
          </Marker>
        ))}
    </>
  );
};

export default OpeningsMapEntry;
