export type OpeningPolygon = {
  key: string;
  bounds: number[][][];
  properties: object;
  id: string;
  positionLat: number;
  positionLong: number;
  popup?: JSX.Element;
};
