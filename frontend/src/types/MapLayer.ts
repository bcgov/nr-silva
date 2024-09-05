export type MapLayer = {
  position: number;
  name: string;
  format: string;
  layers: string;
  transparent: boolean;
  styles: {name: string, title: string}[];
  catalogueUrl: string;
  getCapabilitiesUrl: string;
};

export type BaseMapLayer = {
  name: string;
  url: string;
  attribution: string;
  default: boolean;
};
