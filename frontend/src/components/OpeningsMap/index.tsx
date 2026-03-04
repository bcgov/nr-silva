import React, { useEffect, useRef, useState } from "react";
import { LatLngExpression } from "leaflet";
import {
  LayersControl,
  MapContainer,
  TileLayer,
  useMapEvents,
  WMSTileLayer,
  ZoomControl,
} from "react-leaflet";
import { Feature, FeatureCollection, Geometry } from "geojson";
import { getPropertyForFeature, MapKindType, MapLayer } from "@/types/MapLayer";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import OpeningsMapResizer from "@/components/OpeningsMapResizer";
import OpeningsMapEntry from "@/components/OpeningsMapEntry";
import OpeningsMapFitBound from "@/components/OpeningsMapFitBound";
import OpeningsMapEntryPopup from "../OpeningsMapEntryPopup";

import { allLayers } from "./constants";
import { getMapQueries, getUserLocation } from "./fetcher";

import "./styles.scss";

interface MapProps {
  openingIds: number[] | null;
  setOpeningPolygonNotFound: (value: boolean, openingId: number | null) => void;
  mapHeight?: number;
  layerFilter?: boolean;
  kind?: MapKindType[];
  isDetailsPage?: boolean;
  isForestCoverMap?: boolean;
  isActivitiesMap?: boolean;
  selectedForestCoverIds?: string[];
  selectedSilvicultureActivityIds?: string[];
  selectedDisturbanceIds?: string[];
}

const OpeningsMap: React.FC<MapProps> = ({
  openingIds,
  setOpeningPolygonNotFound,
  mapHeight = 480,
  layerFilter = false,
  kind = [MAP_KINDS.opening],
  isDetailsPage = false,
  isForestCoverMap = false,
  isActivitiesMap = false,
  selectedForestCoverIds,
  selectedSilvicultureActivityIds,
  selectedDisturbanceIds,
}) => {
  const [position, setPosition] = useState<LatLngExpression>([
    48.43737, -123.35883,
  ]);
  const [zoomLevel, setZoomLevel] = useState<number>(13);
  const [mapSize, setMapSize] = useState<number>(mapHeight);

  const [hoveredFeature, setHoveredFeature] = useState<Feature<Geometry, any> | null>(null);
  const [selectedFeature, setSelectedFeature] = useState<Feature<Geometry, any> | null>(null);
  const [selectionByKind, setSelectionByKind] = useState<Record<string, string | null>>({});
  const [isPopupHovered, setIsPopupHovered] = useState(false);
  const isPopupHoveredRef = useRef(isPopupHovered);

  const polygonClickedRef = useRef(false);
  const openingsRef = useRef<FeatureCollection[]>([]);
  const kindKey = Array.isArray(kind) ? kind.join(",") : String(kind);

  /**
   * Fetch map queries directly from the openingIds prop — no intermediate state
   * so the queries update in the same render as the prop change.
   */
  const mapQueries = getMapQueries(openingIds ?? [], ...kind);

  /**
   * Derive openings synchronously from query results.
   * Uses a ref to preserve the last successful data while new queries are loading,
   * preventing the map from flashing empty during transitions.
   */
  const openings: FeatureCollection[] = React.useMemo(() => {
    if (!openingIds?.length) {
      openingsRef.current = [];
      return [];
    }
    const allSuccess = mapQueries.length > 0 && mapQueries.every((q) => q.status === "success");
    if (allSuccess) {
      openingsRef.current = mapQueries.map((q) => q.data as FeatureCollection);
    }
    return openingsRef.current;
  }, [
    mapQueries.map((q) => q.status).join(","),
    openingIds?.join(","),
    kind?.join(","),
  ]);

  const polygonsToRender = React.useMemo(() => {
    if (isForestCoverMap) {
      // Forest cover: show only explicitly selected forest cover polygons
      const selectedSet = new Set(selectedForestCoverIds);
      return openings.map(fc => ({
        ...fc,
        features: fc.features.filter(
          feature =>
            feature.properties?.FOREST_COVER_ID != null &&
            feature.properties?.SILV_POLYGON_NUMBER != null &&
            selectedSet.has(
              `${feature.properties.FOREST_COVER_ID}-${feature.properties.SILV_POLYGON_NUMBER}`
            )
        ),
      }));
    } else if (isActivitiesMap) {
      // Activities map: disturbance (DN) and silviculture activities are filtered separately
      // so each row type can independently control what's shown.
      const activitySet = new Set(selectedSilvicultureActivityIds ?? []);
      const disturbanceSet = new Set(selectedDisturbanceIds ?? []);
      return openings.map(fc => ({
        ...fc,
        features: fc.features.filter(feature => {
          const atuId = feature.properties?.ACTIVITY_TREATMENT_UNIT_ID;
          const baseCode = feature.properties?.SILV_BASE_CODE;
          if (atuId == null || !baseCode) return false;
          const compoundId = `${atuId}-${baseCode}`;
          if (baseCode === 'DN') {
            // Disturbance polygon: only show if explicitly selected as disturbance
            return disturbanceSet.has(compoundId);
          }
          // Silviculture activity polygon: only show if explicitly selected as activity
          return activitySet.has(compoundId);
        }),
      }));
    } else {
      return openings;
    }
  }, [
    isForestCoverMap,
    isActivitiesMap,
    selectedForestCoverIds,
    selectedSilvicultureActivityIds,
    selectedDisturbanceIds,
    openings
  ]);

  const setUserLocation = async () => {
    const userLocation = await getUserLocation();
    setPosition({ lat: userLocation.lat, lng: userLocation.lng });
    setZoomLevel(userLocation.zoom);
  };

  /**
   * Get user's location when no opening IDs are provided.
   */
  useEffect(() => {
    if (!openingIds?.length) {
      (async () => await setUserLocation())();
    }
  }, [openingIds]);

  const handleSelectFeature = (feature: Feature<Geometry, any> | null) => {
    setSelectedFeature(feature);
    setSelectionByKind((prev) => ({
      ...prev,
      [kindKey]: feature ? String(feature.id) : null,
    }));
  };

  useEffect(() => {
    const storedId = selectionByKind[kindKey];
    if (isDetailsPage && storedId && openings.length > 0) {
      const found = openings
        .flatMap((fc) => fc.features)
        .find((f) => String(f.id) === storedId);
      if (found) {
        setSelectedFeature(found);
      }
    } else {
      setSelectedFeature(null);
    }
    setHoveredFeature(null);
  }, [kindKey, openings]);

  /**
   * Report errors from failed map queries to the parent.
   */
  useEffect(() => {
    const errorIds = mapQueries
      .filter((query) => query.error)
      .map(
        (query) =>
          (query.error as Error & { cause?: { openingId?: string } }).cause
            ?.openingId
      )
      .filter(Boolean)
      .map((id) => Number(id));

    if (errorIds.length > 0) {
      setOpeningPolygonNotFound(true, errorIds[0] ?? null);
    }
  }, [mapQueries.map((query) => query.status).join(",")]);

  /**
   * This effect is used to update the isPopupHoveredRef when the isPopupHovered state changes.
   * This is used to determine if the popup is hovered or not.
   */
  useEffect(() => {
    isPopupHoveredRef.current = isPopupHovered;
  }, [isPopupHovered]);

  /**
   * This function returns extra parameters for the WMS layer, if the correct set of
   * parameters is provided. It checks if the layerFilter is true and if there are
   * openingIds available. If so, it constructs a CQL_FILTER string with the opening IDs
   * and returns it as an object. Otherwise, it returns an empty object.
   * This should be true only inside opening details page, as it can impact negatively
   * the map layers when displaying all the openings.
   * @param filterable - Indicates if the layer is filterable
   * @returns extra parameters for the WMS layer
   */
  const extraParameters = (filterable: boolean) => {
    if (layerFilter && openingIds?.length && filterable) {
      return {
        CQL_FILTER: openingIds
          .map((id) => `OPENING_ID=${id}`)
          .reduce((prev, subsequent) => `${prev};${subsequent}`),
      };
    }
    return {};
  };

  const MapClickHandler: React.FC<{
    onMapClick: () => void;
    polygonClickedRef: React.RefObject<boolean>; // updated type
  }> = ({ onMapClick, polygonClickedRef }) => {
    useMapEvents({
      click: () => {
        if (polygonClickedRef.current) {
          polygonClickedRef.current = false; // reset for next click
          return;
        }
        onMapClick();
      },
    });
    return null;
  };
  return (
    <div className="opening-map-container" style={{ height: `${mapSize}px`, width: "100%" }}>
      {/* Popup info in top left */}

      {isForestCoverMap && selectedForestCoverIds?.length === 0 && (
        <div className="opening-map-empty-message">
          No forest cover polygon is selected and displayed. Select from the table to show on map.
        </div>
      )}

      {isForestCoverMap && selectedForestCoverIds && selectedForestCoverIds?.length > 0 && (
        <div className="opening-map-selected-message">
          Showing selected forest cover polygons
        </div>
      )}

      {isActivitiesMap && (
        (() => {
          const silvCount = selectedSilvicultureActivityIds?.length ?? 0;
          const disturbanceCount = selectedDisturbanceIds?.length ?? 0;
          if (silvCount === 0 && disturbanceCount === 0) {
            return (
              <div className="opening-map-empty-message">
                No activities or disturbances are selected and displayed. Select from the table to show on map.
              </div>
            );
          }
          if (silvCount > 0 && disturbanceCount === 0) {
            return (
              <div className="opening-map-selected-message">
                Showing selected activities
              </div>
            );
          }
          if (silvCount === 0 && disturbanceCount > 0) {
            return (
              <div className="opening-map-selected-message">
                Showing selected disturbances
              </div>
            );
          }
          if (silvCount > 0 && disturbanceCount > 0) {
            return (
              <div className="opening-map-selected-message">
                Showing selected activities and disturbances
              </div>
            );
          }
          return null;
        })()
      )}

      {selectedFeature || hoveredFeature ? (
        <div className="map-popup-top-left"
          onMouseEnter={() => setIsPopupHovered(true)}
          onMouseLeave={() => {
            setIsPopupHovered(false);
            setHoveredFeature(null);
          }}
        >
          {hoveredFeature ? (
            <OpeningsMapEntryPopup
              openingId={hoveredFeature.properties?.OPENING_ID}
              data={getPropertyForFeature(hoveredFeature)}
              feature={{
                type: "FeatureCollection",
                features: [hoveredFeature],
              }}
              isSelected={selectedFeature?.id === hoveredFeature.id}
            />
          ) : selectedFeature ? (
            <OpeningsMapEntryPopup
              openingId={selectedFeature.properties?.OPENING_ID}
              data={getPropertyForFeature(selectedFeature)}
              feature={{
                type: "FeatureCollection",
                features: [selectedFeature],
              }}
              isSelected={true}
            />
          ) : null}
        </div>
      ) : null}


      {/* Display the user's location if no openings are found */}
      <MapContainer
        center={position}
        zoom={zoomLevel}
        style={{ height: "100%", minHeight: "100%" }}
        zoomControl={false}
        scrollWheelZoom={false}
      >
        <MapClickHandler
          onMapClick={() => {
            setSelectedFeature(null);
            setHoveredFeature(null);
            setSelectionByKind((prev) => ({
              ...prev,
              [kindKey]: null,
            }));
          }}
          polygonClickedRef={polygonClickedRef}
        />

        <ZoomControl position="bottomright" />

        {/* Resizer to adjust the map height */}
        <OpeningsMapResizer height={mapSize} />

        {/* Display Opening polygons, if any */}
        <OpeningsMapEntry
          polygons={polygonsToRender}
          hoveredFeature={hoveredFeature}
          setHoveredFeature={setHoveredFeature}
          selectedFeature={selectedFeature}
          setSelectedFeature={(feature) => {
            polygonClickedRef.current = true;
            handleSelectFeature(feature);
          }}
          isPopupHoveredRef={isPopupHoveredRef}
        />
        <OpeningsMapFitBound
          polygons={openings}
          defaultLocation={position}
          defaultZoom={zoomLevel}
        />

        {/* Default layers */}
        {allLayers.length > 0 && (
          <LayersControl position="topright">
            <LayersControl.BaseLayer checked name="ESRI Topography">
              <TileLayer
                url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}"
                attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community"
                zIndex={-10000}
              />
            </LayersControl.BaseLayer>
            <LayersControl.BaseLayer name="ESRI Satellite">
              <TileLayer
                url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
                attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community"
                zIndex={-10000}
              />
            </LayersControl.BaseLayer>
            <LayersControl.BaseLayer name="OpenTopoMap">
              <TileLayer
                url="https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png"
                attribution='Map data: &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, <a href="http://viewfinderpanoramas.org">SRTM</a> | Map style: &copy; <a href="https://opentopomap.org">OpenTopoMap</a> (<a href="https://creativecommons.org/licenses/by-sa/3.0/">CC-BY-SA</a>)'
                zIndex={-10000}
              />
            </LayersControl.BaseLayer>
            <LayersControl.BaseLayer name="OpenStreets">
              <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                zIndex={-10000}
              />
            </LayersControl.BaseLayer>
            {allLayers.map((layer: MapLayer) => (
              <LayersControl.Overlay key={layer.name} name={layer.name}>
                <WMSTileLayer
                  url="https://openmaps.gov.bc.ca/geo/ows"
                  params={{
                    format: layer.format,
                    layers: layer.layers,
                    transparent: layer.transparent,
                    styles: layer.styles[0]!.name,
                    ...extraParameters(layer.filterable),
                  }}
                />
              </LayersControl.Overlay>
            ))}
          </LayersControl>
        )}
      </MapContainer>
    </div>
  );
};

export default OpeningsMap;
