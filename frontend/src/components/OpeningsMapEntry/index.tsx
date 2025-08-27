import React, { useEffect, useState, useRef } from "react";
import { renderToString } from "react-dom/server";
import L from "leaflet";
import { useMapEvents, Popup, GeoJSON, Marker } from "react-leaflet";
import { Feature, FeatureCollection, Geometry } from "geojson";
import {
  getStyleForFeature,
  getPropertyForFeature,
  getPopupCenter,
  getCenterOfFeatureCollection,
} from "@/types/MapLayer";
import OpeningsMapEntryPopup from "@/components/OpeningsMapEntryPopup";
import "./styles.scss";

interface OpeningsMapEntryProps {
  polygons: FeatureCollection[];
  hoveredFeature: Feature<Geometry, any> | null;
  setHoveredFeature: (feature: Feature<Geometry, any> | null) => void;
  selectedFeature: Feature<Geometry, any> | null;
  setSelectedFeature: (feature: Feature<Geometry, any> | null) => void;
  isPopupHoveredRef?: React.MutableRefObject<boolean>;
}

const OpeningsMapEntry: React.FC<OpeningsMapEntryProps> = ({ polygons, hoveredFeature, setHoveredFeature, selectedFeature, setSelectedFeature, isPopupHoveredRef }) => {
  // State to hold the polygons
  // This is used to set the map view when the component mounts
  const [features, setFeatures] = useState<FeatureCollection[]>([]);
  const [zoom, setZoom] = useState<number>(13);

  const lastHoveredFeatureIdRef = useRef<string | number | null>(null);

  // Get the map instance from react-leaflet
  const map = useMapEvents({
    zoomend: () => setZoom(map.getZoom()),
  });

  const markerIcon = new L.Icon({
    iconUrl: "/marker.svg",
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41],
  });

  const markerHoveredIcon = new L.Icon({
    iconUrl: "/marker-hovered.svg",
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41],
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
      .reduce((acc, id) => `${id}-${index}`, "") ?? `geo-${index}`;

  /**
   * Function to get the opening ID from the polygon
   * This is used to display the opening ID in the popup when a polygon is clicked
   * @param polygon The polygon to get the opening ID from
   * @returns The opening ID of the polygon or 0 if not found
   */
  const getOpeningIdByCollection = (polygon: FeatureCollection) =>
    polygon.features
      .filter(Boolean)
      .map((feature) => feature.properties?.OPENING_ID)
      .filter(Boolean)
      .map((id) => String(id))
      .map((id) => parseFloat(id))
      .find((id) => id && id !== 0) ?? 0;

  /**
   * Function to get the opening ID from the polygon
   * This is used to display the opening ID in the popup when a polygon is clicked
   * @param feature The feature to get the opening ID from
   * @returns The opening ID of the polygon or 0 if not found
   */
  const getOpeningIdByFeature = (feature: Feature<Geometry, any>) => {
    const openingId = feature.properties?.OPENING_ID || "0";
    return parseFloat(openingId);
  };

  /**
   * Effect to set the map view when the component mounts or when the polygons change
   */
  useEffect(() => {
    setFeatures([...polygons]);
  }, [polygons]);

  useEffect(() => {
    if (hoveredFeature) {
      setTimeout(() => {
        map.eachLayer((l: L.Layer) => {
          if (
            (l as any).feature &&
            (l as any).feature.id === hoveredFeature.id &&
            (l as L.Path).bringToFront
          ) {
            (l as L.Path).bringToFront();
          }
        });
      }, 0);
    }
  }, [hoveredFeature, selectedFeature, map]);

  const onEachFeature = (featureCollection: FeatureCollection) => (feature: Feature<Geometry, any>, layer: L.Layer) => {
    layer.on({
      mouseover: () => {
        setHoveredFeature(feature);
        lastHoveredFeatureIdRef.current = feature.id ?? null;
        if ((layer as L.Path).bringToFront) (layer as L.Path).bringToFront();
      },
      mouseout: () => {
        const thisFeatureId = feature.id;
        setTimeout(() => {
          if (
            lastHoveredFeatureIdRef.current === thisFeatureId &&
            !isPopupHoveredRef?.current
          ) {
            setHoveredFeature(null);
          }
        }, 50);
      },
      click: () => {
        setSelectedFeature(feature);
        if ((layer as L.Path).bringToFront) (layer as L.Path).bringToFront();

        const currentZoom = map.getZoom();
        const geoJsonLayer = L.geoJSON(feature);
        const bounds = geoJsonLayer.getBounds();
        const mapBounds = map.getBounds();

        if ((currentZoom <= 14 || currentZoom >= 17) || !mapBounds.contains(bounds)) {
          map.flyToBounds(bounds, { maxZoom: 16, animate: true, duration: 0.5 });
        }
      },
    });
  };

  return (
    <>
      {zoom > 10 &&
        features.filter(Boolean).map((featureCollection, collectionIndex) =>
          featureCollection.features
            .filter((feature) => feature.geometry)
            .map((feature, featureIndex) => (
              <GeoJSON
                data-testid={`geojson-${geoKey(featureCollection, collectionIndex)}-${featureIndex}`}
                key={`geojson-${geoKey(featureCollection, collectionIndex)}-${featureIndex}`}
                data={feature}
                style={() =>
                  getStyleForFeature(
                    feature,
                    selectedFeature,
                    hoveredFeature,
                    featureIndex,
                    featureCollection.features.length
                  )
                }
                onEachFeature={onEachFeature(featureCollection)}
              />
            ))
        )
      }
      {zoom <= 10 &&
        features.filter(Boolean).map((featureCollection, index) =>
          featureCollection?.features
            ?.filter((feature) => feature.geometry)
            .map((feature, fIndex) => (
              <Marker
                icon={
                  (hoveredFeature && hoveredFeature.id === feature.id) ||
                    (selectedFeature && selectedFeature.id === feature.id)
                    ? markerHoveredIcon
                    : markerIcon
                }
                data-testid="marker"
                key={`marker-${geoKey(featureCollection, index)}-${fIndex}`}
                position={getCenterOfFeatureCollection(featureCollection)}
                eventHandlers={{
                  click: () => {
                    setSelectedFeature(feature);

                    const geoJsonLayer = L.geoJSON(feature);
                    const bounds = geoJsonLayer.getBounds();
                    map.flyToBounds(bounds, { maxZoom: 15, animate: true, duration: 1.0 });
                  },
                  mouseover: () => {
                    setHoveredFeature(feature);
                  },
                  mouseout: () => {
                    setHoveredFeature(null);
                  },
                }}
              />
            ))
        )
      }
    </>
  );
};

export default OpeningsMapEntry;
