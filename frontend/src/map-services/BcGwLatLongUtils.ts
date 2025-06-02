import { LatLngExpression } from "leaflet";

/**
 * Shifts BC GW response Lng-Lat to Lat-Lng format.
 *
 * @param {number[][][]} coordinates geometry object from the api.
 * @returns {number[][][]} the same geometry object with lat and long shifted.
 */
export const shiftBcGwLngLat2LatLng = (coordinates: number[][][]): LatLngExpression[][] => {
  const newCoord: LatLngExpression[][] = [];
  for (let i = 0, len = coordinates.length; i < len; i++) {
    const polygon: number[][] | undefined = coordinates[i];
    if (!polygon) {
      continue;
    }
    const newSubArray: LatLngExpression[] = []

    for (let j = 0, lenj = polygon.length; j < lenj; j++) {
      const lat = polygon[j]![1];
      const long = polygon[j]![0];
      const polygonPoint: LatLngExpression = [lat!, long!];
      newSubArray.push(polygonPoint);
    }
    newCoord.push(newSubArray);
  }
  return newCoord;
}

const createLatLngExpressionFromPointOrRing = (ringOrPoint: number[] | number[][]): LatLngExpression | LatLngExpression[] => {
  return Array.isArray(ringOrPoint[0])
    ? (ringOrPoint as number[][]).map(([lng, lat]) => [lat, lng] as LatLngExpression)
    : [ringOrPoint[1], ringOrPoint[0]] as LatLngExpression;
}

/**
 * Convert GeoJSON coordinates to Leaflet LatLng format.
 *
 * @param geoJsonCoordinates - GeoJSON coordinates (Polygon or MultiPolygon format)
 * @returns Leaflet LatLng coordinates (Polygon or MultiPolygon format)
 */
export const convertGeoJsonToLatLng = (geoJsonCoordinates: number[][][] | number[][][][]): LatLngExpression[] | LatLngExpression[][] | LatLngExpression[][][] => {
  const polygons = geoJsonCoordinates.map((polygonOrRing) => polygonOrRing.map(createLatLngExpressionFromPointOrRing));
  return polygons as LatLngExpression[] | LatLngExpression[][] | LatLngExpression[][][];
};

