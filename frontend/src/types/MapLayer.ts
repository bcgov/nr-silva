export type MapLayer = {
  key: string;
  name: string;
  pathOptions: object;
  popup?: JSX.Element;
  bounds: number[][][];
  properties: object;
};
