/**
 * Shifts BC GW response Lng-Lat to Lat-Lng format.
 *
 * @param coordinates geometry object from the api.
 * @returns the same geometry object with lat and long shifted.
 */
export const shiftBcGwLngLat2LatLng = (coordinates: number[][][]) => {
  const newCoord = [];
  for (let i = 0, len = coordinates.length; i < len; i++) {
    const polygon: number[][] = coordinates[i];
    const newSubArray = []

    for (let j = 0, lenj = polygon.length; j < lenj; j++) {
      const lat = polygon[j][1];
      const long = polygon[j][0];
      const polygonPoint = [lat, long];
      newSubArray.push(polygonPoint);
    }
    newCoord.push(newSubArray);
  }
  return newCoord;
}
