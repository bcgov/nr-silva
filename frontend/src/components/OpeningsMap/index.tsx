import React, { useEffect, useState } from "react";
import { LatLngExpression } from "leaflet";
import {
  LayersControl,
  MapContainer,
  TileLayer,
  WMSTileLayer,
} from "react-leaflet";
import { FeatureCollection } from "geojson";
import { MapKindType, MapLayer } from "@/types/MapLayer";
import OpeningsMapResizer from "@/components/OpeningsMapResizer";
import OpeningsMapEntry from "@/components/OpeningsMapEntry";
import OpeningsMapFitBound from "@/components/OpeningsMapFitBound";
import OpeningsMapFullScreen from "@/components/OpeningsMapFullScreen";

import { allLayers } from "./constants";
import { getMapQueries, getUserLocation } from "./fetcher";

import "./styles.scss";

interface MapProps {
  openingIds: number[] | null;
  setOpeningPolygonNotFound: (value: boolean, openingId: number | null) => void;
  mapHeight?: number;
  layerFilter?: boolean;
  kind?: MapKindType[];
}

const OpeningsMap: React.FC<MapProps> = ({
  openingIds,
  setOpeningPolygonNotFound,
  mapHeight = 400,
  layerFilter = false,
  kind = ["WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW"],
}) => {
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openings, setOpenings] = useState<FeatureCollection[]>([]);
  const [position, setPosition] = useState<LatLngExpression>([
    48.43737, -123.35883,
  ]);
  const [zoomLevel, setZoomLevel] = useState<number>(13);
  const [mapSize, setMapSize] = useState<number>(mapHeight);

  /**
   * This function is used to fetch the map queries based on the selected opening IDs
   * and the kind of map data.
   */
  const mapQueries = getMapQueries(selectedOpeningIds ?? [], ...kind);

  /**
   * This effect is used to set the map position and zoom level when the component mounts
   * or when the openingIds prop changes. If no openingIds are provided, it will
   * attempt to get the user's location.
   */
  const setUserLocation = async () => {
    const userLocation = await getUserLocation();
    setPosition({ lat: userLocation.lat, lng: userLocation.lng });
    setZoomLevel(userLocation.zoom);
  };

  /**
   * This effect is used to set the selected opening IDs when the component mounts
   * or when the openingIds prop changes. If no openingIds are provided, it will
   * attempt to get the user's location.
   */
  useEffect(() => {
    setSelectedOpeningIds(openingIds || []);
    if (!openingIds?.length) {
      (async () => await setUserLocation())();
    }
  }, [openingIds]);

  /**
   * This effect is used to update the map with the fetched polygons.
   * It checks if all queries are successful and updates the openings state
   * with the fetched data. If there are any errors, it sets the error state.
   */
  useEffect(() => {
    const allSuccess = mapQueries.every((query) => query.status === "success");
    if (allSuccess) {
      setOpenings(mapQueries.map((query) => query.data as FeatureCollection));
    } else {
      // Check if there are any errors and extract their IDs
      const errorIds = mapQueries
        .filter((query) => query.error)
        .map(
          (query) =>
            (query.error as Error & { cause?: { openingId?: string } }).cause
              ?.openingId
        )
        .filter(Boolean) // Remove undefined/null values
        .map((id) => Number(id));

      if (errorIds.length > 0) {
        setOpeningPolygonNotFound(true, errorIds[0] ?? null);
      }
    }
  }, [
    mapQueries.map((query) => query.status).join(","),
    kind?.join(","),
    openingIds?.join(","),
  ]);

  /**
   * This effect is used to get the user's location when the component mounts.
   */
  useEffect(() => {
    (async () => await setUserLocation())();
  }, []);

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

  return (
    <div className="opening-map-container" style={{ height: `${mapSize}px`, width: "100%" }}>
      {/* Display the user's location if no openings are found */}
      <MapContainer
        center={position}
        zoom={zoomLevel}
        style={{ height: "100%", minHeight: "100%" }}
      >
        {/* Resizer to adjust the map height */}
        <OpeningsMapResizer height={mapSize} />

        {/* Display Opening polygons, if any */}
        <OpeningsMapEntry polygons={openings} />
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
