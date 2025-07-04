import React, { useEffect } from "react";
import { useMap } from "react-leaflet";
import L, { LatLngExpression } from "leaflet";
import { FeatureCollection } from "geojson";

interface OpeningsMapFitBoundProps {
  polygons: FeatureCollection[];
  defaultLocation: LatLngExpression;
  defaultZoom: number;
}

const OpeningsMapFitBound: React.FC<OpeningsMapFitBoundProps> = ({
  polygons,
  defaultLocation,
  defaultZoom,
}) => {
  const map = useMap();

  /**
   * Effect to set the map view when the component mounts or when the polygons change
   * This is used to ensure that the map is centered on the polygons
   * and that the map is resized to fit the polygons
   * If no polygons are found, set the map to the default location
   * If polygons are found, calculate the bounds to include all polygons
   * and set the map view to the bounds
   * If the bounds are not valid, set the map to the default location
   */
  useEffect(() => {
    if (polygons.length > 0) {
      // Calculate bounds to include all polygons
      // This is to make sure the map resizes for the correct view
      const bounds = L.featureGroup(
        polygons.map((geoJson) => L.geoJSON(geoJson))
      ).getBounds();

      if (bounds.isValid()) map.fitBounds(bounds, { padding: [20, 20] });
      else map.setView(defaultLocation, defaultZoom, { animate: true });
    } else {
      // If no polygons are found, set the map to the default location
      map.setView(defaultLocation, defaultZoom, { animate: true });
    }
  }, [polygons, map, defaultLocation, defaultZoom]);

  return null;
};

export default OpeningsMapFitBound;
