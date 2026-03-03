import React, { useEffect, useState, useRef } from "react";
import L from "leaflet";
import { useMapEvents, GeoJSON, Marker } from "react-leaflet";
import { Feature, FeatureCollection, Geometry } from "geojson";
import {
  getStyleForFeature,
  getCenterOfFeatureCollection,
} from "@/types/MapLayer";
import "./styles.scss";

interface OpeningsMapEntryProps {
  polygons: FeatureCollection[];
  hoveredFeature: Feature<Geometry, unknown> | null;
  setHoveredFeature: (feature: Feature<Geometry, unknown> | null) => void;
  selectedFeature: Feature<Geometry, unknown> | null;
  setSelectedFeature: (feature: Feature<Geometry, unknown> | null) => void;
  isPopupHoveredRef?: React.RefObject<boolean>;
}

const OpeningsMapEntry: React.FC<OpeningsMapEntryProps> = ({ polygons, hoveredFeature, setHoveredFeature, selectedFeature, setSelectedFeature, isPopupHoveredRef }) => {
  const [zoom, setZoom] = useState<number>(13);

  const lastHoveredFeatureIdRef = useRef<string | number | null>(null);

  const hasFeatureProperty = (l: unknown): l is L.Layer & { feature: Feature<Geometry, unknown> } => {
    return typeof l === 'object' && l !== null && 'feature' in l;
  };

  const isPathLayer = (l: unknown): l is L.Path => {
    return typeof l === 'object' && l !== null && 'bringToFront' in l;
  };

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
      .reduce((_acc, id) => `${id}-${index}`, "") ?? `geo-${index}`;

  useEffect(() => {
    if (hoveredFeature) {
      setTimeout(() => {
        map.eachLayer((l: L.Layer) => {
          if (
            hasFeatureProperty(l) &&
            l.feature.id === hoveredFeature.id &&
            isPathLayer(l)
          ) {
            l.bringToFront();
          }
        });
      }, 0);
    }
  }, [hoveredFeature, selectedFeature, map]);

  const onEachFeature = (feature: Feature<Geometry, unknown>, layer: L.Layer) => {
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

        // This condition checks if the current zoom level is outside the preferred range (between 15 and 16 inclusive)
        // or if the polygon's bounds are not fully visible within the current map view.
        // If either is true, the map view will animate to fit the polygon's bounds.
        if ((currentZoom <= 14 || currentZoom >= 17) || !mapBounds.contains(bounds)) {
          map.flyToBounds(bounds, { maxZoom: 16, animate: true, duration: 0.5 });
        }
      },
    });
  };

  return (
    <>
      {zoom > 10 &&
        polygons.filter(Boolean).map((featureCollection, collectionIndex) =>
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
                onEachFeature={onEachFeature}
              />
            ))
        )
      }
      {zoom <= 10 &&
        polygons.filter(Boolean).map((featureCollection, index) =>
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
