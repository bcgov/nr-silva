import { LatLngExpression } from "leaflet";

export type OpeningPolygon = {
  key: string;
  bounds: LatLngExpression[] | LatLngExpression[][] | LatLngExpression[][][];
  properties: Record<string, unknown>;
  id: string;
  positionLat: number;
  positionLong: number;
  popup?: JSX.Element;
};
