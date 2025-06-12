import { useQueries } from "@tanstack/react-query";
import { MapKindType, MapPositionType } from "@/types/MapLayer";
import { THREE_HALF_HOURS, THREE_HOURS } from "@/constants/TimeUnits";
import API from "@/services/API";
import { defaultLocation } from "./constants";

const queries = (openingIds: number[], kind: MapKindType) =>
  openingIds.map((id) => ({
    queryKey: ["opening", "map", id, { kind }],
    queryFn: () => API.OpeningMapsEndpointService.getOpeningPolygonAndProperties(id.toString(), kind),
    staleTime: THREE_HOURS,
    cacheTime: THREE_HALF_HOURS,
    refetchOnReconnect: false,
  }));

export const getMapQueries = (openingIds: number[], kind: MapKindType) => useQueries({ queries: queries(openingIds, kind) });

export const getUserLocation = async (): Promise<MapPositionType> => {
  if (navigator.geolocation) {
    const options = {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0,
    };

    const requestCurrentLocation = (): Promise<MapPositionType> => {
      return new Promise((resolve) => {
        navigator.geolocation.getCurrentPosition(
          (currentPosition: GeolocationPosition) => {
            resolve({
              lat: currentPosition.coords.latitude,
              lng: currentPosition.coords.longitude,
              zoom: 8,
            });
          },
          () => {
            resolve(defaultLocation);
          },
          options
        );
      });
    };

    const permissionResult = await navigator.permissions.query({
      name: "geolocation",
    });
    if (
      permissionResult.state === "granted" ||
      permissionResult.state === "prompt"
    ) {
      return requestCurrentLocation();
    }
  }

  // Fallback in case geolocation is not available
  return Promise.resolve(defaultLocation);
};
