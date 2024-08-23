import { LatLngExpression } from "leaflet";

export type OpeningPolygon = {
  key: string;
  bounds: LatLngExpression[][];
  properties: object;
  id: string;
  positionLat: number;
  positionLong: number;
  popup?: JSX.Element;
};
