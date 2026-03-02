import { useQueries } from "@tanstack/react-query";
import { MapKindType, MapPositionType } from "@/types/MapLayer";
import API from "@/services/API";
import { defaultLocation } from "./constants";

const queries = (openingIds: number[], ...kinds: MapKindType[]) =>
  openingIds.map((id) => ({
    queryKey: ["opening", "map", id, { kinds }],
    queryFn: () => API.OpeningMapsEndpointService.getOpeningPolygonAndProperties(id.toString(), kinds.join(",")),
    refetchOnReconnect: false,
  }));

export const getMapQueries = (openingIds: number[], ...kinds: MapKindType[]) => useQueries({ queries: queries(openingIds, ...kinds) });

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
