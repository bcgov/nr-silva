import React, { useEffect, useState } from "react";
import { OpeningPolygon } from "../../types/OpeningPolygon";
import { MapLayer } from "../../types/MapLayer";
import { allLayers } from "./constants";
import axios from "axios";
import { getAuthIdToken } from "../../services/AuthService";
import { convertGeoJsonToLatLng } from "../../map-services/BcGwLatLongUtils";
import {
  LayersControl,
  MapContainer,
  TileLayer,
  WMSTileLayer,
} from "react-leaflet";
import { LatLngExpression } from "leaflet";

import OpeningsMapEntry from "../OpeningsMapEntry";
import { API_ENDPOINTS, defaultHeaders } from "@/services/apiConfig";

interface MapProps {
  openingIds: number[] | null;
  openingId: number | null;
  setOpeningPolygonNotFound: (value: boolean, openingId: number | null) => void;
  mapHeight?: number;
}

const OpeningsMap: React.FC<MapProps> = ({
  openingIds,
  openingId,
  setOpeningPolygonNotFound,
  mapHeight = 400,
}) => {
  const [selectedOpeningIds, setSelectedOpeningIds] = useState<number[]>([]);
  const [openings, setOpenings] = useState<OpeningPolygon[]>([]);
  const [position, setPosition] = useState<LatLngExpression>([
    48.43737, -123.35883,
  ]);
  const [layers, setLayers] = useState<MapLayer[]>([]);
  const authToken = getAuthIdToken();
  const [zoomLevel, setZoomLevel] = useState<number>(13);

  const getOpeningPolygonAndProps = async (
    selectedOpeningId: number | null
  ): Promise<OpeningPolygon | null> => {

    if (!selectedOpeningId) return Promise.resolve(null);

    const response = await axios.get(
      API_ENDPOINTS.openingMap(selectedOpeningId),
      defaultHeaders(authToken)
    );

    const { data } = response;

    if (data.features && data.features.length) {
      const openingsList: OpeningPolygon[] = [];
      for (let i = 0, len = data.features.length; i < len; i++) {
        if (data.features[i].geometry) {
          const openingGeometry = convertGeoJsonToLatLng(
            data.features[i].geometry.coordinates
          );

          const openingObj: OpeningPolygon = {
            key: data.features[i].id,
            bounds: openingGeometry,
            properties: data.features[i].properties,
            id: data.features[i].id,
            positionLat: (data.bbox[1] + data.bbox[3]) / 2,
            positionLong: (data.bbox[0] + data.bbox[2]) / 2,
          };
          openingsList.push(openingObj);
        } else {
          setOpeningPolygonNotFound(true, selectedOpeningId);
        }
      }

      // Handling arrays to allow querying more than 1 at once.
      if (openingsList.length) {
        return openingsList[0];
      }
    } else {
      setOpeningPolygonNotFound(true, selectedOpeningId);
    }

    return null;
  };

  const callBcGwApi = async (
    currentOpeningId: number
  ): Promise<OpeningPolygon | null> => {
    return await getOpeningPolygonAndProps(currentOpeningId);
  };

  const getUserLocation = async () => {
    if (navigator.geolocation) {
      const options = {
        enableHighAccuracy: true,
        timeout: 5000,
        maximumAge: 0,
      };

      const requestCurrentLocation = () => {
        navigator.geolocation.getCurrentPosition(
          (currentPosition: GeolocationPosition) => {
            setPosition({
              lat: currentPosition.coords.latitude,
              lng: currentPosition.coords.longitude,
            });
            setZoomLevel(8);
          },
          () => {
            setPosition({ lat: 51.339506220208065, lng: -121.40991210937501 });
            setZoomLevel(6);
          },
          options
        );
      };

      const permissionResult = await navigator.permissions.query({
        name: "geolocation",
      });
      if (
        permissionResult.state === "granted" ||
        permissionResult.state === "prompt"
      ) {
        requestCurrentLocation();
      }
    }
  };

  const loadOpeniningPolygons = async (
    providedIds: number[]
  ): Promise<void> => {
    setOpeningPolygonNotFound(false, null);

    if (providedIds?.length) {
      const results = await Promise.all(providedIds.map(callBcGwApi));
      setOpenings(results.filter((opening) => opening !== null));
    } else {
      setOpenings([]);
    }

    const filtered = allLayers.filter((l) => l.name.length > 0);
    if (filtered.length) {
      setLayers(filtered);
    }
  };

  useEffect(() => {
    setSelectedOpeningIds(openingId ? [openingId] : []);
    if (!openingId) {
      (async () => {
        await getUserLocation();
      })();
    }
  }, [openingId]);

  useEffect(() => {
    setSelectedOpeningIds(openingIds || []);
    if (!openingIds?.length) {
      (async () => {
        await getUserLocation();
      })();
    }
  }, [openingIds]);

  useEffect(() => {
    loadOpeniningPolygons(selectedOpeningIds);
  }, [selectedOpeningIds]);

  useEffect(() => {
    (async () => {
      await getUserLocation();
    })();
  }, []);

  return (
    <MapContainer
      center={position}
      zoom={zoomLevel}
      style={{ height: `${mapHeight}px`, width: "100%" }}
    >
      <TileLayer
        url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}"
        attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community"
        zIndex={-10000}
      />

      {/* Display Opening polygons, if any */}
      <OpeningsMapEntry
        polygons={openings}
        defaultLocation={position}
        defaultZoom={zoomLevel}
      />

      {/* Default layers */}
      {layers.length && (
        <LayersControl position="topright">
          {layers.map((layer: MapLayer) => (
            <LayersControl.Overlay key={layer.name} name={layer.name}>
              <WMSTileLayer
                url="https://openmaps.gov.bc.ca/geo/ows"
                params={{
                  format: layer.format,
                  layers: layer.layers,
                  transparent: layer.transparent,
                  styles: layer.styles.map((s) => s.name).join(","),
                }}
              />
            </LayersControl.Overlay>
          ))}
        </LayersControl>
      )}
    </MapContainer>
  );
};

export default OpeningsMap;
