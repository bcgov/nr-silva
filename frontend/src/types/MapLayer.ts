export type MapLayer = {
  name: string;
  format: string;
  layers: string;
  transparent: boolean;
  styles: {name: string, title: string}[];
  catalogueUrl: string;
  getCapabilitiesUrl: string;
};
